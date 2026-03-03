package com.example.imdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_filmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(nullable = false)
    private double nota;

    @Column(name = "genero_ids")
    private String generoIds;

    @Column(name = "genero_nomes")
    private String generoNomes;
}