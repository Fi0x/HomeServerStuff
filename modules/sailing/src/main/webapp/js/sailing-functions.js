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

    location.replace(`${baseUrlNormal}/race/new?raceOverviewUrl=${encodeURIComponent(url)}`);
}

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
            let thead = document.createElement("thead");
            let headRow = document.createElement("tr");
            headRow.classList.add("underlined-row");
            addTableHeader(headRow, "Group");
            addTableHeader(headRow, "Skipper");
            addTableHeader(headRow, "Ship-name");
            addTableHeader(headRow, "Boat-class");
            thead.appendChild(headRow);
            table.appendChild(thead);
            let tbody = document.createElement("tbody");
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

    let group = document.createElement("td");
    group.innerText = shipResult.raceGroup;
    group.classList.add("align-text-center");
    tbody.appendChild(group);
    let skipper = document.createElement("td");
    skipper.innerText = shipResult.skipper;
    skipper.classList.add("align-text-center");
    tbody.appendChild(skipper);
    let ship = document.createElement("td");
    ship.innerText = shipResult.shipName;
    ship.classList.add("align-text-center");
    tbody.appendChild(ship);
    let boatClass = document.createElement("td");
    boatClass.innerText = shipResult.shipClass;
    boatClass.classList.add("align-text-center");
    tbody.appendChild(boatClass);

    //TODO Make rows selectable to decide which results to keep and which to remove

    //Example races:
    // https://www.manage2sail.com/de-DE/event/7da1f04b-bd3a-4068-8d31-4ecf17bdc1bb#!/
    // https://www.manage2sail.com/de-DE/event/6695807a-a06f-49d5-863d-39c96c82d6cf#!/

    tbody.appendChild(row);
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

function selectCertificate(certificateId) {
    let selectedCertificate = certificates.find(c => c.id === certificateId);

    if (!selectedCertificate)
        return;

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

function deleteCertificate(certId) {

    let idx = certificates.indexOf(certificates.find(c => c.id === certId));
    if (idx >= 0)
        certificates.splice(idx, 1);

    $.post(`${baseUrl}/orc/remove/${certId}`);

    let row = document.getElementById(`cert${certId}`);
    row.parentElement.removeChild(row);
}

function deleteRace(button) {
    button.hidden = true;
    button.parentNode.parentNode.classList.add("red");
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
        bufferRace: `${document.getElementById('raceBuffer' + index).checked}`
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

function reloadRace(name, date, group, url, button) {
    button.style.display = 'none';

    fetch(`${baseUrl}/race/remove/${name}/${date}/${group}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.status !== 200) {
            alert("Could not clear database before reloading (" + response.status + ")");
            throw response;
        }

        $.post(`${baseUrl}/race/add`, url, function () {
            location.reload();
        });
    }).catch(() => {
        console.log("Error when reloading race");
        button.style.display = '';
    });
}

function deleteResult(raceName, date, group, skipper, shipName, button) {
    button.style.display = 'none';

    fetch(`${baseUrl}/race/remove/${raceName}/${date}/${group}?skipper=${skipper}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.status !== 200) {
            alert("Could not delete result (" + response.status + ")");
            button.style.display = '';
        } else {
            let combinedId = shipName.replace(/\s/g, '') + skipper.replace(/\s/g, '');
            let raceId = raceName.replace(/\s/g, '') + group.replace(/\s/g, '');
            document.getElementById(`${combinedId}position${raceId}`).innerText = '';
            document.getElementById(`${combinedId}points${raceId}`).innerText = '';
        }
    });
}

function updateFilterState() {
    let allOptions = document.getElementsByClassName("filter-option");
    let validFilters = [];
    for (let option of allOptions) {
        let checkbox = option.getElementsByTagName('input')[0];
        if (checkbox.checked) {
            validFilters.push(option.innerText);
        }
    }

    let rows = document.getElementsByTagName("tr");
    for (let rowIdx = 0; rowIdx < rows.length; rowIdx++) {
        if (validFilters.length < 1) {
            rows[rowIdx].style.display = "";
        } else {
            let tds = rows[rowIdx].getElementsByClassName("filterable");
            let valid = true;

            for (let filter of validFilters) {
                let innerValid = false;
                for (let td of tds) {
                    let txtValue = td.innerText;
                    if (txtValue.indexOf(filter.trim()) > -1) {
                        innerValid = true;
                        break;
                    }
                }
                if (!innerValid)
                    valid = false;
            }

            if (valid)
                rows[rowIdx].style.display = "";
            else
                rows[rowIdx].style.display = "none";
        }
    }
}