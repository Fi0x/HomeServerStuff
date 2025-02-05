const dateOptions = {day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit'}

function updateFilterState() {
    let table = document.getElementById("searchableTable");
    let allOptions = document.getElementsByClassName("filter-option");
    let stringFilter = document.getElementById("searchText").value.toUpperCase();
    let validFilters = [];
    for (let option of allOptions) {
        let checkbox = option.getElementsByTagName('input')[0];
        if (checkbox.checked) {
            validFilters.push(option);
        }
    }

    for (let row of table.getElementsByClassName('clickable-row')) {
        filterRow(validFilters, row, stringFilter);
    }
}

function filterRow(validFilters, row, stringFilter) {
    let visible = "";

    let td = row.getElementsByTagName("td")[1];
    if (td) {
        let txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(stringFilter) > -1) {
        } else {
            visible = "none";
        }
    }
    if (validFilters.length > 0) {
        let tds = row.getElementsByClassName("filter-tag");
        let valid = false;

        for (let tdIdx = 0; tdIdx < tds.length; tdIdx++) {
            let txtValue = tds[tdIdx].textContent || tds[tdIdx].innerText;

            for (let filterIdx = 0; filterIdx < validFilters.length; filterIdx++) {
                let filterValue = validFilters[filterIdx].textContent || validFilters[filterIdx].innerText;
                if (txtValue.indexOf(filterValue.trim()) > -1) {
                    valid = true;
                    break;
                }
            }
            if (valid)
                break;
        }
        if (!valid)
            visible = "none";
    }

    if (row.style.display === visible)
        return;

    row.style.display = visible;

    let checkbox = row.getElementsByClassName('chart-view-checkbox')[0];
    if (!checkbox.checked)
        return;

    let columns = row.getElementsByTagName('td');
    let id = columns[2].innerText + " " + columns[1].innerText;
    let dataset = datasets.find((object) => object.id === id);
    if (!dataset)
        return;

    dataset.hidden = visible !== "";
    chart.update();
}

function selectAllCheckboxes(mainCheckbox) {
    let select = mainCheckbox.checked;
    let checkboxes = document.getElementsByClassName('chart-view-checkbox');
    for (let checkbox of checkboxes) {
        if (checkbox.checked !== select)
            checkbox.click();
    }
}

function selectCheckbox(label, e, id) {
    e.stopPropagation();
    let checkbox = label.getElementsByTagName('input')[0];
    let dataset = datasets.find((object) => object.id === id);

    if (!dataset)
        return;

    let row = label.parentNode.parentNode;
    dataset.hidden = !checkbox.checked || row.style.display === 'none';
    chart.update();
}

function nextChartColor(colorElement, e) {
    e.stopPropagation();
    let sensorId = colorElement.id.replace("color-span", "");
    let currentBackground = rgbToHex(colorElement.style.background);
    let newColorIdx = (lineColors.indexOf(currentBackground) + 1) % lineColors.length;

    if (newColorIdx < 0)
        return;

    let dataset = datasets.find((object) => object.id === sensorId);

    if (!dataset)
        return;

    let newColor = lineColors[newColorIdx];
    colorElement.style.background = newColor;
    dataset.backgroundColor = newColor;
    dataset.borderColor = newColor;
    chart.update();
}

function rgbToHex(rgb) {
    if (/^#[0-9A-F]{6}$/i.test(rgb))
        return rgb;

    rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);

    function hex(x) {
        return ("0" + parseInt(x).toString(16)).slice(-2);
    }

    return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
}

function subscribeToDataUpdates(functionToRun) {
    const eventSource = new EventSource(`${baseUrl}/subscribe`);
    eventSource.onmessage = e => {
        functionToRun(JSON.parse(e.data));
    };
    eventSource.onopen = () => {
    };
    eventSource.onerror = e => {
        if (e.readyState === EventSource.CLOSED) {
            console.log('live-connection to server lost');
        } else {
            console.log(e);
        }
    };
}

function newDataForSingleSensor(extendedDataDto) {
    console.log(extendedDataDto);
    // TODO: Update chart for sensor
    //  Update table for sensor (Add new line at top; set correct color for new line; update color of last line if necessary)
}

function newDataForSensorList(extendedDataDto) {
    let correctRow = document.getElementById(`sensorRow${extendedDataDto.address}${extendedDataDto.sensorName}`)
    if (correctRow) {
        let entries = correctRow.children;
        entries[3].innerText = extendedDataDto.value;
        entries[4].innerText = new Date(extendedDataDto.timestamp).toLocaleDateString('de-DE', dateOptions);
        correctRow.classList.remove('yellow', 'red');
        if (Date.now() - extendedDataDto.timestamp > extendedDataDto.delay * 2) {
            correctRow.classList.add('yellow');
        } else if (extendedDataDto.value < extendedDataDto.min || extendedDataDto.value > extendedDataDto.max) {
            correctRow.classList.add('red');
        }
        let dataset = datasets.find((set) => set.id === extendedDataDto.address + " " + extendedDataDto.sensorName);
        let timestampedDate = new Date(extendedDataDto.timestamp).toISOString();
        if (dataset) {
            dataset.data.push({
                x: `${timestampedDate}`,
                y: extendedDataDto.value
            });
        }
        chart.update();
    } else {
        console.log("Could not find sensor in list");
    }
}