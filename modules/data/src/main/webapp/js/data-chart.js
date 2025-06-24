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

const statSets = [
    {
        id: "min",
        label: "Min",
        data: sensorMinData,
        backgroundColor: "#ff000f",
        borderColor: "#ff000f",
        fill: false,
        borderWidth: 1,
        pointRadius: 1,
        lineTension: 0.5
    },
    {
        id: "max",
        label: "Max",
        data: sensorMaxData,
        backgroundColor: "#00bbff",
        borderColor: "#00bbff",
        fill: false,
        borderWidth: 1,
        pointRadius: 1,
        lineTension: 0.5
    },
    {
        id: "avg",
        label: "Average",
        data: sensorAvgData,
        backgroundColor: "#5bff00",
        borderColor: "#5bff00",
        fill: false,
        borderWidth: 1,
        pointRadius: 1,
        lineTension: 0.5
    }
];

const dateFormat = {
    day: "numeric",
    month: "numeric",
    hour: "numeric",
    minute: "numeric"
};

// TODO: Allow data deletion in tooltip
let chart = new Chart(document.getElementById("dataChart"), {
    type: 'line',
    options: {
        scales: {
            x: {
                type: 'time',
                time: {
                    tooltipFormat: 'DD.MM HH:mm:ss',
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

let statChart = new Chart(document.getElementById("statChart"), {
    type: 'line',
    options: {
        scales: {
            x: {
                type: 'time',
                time: {
                    tooltipFormat: 'DD.MM HH:mm:ss',
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
                    beforeTitle: function (context) {
                        return context[0].dataset.label;
                    },
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
        datasets: statSets
    }
})