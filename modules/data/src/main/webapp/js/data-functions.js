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
const xValues = [0, 1, 2, 3, 4, 5, 7, 10, 20, 21, 23, 24]; //TODO: Change these to the timestamps from the data-object
const yValues = [15, 22, 23, undefined, 24, 20, 18, 20, 21, 21, 22, 20]; //TODO: Change these to the measurements from the data-object
new Chart("dataChart", {
    type: "line",
    data: {
        // labels: xValues,
        datasets: [
            {
                backgroundColor: "rgba(100, 100, 255, 1.0)",
                borderColor: "rgba(0, 0, 255, 0.5)",
                data: yValues,
                fill: false
            }
        ]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    min: 10,
                    max: 30
                }
            }],
            x: {
                type: "timeseries" //TODO: Get this to work
            }
        }
    }
})