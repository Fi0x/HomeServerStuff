const cssWhite = getComputedStyle(document.body).getPropertyValue('--custom-white');
const cssBlack = getComputedStyle(document.body).getPropertyValue('--custom-black');

const dateFormat = {
    day: "numeric",
    month: "numeric",
    hour: "numeric",
    minute: "numeric"
};

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
                display: false
            },
            // tooltip: {
            //     callbacks: {
            //         label: function (context) {
            //             return context.parsed.y + sensorInformation.unit
            //         }
            //     }
            // },
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
                    // limits: {
                    //     x: {
                    //         min: sensorData[0],
                    //         max: sensorData[sensorData.length - 1]
                    //     }
                    // }
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

                let sensorUnit = sensorElement.tags.toLowerCase().includes('temp')
                    ? 'Temp'
                    : sensorElement.tags.toLowerCase().includes('humid') || sensorElement.tags.toLowerCase().includes('feucht')
                        ? 'Humid'
                        : 'y';

                let sensorData = [];
                Array.from(resultList).forEach((set) => {
                    let entry = {
                        x: `${set.timestamp}`//TODO: Convert timestamp to date-format with pattern yyyy-MM-dd HH:mm:ss
                    };
                    entry['y'] = set.value;//TODO: Use sensorUnit as key
                    sensorData.push(entry);

                    // sensorData.push({
                    //     x: `${set.timestamp}`,//TODO: Convert timestamp to date-format with pattern yyyy-MM-dd HH:mm:ss
                    //     temp: `${set.value}`
                    // })
                });


                let dataset = {
                    label: 'Data-Name',
                    data: sensorData,
                    backgroundColor: cssWhite,
                    borderColor: cssWhite,
                    fill: false,
                    borderWidth: 1,
                    pointRadius: 0.5,
                    lineTension: 0.5,
                    yAxisId: sensorUnit
                };

                console.log(dataset);

                datasets.push(dataset);
                chart.update();

                // res.sort((a, b) => {
                //     b.name.localeCompare(a.name);
                // });
                //
                // res.forEach(s => {
                //     const {protocol, ip, port, name} = s;
                //     const li = getMenuListElement(protocol, ip, port, name, null);
                //     list.insertBefore(li, githubElement);
                // });
            }
        });
    });
}