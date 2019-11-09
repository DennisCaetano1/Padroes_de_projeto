package cadi.hello;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;

public class ModelCadi {

//	mongoClient mongoClient = new mongoClient("app");
	MongoClient mongoClient = new MongoClient( "127.0.0.1" );
	MongoDatabase db = mongoClient.getDatabase("app");

	public String search(String chave, String valor) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document(chave, valor));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		// System.out.println(foundJson);
		return foundJson;
	}
	public String searchUsuario(String chave, String valor) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("cadi");
		FindIterable<Document> found = projects.find(new Document(chave, valor));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		// System.out.println(foundJson);
		return foundJson;
	}
	public FindIterable<Document> buscaPorDono(String email) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find(new Document("responsavel-cadi", email));

		return found;
	}
	public String buscaSemDono() {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document("responsavel-cadi", ""));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		// System.out.println(foundJson);
		return foundJson;
	}
	


	public void addCADI(Document doc) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> researches = db.getCollection("cadi");
		researches.insertOne(doc);
	}

	public void addProjeto(Document doc) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projeto = db.getCollection("projeto");
		projeto.insertOne(doc);
	}
	
	//teste
	public void addProfessores(Document doc) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> professor = db.getCollection("professor");
		professor.insertOne(doc);
	}

	public Document login(String email, String senha) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> cadi = db.getCollection("cadi");
		Document found = cadi.find(new Document("email", email).append("senha", senha)).first();
		return found;
	}
	
	public Document ativarCadi(String email) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> cadis = db.getCollection("cadi");
		Document cadi = searchByEmail(email);
		cadis.deleteOne(cadi);
		cadi.replace("ativo", true);
		BasicDBObject query = new BasicDBObject();
		query.append("id", cadi.get("id"));
		cadis.replaceOne(query, cadi);
		//cadis.findOneAndUpdate(query, cadi, (new FindOneAndUpdateOptions()).upsert(true));
		return cadi;
	}

	
	public Document searchByEmail(String email) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> cadi = db.getCollection("cadi");
		Document found = cadi.find(new Document("email", email)).first();
		return found;

	}

	public FindIterable<Document> listaProjetos() {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find();
		return found;
	}

	
	public List<String> listCadi() {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> cadiF = db.getCollection("cadi");
		FindIterable<Document> cadi= cadiF.find();
		List<String> listCadi = new ArrayList<String>();
		for(Document proj:cadi) {
			listCadi.add(proj.toJson());
		}
		return listCadi;
	}
	
	//test profs
	public FindIterable<Document> listProf() {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> prof = db.getCollection("professor");
		FindIterable<Document> found = prof.find();
		return found;
	}

	public void alterarId (String id, Document alteracao){
		Document filter = new Document("id", id);
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> cadiF = db.getCollection("cadi");
		cadiF.updateOne(filter, alteracao);
		}
	
	public void addReuniao(Document doc) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> reuniao = db.getCollection("reuniao");
		reuniao.insertOne(doc);

	}
	
	
	/*Update*/
	public Document updateProjeto(Document projeto) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}
	
	
	public Document updateCadi(Document projeto) {
		MongoDatabase db = mongoClient.getDatabase("app");
		MongoCollection<Document> projetos = db.getCollection("cadi");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}
	
	

}
