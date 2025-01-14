// import {de} from '/date-fns/locale';
// import {de} from 'chartjs-adapter-luxon';
// import {de} from 'luxon';

// Further reading: https://www.w3schools.com/js/js_graphics_chartjs.asp
// https://www.chartjs.org/docs/latest/samples/scales/time-line.html
// https://github.com/chartjs/Chart.js/blob/master/docs/scripts/utils.js
//TODO: Make the axes use regular steps for irregular values

const cssWhite = getComputedStyle(document.body).getPropertyValue('--custom-white');
const cssBlack = getComputedStyle(document.body).getPropertyValue('--custom-black');

const javaData = {
    // label: 'Data-Name',
    data: sensorData,
    backgroundColor: cssWhite,
    borderColor: cssWhite,
    fill: false,
    borderWidth: 1,
    pointRadius: 0.5,
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
                // type: 'time',
                time: {
                    tooltipFormat: 'DD T'
                },
                // distribution: 'linear',
                // title: {
                //     display: true,
                //     text: 'Date'
                // },
                ticks: {
                    color: cssWhite,
                    callback: function (value, index, values) {
                        return new Date(sensorData[index].x).toLocaleDateString(undefined, dateFormat);
                    }
                },
                grid: {
                    color: cssBlack
                }
                // adapters: {
                //     date: {
                //         locale: luxon
                //     }
                // }
            },
            y: {
                // title: {
                //     display: true,
                //     text: 'Value'
                // },
                ticks: {
                    color: cssWhite,
                    callback: function (value, index, values) {
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
            }
        }
    },
    data: {
        datasets: [javaData]
    }
})