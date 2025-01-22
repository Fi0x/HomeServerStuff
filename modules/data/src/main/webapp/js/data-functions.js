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