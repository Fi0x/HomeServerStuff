// import {de} from 'date-fns/locale';

// Further reading: https://www.w3schools.com/js/js_graphics_chartjs.asp
// https://www.chartjs.org/docs/latest/samples/scales/time-line.html
// https://github.com/chartjs/Chart.js/blob/master/docs/scripts/utils.js
//TODO: Make the axes use regular steps for irregular values
const xValues = [1733253258538, 1733253268538, 1733253278538, 1733253280538, 1733253281538, 1733253285538, 1733253385538, 1733253395538, 1733253405538, 1733253455538, 1733253465538, 1733253468538]; //TODO: Change these to the timestamps from the data-object
const yValues = [15, 22, 23, undefined, 24, 20, 18, 20, 21, 21, 22, 20]; //TODO: Change these to the measurements from the data-object
const combinedData = [
    {x: '2024-12-01', y: 15},
    {x: '2024-12-02', y: 22},
    {x: '2024-12-04', y: 23},
    // {x: 1733253280538, y: undefined},
    {x: '2024-12-07', y: 24},
    {x: '2024-12-20', y: 20}];
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

new Chart("dataChart", {
    type: "line",
    data: sensorData,
    options: {
        scales: {
            x: {
                type: "time",
                time: {
                    tooltipFormat: 'DD T'
                },
                title: {
                    display: true,
                    text: 'Date'
                },
                adapters: {
                    date: {
                        locale: de
                    }
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'value'
                }
            }
        }
    }
})