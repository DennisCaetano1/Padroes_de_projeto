$(document).ready(function() {
    /* Validar se usuario não fez logout */
    var session_login = sessionStorage.getItem("sess_email_cadi");
    if(session_login != null){	
        window.location.href = 'principal.html';
    }
    var cadastro = document.getElementById('cadastro');
    window.onclick = function(event) {
        if (event.target == cadastro) {
            cadastro.style.display = "none";
        }
    }
});

$('#form_register').submit(function(e){  
    e.preventDefault();
    json = {
           nome: $("#nome-cadastro").val(),
           email: $("#email-cadastro").val(),
           senha: $("#senha-cadastro").val(),
           //Nivel em teste!!!
           nivel: 1
       }
    jsonString = JSON.stringify(json);
    $.post("/cadicadastro",jsonString,'json');
    fechaPopupCadastro(event);
});

$('#form_login').submit(function(e){    
				
    e.preventDefault();
    
    var userName = $('#email-login').val().trim();
    var password = $('#senha-login').val().trim();
    
    $.post("/cadi", JSON.stringify({'email': userName, 'senha': password}), function(data){
            
        if(data.nivel){
            window.location.href = 'principal.html';
            sessionStorage.setItem("sess_email_cadi",data.email);
        } else {
            alert("Usuário não localizado");
            window.location.href = 'index.html';
        }
            
    }, "json");
    
});


function abrePopupCadastro(event) {
    event.preventDefault();
    document.getElementById('cadastro').style.display='block';    
}

function fechaPopupCadastro(event) {
    event.preventDefault();
    document.getElementById('cadastro').style.display='none';    
}

