package com.example.imdb.controller;

import com.example.imdb.model.Filme;
import com.example.imdb.service.FilmeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImdbController {

    private final FilmeService filmeService;
    private final DadosService dadosService;

    @GetMapping("/filmes")
    public ResponseEntity<List<Filme>> listarFilmes() {
        return ResponseEntity.ok(filmeService.buscarTodos());
    }

    @GetMapping("/filmes/buscar")
    public ResponseEntity<List<Filme>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(filmeService.buscarPorNome(nome));
    }

    @GetMapping("/filmes/genero")
    public ResponseEntity<List<Filme>> buscarPorGenero(@RequestParam String genero) {
        return ResponseEntity.ok(filmeService.buscarPorGenero(genero));
    }

    @GetMapping("/filmes/estatisticas")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        List<Filme> filmes = filmeService.buscarTodos();
        if (filmes.isEmpty()) return ResponseEntity.noContent().build();

        double[] moda = dadosService.obterModa(filmes, Filme::getNota);
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", filmes.size());
        stats.put("media", String.format("%.2f", dadosService.obterMedia(filmes, Filme::getNota)));
        stats.put("mediana", String.format("%.2f", dadosService.obterMediana(filmes, Filme::getNota)));
        stats.put("moda", moda[0]);
        stats.put("modaRepeticoes", (int) moda[1]);
        stats.put("variancia", String.format("%.2f", dadosService.obterVariancia(filmes, Filme::getNota)));
        stats.put("desvioPadrao", String.format("%.2f", dadosService.obterDesvioPadrao(filmes, Filme::getNota)));
        return ResponseEntity.ok(stats);
    }
}