/**/
var session_login = sessionStorage.getItem("sess_email_cadi");


if(session_login == null){
        window.location.href = 'index.html';
}else{

  $(document).ready(function () {
      

      let timeline = new Timeline('/dono');
      let projects;
      let maisInfoModal = $('#modal-mais-info');

/* Informações do Usuário CADI */
      $.post("/usuarioLogado", JSON.stringify({'email': session_login}), function(user){
        userData(user);
      }, "json");
 
    
/* </> Informações do Usuário CADI */


       /* <> Rotas de inicialização dos objetos */
      $.get('/dono', session_login)
          .done(function(projetos){
          projects = JSON.parse(projetos);
          insertProjectsOnTable(projects);
      });

      $.get('/semdono')
          .done(function(projetos){
          projects = JSON.parse(projetos);
          insertSemDono(projects);
      });
      
       /* </> Rotas de inicialização dos objetos */

      /* <> Funções */
      function insertProjectsOnTable(projecs) {
      
          let tbody = $('[data-projects-table-body]');
      
          projecs.forEach(project => {
            let tr = $.parseHTML(`
              <tr data-project-item="${ project._id }">
                <th scope="row">${ project.titulo }</th>
                <td data-timeline-show></td>
                <td>
                  <a href="#" data-toggle="modal" data-target="#modal-mais-info">Mais informações</a>
                </td>
              </tr>
            `);
      
            let $tr = $(tr);
      
            $tr.click(function(e) {
              
              e.preventDefault();
      
              maisInfoModal.find('#modal-label').text(project.titulo);
      
              let pegaElemento = id => $(maisInfoModal.find(`#${id}`));
      
              let elements = [
                {
                  element: pegaElemento('info-descricao-breve'),
                  key: 'descricao-breve'
                },          
                {
                  element: pegaElemento('info-descricao-completa'),
                  key: 'descricao-completa'
                },
                {
                  element: pegaElemento('info-descricao-tecnologias'),
                  key: 'descricao-tecnologias'
                },
                {
                  element: pegaElemento('info-links-externos'),
                  key: '',
                  excessao: true
                },
                {
                  element: pegaElemento('info-link-externo-1'),
                  key: 'link-externo-1'
                },
                {
                  element: pegaElemento('info-link-externo-2'),
                  key: 'link-externo-2'
                },
                {
                  element: pegaElemento('info-empresario'),
                  key: 'responsavel-empresario'
                },
                {
                  element: pegaElemento('info-professores-responsaveis'),
                  key: 'responsavel-professor',
                  excessao: true
                },
                {
                  element: pegaElemento('info-reuniao'),
                  key: 'reuniao',
                  excessao: true
                },
                {
                  element: pegaElemento('info-entregas'),
                  key: 'entregas',
                  excessao: true
                },
                {
                  element: pegaElemento('info-negado'),
                  key: 'status',
                  excessao: true
                }
              ];
      
              elements.forEach(item => {
      
                let contentElement = item.element.find('[data-text-content]');
      
                if (item.key.indexOf('link-externo') != -1) {
                  contentElement.attr('href', project[item.key]);
                }
      
                if (item.key && !item.excessao) {
                  if (!project[item.key]) {
                    item.element.addClass('d-none');
                    return;
                  }
                  else {
                    contentElement.text(project[item.key]);
                  }
                }
                else if (!item.key) {
                  if (!project['link-externo-1'] && !project['link-externo-2']) {
                    item.element.addClass('d-none');
                    return;
                  }
                }
                else if (item.key === 'status') {
                  if (!project.status.negado) {
                    item.element.addClass('d-none');
                    return;
                  }
                  else {
                    contentElement.text(project.status.motivo);
                  }
                }
                else if (item.key === 'reuniao') {
                  let reuniao = project.reuniao;
                  
                  if (!reuniao.data && !reuniao.horario && !reuniao.local) {
                    item.element.addClass('d-none');
                    return;
                  }
                  else {
                    contentElement.text(`${reuniao.data} - ${reuniao.horario} - ${reuniao.local}`);
                  }
                }
                else if (item.key === 'entregas' || item.key ==='responsavel-professor') {
                  
                  if (!project[item.key].length) {
                    item.element.addClass('d-none');
                    return;
                  }
                  else {
                    project[item.key].forEach(x => {
                      contentElement.append($.parseHTML(`<li>${x}</li>`));
                    });
                  }
                }
              });
            });
      
            timeline.insertTimeline($tr.find('[data-timeline-show]').get(0), project);
      
            tbody.append(tr);
          });
      }


      function insertSemDono(projecs) {
      
        let tbody_semdono = $('[data-semdono-table-body]');
    
        projecs.forEach(project => {
          let tr2 = $.parseHTML(`<tr data-project-item="${ project._id }> 
            <th scope="row">${ project.titulo }</th>
                <td>${ project.titulo }</td>
                <td>${ project['descricao-breve'] }</td>
                <td>Nome da Empresa</td>
            </tr>
          `);
    
          let $tr2 = $(tr2);
          
          $tr2.click(function(e) {
              e.preventDefault();
            
              let modbody = $('[modal-body-semdono]');
              let modfooter = $('[modal-footer-semdono]');

              let body_sd =  $.parseHTML(`
              <div><h5>Titulo</h5><p>${ project.titulo }</p></div>
              <div><h5>Descricao: </h5><p>${ project['descricao-breve'] }</p></div>
              <div><h5>Empresário: </h5><p>${ project['responsavel-empresario']}</p></div>
              <div><h5>Empresa: </h5><p>Não tem ainda</p></div>
              <div><h5>Contato: </h5><p>Não tem ainda</p></div>
              `);

              let foot_sd = $.parseHTML(`
                  <button type="button" class="btn btn-secondary" data-dismiss="modal">Atribuir a mim</button>
              `);

              let $submit = $(foot_sd);
              
            
              /* Abre modal */
              document.getElementById('modal_semdono').style.display='block';   
          
              /* Evento que fecha o modal e limpa os dados do append*/
              $('#fecharModal').click(function(){
                document.getElementById('modal_semdono').style.display='none'; 
                modbody.html("");
                modfooter.html("");
              });

              /* evento que submita a atribuição para o CADI */
              $submit.click(function(){
                  $.post("/semdono", JSON.stringify({'_id':project._id, 'responsavel-cadi': sessionStorage.getItem("sess_email_cadi")}), "json");
                  location.reload();
              });

              modbody.append(body_sd);
              modfooter.append(foot_sd);
          });

          tbody_semdono.append(tr2);
        });
      }

      function userData(user){
          /* <> Logou do Usuário */
          let navCADI = $('[data-user]');
          let logout = $.parseHTML(`
          <li><i class="fa fa-sign-out" aria-hidden="true"></i> 
          <button type="button" class="btn btn-danger">Logout</button></li>`);

          let $logout = $(logout);
          $logout.click(function(e) {
              e.preventDefault();
              if (confirm('Realmente deseja Sair ?')) {
                  sessionStorage.clear(session_login);
                  window.location.href = 'index.html';
              }
          });


          /* Alterar Senha */
          let updateSenha = $.parseHTML(`
          <li><i class="fa fa-sign-out" aria-hidden="true"></i>
          <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-update-senha">
              Alterar Senha
          </button>
          </li>`);

          let $updateSenha = $(updateSenha);
          $updateSenha.click(function(e){
            e.preventDefault();
            _formUpdateSenha(user);
          });
          /* </> Alterar Senha */

          /* </> Logou do Usuário */

          /* Pupula Usuário Data */
          let data = $.parseHTML(`
          <li>${user.name}</li>
          <li>${user.nivel == 2 ? "Administrador" : "Usuario CADI"}</li>`);
          /* </> Pupula Usuário Data */
          if(user.nivel == 2) {
            $.post("/listarCadi", function(users){
              _isAdmin(users);
            }, "json");
            
          } 
        
          navCADI.append(data);
          navCADI.append(updateSenha);
          navCADI.append(logout);
         $("li").addClass("list-inline-item");
      }

  /* </> Funções */
 
      
  });

}
function fechaPopupSemDono(event) {
  event.preventDefault();
  document.getElementById('modal_semdono').style.display='none';    
}

