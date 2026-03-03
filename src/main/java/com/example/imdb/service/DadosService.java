import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import com.example.imdb.service.FilmeService;
import java.util.*;

public class DadosService {

    public double obterMedia(List<FilmeService> listaFilmes){
        double soma = 0;
        int cont = 0;
        double resultado = 0;
        for (int i = 0; i < listaFilmes.size(); i++){
            if(listaFilmes.get(i).getNota() > 0) {
                soma += listaFilmes.get(i).getNota();
                cont++;
            }
        }
        resultado = soma/cont;
        return resultado;
    }
    public double [] obterModa(List<FilmeService> listaFilmes) {
        Map<Double, Integer> frequencia  = new HashMap<>();
        for (int i = 0; i < listaFilmes.size(); i++) {
            double nota = Math.round(listaFilmes.get(i).getNota() * 10.0) / 10.0;
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

    public double obterMediana(List<FilmeService> listaFilmes){
        double [] notas = new double[listaFilmes.size()];
        for (int i = 0; i < listaFilmes.size(); i++) {
            notas[i] = Math.round(listaFilmes.get(i).getNota()*10.0)/10.0;
        }
        Arrays.sort(notas);
        int tam = notas.length;
        if(notas.length % 2 == 0){
            double res = (notas[tam/2 - 1] + notas[tam/ 2])/ 2.0;
            return res;
        }else {
            return notas[notas.length/2];
        }
    }
    public double obterDesvioPadrão(List<FilmeService> listaFilmes) {
        double media = obterMedia(listaFilmes);
        double soma = 0;
        int cont = 0;
        double res = 0;
        for (int i = 0; i < listaFilmes.size(); i++) {
            double nota = listaFilmes.get(i).getNota();
            if (nota > 0) {
                soma += Math.pow(nota - media, 2);
                cont++;
            }
        }
        res = Math.sqrt(soma / cont);
        return res;
    }
}