let myCharts= [];

function reload() {
    if (typeof variable !== 'undefined') {
            myCharts.forEach(function(elemento, indice, array) {
                elemento.update();
            })
    }
}

function clearAllGraphics() {
    myCharts.forEach(function(elemento, indice, array) {
        elemento.destroy();
    })
}

function dataValue(label, data, color) {
    return {
        label: label,
        data: data,
        fill: false,
        borderColor: color,
        tension: 0,
        borderWidth: 1
    };
}

function newGraphic(idElement, dataLabels, data) {

        const arrayRange = (start, stop, step) =>
            Array.from(
                { length: (stop - start) / step + 1 },
                (value, index) => start + index * step
            );

        let color = ['rgb(75, 192, 192)', 'rgb(153, 102, 255)', 'rgb(255, 159, 64)', 'rgba(255, 99, 132, 0.2)']
        let dataValues = [];
        for (var i = 0; i < data.length; i++) {
          dataValues.push(dataValue(dataLabels[i], data[i], color[i]));
        }

        let myChart = new Chart(document.getElementById(idElement), {
        type: 'line',
        data: {
            labels: arrayRange(1, data[0].length, 1).map(String),
            datasets: dataValues
        },
        options: {
          scales: {
            y: {
                min: Math.min.apply(Math, data),
                max: Math.max.apply(Math, data)
            }
          }
        }
        });

        window.addEventListener('resize', function() {myChart.resize();});
        myCharts.push(myChart)
}