package com.example.imdb.service;

import com.example.imdb.model.Filme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class GerarArquivos {

    public boolean gerarTxtFilmes(List<Filme> listaFilmes, String nomeArquivo) {
        try (FileWriter escrita = new FileWriter(nomeArquivo + ".txt")) {
            Filme primeiro = listaFilmes.get(0);
            escrita.write(1 + ": " + primeiro.getTitulo() + " (Nota: " + primeiro.getNota() + ")");
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
        try (FileWriter escrita = new FileWriter(nomeArquivo + ".json")) {
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
            StreamResult documentoFinal = new StreamResult(new File(nomeArquivo + ".xml"));

            transformer.transform(documentoFonte, documentoFinal);

            return true;

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao gerar o xml: " + e.getMessage());
            return false;
        }
    }
}