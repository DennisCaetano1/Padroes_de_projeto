let emails = [];
function addStudentToList() {
    var emailWrited = document.getElementById("insertEmailEntrega");
    var email = document.getElementById("insertEmailEntrega").value;
    emails.push(email);
    var table = document.getElementById("myTable");
    var row = table.insertRow(table.length);
    var cell1 = row.insertCell(table.length);
    var cell2 = row.insertCell(table.length);
    cell1.innerHTML = emailWrited.value;
    cell2.innerHTML = "<a href='#' onclick='excluir(" + row.rowIndex + ")'>x</a>";
    document.getElementById("insertEmailEntrega").value = '';
}
function excluir(data) {
    var table = document.getElementById("myTable");
    table.deleteRow(data);
}
function entregar() {

    emails.push(sessionStorage.getItem("sess_email_aluno"));
    const usuario = sessionStorage.getItem("sess_email_aluno");
    descricao = document.getElementById("desc-cadastro").value;
    link = document.getElementById("Link-cadastro").value;
    const callbackSubmit = (data) => {
        //todo
    };
    $.post("entregar",
        JSON.stringify(
            { "id": "1234", "autores": usuario, "descricao": descricao, "link": link }),
        callbackSubmit,
        'json');
    window.location.href = 'principal.html';
}

