function colorDataLines() {
    if (sensorData.length === 0)
        return;

    let currentMillis = new Date().getTime();
    let previousMillis = new Date(sensorData[sensorData.length - 1].x).getTime();

    if (currentMillis - previousMillis > sensorInformation.delay * 2) {
        document.getElementById('dataEntry' + 0).classList.add('yellow');
    } else if (sensorInformation.min > sensorData[sensorData.length - 1].y || sensorInformation.max < sensorData[sensorData.length - 1].y) {
        document.getElementById('dataEntry' + 0).classList.add('red');
    }

    for (let i = 1; i < sensorData.length; i++) {
        currentMillis = new Date(sensorData[i].x).getTime();
        previousMillis = new Date(sensorData[i - 1].x).getTime();
        if (currentMillis - previousMillis > sensorInformation.delay * 2) {
            document.getElementById('dataEntry' + (sensorData.length - i)).classList.add('yellow');
        } else if (sensorInformation.min > sensorData[i - 1].y || sensorInformation.max < sensorData[i - 1].y) {
            document.getElementById('dataEntry' + (sensorData.length - i)).classList.add('red');
        }
    }
}