const cssWhite = getComputedStyle(document.body).getPropertyValue('--custom-white');
const cssBlack = getComputedStyle(document.body).getPropertyValue('--custom-black');

const javaData = {
    data: sensorData,
    backgroundColor: cssWhite,
    borderColor: cssWhite,
    fill: false,
    borderWidth: 1,
    pointRadius: 1,
    lineTension: 0.5
};

const dateFormat = {
    day: "numeric",
    month: "numeric",
    hour: "numeric",
    minute: "numeric"
};

new Chart(document.getElementById("dataChart"), {
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
                        return value + sensorInformation.unit;
                    }
                },
                grid: {
                    color: cssBlack
                }
            }
        },
        plugins: {
            legend: {
                display: false
            },
            tooltip: {
                callbacks: {
                    label: function (context) {
                        return context.parsed.y + sensorInformation.unit
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
                    },
                    limits: {
                        x: {
                            min: sensorData[0],
                            max: sensorData[sensorData.length - 1]
                        }
                    }
                }
            }
        }
    },
    data: {
        datasets: [javaData]
    }
})