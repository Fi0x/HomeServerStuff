const cssWhite = getComputedStyle(document.body).getPropertyValue('--custom-white');
const cssBlack = getComputedStyle(document.body).getPropertyValue('--custom-black');

const dateFormat = {
    day: "numeric",
    month: "numeric",
    hour: "numeric",
    minute: "numeric"
};

const lineColors = [
    "#5bff00",
    "#00ffd9",
    "#0048ff",
    "#a100ff",
    "#ff0008",
    "#ffea00",
    "#00912f",
    "#00bbff",
    "#ff00dd",
    "#880000"
];

const datasets = [];

let chart = new Chart(document.getElementById("dataChart"), {
    type: 'line',
    options: {
        scales: {
            x: {
                type: 'time',
                time: {
                    tooltipFormat: 'DD.MM hh:mm:ss',
                    displayFormats: {
                        day: 'MMM DD YY'
                    }
                },
                ticks: {
                    color: cssWhite,
                    callback: function (value) {
                        return new Date(value).toLocaleDateString(undefined, dateFormat);
                    }
                },
                grid: {
                    color: cssBlack
                }
            },
            y: {
                type: 'linear',
                display: false,
                position: 'left',
                ticks: {
                    color: cssWhite,
                    callback: function (value) {
                        return value;
                    }
                },
                grid: {
                    drawOnChartArea: false,
                    color: cssBlack
                }
            },
            Humid: {
                type: 'linear',
                display: true,
                position: 'right',
                grid: {
                    color: cssBlack
                },
                ticks: {
                    color: cssWhite,
                    callback: function (value) {
                        return value + '%';
                    }
                }
            },
            Temp: {
                type: 'linear',
                display: true,
                position: 'left',
                ticks: {
                    color: cssWhite,
                    callback: function (value) {
                        return value + '°C';
                    }
                },
                grid: {
                    drawOnChartArea: false,
                    color: cssBlack
                }
            }
        },
        plugins: {
            legend: {
                labels: {
                    color: cssWhite,
                    boxWidth: 30,
                    boxHeight: 30,
                    useBorderRadius: true,
                    borderRadius: 15,
                    font: {
                        size: 15,
                        weight: "bold"
                    }
                }
            },
            tooltip: {
                callbacks: {
                    label: function (context) {
                        let sensorElement = sensorNames.filter(sensor => sensor.id === context.dataset.id);
                        if (sensorElement?.length > 0)
                            return context.parsed.y + sensorElement[0].unit;

                        return context.parsed.y;
                    }
                }
            },
            zoom: {
                pan: {
                    enabled: true,
                    mode: 'x',
                    modifierKey: 'ctrl',
                    scaleMode: 'x'
                },
                zoom: {
                    mode: 'x',
                    wheel: {
                        enabled: true
                    },
                    pinch: {
                        enabled: true
                    }
                }
            }
        }
    },
    data: {
        datasets: datasets
    }
});

function loadChartData(sensorList) {
    sensorList.forEach((sensorElement) => {
        $.ajax({
            url: `${baseUrl}/${sensorElement.address}/${sensorElement.name}/last-timeframe`,
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                let resultList = Object.keys(res).map((key) => {
                    return {
                        timestamp: key,
                        value: res[key]
                    }
                });

                if (resultList.length === 0)
                    return;

                let sensorUnit = sensorElement.tags.toLowerCase().includes('temp')
                    ? 'Temp'
                    : sensorElement.tags.toLowerCase().includes('humid') || sensorElement.tags.toLowerCase().includes('feucht')
                        ? 'Humid'
                        : 'y';

                let sensorData = [];
                Array.from(resultList).forEach((set) => {
                    let entry = {
                        x: `${set.timestamp}`,
                        y: set.value
                    };
                    sensorData.push(entry);
                });

                let dataset = {
                    id: sensorElement.id,
                    label: sensorElement.name,
                    data: sensorData,
                    backgroundColor: lineColors[datasets.length % lineColors.length],
                    borderColor: lineColors[datasets.length % lineColors.length],
                    fill: false,
                    borderWidth: 1,
                    pointRadius: 1,
                    lineTension: 0.5,
                    yAxisID: sensorUnit,
                    hidden: true
                };

                datasets.push(dataset);
                chart.update();
            }
        });
    });
}