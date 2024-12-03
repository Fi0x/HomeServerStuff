function searchFunction() {
    var input, filter, table, rows, td, i, txtValue;
    input = document.getElementById("searchText");
    filter = input.value.toUpperCase();
    table = document.getElementById("searchableTable");
    rows = table.getElementsByTagName("tr");

    for (i = 0; i < rows.length; i++) {
        td = rows[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    }
}

// Further reading: https://www.w3schools.com/js/js_graphics_chartjs.asp
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
new Chart("dataChart", {
    type: "line",
    data: {
        // labels: xValues,
        datasets: [
            {
                backgroundColor: "rgba(100, 100, 255, 1.0)",
                borderColor: "rgba(0, 0, 255, 0.5)",
                data: combinedData,
                fill: false
            }
        ]
    },
    options: {
        scales: {
            // yAxes: [{
            //     ticks: {
            //         min: 10,
            //         max: 30
            //     }
            // }],
            xAxes: {
                type: "time", //TODO: Get this to work
                time: {
                    unit: 'day'
                }
            }
        }
        // parsing: {
        //     xAxisKey: 'x',
        //     yAxisKey: 'y'
        // }
    }
})