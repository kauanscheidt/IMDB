
      google.charts.load('current', {'packages':['corechart', 'bar']});
      google.charts.setOnLoadCallback(buscarDados);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Year', 'Sales',],
          ['2004',  1000, ],
          ['2005',  1170,],
          ['2006',  660,],
          ['2007',  1030,]
        ]);

        var options = {
          title: 'Filmes Media',
          curveType: 'function',
          legend: { position: 'bottom' }
        };

        var chart = new google.visualization.LineChart(document.getElementById('graficoMedia'));

        chart.draw(data, options);
      }

      async function buscarDados() {
         var response = await fetch('http://localhost:8080/api/filmes/estatisticas');
         var dados = await response.json();
         graficoColunas(dados);
      }
      function graficoColunas(dados) {
        var data = google.visualization.arrayToDataTable([
          ['calculos', 'valor'],
          ['Média', dados.media],
          ['Moda', dados.moda],
          ['Mediana', dados.mediana],
          ['Desvio Padrão', dados.desvioPadrao],

        ]);

        var options = {
          chart: {
            title: 'Estatistica dos filmes',
            subtitle: 'Calculos com as notas dos filmes.',
          }
        };

        var chart = new google.charts.Bar(document.getElementById('graficoColunas'));

        chart.draw(data, google.charts.Bar.convertOptions(options));
      }