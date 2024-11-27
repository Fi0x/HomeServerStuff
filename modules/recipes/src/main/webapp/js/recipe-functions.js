function searchFunction() {
    let input, filter, table, rows, td, i, txtValue;
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

function updateFilterState() {
    console.log("Filtering")
    //TODO
}

function deleteElement(list, idx) {
    console.log("Deleting from list " + list + " index " + idx);
    let jsList = ["A", "B", "C"];
    console.log("This is a list: " + jsList);
    jsList.splice(0, 1);
    console.log("The js list: " + jsList);
    list.splice(idx, 1);
    console.log("New list: " + list);
    //TODO
}

function addElement(recipe, listType) {
    console.log("Adding to list " + listType);
    console.log(recipe);
    //TODO
}