function loadDateFields() {
    for (let input of document.getElementsByClassName("date-input")) {
        input.valueAsDate = new Date();
    }
}

function saveNewRaceInfo() {
    let raceName = document.getElementById("raceName").value;
    let raceGroup = document.getElementById("raceGroup").value;
    let startDate = new Date(document.getElementById("startDate").value).getTime();

    let dto = getRaceInfoDto(raceName, startDate, raceGroup);

    fetch(`${baseUrl}/race/save/info`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dto)
    }).then(response => {
        if (!response.ok)
            throw "Could not save race (" + response.status + ")";

        window.location.replace(`${baseUrlNormal}/race/${raceName}/${startDate}/${raceGroup}/edit`);
    }).catch(error => {
        alert(error);
    });
}

function getRaceInfoDto(raceName, startDate, raceGroup) {
    let endDate = new Date(document.getElementById("endDate").value).getTime();
    let bufferRace = document.getElementById("bufferRace").checked;
    let orcRace = document.getElementById("orcRace").checked;
    let participants = document.getElementById("participants").value;
    let scoreModifier = document.getElementById("scoreModifier").value;

    return {
        name: raceName,
        longDate: startDate,
        endDate: endDate,
        raceGroup: raceGroup,
        scoreModifier: scoreModifier,
        orcRace: orcRace,
        bufferRace: bufferRace,
        participants: participants,
    };
}

function saveModifiedResults() {
    let tableBody = document.getElementById("resultTableBody");
    let dtoList = [];
    for (let row of tableBody.children) {
        let inputs = row.getElementsByTagName("input");

        let dto = {
            name: `${raceInfo.name}`,
            startDate: `${raceInfo.startDate}`,
            raceGroup: `${inputs[3].value}`,
            skipper: `${inputs[1].value}`,
            endDate: `${raceInfo.endDate}`,
            url: `${raceInfo.url}`,
            shipName: `${inputs[0].value}`,
            position: `${inputs[4].value}`,
            shipClass: `${inputs[2].value}`
        };
        dtoList.push(dto);
    }

    console.log(dtoList);

    fetch(`${baseUrl}/race/save`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dtoList)
    }).then(response => {
        if (!response.ok)
            throw "Could not save updated race-results";
        location.replace(`${baseUrlNormal}/race-list`);
    }).catch(error => {
        alert(error);
    });
}

function createNewResultRow() {
    let body = document.getElementById("resultTableBody");
    let row = document.createElement("tr");
    body.appendChild(row);
    createTextInput(row, "Ship Name");
    createTextInput(row, "Skipper Name");
    createTextInput(row, "Ship Class");
    let raceGroupInput = createTextInput(row, "Race Group");
    raceGroupInput.value = raceInfo.raceGroup;
    let positionInput = createTextInput(row, "1");
    positionInput.value = "1";
    positionInput.type = "number";
    let deleteButtonCell = document.createElement("td");
    let deleteButton = document.createElement("a");
    deleteButton.classList.add("btn");
    deleteButton.classList.add("btn-danger")
    deleteButton.innerText = "Delete";
    deleteButton.onclick = () => {
        row.parentElement.removeChild(row);
    }
    deleteButtonCell.appendChild(deleteButton);
    row.appendChild(deleteButtonCell);
}

function createTextInput(parent, placeholder) {
    let cell = document.createElement("td");
    let inputElement = document.createElement("input");
    inputElement.placeholder = placeholder;
    cell.appendChild(inputElement);
    parent.appendChild(cell);

    return inputElement;
}