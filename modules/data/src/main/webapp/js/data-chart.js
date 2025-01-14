// import {de} from '/date-fns/locale';
// import {de} from 'chartjs-adapter-luxon';
// import {de} from 'luxon';

// Further reading: https://www.w3schools.com/js/js_graphics_chartjs.asp
// https://www.chartjs.org/docs/latest/samples/scales/time-line.html
// https://github.com/chartjs/Chart.js/blob/master/docs/scripts/utils.js
//TODO: Make the axes use regular steps for irregular values

const javaData = {
    label: 'Data-Name',
    data: sensorData,
    backgroundColor: "rgb(255,255,255)",
    borderColor: "rgb(255,255,255)",
    fill: false,
    borderWidth: 1,
    pointRadius: 0.5,
    lineTension: 0.5
};

let valueUnit = "%";
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
                title: {
                    display: true,
                    text: 'Date'
                },
                ticks: {
                    callback: function (value, index, values) {
                        return new Date(sensorData[index].x).toLocaleDateString(undefined, dateFormat);
                    }
                }
                // adapters: {
                //     date: {
                //         locale: luxon
                //     }
                // }
            },
            y: {
                title: {
                    display: true,
                    text: 'Value'
                },
                ticks: {
                    callback: function (value, index, values) {
                        return value + valueUnit;
                    }
                }
            }
        },
        plugins: {
            legend: {
                display: false
            }
        }
    },
    data: {
        datasets: [javaData]
    }
})