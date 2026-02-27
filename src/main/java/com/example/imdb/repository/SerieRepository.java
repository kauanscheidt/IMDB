package com.example.imdb.repository;

import com.example.imdb.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SerieRepository extends JpaRepository<Serie, Long> {

}