function _formUpdateSenha(user){

  let form_senha =  `
    <div class="modal fade" id="modal-update-senha" tabindex="-1" role="dialog" aria-labelledby="modal-update-senha" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Alteração de Senha</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
       Senha: <input class="form-control"  type="password" id="senha-antiga" name="senha-antiga" placeholder="Senha Atual" style="max-width:350px" required>
       Nova Senha: </label><input class="form-control" type="password" id="senha-nova1" name="senha-nova1" placeholder="Nova Senha" style="max-width:350px" required>
       Nova Senha Novamente: </label><input class="form-control" type="password" id="senha-nova2" name="senha-nova2" placeholder="Nova Senha" style="max-width:350px" required>
        </div>
        <div class="modal-footer" >
          <button type="submit" class="btn btn-primary alterarSenha" id="alterarSenha">Salvar mudanças</button>
          <div id="modal-footer-password"></div>
          </div>
      </div>
    </div>
  </div>`;

  /* Evento insere modal no HTML */
  $(document.body).prepend(form_senha);
  /* Evento Remove modal do HTML */
  $('.close').click(function(e){
    e.preventDefault();
    $("#modal-update-senha").remove();
    $(".modal-backdrop ").remove();
  });
  /* Evento submita a senha nova */
  $('#alterarSenha').click(function(e){
    e.preventDefault();
    $("#modal-footer-password").html('');
    var senhaAntiga = $("#senha-antiga").val();
    var senha1 = $("#senha-nova1").val();
    var senha2 = $("#senha-nova2").val();
    
 
    if(senhaAntiga === user.senha && senhaAntiga != null){
        if(senha1 === senha2 && senha1 != null && senha2 != null){
          $.post("/updateCadi", JSON.stringify({'_id':user._id, 'senha': senha1}), "json");
         
        }else{
          let passNotEquals = $.parseHTML(`<div class="alert alert-danger" role="alert">
          Senha de nova ou senha de confirmação inválidas ou não correspondentes.</div>`);
          $('#modal-footer-password').append(passNotEquals);
        }
    }else{
      let passNotEquals = $.parseHTML(`<div class="alert alert-danger" role="alert">
          Senha não corresponde com a atual!, por favor insira a senha correta.
      </div>`);
      $('#modal-footer-password').append(passNotEquals);
    }
   

  });
}

function _isAdmin(users){
    let ul_tabs = $('[tabs-nav-user]');
    let div_content = $('[tabs-content-user]');

    let nav =  $.parseHTML(`
    <li class="nav-item">
      <a class="nav-link" data-toggle="pill" href="#usuarios">Usuários</a>
    </li>`);

    let content = $.parseHTML(`<div id="usuarios" class="container tab-pane fade">
            <table class="table">
            <thead class="thead-dark">
                <tr>
                    <td scope="col">Nome</td>
                    <td scope="col">Email</td>
                    <td scope="col">Satus</td>
                </tr>
            </thead>
            <tbody data-user-admintab id="tabela-projetos">

            </tbody>
        </table>
    </div>`);

    ul_tabs.append(nav);
    div_content.append(content);

    let tbody_data_users = $('[data-user-admintab]');
    
    users.forEach(user => {
      let tr = $.parseHTML(`<tr data-user-item="${ user._id }> 
        <th scope="row"></th>
            <td>${ user.name }</td>
            <td>${ user.email }</td>
            <td>${ user.nivel < 1 ? 'Aguardando Aprovação' : user.nivel == 2 ? 'Administrador' : 'CADI' }</td>
        </tr>
    `);
       tbody_data_users.append(tr);
      
    });
}