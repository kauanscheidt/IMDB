package com.example.imdb.service;

import com.example.imdb.model.Filme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

@Service
public class GerarArquivos {

    private final String PASTA = "relatorios/";
    private final DadosService dadosService;

    public GerarArquivos(DadosService dadosService) {
        this.dadosService = dadosService;
        new File(PASTA).mkdirs();
    }

    public boolean gerarTxtFilmes(List<Filme> listaFilmes, String nomeArquivo) {
        try (FileWriter escrita = new FileWriter(PASTA + nomeArquivo + ".txt")) {
            Filme primeiro = listaFilmes.get(0);
            escrita.write(1 + ": " + primeiro.getTitulo() + " (Nota: " + primeiro.getNota() + ")" + primeiro.getTitulo());
            for (int i = 1; i < listaFilmes.size(); i++) {
                Filme f = listaFilmes.get(i);
                escrita.write("\n" + (i + 1) + ": " + f.getTitulo() + " (Nota: " + f.getNota() + ")");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao gerar o txt: " + e.getMessage());
            return false;
        }
    }

    public boolean gerarJsonFilmes(List<Filme> listaFilmes, String nomeArquivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter escrita = new FileWriter(PASTA + nomeArquivo + ".json")) {
            gson.toJson(listaFilmes, escrita);
            return true;
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao gerar o Json: " + e.getMessage());
            return false;
        }
    }

    public boolean gerarXmlFilmes(List<Filme> listaFilmes, String nomeArquivo) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element filmes = doc.createElement("filmes");
            doc.appendChild(filmes);

            for (int i = 0; i < listaFilmes.size(); i++) {
                Filme f = listaFilmes.get(i);

                Element filme = doc.createElement("filme");
                filmes.appendChild(filme);

                Element titulo = doc.createElement("titulo");
                titulo.setTextContent(f.getTitulo());
                filme.appendChild(titulo);

                Element nota = doc.createElement("nota");
                nota.setTextContent(String.valueOf(f.getNota()));
                filme.appendChild(nota);

                Element genero = doc.createElement("genero");
                genero.setTextContent(f.getGeneroNomes());
                filme.appendChild(genero);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource documentoFonte = new DOMSource(doc);
            StreamResult documentoFinal = new StreamResult(new File(PASTA + nomeArquivo + ".xml"));

            transformer.transform(documentoFonte, documentoFinal);

            return true;

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao gerar o xml: " + e.getMessage());
            return false;
        }
    }

    public boolean gerarHtmlFilmes(List<Filme> listaFilmes, String nomeArquivo) {
        try {
            InputStream is = getClass().getResourceAsStream("/templates/M3_Movies.html");
            if (is == null) {
                System.out.println("Template HTML nao encontrado em /templates/M3_Movies.html");
                return false;
            }
            String html = new String(is.readAllBytes());

            double media = dadosService.obterMedia(listaFilmes);
            double[] moda = dadosService.obterModa(listaFilmes);
            double mediana = dadosService.obterMediana(listaFilmes);
            double desvio = dadosService.obterDesvioPadrao(listaFilmes);

            int ruim = 0, regular = 0, bom = 0;
            for (int i = 0; i < listaFilmes.size(); i++) {
                double nota = listaFilmes.get(i).getNota();
                if (nota < 5)      ruim++;
                else if (nota < 7) regular++;
                else               bom++;
            }

            List<Filme> top10 = listaFilmes.stream()
                    .sorted(Comparator.comparingDouble(Filme::getNota).reversed())
                    .limit(10)
                    .toList();

            StringBuilder top10Html = new StringBuilder();
            for (int i = 0; i < top10.size(); i++) {
                Filme f = top10.get(i);
                top10Html.append("<div class=\"filme\">")
                        .append("<p class=\"posicao\">").append(i + 1).append("</p>")
                        .append("<img src=\"").append(f.getImagemUrl()).append("\" alt=\"").append(f.getTitulo()).append("\">")
                        .append("<div class=\"filme-info\">")
                        .append("<p class=\"titulo\">").append(f.getTitulo()).append("</p>")
                        .append("<p class=\"genero\">").append(f.getGeneroNomes()).append("</p>")
                        .append("</div>")
                        .append("<p class=\"nota\">").append(String.format("%.1f", f.getNota())).append("</p>")
                        .append("</div>\n");
            }

            html = html.replace("{{TOTAL}}",    String.valueOf(listaFilmes.size()));
            html = html.replace("{{MEDIA}}",    String.format("%.1f", media));
            html = html.replace("{{MODA}}",     String.format("%.1f", moda[0]));
            html = html.replace("{{MEDIANA}}",  String.format("%.1f", mediana));
            html = html.replace("{{DESVIO}}",   String.format("%.1f", desvio));
            html = html.replace("{{MEDIA_JS}}",  String.valueOf(media));
            html = html.replace("{{MODA_JS}}",   String.valueOf(moda[0]));
            html = html.replace("{{MEDIANA_JS}}", String.valueOf(mediana));
            html = html.replace("{{DESVIO_JS}}",  String.valueOf(desvio));
            html = html.replace("{{RUIM_JS}}",    String.valueOf(ruim));
            html = html.replace("{{REGULAR_JS}}", String.valueOf(regular));
            html = html.replace("{{BOM_JS}}",     String.valueOf(bom));
            html = html.replace("{{TOP10_HTML}}", top10Html.toString());

            FileWriter fw = new FileWriter(PASTA + nomeArquivo + ".html");
            fw.write(html);
            fw.close();

            System.out.println("HTML salvo em: " + PASTA + nomeArquivo + ".html");

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(PASTA + nomeArquivo + ".html"));
            }

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao gerar HTML: " + e.getMessage());
            return false;
        }
    }
}