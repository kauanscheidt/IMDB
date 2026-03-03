package com.example.imdb.dto;

import java.util.List;

public record RespostaTmdb(
        List<FilmeTmdb> results,
        int total_pages,
        int total_results
) {}