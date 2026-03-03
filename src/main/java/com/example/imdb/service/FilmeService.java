package com.example.imdb.service;

import com.example.imdb.model.Filme;
import com.example.imdb.repository.FilmeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmeService {

    private final FilmeRepository filmeRepository;

    public void salvarTodos(List<Filme> filmes) {
        filmeRepository.saveAll(filmes);
        System.out.println(filmes.size() + " filmes salvos no banco.");
    }

    public List<Filme> buscarTodos() {
        return filmeRepository.findAll();
    }

    public List<Filme> buscarPorNome(String nome) {
        return filmeRepository.findByTituloContainingIgnoreCase(nome);
    }

    public List<Filme> buscarPorGenero(String genero) {
        return filmeRepository.findByGeneroNomesContainingIgnoreCase(genero);
    }
}