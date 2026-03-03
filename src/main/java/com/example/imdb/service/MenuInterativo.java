package com.example.imdb.service;

import com.example.imdb.dto.Filtro;
import com.example.imdb.model.Filme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class MenuInterativo implements CommandLineRunner {

    private final TmdbService tmdbService;
    private final FilmeService filmeService;
    private final DadosService dadosService;
    private final GerarArquivos gerarArquivos;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        System.out.println("\n--- SISTEMA TMDB ---\n");

        boolean rodando = true;
        while (rodando) {
            System.out.println("1. Filmes");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> menuFilmes();
                case "0" -> { System.out.println("Encerrando."); rodando = false; }
                default  -> System.out.println("Opcao invalida.\n");
            }
        }
    }

    private void menuFilmes() {
        System.out.println("\n--- FILMES ---");
        System.out.println("1. Buscar por Nome");
        System.out.println("2. Buscar por Genero");
        System.out.println("3. Top Mais Avaliados");
        System.out.println("4. Top Mais Populares");
        System.out.println("5. Ver estatisticas");
        System.out.println("0. Voltar");
        System.out.print("Opcao: ");

        String opcao = scanner.nextLine().trim();
        List<Filme> filmes = null;
        String nomeArquivo = "filmes";

        switch (opcao) {
            case "1" -> {
                System.out.print("Nome do filme: ");
                String nome = scanner.nextLine().trim();
                filmes = tmdbService.buscarFilmes(Filtro.NOME, nome);
                nomeArquivo = "filmes_" + nome.replaceAll("\\s+", "_").toLowerCase();
            }
            case "2" -> {
                System.out.println("\nGeneros: 12-Aventura | 35-Comedia | 99-Documentario");
                System.out.println("         18-Drama    | 14-Fantasia | 27-Terror");
                System.out.println("         878-Ficcao Cientifica | 37-Faroeste");
                System.out.print("ID do genero: ");
                String generoId = scanner.nextLine().trim();
                filmes = tmdbService.buscarFilmes(Filtro.GENERO, generoId);
                nomeArquivo = "filmes_genero_" + generoId;
            }
            case "3" -> {
                filmes = tmdbService.buscarFilmes(Filtro.AVALIACAO, "");
                nomeArquivo = "filmes_top_avaliados";
            }
            case "4" -> {
                filmes = tmdbService.buscarFilmes(Filtro.POPULARIDADE, "");
                nomeArquivo = "filmes_populares";
            }
            case "5" -> {
                List<Filme> todos = filmeService.buscarTodos();
                if (todos.isEmpty()) {
                    System.out.println("Nenhum filme no banco. Faca uma busca primeiro.\n");
                } else {
                    exibirEstatisticas(todos);
                }
                return;
            }
            case "0" -> { return; }
            default  -> { System.out.println("Opcao invalida.\n"); return; }
        }

        if (filmes != null && !filmes.isEmpty()) {
            perguntarSalvarBanco(filmes);
            perguntarExportacao(filmes, nomeArquivo);
        }
    }

    private void perguntarSalvarBanco(List<Filme> filmes) {
        System.out.print("\nDeseja salvar os filmes no banco? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            filmeService.salvarTodos(filmes);
        }
    }

    private void perguntarExportacao(List<Filme> filmes, String nomeArquivo) {
        System.out.println("\n1. HTML  2. TXT  3. JSON  4. XML  5. Todos  0. Nao exportar");
        System.out.print("Opcao: ");

        switch (scanner.nextLine().trim()) {
            case "1" -> gerarArquivos.gerarHtmlFilmes(filmes, nomeArquivo);
            case "2" -> gerarArquivos.gerarTxtFilmes(filmes, nomeArquivo);
            case "3" -> gerarArquivos.gerarJsonFilmes(filmes, nomeArquivo);
            case "4" -> gerarArquivos.gerarXmlFilmes(filmes, nomeArquivo);
            case "5" -> {
                gerarArquivos.gerarHtmlFilmes(filmes, nomeArquivo);
                gerarArquivos.gerarTxtFilmes(filmes, nomeArquivo);
                gerarArquivos.gerarJsonFilmes(filmes, nomeArquivo);
            }
        }
        System.out.println();
    }

    private void exibirEstatisticas(List<Filme> filmes) {
        System.out.println("\nEstatisticas (" + filmes.size() + " filmes):");
        System.out.printf("  Media:         %.2f%n", dadosService.obterMedia(filmes);
        System.out.printf("  Mediana:       %.2f%n", dadosService.obterMediana(filmes);
        double[] moda = dadosService.obterModa(filmes);
        System.out.printf("  Moda:          %.2f (repete %d vezes)%n", moda[0], (int) moda[1]);
        System.out.printf("  Desvio Padrao: %.2f%n%n", dadosService.obterDesvioPadrao(filmes);
    }
}