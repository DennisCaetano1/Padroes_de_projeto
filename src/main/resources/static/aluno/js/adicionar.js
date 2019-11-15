$(document)
		.ready(
				function() {

					document.getElementById('cadastro').style.display = 'none';

					let email = sessionStorage.getItem("sess_email_aluno");
					let tela = document.querySelector('#tabela-projetos');
					let telaemail = document.getElementById('bodyemail');
					let rota = "/dono/" + email
					let retorno = {}

					$
							.get(
									rota,
									function(data, err) {

										let projetos = JSON.parse(data);
										for (let i = 0; i < projetos.length; i++) {
											var $tela = document
													.querySelector('#tpjr'), HTMLTemporario = $tela.innerHTML, HTMLNovo = "<tr> <th>"
													+ projetos[i]._id
													+ "</th>"
													+ "<th>"
													+ projetos[i].nome
													+ "</th>"
													+ "<th>"
													+ projetos[i].fase
													+ "</th>"
													+ "<th><a href class='text' onclick='abrePopupPost(event)'>Entregar</a></th>"
													+ "</tr>";
											HTMLTemporario = HTMLTemporario
													+ HTMLNovo;
											$tela.innerHTML = HTMLTemporario;
										}
									});

					$('#botao-add')
							.click(
									function() {
										let codigoProjeto = $(
												"#codigo-projetoLabel").val();
										let email = sessionStorage
												.getItem("sess_email_aluno");
										let jsonSend = '{"id": "'
												+ codigoProjeto + '","email":"'
												+ email + '"}';

										$
												.post(
														"/add-projeto",
														jsonSend,
														function(json) {
															alert("Adicionado projeto "
																	+ codigoProjeto
																	+ " com sucesso!");
															window.location.href = 'principal.html'
														});
									});

					$('#botao-add-aluno')
							.click(
									function(event) {
										event.preventDefault();
										document.getElementById('cadastro').style.display = 'block';
									});
				});

function abrePopupLogin(event) {
	event.preventDefault();
	document.getElementById('login').style.display = 'block';
}

function fechaPopupLogin(event) {
	event.preventDefault();
	document.getElementById('login').style.display = 'none';
}

function fechaPopupCadastro(event) {
	event.preventDefault();
	document.getElementById('cadastro').style.display = 'none';
}

function abrePopupPost(event) {
	event.preventDefault();
	document.getElementById('cadastro').style.display = 'block';
}