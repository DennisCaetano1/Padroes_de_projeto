package cadi.hello;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;

import spark.Request;
import spark.Response;
import spark.Route;

public class ControllerCadi {

	private ModelCadi model;
	private String WhoIsauth;

	public ControllerCadi(ModelCadi model) {
		super();
		this.model = model;
	}
	
	public String getWhoIsauth() {
		return WhoIsauth;
	}

	public void setWhoIsauth(String whoIsauth) {
		WhoIsauth = whoIsauth;
	}
	
	public void Auth() { // Gera um token de autenticação para o usuário
		post("/Auth", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {

				try {
					response.header("Access-Control-Allow-Origin", "*");

					// set
					JSONObject myjson = new JSONObject(request.body());
					Jwt AuthEngine = new Jwt();
					
					// try to find user
					Document user = model.searchByEmail(myjson.getString("email"));

					String email = user.getString("email");
					String senhaDigitada = myjson.getString("senha");
					String senhaArmazenada = user.getString("senha");
					boolean usuarioAtivo = user.getBoolean("ativo");

					if (email.length() > 0 && senhaDigitada.equals(senhaArmazenada) && usuarioAtivo) {
						response.status(200);
						return AuthEngine.GenerateJwt(email);
					}
					response.status(403);
					return "Usuário inexistente ou inativo";

				} catch (JSONException ex) {
					return "erro 500 " + ex;
				}
			}
		});
	}
	
	public boolean IsAuth(String body) { // Verifica se o usuário está autenticado
		try {
			// setting
			JSONObject myjson = new JSONObject(body);
			Jwt AuthEngine = new Jwt();

			// try to find user
			String emailOrNull = AuthEngine.verifyJwt((myjson.getString("token")));

			if(emailOrNull == null) {
				return false;
			}else {
				setWhoIsauth(emailOrNull);
				return true;
			}

		} catch (JSONException ex) {
			return false;
		}
	}
	
	public void ativarUsuario() { // é chamado quando o usuario recebe o link de ativação no email
		get("/active/:email", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				String email = new String(Base64.getDecoder().decode ( request.params("email")  )) ;
				Document found = model.ativarCadi(email);
				if (!found.isEmpty()) {
					response.redirect("http://localhost:8083/cadi/index.html");
				}
				return null;
			}
		});
	}
	

	public void loginCadi() {
		post("/cadi", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				response.header("Access-Control_Allow-Origin", "*");

				JSONObject json = new JSONObject(request.body());
				String email = json.getString("email");
				String senha = json.getString("senha");
				try {
					Document cadi = model.login(email, senha);
					System.out.println(cadi);
					if ((Boolean)cadi.get("ativo")==true){
						return cadi.toJson();
					}
					return null;
				} catch (NullPointerException e) {
					return null;
				}

			}
		});
	}
	public void atribuirProjeto() {
		post("/semdono", (Request request, Response response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			JSONObject json = new JSONObject(request.body());
			model.updateProjeto(Document.parse(request.body() ));
			return model.buscaSemDono();
		});
	}
	public void inserirCADI() {
		/*post("/cadicadastro", (Request request, Response response) -> {

			System.out.println("Chamou");
			response.header("Access-Control-Allow-Origin", "*");

			Document cadi = Document.parse(request.body());

			model.addCADI(cadi);

			return cadi.toJson();
		});*/
		post("/cadicadastro", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				try {
					response.header("Access-Control-Allow-Origin", "*");
					String jsonString = request.body();
					Document userData = Document.parse(jsonString);

					userData.append("ativo", false);

					Document found = model.searchByEmail(userData.getString("email"));

					if (found == null || found.isEmpty()) {
						model.addCADI(userData);
						new EmailService(userData).sendSimpleEmail();
						return userData.toJson();
					} else {
						return "Email já cadastrado";
					}
				} catch (Exception ex) {
					return "erro 500 " + ex;
				}
			}
		});
		
	}
	
	public void atualizaCadi() {
		post("/updateCadi", (Request request, Response response) -> {
			System.out.println("Chamou Aqui");
			response.header("Access-Control-Allow-Origin", "*");
			JSONObject json = new JSONObject(request.body());
			System.out.println(json);
			model.updateCadi(Document.parse(request.body()));
			return model.buscaSemDono();
		});
	}

	public void projetos() {
		get("/projetos", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {

				FindIterable<Document> projectsFound = model.listaProjetos();

				return StreamSupport.stream(projectsFound.spliterator(), false).map(Document::toJson)
						.collect(Collectors.joining(", ", "[", "]"));
			}
		});
	}

	public void search() {
		get("/search", (request, response) -> {
			return model.search(request.queryParams("chave"), request.queryParams("valor"));
		});
		post("/usuarioLogado", (request, response) -> {
			JSONObject json = new JSONObject(request.body());
			String email = json.getString("email");
			return model.searchByEmail(email).toJson();
		});

		get("/dono", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {
				String email = request.queryString();
				FindIterable<Document> projectFound = model.buscaPorDono(email);
				return StreamSupport.stream(projectFound.spliterator(), false)
						.map(Document::toJson)
						.collect(Collectors.joining(", ", "[", "]"));
			}
		});
		get("/semdono", (request, response) -> {
			return model.buscaSemDono();
		});
		
		post("/putProf", (request, response) -> {
			Document projetoComProfessor = Document.parse(request.body());

			model.updateProjeto(projetoComProfessor);

			return projetoComProfessor.toJson();
		});
		
		post("/putCadi", (request, response) -> {
			Document projetoComCadi = Document.parse(request.body());

			model.updateProjeto(projetoComCadi);

			return projetoComCadi.toJson();
		});
		
		post("/pulafase", (request, response) -> {
			Document projeto = Document.parse(request.body());

			model.updateProjeto(projeto);

			return projeto.toJson();
		});

	}
	
	public void inserirReuniao() {
		get("/reuniao", (Request request, Response response) -> {

			response.header("Access-Control-Allow-Origin", "*");

			Document reuniao = Document.parse(request.body());

			model.addReuniao(reuniao);

			return reuniao.toJson();
		});
	}

	/*
	 * Metodo que recebe uma id pela url (ex: /alterarId?Id=8) e um json no corpo do
	 * metodo com as alteraï¿½ï¿½es direcionadas para o objeto que tiver a Id
	 * especificada. Ex: { 'professor':'fulano }
	 * 
	 * objeto final:
	 * 
	 * { 'id': 8, 'professor':'fulano' }
	 */

	/*public void alterarId() {
		post("/alterarId", (req, res) -> {
			model.alterarId(req.queryParams("id"), new Document("$set", Document.parse(req.body())));
			return model.listCadi();
		});
	}

	public void listCadi() {
		get("/listarCadi", (req, res) -> {
			return model.listCadi();
		});
	}
	*/
	
	public void listCadi() {
		post("/listarCadi", (req, res) -> {
			return model.listCadi();
		});
	}
	public void listProf() {
		get("/listarProf", new Route() {
			@Override
			public Object handle(final Request request, final Response response) {

				FindIterable<Document> profsFound = model.listProf();

				return StreamSupport.stream(profsFound.spliterator(), false).map(Document::toJson)
						.collect(Collectors.joining(", ", "[", "]"));
			}
		});
	}

}
