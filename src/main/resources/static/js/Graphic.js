const charts = [];

const green = 'rgb(75, 192, 192)'
const purple = 'rgb(153, 102, 255)'
const orange = 'rgb(255, 159, 64)'
const pink = 'rgba(255, 99, 132, 0.2)'
const colors = [green, purple, orange, pink]

function clearAllGraphics() {
    charts.forEach(function(element) {
        element.destroy();
    })
}

function newGraphic(idElement, dataLabels, data) {
        let newChart = new Chart(document.getElementById(idElement), {
        type: 'line',
        data: {
            labels: generateXAxisLabels(1, data[0].length, 1),
            datasets: generateDatasets(dataLabels, data)
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

        window.addEventListener('resize', function() {newChart.resize();});
        charts.push(newChart)
}

function generateXAxisLabels(start, stop, step) {
    return Array.from(
        { length: (stop - start) / step + 1 },
        (value, index) => start + index * step
    ).map(String)
}

function generateDatasets(dataLabels, data) {
    let dataValues = [];
    data.forEach((element, index) => {
        dataValues.push(dataset(dataLabels[index], element, colors[index]));
    });
    return dataValues;
}

function dataset(label, data, color) {
    return {
        label: label,
        data: data,
        fill: false,
        borderColor: color,
        tension: 0,
        borderWidth: 1
    };
}
