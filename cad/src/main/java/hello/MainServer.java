
package hello;

import static spark.Spark.*;

import org.bson.Document;

import com.mongodb.client.FindIterable;
//import org.json.me;

import Cadi.ControllerCadi;
import Cadi.ModelCadi;


public class MainServer {
	
	final static ModelCadi model = new ModelCadi();
	
    public static void main(String[] args) {
 
		// Get port config of heroku on environment variable
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8083;
        }
        port(port);

		//Servir conteudo html, css e javascript
		staticFileLocation("/static");

		inicializarPesquisa();
		inicializarUsers();
 
		ControllerCadi controller = new ControllerCadi(model);
		
		controller.inserirCADI();
	    controller.search();
	    controller.loginCadi();
	    controller.projetos();
	    controller.atribuirProjeto();
	    controller.listCadi();
	    controller.listProf();
	    controller.inserirReuniao();
	    controller.Auth();
	    controller.ativarUsuario();  
	    controller.atualizaCadi();
    }
    
    public static void inicializarUsers() {
    	//CADI
    	model.addCADI(Document.parse("{'email':'rone@email.com','name':'John', 'senha':'11111', 'nivel':'1', 'ativo':true}"));
    	model.addCADI(Document.parse("{'email':'cadi.admin@fatec.sp.gov.br','name':'Administrador', 'senha':'1234', 'nivel':'2', 'ativo':true}"));
    	model.addCADI(Document.parse("{'email':'teste@email.com','name':'Francisco', 'senha':'000', 'nivel':'0', 'ativo':true}"));
    	//Professores
    	model.addProfessores(Document.parse("{'name':'Giuliano', 'email':'Giuliano@fatec.sp.gov.br', 'projeto-atribuido':'', 'nivel':'1'}"));
    	model.addProfessores(Document.parse("{'name':'Sakaue', 'email':'Sakaue@fatec.sp.gov.br', 'projeto-atribuido':'', 'nivel':'1'}"));
    	model.addProfessores(Document.parse("{'name':'Nanci', 'email':'Nanci@fatec.sp.gov.br', 'projeto-atribuido':'', 'nivel':'1'}"));
    }
    
    public static void inicializarPesquisa(){
    	
    	
    	model.addProjeto(Document.parse("{'titulo':'Teste','descricao-breve' :'Teste descricao', 'descricao-completa':'','descricao-tecnologias':'','link-externo-1':'','link-externo-2':'','fase': 1,'reuniao' :{'data' :'','horario' :'','local':'','datas-possiveis' : [] },'status' : {'negado' : false,'motivo':'' },'entregas' : [],'alunos':[],'responsavel-cadi':'','responsavel-professor':[],'responsavel-empresario':'teste@teste'}"));
		model.addProjeto(Document.parse("{'titulo' : 'Teste1', 'descricao-breve' : 'Teste descricao', 'descricao-completa' : '', 'descricao-tecnologias' : '', 'link-externo-1' : '', 'link-externo-2' : '', 'fase' : 1, 'reuniao' : { 'data' : '', 'horario' : '', 'local' : '', 'datas-possiveis' : [] }, 'status' : { 'negado' : false, 'motivo' : 'falta de informaÃ§Ãµes' }, 'entregas' : [], 'alunos' : [], 'responsavel-cadi' : '', 'responsavel-professor' : [], 'responsavel-empresario' : 'teste@teste' }"));
		model.addProjeto(Document.parse("{'titulo' : 'Teste2', 'descricao-breve' : 'Teste descricao', 'descricao-completa' : 'Essa é a descrição completa', 'descricao-tecnologias' : 'Essa é a descrição de tecnologias', 'link-externo-1' : 'http://linkzao.com', 'link-externo-2' : 'http://linkzera.com', 'fase' : 3, 'reuniao' : { 'data' : '', 'horario' : '', 'local' : '', 'datas-possiveis' : [] }, 'status' : { 'negado' : false, 'motivo' : 'falta de informaÃ§Ãµes' }, 'entregas' : [], 'alunos' : [], 'responsavel-cadi' : '', 'responsavel-professor' : [], 'responsavel-empresario' : 'teste@teste' }"));
		model.addProjeto(Document.parse("{'titulo' : 'Teste3', 'descricao-breve' : 'Teste descricao', 'descricao-completa' : 'Essa é a descrição completa', 'descricao-tecnologias' : 'Essa é a descrição de tecnologias', 'link-externo-1' : 'http://linkzao.com', 'link-externo-2' : 'http://linkzera.com', 'fase' : 4, 'reuniao' : { 'data' : '', 'horario' : '', 'local' : '', 'datas-possiveis' : [] }, 'status' : { 'negado' : false, 'motivo' : 'falta de informaÃ§Ãµes' }, 'entregas' : [], 'alunos' : [], 'responsavel-cadi' : '', 'responsavel-professor' : [], 'responsavel-empresario' : 'teste@teste' }"));
    	}
    }
    
    
