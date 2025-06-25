function searchFunction() {
    var input, filter, table, rows, tds, i, txtValue;
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

function addCertificate() {
    let textField = document.getElementById("newCertificateText");
    let certificateId = textField.value;

    $.ajax({
        type: 'GET',
        url: `${baseUrl}/orc/add/${certificateId}`,
        dataType: 'json',
        success: function (res) {
            //TODO: Maybe update the orc-certificate-list with the new data
            console.log(res);
        }
    });
}

function addRace() {
    let textField = document.getElementById("newRaceUrl");
    let url = textField.value;

    $.post(`${baseUrl}/race/add`, url, function (res) {
        //TODO: Maybe update the result-list with the new data
        console.log(res);
    });
}

function fillRaceResults() {
    let scorePairs = [];
    for (let result of raceResults) {
        let combinedId = result.shipName.replace(/\s/g, '') + result.skipper.replace(/\s/g, '');

        let totalPoints = 0;
        for (let singleResult of result.singleRaceResults) {
            let raceId = singleResult.raceName.replace(/\s/g, '') + singleResult.raceGroup.replace(/\s/g, '');
            console.log(`${combinedId}position${raceId}`);
            document.getElementById(`${combinedId}position${raceId}`).innerText = singleResult.position;

            let race = races.find(r => r.raceName === singleResult.raceName && r.raceGroup === singleResult.raceGroup);
            document.getElementById(`${combinedId}points${raceId}`).innerText = singleResult.score.toFixed(1).toString();
            totalPoints = totalPoints + Number(singleResult.score);
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