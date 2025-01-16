const cssWhite = getComputedStyle(document.body).getPropertyValue('--custom-white');
const cssBlack = getComputedStyle(document.body).getPropertyValue('--custom-black');

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
        datasets: []
    }
});

function loadChartData(sensorList) {
    for (let sensorElement in sensorList) {
        $.ajax({
            // TODO: Ensure this string converts correctly to a valid url
            url: `$\{pageContext.request.contextPath\}/api/data/$\{sensorElement.address\}/$\{sensorElement.name\}`,
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                // TODO: Convert result to sensorData and put it into dataset

                console.log("Result fetched successfully: " + res);
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
    }
}