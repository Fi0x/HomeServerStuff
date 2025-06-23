function searchFunction() {
    var input, filter, table, rows, td, i, txtValue;
    input = document.getElementById("searchText");
    filter = input.value.toUpperCase();
    table = document.getElementById("searchableTable");
    rows = table.getElementsByTagName("tr");

    for (i = 0; i < rows.length; i++) {
        td = rows[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    }
}

function addCertificate() {
    let textField = document.getElementById("newCertificateText");
    let certificateId = textField.value;
    const url = "https://data.orc.org/public/WPub.dll?action=activecerts";

    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'text/xml',
        success: function (res) {

            console.log(res);

            let certificateData = {
                id: certificateId,
                shipName: "",
                certificateType: "",
                country: "",
                shipClass: "",
                singleNumber: 0.0,
                tripleLongLow: 0.0,
                tripleLongMid: 0.0,
                tripleLongHigh: 0.0,
                tripleUpDownLow: 0.0,
                tripleUpDownMid: 0.0,
                tripleUpDownHigh: 0.0
            };
        }
    });
}