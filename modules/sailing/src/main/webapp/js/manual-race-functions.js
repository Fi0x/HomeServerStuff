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