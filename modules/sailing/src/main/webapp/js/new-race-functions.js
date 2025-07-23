const shipRaceResults = [];

function loadClassResults(button) {
    button.parentNode.removeChild(button);

    let tableParent = document.getElementById("classResultTableParent");
    for (let i = 0; i < raceClasses.length; i++) {
        if (document.getElementById(`classSelection${i}`).checked) {
            let tableTopic = document.createElement("h3");
            tableTopic.innerText = raceClasses[i].className;
            tableParent.appendChild(tableTopic);
            let table = document.createElement("table");
            table.classList.add("table");
            table.classList.add("top-margin");
            table.classList.add("full-width");
            let thead = document.createElement("thead");
            let headRow = document.createElement("tr");
            headRow.classList.add("underlined-row");
            addTableHeader(headRow, "Group");
            addTableHeader(headRow, "Position");
            addTableHeader(headRow, "Skipper");
            addTableHeader(headRow, "Ship-name");
            addTableHeader(headRow, "Boat-class");
            let checkmarkTopicCell = document.createElement("th");
            checkmarkTopicCell.classList.add("align-text-center")
            let tbody = document.createElement("tbody");
            let checkmarkTopic = document.createElement("input");
            checkmarkTopic.type = "checkbox";
            checkmarkTopic.checked = true;
            checkmarkTopic.onclick = () => {
                for (let row of tbody.children) {
                    row.children[row.children.length - 1].children[0].checked = checkmarkTopic.checked;
                }
            }
            checkmarkTopicCell.appendChild(checkmarkTopic);
            headRow.appendChild(checkmarkTopicCell);
            thead.appendChild(headRow);
            table.appendChild(thead);
            table.appendChild(tbody);
            tableParent.appendChild(table);

            fetch(`${baseUrl}/race/load`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(raceClasses[i])
            }).then(response => {
                if (!response.ok)
                    throw "Could not load results for class: " + raceClasses[i].className;
                response.json().then(jsonResponse => {
                    jsonResponse.forEach(element => addResultRow(tbody, element));
                });
            }).catch(error => {
                alert(error);
            });
        }

        document.getElementById("saveSelectedButton").style.display = '';
    }
}

function addTableHeader(row, text) {
    let topic = document.createElement("th");
    topic.innerText = text;
    topic.classList.add("align-text-center")
    row.appendChild(topic);
}

function addResultRow(tbody, shipResult) {
    let row = document.createElement("tr");
    addCell(row, shipResult.raceGroup);
    addCell(row, shipResult.position);
    addCell(row, shipResult.skipper);
    addCell(row, shipResult.shipName);
    addCell(row, shipResult.shipClass);
    let checkmarkCell = document.createElement("td");
    checkmarkCell.classList.add("align-text-center");
    let checkmark = document.createElement("input");
    checkmark.type = "checkbox";
    checkmark.checked = true;
    checkmark.id = `${shipRaceResults.length}`;
    checkmarkCell.appendChild(checkmark);
    row.appendChild(checkmarkCell);

    shipRaceResults.push(shipResult);

    //Example races:
    // https://www.manage2sail.com/de-DE/event/7da1f04b-bd3a-4068-8d31-4ecf17bdc1bb#!/
    // https://www.manage2sail.com/de-DE/event/6695807a-a06f-49d5-863d-39c96c82d6cf#!/

    tbody.appendChild(row);
}

function addCell(parent, text) {
    let groupCell = document.createElement("td");
    groupCell.classList.add("align-text-center");
    groupCell.innerText = text;
    parent.appendChild(groupCell);
}

function saveSelectedResults(button) {
    button.parentNode.removeChild(button);

    let rowsToSave = [];
    for (let tbody of document.getElementById("classResultTableParent").getElementsByTagName("tbody")) {
        for (let row of tbody.children) {
            let checkbox = row.children[row.children.length - 1].children[0];
            let checked = checkbox.checked;
            if (!checked)
                continue;
            let raceResult = shipRaceResults[checkbox.id];
            rowsToSave.push(raceResult);
        }
    }

    fetch(`${baseUrl}/race/save`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(rowsToSave)
    }).then(response => {
        if (!response.ok)
            throw "Could not save race-results";
        location.replace(`${baseUrlNormal}/race-list`);
    }).catch(error => {
        alert(error);
    });
}