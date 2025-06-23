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

    console.log("addCertificate() called")


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