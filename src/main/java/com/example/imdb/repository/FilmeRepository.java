package com.example.imdb.repository;

import com.example.imdb.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FilmeRepository extends JpaRepository<Filme, Long> {

}
