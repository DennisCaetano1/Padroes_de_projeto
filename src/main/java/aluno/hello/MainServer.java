
package aluno.hello;

import static spark.Spark.*;

import org.bson.Document;

import com.mongodb.client.FindIterable;
//import org.json.me;


public class MainServer {

	final static ModelAluno model = new ModelAluno();
	
    public static void main(String[] args) {

		// Get port config of heroku on environment variable
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8080;
        }
        port(port);

		//Servir conteudo html, css e javascript
		staticFileLocation("/static");

		inicializarPesquisa();
 
		ControllerAluno controller = new ControllerAluno(model);

		controller.inserirAluno();
	    controller.search();
	    controller.loginAluno();
	    controller.projetos();
	    controller.atribuirProjeto();
	    model.addAluno(Document.parse("{'email':'leo@antenas.com','name':'Leo', 'senha':'12345', 'nivel':'1'}"));
    }
    
    
    public static void inicializarPesquisa(){
    	model.addProjeto(Document.parse("{'_id': '1234', 'nome':'Programa sempre teste!', 'fase':'2', 'responsavel-aluno': '', 'responsavel-prof': '','responsavel-cadi':'joao@email.com'}"));
    	model.addProjeto(Document.parse("{'_id':'4321','nome':'Codigo elegante', 'fase':'1', 'responsavel-aluno': '','responsavel-cadi':'joao@email.com'}"));
		model.addProjeto(Document.parse("{'_id': '2', 'nome':'Integra-me', 'fase':'1', 'responsavel-aluno': '','responsavel-cadi':''}"));
    }
}
