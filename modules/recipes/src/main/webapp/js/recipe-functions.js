const listSizes =
    {
        'ingredients': -1,
        'tags': -1
    };

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
    // list.splice(idx, 1);
    console.log("New list: " + list);
    //TODO
}

function addElement(listSize, listType) {
    if (listSizes[listType] < 0)
        listSizes[listType] = listSize;
    else
        listSizes[listType]++;

    let buttonParagraph = document.getElementById(`${listType}Btn`)
    let parent = buttonParagraph.parentElement;
    let newParagraph = document.createElement('p');

    let newForm = document.createElement('input');
    newForm.id = listType + listSizes[listType];
    newForm.name = `${listType}[${listSizes[listType]}]`;
    newForm.classList.add('long-input');
    newForm.type = 'text';
    newForm.value = '';

    let newAnchor = document.createElement('a');
    newAnchor.classList.add('btn-danger');
    newAnchor.classList.add('round-button');
    newAnchor.setAttribute('onclick', `deleteElement(${null}, ${listSizes[listType]})`); //TODO: Change to correct recipe-list
    newAnchor.innerText = 'X';

    newParagraph.append(newForm);
    newParagraph.append(newAnchor);
    parent.insertBefore(newParagraph, buttonParagraph);
}