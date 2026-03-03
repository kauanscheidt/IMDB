package com.example.imdb.service;

import com.example.imdb.model.Filme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

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
}