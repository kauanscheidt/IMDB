package com.example.imdb.service;

import com.example.imdb.dto.FilmeTmdb;
import com.example.imdb.dto.Filtro;
import com.example.imdb.dto.RespostaTmdb;
import com.example.imdb.model.Filme;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final  HttpClient CLIENT = HttpClient.newHttpClient();
    private final Gson GSON = new Gson();
    private final String BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final int MAX_PAGES = 500;

    private String obterGenero(int id) {
        return switch (id) {
            case 12 -> "Aventura";
            case 35 -> "Comédia";
            case 99 -> "Documentário";
            case 18 -> "Drama";
            case 14 -> "Fantasia";
            case 27 -> "Terror";
            case 878 -> "Ficção Científica";
            case 37 -> "Faroeste";
            default -> "";
        };
    }

    private Filme converter(FilmeTmdb f) {
        return Filme.builder()
                .titulo(f.title())
                .imagemUrl(f.poster_path() != null ? BASE_URL + f.poster_path() : "")
                .nota(f.vote_average())
                .generoIds(f.genre_ids() != null
                        ? f.genre_ids().stream().map(String::valueOf).collect(Collectors.joining(","))
                        : "")
                .generoNomes(f.genre_ids() != null
                        ? f.genre_ids().stream().map(this::obterGenero).collect(Collectors.joining(", "))
                        : "")
                .build();
    }

public List<Filme> buscarFilmes(Filtro filtro, String busca) {
    String urlBase =  buildUrlFilmes(filtro, busca);

    List<Filme> listaFilmes = new ArrayList<>();

    System.out.println("Procurando filmes...");

    for (int i = 1; i <= MAX_PAGES; i++) {
        try {
            String urlFilme = urlBase + i;

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlFilme)).build();
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            RespostaTmdb resposta = GSON.fromJson(response.body(), RespostaTmdb.class);

            if (i > resposta.total_pages()) break;

            for (FilmeTmdb f : resposta.results()) {
                listaFilmes.add(converter(f));
            }

        } catch (Exception e) {
            System.out.println("Erro na pagina " + i + ": " + e.getMessage());
            break;
        }

    }
    System.out.println(listaFilmes.size() + " filmes encontrados.");
    return listaFilmes;
    }

    private String buildUrlFilmes(Filtro filtro, String busca) {
        return switch (filtro) {
            case AVALIACAO    -> "https://api.themoviedb.org/3/movie/top_rated?language=pt-BR&api_key=" + apiKey + "&page=";
            case POPULARIDADE -> "https://api.themoviedb.org/3/movie/popular?language=pt-BR&api_key=" + apiKey + "&page=";
            case GENERO       -> "https://api.themoviedb.org/3/discover/movie?language=pt-BR&with_genres=" + busca + "&api_key=" + apiKey + "&page=";
            case NOME         -> "https://api.themoviedb.org/3/search/movie?language=pt-BR&query=" + URLEncoder.encode(busca, StandardCharsets.UTF_8) + "&api_key=" + apiKey + "&page=";
        };
    }
}