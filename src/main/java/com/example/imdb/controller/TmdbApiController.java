package com.example.imdb.controller;


import com.example.imdb.service.TmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TMDB")
@RequiredArgsConstructor
public class TmdbApiController {

    private final TmdbService tmdbService;

    @
    @PostMapping("/v1")
    public ResponseEntity<String> post(@RequestBody String value) {
        tmdbService.consumirEPersistir(value);

        return ResponseEntity.ok("Processamento concluído com sucesso para o termo: " + value);
    }
}

