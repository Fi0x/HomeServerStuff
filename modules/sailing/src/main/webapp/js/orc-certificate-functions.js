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

function deleteCertificate(certId) {

    let idx = certificates.indexOf(certificates.find(c => c.id === certId));
    if (idx >= 0)
        certificates.splice(idx, 1);

    $.post(`${baseUrl}/orc/remove/${certId}`);

    let row = document.getElementById(`cert${certId}`);
    row.parentElement.removeChild(row);
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