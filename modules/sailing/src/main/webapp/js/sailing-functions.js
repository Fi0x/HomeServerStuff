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
        success: function () {
            location.reload();
        }
    });
}

function addRace() {
    let textField = document.getElementById("newRaceUrl");
    let url = textField.value;

    $.post(`${baseUrl}/race/add`, url, function () {
        location.reload();
    });
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

function selectCertificate(certificateId) {
    let selectedCertificate = certificates.find(c => c.id === certificateId);

    for (let cert of certificates) {
        let row = document.getElementById(`cert${cert.id}`);
        row.classList.remove('selection');

        setTimeDifferenceOnElements(selectedCertificate.singleNumber - cert.singleNumber, `single${cert.id}`);
        setTimeDifferenceOnElements(selectedCertificate.tripleLongLow - cert.tripleLongLow, `trilolow${cert.id}`);
        setTimeDifferenceOnElements(selectedCertificate.tripleLongMid - cert.tripleLongMid, `trilomid${cert.id}`);
        setTimeDifferenceOnElements(selectedCertificate.tripleLongHigh - cert.tripleLongHigh, `trilohigh${cert.id}`);
        setTimeDifferenceOnElements(selectedCertificate.tripleUpDownLow - cert.tripleUpDownLow, `triuplow${cert.id}`);
        setTimeDifferenceOnElements(selectedCertificate.tripleUpDownMid - cert.tripleUpDownMid, `triupmid${cert.id}`);
        setTimeDifferenceOnElements(selectedCertificate.tripleUpDownHigh - cert.tripleUpDownHigh, `triuphigh${cert.id}`);
    }
    document.getElementById(`cert${certificateId}`).classList.add('selection');
}

function setTimeDifferenceOnElements(diff, elementId) {
    let minutes = (diff * 60).toFixed(0).toString();
    let seconds = ((diff * 60 - minutes) * 60).toFixed(0).toString();
    let element = document.getElementById(elementId);

    element.innerText = `${minutes} min ${seconds}s`;
    if (diff > 0)
        element.classList = ['red-text-bold text-nowrap'];
    else if (diff < 0)
        element.classList = ['green-text-bold text-nowrap'];
    else
        element.classList = ['text-nowrap'];
}