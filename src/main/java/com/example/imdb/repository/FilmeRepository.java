package com.example.imdb.repository;

import com.example.imdb.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByTituloContainingIgnoreCase(String titulo);
    List<Filme> findByGeneroNomesContainingIgnoreCase(String genero);
    boolean existsByTitulo(String titulo);
}