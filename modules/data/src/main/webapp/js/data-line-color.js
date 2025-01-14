function colorDataLines() {
    if (dataList.length === 0)
        return;

    let currentMillis = new Date().getTime();
    let previousMillis = new Date(dataList[0]).getTime();

    if (currentMillis - previousMillis > sensorInformation.delay * 2) {
        document.getElementById('dataEntry' + (0)).classList.add('yellow');
    }

    for (let i = 0; i < dataList.length - 1; i++) {
        currentMillis = new Date(dataList[i]).getTime();
        previousMillis = new Date(dataList[i + 1]).getTime();
        if (currentMillis - previousMillis > sensorInformation.delay * 2) {
            document.getElementById('dataEntry' + (i + 1)).classList.add('yellow');
        }
    }
}