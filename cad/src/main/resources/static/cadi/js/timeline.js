var Timeline = function (endpoint) {

  if (!endpoint) {
    throw new Error('É preciso de um endpoint de salvamento de projeto para instanciar Timeline');
  }
  else if (!window.jQuery || !$().emulateTransitionEnd) {
    throw new Error('É preciso que o Bootstrap 4 (CSS e JS) e o JQuery esteja sendo importado');
  }

  function _getInitialModalHTML(projeto) {
    return `
      <div class="modal fade" id="modal-extra-${ projeto._id.$oid}" tabindex="-1" role="dialog" aria-labelledby="modal-label" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modal-label"></h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
            </div>
          </div>
        </div>
      </div>
    `;
  }

  function _customPopupElement(projeto, inputsHTML) {

    let modalExtra = `#modal-extra-${projeto._id.$oid} `;

    $('#modal-label').text(projeto.titulo);

    $(modalExtra + '.modal-body').html(inputsHTML);

    if ([1, 3].indexOf(projeto.fase) != -1) {
      $(modalExtra + '.modal-footer').append(`
      <div class="btn-group btn-group-toggle" data-toggle="buttons">
          <button type="button" class="btn btn-primary" aceitar-avaInit>Aceitar</button>
          <button type="button" class="btn btn-primary" recusar-avaInit>Recusar</button>
      </div>
      `);

      $('[aceitar-avaInit]').click(function (e) {
        if (projeto.fase === 1) {
          $.post("/pulafase", JSON.stringify({'_id':projeto._id, 'fase':2}), "json");
          location.reload();
        }
        if(projeto.fase === 3){
          $.post("/pulafase", JSON.stringify({'_id':projeto._id, 'fase':4}), "json");
          location.reload();
        }
      });
    }
    // else if (projeto.fase == 4) {
    //   $('.modal-dialog').addClass('modal-xl');
    // }
  }

  function insertTimeline(target, projeto) {

    function _getIcon(iconName) {
      return iconName;
    }

    function _getEventClass(fase) {

      var base = 'event-circle--';

      if (projeto.status.negado)
        return base + 'red';
      else if (fase.isActive)
        return base + 'green';
      else if (fase.isPending)
        return base + 'yellow';
      else if (fase.isWaitingForInput)
        return base + 'blue';
      else
        return '';
    }

    function _getAvalInicHTML() {
      return `
      <div>
        <h3>Titulo do Projeto</h3>
        <p data-titulo>${projeto.titulo}</p>
      </div>
      <div>
         <h3>Descrição Breve</h3>
         <p data-descricao-breve>${projeto['descricao-breve']}</p>
      </div>
      
   `;
    }

    function _getCadastroCompletoHTML() {
      return `
        <form data-form-project-change>
          <div class="form-group">
            <label for="desc-completa">Descrição Completa:</label>
            <textarea data-descricao-completa class="form-control" id="desc-completa" rows="3">${projeto['descricao-completa']}</textarea>
          </div>
          <div class="form-group">
            <label for="desc-tecnologias">Descrição das Tecnologias:</label>
            <textarea data-descricao-tecnologias class="form-control" id="desc-tecnologias" rows="3">${projeto['descricao-tecnologias']}</textarea>
          </div>
          <div class="form-group">
            <label for="link-externo-2">Link externo 2:</label>
            <input data-link-externo-2 type="text" class="form-control" value="${projeto['link-externo-2']}" id="link-externo-2">
          </div>
        </form>`;
    }

    function _getAvalDetalHTML() {
      return `
        <div>
            <h3>Titulo do Projeto</h3>
            <p data-titulo>${projeto.titulo}</p>
        </div>
        <div>
            <h3>Descrição Breve</h3>
            <p data-descricao-breve>${projeto['descricao-breve']}</p>
        </div>
        <div>
            <h3>Descrição Completa</h3>
            <p data-descricao-completa>${projeto['descricao-completa']}</p>
        </div>
        <div>
            <h3>Descrição Tecnologias</h3>
            <p data-descricao-tecnologias>${projeto['descricao-tecnologias']}</p>
        </div>
        <div>
            <h3>Link Externo 1</h3>
            <a data-link-externo href="${projeto['link-externo-1']}">${projeto['link-externo-1']}</a>
        </div>
        <div>
            <h3>Link Externo 2</h3>
            <a data-link-externo-2 href="${projeto['link-externo-2']}">${projeto['link-externo-2']}</a>
        </div>
        

        `;
    }


    function _getReuniaoHTML() {
      return `
      <form data-form-project-change>
      <div class="form-group">
        <label for="formGroupInserirReunião">Insira data para reunião:</label>
        <input type="datetime-local" class="form-control" name="date" id="formGroupInserirReunião" min="2019-10-14">
        <span class="validity"></span>
        <br>
        <button type="button" class="btn btn-success" id="insere-data" name="insere-data">Inserir</button>
      </div>
        <script>
            $(document).ready(function () {
                var contaData = 0;
                <!-- testar o js -->
            });
        </script>
        <label for="data-reuniao">Datas possiveis a reunião:</label>
        <table class="table table-hover">
                  
              <thead>
              <tr>
              <td scope="col">Datas</th>
              <td scope="col">Remover</th>
              </tr>
              </thead>
              <!-- Popula a tabela com base no JS-->
              <tbody></tbody>
        </table>
          <!--criar script para popular array, EX.: dropdown de profs-->
        <div class="form-group">
        <label for="formGroupInserirEntrega">Insira data esperada de entrega do projeto:</label>
        <input type="date" class="form-control" id="formGroupInserirEntrega" min="2019-10-14">
        <br/>
        <label for="professores">Selecione os Professores:</label>
            </div> 
        <span class="validity"></span>
           	<script>
           	  $(document).ready(function () {
			
                $.getJSON("/listarProf", function(data){
                  
                  var profs = [];
                  
                  $.each(data, function(i){
                      
                    profs.push("<option>" + "Nome: " + this.name + " | Email: " + this.email + "</option>");
                  });	

                $('#professor').append(profs);
				        });
				      });
			      </script>
              <select multiple class="form-control" id="professor">
              </select>
            </div>      
            <!--  <button type="submit" class="btn btn-success" data-dismiss="modal">Designar</button> --!>
        </form>
      `;
    }

    function _getEntregasHTML() {
      return `
        <table class="table">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Aluno Responsável</th>
              <th scope="col">Link repositório</th>
              <th scope="col">Link Cloud</th>
              <th scope="col">Comentários</th>
            </tr>
          </thead>
          <tbody>
            ${
        projeto.entregas.map((entrega, index) =>
          `
                  <tr>
                    <th scope="row">${ index + 1}</th>
                    <td>${ entrega['aluno-responsavel']}</td>
                    <td><a href="${ entrega['link-repositorio']}" target="_blank">${entrega['link-repositorio']}</a></td>
                    <td><a href="${ entrega['link-cloud']}" target="_blank">${entrega['link-cloud']}</a></td>
                    <td>${ entrega.comentario}</td>
                  </tr>
                `
        ).join('')
        }
          </tbody>
        </table>
      `;
    }

    function _getNegadoHTML() {
      return `
        <h5>Projeto negado:</h5>
        <p>${ projeto.status.motivo}</p>
      `;
    }

    function _setInputPopupStructure(modelo) {
      var modeloHTML = {
        1: _getAvalInicHTML(),
        2: _getCadastroCompletoHTML(),
        3: _getAvalDetalHTML(),
        4: _getReuniaoHTML(),
        5: _getEntregasHTML(),
        negado: _getNegadoHTML()
      }[modelo];

      _customPopupElement(projeto, modeloHTML);
    }

    $(document.body).prepend(_getInitialModalHTML(projeto));

    _setInputPopupStructure(projeto.status.negado ? 'negado' : projeto.fase);

    var fases = [
      {
        icon: _getIcon(''),
        title: 'Cadastro Inicial',
        isActive: true,
        isPending: false,
        isWaitingForInput: projeto.fase == 0
      },
      {
        icon: _getIcon(''),
        title: 'Avaliação Inicial',
        isActive: projeto.fase > 1,
        isPending: false,
        isWaitingForInput: projeto.fase == 1
      },
      {
        icon: _getIcon(''),
        title: 'Cadastro Detalhado',
        isActive: projeto.fase > 2,
        isPending: projeto.fase == 2,
        isWaitingForInput: false //projeto.fase == 2 && (!projeto['descricao-completa'] || !projeto['descricao-tecnologias'])
      },
      {
        icon: _getIcon(''),
        title: 'Avaliação Detalhada',
        isActive: projeto.fase > 3,
        isPending: false,
        isWaitingForInput: projeto.fase == 3
      },
      {
        icon: _getIcon(''),
        title: 'Reunião',
        isActive: projeto.fase > 4,
        isPending: projeto.fase == 4 && !projeto.reuniao['datas-possiveis'].length,
        isWaitingForInput: projeto.fase == 4 //projeto.fase == 4 && projeto.reuniao['datas-possiveis'].length
      },
      {
        icon: _getIcon(''),
        title: 'Entrega',
        isActive: projeto.fase == 5 && projeto.entregas.length,
        isPending: projeto.fase > 4,// projeto.fase == 5 && !projeto.entregas.length,
        isWaitingForInput: false
      }
    ];

    target.innerHTML = `
      <div class="timeline fase-${ projeto.fase} ${projeto.status.negado ? 'negado' : ''}">
      ${
      fases.map((fase, index) => {
        let tag = 'div';
        let extraAttributes = '';
        if (fase.isWaitingForInput || (index === 5 && fase.isActive) || projeto.status.negado) {
          tag = 'a';
          extraAttributes = `
              href="#" 
              data-toggle="modal" 
              data-target="#modal-extra-${ projeto._id.$oid}"
              data-open-to-input`;
        }
        return `
            <${ tag} 
              class="timeline__event" 
              ${ extraAttributes}>
              <div class="event-circle ${ _getEventClass(fase)}">
                ${ fase.icon}
              </div>
              <label class="event-label">${ fase.title}</label>
            </${ tag}>`;
      }).join('')
      }
      </div>
    `;
  }

  return {
    insertTimeline
  };
};