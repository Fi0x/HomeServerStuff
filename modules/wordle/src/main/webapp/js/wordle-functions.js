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

function updateSaveState(originalWord, text, isAlreadySaved, listIndex) {
    if (originalWord !== text || !isAlreadySaved) {
        document.getElementById("deleteButton" + listIndex).style.visibility = 'hidden';
        document.getElementById("saveButton" + listIndex).style.visibility = 'visible';
    }
}

function updateRealState(checkbox) {
    let elements = document.getElementsByClassName("onlyFictionalLanguageOption");
    for (let i = 0; i < elements.length; i++) {
        if (elements[i].type === "checkbox") {
            elements[i].disabled = checkbox.checked;
            continue;
        }
        elements[i].readOnly = checkbox.checked;
        if (checkbox.checked) {
            elements[i].classList.add("non-editable");
        } else {
            elements[i].classList.remove("non-editable")
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

    let rows = table.getElementsByTagName('tr');
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