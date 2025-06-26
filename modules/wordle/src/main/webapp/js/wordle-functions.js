function initializeTable() {
    let table = document.getElementById("fieldTable");

    for (let row = 0; row < 6; row++) {
        let rowElement = document.createElement('tr');
        for (let column = 0; column < 5; column++) {
            let cell = document.createElement('td')
            cell.classList.add("riddleCell");
            cell.id = "riddleCell" + row + "-" + column;
            rowElement.append(cell);
        }
        table.append(rowElement);
    }
}
