function loadRaceList() {
    let table = document.getElementById("racesTable");
    for (let i = 0; i < races.length; i++) {
        let race = races[i];
        let tbodyId = race.startDate.substring(race.startDate.length - 4);
        let tbody = document.getElementById(tbodyId);
        if (!tbody) {
            tbody = document.createElement("tbody");
            tbody.id = tbodyId;
            table.appendChild(tbody);
            let headerRow = document.createElement("tr");
            tbody.appendChild(headerRow);
            let subtitle = document.createElement("th");
            subtitle.innerText = tbodyId;
            subtitle.colSpan = 10;
            subtitle.classList.add("align-text-center")
            headerRow.appendChild(subtitle);
        }
        createRaceRow(tbody, race, i);
    }
}

function createRaceRow(tbody, race, index) {
    let row = document.createElement("tr");
    tbody.appendChild(row);
    createInputCell(row, `raceName${index}`, race.raceName);
    createInputCell(row, `raceGroup${index}`, race.raceGroup);
    createTextCell(row, race.startDate);
    createCheckboxCell(row, `raceBuffer${index}`, race.bufferRace);
    createCheckboxCell(row, `raceOrc${index}`, race.orcRace);
    let participantCell = createInputCell(row, `raceParticipants${index}`, race.participants);
    participantCell.classList.add("align-text-center");
    participantCell.children[0].type = "number";
    let scoreCell = createInputCell(row, `raceScore${index}`, race.scoreModifier);
    scoreCell.classList.add("align-text-center");
    scoreCell.children[0].type = "number";
    let deleteButton = createButtonCell(row, `deleteButton${index}`, "btn-danger", "Delete");
    deleteButton.onclick = () => {
        deleteButton.hidden = true;
        deleteButton.parentNode.parentNode.classList.add("red");
    };
    let updateButton = createButtonCell(row, `updateButton${index}`, "btn-edit", "Save Changes");
    updateButton.onclick = () => updateRace(index, `${race.raceName}`, `${race.longDate}`, `${race.raceGroup}`);
    if (race.url !== null) {
        let reloadButton = createButtonCell(row, `reloadButton${index}`, "btn", "Reload Results");
        reloadButton.onclick = () => location.replace(`${baseUrlNormal}/race/new?raceOverviewUrl=${encodeURIComponent(race.url)}`);
    }
}

function createInputCell(row, id, text) {
    let cell = document.createElement("td");
    row.appendChild(cell);
    let input = document.createElement("input");
    input.id = id;
    input.value = text;
    cell.appendChild(input);
    return cell;
}


function createTextCell(row, text) {
    let cell = document.createElement("td");
    cell.innerText = text;
    row.appendChild(cell);
    return cell;
}

function createCheckboxCell(row, id, checked) {
    let cell = document.createElement("td");
    cell.classList.add("align-text-center");
    row.appendChild(cell);
    let checkbox = document.createElement("input");
    checkbox.id = id;
    checkbox.type = "checkbox";
    checkbox.checked = checked;
    cell.appendChild(checkbox);
}

function createButtonCell(row, id, elementClass, text) {
    let cell = document.createElement("td");
    cell.classList.add("align-content-center");
    row.appendChild(cell);
    let anchor = document.createElement("a");
    anchor.id = id;
    anchor.classList.add(elementClass);
    anchor.innerText = text;
    cell.appendChild(anchor);
    return anchor;
}

function updateRace(index, name, date, group) {
    let deleteFlag = document.getElementById(`deleteButton${index}`).hasAttribute('hidden');
    if (deleteFlag) {
        fetch(`${baseUrl}/race/remove/${name}/${date}/${group}`, {
            method: 'DELETE'
        }).then(response => {
            if (response.status !== 200)
                alert("Could not delete race (" + response.status + ")");
            else
                location.reload();
        });
        return;
    }

    let dto = {
        name: `${document.getElementById('raceName' + index).value}`,
        raceGroup: `${document.getElementById('raceGroup' + index).value}`,
        scoreModifier: `${document.getElementById('raceScore' + index).value}`,
        orcRace: `${document.getElementById('raceOrc' + index).checked}`,
        bufferRace: `${document.getElementById('raceBuffer' + index).checked}`,
        participants: `${document.getElementById('raceParticipants' + index).value}`
    };
    fetch(`${baseUrl}/race/update/${name}/${date}/${group}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dto)
    }).then(response => {
        if (!response.ok)
            alert("Could not update race-information (" + response.status + ")");
        else
            location.reload();
    })
}

function addRace() {
    let textField = document.getElementById("newRaceUrl");
    let url = textField.value;

    location.replace(`${baseUrlNormal}/race/new?raceOverviewUrl=${encodeURIComponent(url)}`);
}