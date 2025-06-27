let currentRow = 0;
let selectedField = 0;

function initializeTable() {
    let table = document.getElementById("fieldTable");

    for (let row = 0; row < 6; row++) {
        let rowElement = document.createElement('tr');
        for (let column = 0; column < 5; column++) {
            let cell = document.createElement('td')
            cell.classList.add("riddleCell");
            cell.id = "riddleCell" + row + column;
            cell.onclick = function () {
                clickedOnCell(column);
            }
            rowElement.append(cell);
        }
        table.append(rowElement);
    }
    window.addEventListener("keydown", function (event) {
        let code = event.keyCode;
        let label = event.key;
        if ((code < 65 && code !== 8 && code !== 13) || code > 90)
            return;

        pressKey(code, label.toUpperCase());
    });

    selectField(currentRow, selectedField);
}

function pressKey(code, label) {
    let currentCell = document.getElementById("riddleCell" + currentRow + selectedField);

    if (code === 8) {
        currentCell.innerText = "";
        selectedField--;
    } else if (code === 13) {
        let word = getCurrentWord();
        if (word.length !== 5) {
            alert("5 letters required");
            return;
        }
        checkCurrentWord(word);
    } else {
        currentCell.innerText = label;
        selectedField++;
    }

    if (selectedField < 0)
        selectedField = 0;
    else if (selectedField > 4)
        selectedField = 4;

    currentCell.classList.remove("selectedCell");
    selectField(currentRow, selectedField);
}

function clickedOnCell(column) {
    let currentCell = document.getElementById("riddleCell" + currentRow + selectedField);
    currentCell.classList.remove("selectedCell");
    selectField(currentRow, column);
}

function selectField(row, column) {
    currentRow = row;
    selectedField = column;
    let currentCell = document.getElementById("riddleCell" + currentRow + selectedField);
    currentCell.classList.add("selectedCell");
}

function getCurrentWord() {
    let word = "";

    let cells = document.getElementById("riddleCell" + currentRow + selectedField).parentElement.children;
    for (let cell of cells) {
        word += cell.innerText;
    }

    return word;
}

function checkCurrentWord(word) {

    fetch(`${baseUrl}/words/verify?word=${word}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(gameSettings)
    }).then(function (response) {
        if (!response.ok) {
            return Promise.reject(response);
        }
        response.json().then(jsonResponse => {
            let correctLetters = colorCodeResults(jsonResponse);
            if (correctLetters === 5) {
                completeLevel();
            } else {
                currentRow++;
                if (currentRow > 5) {
                    failLevel();
                }
                selectedField = 0;
                selectField(currentRow, selectedField);
            }
        });
    }).catch(error => {
        if (error.status === 406) {
            alert("Word is not in word-list")
        }
    });

    return false;
}

function colorCodeResults(jsonResponse) {
    let correctLetters = 0;
    let cells = document.getElementById("riddleCell" + currentRow + 0).parentElement.children;
    for (let column = 0; column < 5; column++) {
        let currentCell = cells[column];
        let keyCode = currentCell.innerText.charCodeAt(0);
        switch (jsonResponse.characterValidation[column]) {
            case -1:
                currentCell.classList.remove("selectedCell");
                currentCell.classList.add("inactiveCell");
                document.getElementById(`key${keyCode}`).classList.add("inactiveKey")
                break;
            case 0:
                currentCell.classList.remove("selectedCell");
                currentCell.classList.add("yellow");
                document.getElementById(`key${keyCode}`).classList.add("yellow")
                break;
            case 1:
                correctLetters++;
                currentCell.classList.remove("selectedCell");
                currentCell.classList.add("green");
                document.getElementById(`key${keyCode}`).classList.add("green")
                break;
        }
    }
    return correctLetters;
}

function completeLevel() {
    let keyboardParent = hideKeyboard();

    let result = {
        timestamp: gameSettings.timestamp,
        tries: currentRow + 1,
        requiredTime: gameSettings.started
    }
    sendAndShowResults(keyboardParent, result);

    showMenuButton(keyboardParent);
}

function displayStats(parentElement, statsJson) {
    let statsDiv = document.createElement("div");
    statsDiv.append(getStatElement(`Player: ${statsJson.playerName}`));

    if (statsJson.tries < 0) {
        statsDiv.append(getStatElement(`LEVEL FAILED`));
    } else {
        statsDiv.append(getStatElement(`Required Time: ${(statsJson.requiredTime / 1000).toFixed(0)}s`));
        statsDiv.append(getStatElement(`Tries: ${statsJson.tries}`));
    }

    statsDiv.classList.add("stats");
    parentElement.prepend(statsDiv);
}

function getStatElement(text) {
    let element = document.createElement("p");
    element.innerText = text;
    return element;
}

function failLevel() {
    let keyboardParent = hideKeyboard();
    showMenuButton(keyboardParent);
    let result = {
        timestamp: gameSettings.timestamp,
        tries: -1,
        requiredTime: gameSettings.started
    }
    sendAndShowResults(keyboardParent, result);
}

function sendAndShowResults(parentElement, results) {
    fetch(`${baseUrl}/results/save`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(results)
    }).then(function (response) {
        if (!response.ok)
            alert("Result already saved");
        else
            response.json().then(jsonResult => {
                displayStats(parentElement, jsonResult);
            });
    });
}

function hideKeyboard() {
    let keyboard = document.getElementsByClassName("keyboardRow");
    let keyboardParent = keyboard[0].parentElement;
    for (let i = keyboard.length; i > 0; i--)
        keyboardParent.removeChild(keyboard[i - 1]);

    return keyboardParent;
}

function showMenuButton(parent) {
    let menuButton = document.createElement("a");
    menuButton.innerText = "Return to menu";
    menuButton.classList.add("btn");
    menuButton.href = `/`;
    parent.append(menuButton);
}

function addNewWord() {
    let word = document.getElementById("newWordInput").value.toUpperCase();

    fetch(`${baseUrl}/words/validate/${word}?newCreation=true`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    }).then(function (response) {
        if (!response.ok)
            console.log("Something went wrong when adding a new word");
        else
            response.json().then(jsonResult => {
                document.getElementById("responseTextId").innerText = jsonResult.resultMessage;
            });
    });
}

function inValidateAndUpdateTexts(url) {
    let buttons = document.getElementsByClassName("clickable");
    for (let button of buttons) {
        button.style.display = 'none';
    }

    let word = document.getElementById("verificationWord").innerText.toUpperCase();
    fetch(url + word, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    }).then(function (response) {
        if (!response.ok)
            console.log("Something went wrong when (in-)validating a new word");
        else
            response.json().then(jsonResult => {
                document.getElementById("responseTextId").innerText = jsonResult.resultMessage;
                document.getElementById("verificationWord").innerText = jsonResult.nextWord;
            });

        for (let button of buttons) {
            button.style.display = '';
        }
    });
}