package com.example.imdb.dto;

import java.util.List;

public record FilmeTmdb(
        int id,
        String title,
        String poster_path,
        double vote_average,
        List<Integer> genre_ids
) {}