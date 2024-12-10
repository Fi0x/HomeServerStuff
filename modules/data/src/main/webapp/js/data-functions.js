function searchFunction() {
    let input = document.getElementById("searchText");
    let filter = input.value.toUpperCase();
    let table = document.getElementById("searchableTable");
    let rows = table.getElementsByTagName("tr");

    showOrHideRow(rows, filter);
}

function showOrHideRow(rows, filter) {
    for (let i = 0; i < rows.length; i++) {
        let td = rows[i].getElementsByTagName("td")[0];
        if (td) {
            let txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    }
}

function updateFilterState() {
    let table = document.getElementById("searchableTable");
    let allOptions = document.getElementsByClassName("filter-option");
    let validFilters = [];
    for (let i = 0; i < allOptions.length; i++) {
        let checkbox = allOptions[i].getElementsByTagName('input')[0];
        if (checkbox.checked) {
            validFilters.push(allOptions[i]);
        }
    }

    let rows = table.getElementsByClassName('clickable-row');
    for (let rowIdx = 0; rowIdx < rows.length; rowIdx++) {
        if (validFilters.length < 1) {
            rows[rowIdx].style.display = "";
        } else {
            let tds = rows[rowIdx].getElementsByClassName("filter-tag");
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
            if (valid)
                rows[rowIdx].style.display = "";
            else
                rows[rowIdx].style.display = "none";
        }
    }
}