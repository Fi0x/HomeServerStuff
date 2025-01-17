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
                ticks: {
                    color: cssWhite,
                    callback: function (value) {
                        return value;
                    }
                },
                grid: {
                    color: cssBlack
                }
            },
            // Humid: {
            //     ticks: {
            //         color: cssWhite,
            //         callback: function (value) {
            //             return value + '%';
            //         }
            //     },
            //     grid: {
            //         color: cssBlack
            //     }
            // },
            // Temp: {
            //     ticks: {
            //         color: cssWhite,
            //         callback: function (value) {
            //             return value + '°';
            //         }
            //     },
            //     grid: {
            //         color: cssBlack
            //     }
            // }
        },
        plugins: {
            legend: {
                labels: {
                    color: cssWhite
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
            url: `${baseUrl}/${sensorElement.address}/${sensorElement.name}`,
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                // TODO: Convert result to sensorData and put it into dataset

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
                        x: `${set.timestamp}`
                    };
                    entry['y'] = set.value;//TODO: Use sensorUnit as key
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
                    yAxisId: sensorUnit
                };

                datasets.push(dataset);
                chart.update();
            }
        });
    });
}