// import {de} from '/date-fns/locale';
// import {de} from 'chartjs-adapter-luxon';
// import {de} from 'luxon';

// Further reading: https://www.w3schools.com/js/js_graphics_chartjs.asp
// https://www.chartjs.org/docs/latest/samples/scales/time-line.html
// https://github.com/chartjs/Chart.js/blob/master/docs/scripts/utils.js
//TODO: Make the axes use regular steps for irregular values

const dataFromExample = {
    labels: [ // Date Objects
        '2024-12-01', '2024-12-02', '2024-12-03', '2024-12-04', '2024-12-05', '2024-12-06', '2024-12-07'
    ],
    datasets: [{
        label: 'Dataset with point data',
        backgroundColor: "rgb(0, 255, 0, 0.5)",
        borderColor: "rgb(0, 255, 0, 1)",
        fill: false,
        data: [{
            x: new Date(1733253258538).toISOString(),
            y: 10
        }, {
            x: new Date(1733253268538).toISOString(),
            y: 50
        }, {
            x: new Date(1733253278538).toISOString(),
            y: 5
        }, {
            x: new Date(1733253385538).toISOString(),
            y: 38
        }],
    }]
};

const javaData = {
    label: 'Data-Name',
    backgroundColor: "rgb(0, 255, 0, 0.5)",
    borderColor: "rgb(0, 255, 0, 1)",
    fill: false,
    data: sensorData
};

// new Chart("dataChart", {
//     type: "line",
//     data: {
//         labels: ["a", "b"],
//         datasets: [
//             {
//                 label: "First data",
//                 data: [
//                     {
//                         x: "a",
//                         y: 3
//                     }
//                 ]
//             }
//         ]
//     }
// })

// new Chart("dataChart", {
//     type: "line",
//     data: javaData,
//     options: {
//         scales: {
//             x: {
//                 type: "time",
//                 time: {
//                     tooltipFormat: 'DD T'
//                 },
//                 title: {
//                     display: true,
//                     text: 'Date'
//                 },
//                 adapters: {
//                     date: {
//                         locale: luxon
//                     }
//                 }
//             },
//             y: {
//                 title: {
//                     display: true,
//                     text: 'value'
//                 }
//             }
//         }
//     }
// })

new Chart(document.getElementById("dataChart"), {
    type: 'line',
    options: {
        scales: {
            x: {
                // type: 'time',
                // distribution: 'linear',
                title: {
                    display: true,
                    text: 'Date'
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'Value'
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
        datasets: [{
            data: sensorData,
            backgroundColor: [
                'rgba(255,255,255)'
            ],
            borderColor: [
                'rgb(255,255,255)'
            ],
            borderWidth: 1,
            pointRadius: 0.5
        }]
    }
})