function searchFunction() {
    let input, filter, table, rows, tds, i, txtValue;
    input = document.getElementById("searchText");
    filter = input.value.toUpperCase();
    table = document.getElementById("searchableTable");
    rows = table.getElementsByTagName("tr");

    for (i = 1; i < rows.length; i++) {
        tds = rows[i].getElementsByTagName("td");
        let match = false;
        if (filter === "")
            match = true;
        else {
            for (let j = 0; j <= 3; j++) {
                if (tds[j]) {
                    txtValue = tds[j].textContent || tds[j].innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        match = true;
                        break;
                    }
                }
            }
        }
        if (match) {
            rows[i].style.display = "";
        } else {
            rows[i].style.display = "none";
        }
    }
}

function fillRaceResults() {
    let scorePairs = [];
    for (let result of raceResults) {
        let combinedId = result.shipName.replace(/\s/g, '') + result.skipper.replace(/\s/g, '');

        let totalPoints = 0;
        for (let singleResult of result.singleRaceResults) {
            let raceId = singleResult.raceName.replace(/\s/g, '') + singleResult.raceGroup.replace(/\s/g, '');
            let positionElement = document.getElementById(`${combinedId}position${raceId}`);
            let scoreElement = document.getElementById(`${combinedId}points${raceId}`);

            document.getElementById(`${combinedId}button${raceId}`).style.display = '';

            positionElement.innerText = singleResult.position;
            scoreElement.innerText = singleResult.score.toFixed(1).toString();
            if (singleResult.crossed) {
                positionElement.classList.add("crossed");
                scoreElement.classList.add("crossed");
            } else {
                totalPoints += Number(singleResult.score);
            }
        }

        document.getElementById(`${combinedId}pointsTotal`).innerText = totalPoints.toFixed(1).toString();

        let positionElement = document.getElementById(`${combinedId}positionOverall`);
        scorePairs.push({
            score: totalPoints,
            element: positionElement,
            rowElement: positionElement.parentElement
        });
    }

    let tableBody = document.getElementsByTagName("tbody").item(0);
    while (tableBody.firstChild) {
        tableBody.removeChild(tableBody.lastChild);
    }

    scorePairs.sort((a, b) => b.score - a.score);
    for (let i = 1; i < scorePairs.length; i++) {
        scorePairs[i - 1].element.innerText = i;
        tableBody.appendChild(scorePairs[i - 1].rowElement);
    }
}

function deleteResult(raceName, date, group, skipper, shipName, button, deleteRow = false) {
    button.style.display = 'none';

    fetch(`${baseUrl}/race/remove/${raceName}/${date}/${group}?skipper=${skipper}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.status !== 200) {
            alert("Could not delete result (" + response.status + ")");
            button.style.display = '';
        } else {
            if (deleteRow) {
                button.parentElement.parentElement.remove();
            } else {
                let combinedId = shipName.replace(/\s/g, '') + skipper.replace(/\s/g, '');
                let raceId = raceName.replace(/\s/g, '') + group.replace(/\s/g, '');
                document.getElementById(`${combinedId}position${raceId}`).innerText = '';
                document.getElementById(`${combinedId}points${raceId}`).innerText = '';
            }
        }
    });
}

function toggleOrcRaces(checkbox) {
    let state = checkbox.checked ? 'none' : '';
    let orcCells = document.getElementsByClassName("orcfalse");
    for (let cell of orcCells) {
        cell.style.display = state;
    }
}