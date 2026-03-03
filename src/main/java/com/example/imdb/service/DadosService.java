package com.example.imdb.service;

import com.example.imdb.model.Filme;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DadosService {

    public double obterMedia(List<Filme> filmes) {
        double soma = 0;
        int cont = 0;
        double resultado = 0;
        for (int i = 0; i < filmes.size(); i++){
            if(filmes.get(i).getNota() > 0) {
                soma += filmes.get(i).getNota();
                cont++;
            }
        }
        resultado = soma/cont;
        return resultado;
    }

    public double[] obterModa(List<Filme> filmes) {
        Map<Double, Integer> frequencia = new HashMap<>();
        for (int i = 0; i < filmes.size(); i++) {
            double nota = Math.round(filmes.get(i).getNota() * 10.0) / 10.0;
            if(nota > 0) {
                frequencia.put(nota, frequencia.getOrDefault(nota, 0) + 1);
            }
        }
        double moda = -1;
        int maxFreq = 0;
        for (Map.Entry<Double, Integer> entry : frequencia.entrySet()) {
            if (entry.getValue() > maxFreq) {
                maxFreq = entry.getValue();
                moda = entry.getKey();
            }
        }
        return new double[]{moda, maxFreq};
    }

    public double obterMediana(List<Filme> filmes) {
        double[] notas = new double[filmes.size()];
        for (int i = 0; i < filmes.size(); i++) {
            notas[i] = Math.round(filmes.get(i).getNota()*10.0)/10.0;
        }
        Arrays.sort(notas);
        int tam = notas.length;
        if(notas.length % 2 == 0){
            double res = (notas[tam/2 - 1] + notas[tam/2])/ 2.0;
            return res;
        }else {
            return notas[notas.length/2];
        }
    }

    public double obterDesvioPadrao(List<Filme> filmes) {
        double media = obterMedia(filmes);
        double soma = 0;
        int cont = 0;
        double res = 0;
        for (int i = 0; i < filmes.size(); i++) {
            double nota = filmes.get(i).getNota();
            if (nota > 0) {
                soma += Math.pow(nota - media, 2);
                cont++;
            }
        }
        res = Math.sqrt(soma / cont);
        return res;
    }
}