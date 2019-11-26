package professor.hello;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;

public class ModelProfessor {

	MongoClient mongoClient = new MongoClient( "127.0.0.1" );
	MongoDatabase db = mongoClient.getDatabase("professor");
	
	public FindIterable<Document> myProjects(Document email) {
		
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find(new Document("responsavel-professor", email));

		return found;
	}
	

	public void addProjeto(Document doc) {
		MongoCollection<Document> projeto = db.getCollection("projeto");
		projeto.insertOne(doc);
	}
	
	//teste
	public void addProfessor(Document doc) {
		MongoCollection<Document> professor = db.getCollection("professor");
		professor.insertOne(doc);
	}

	public Document login(String email, String senha) {
		MongoCollection<Document> prof = db.getCollection("professor");
		Document found = prof.find(new Document("email", email).append("senha", senha)).first();
		return found;
	}
	

	
	public Document ativarProfessor(String email) {
		Document prof = searchByEmail(email);
		prof.replace("ativo", true);
		return updateProfessor(prof);
	}

	
	public Document searchByEmail(String email) {
		MongoCollection<Document> prof = db.getCollection("professor");
		Document found = prof.find(new Document("email", email)).first();
		return found;

	}

	/*Update*/
	public Document updateProjeto(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}
	
	
	public Document updateProfessor(Document projeto) {
		MongoCollection<Document> projetos = db.getCollection("professor");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}
	
	

}
