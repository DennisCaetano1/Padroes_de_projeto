package aluno.hello;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;

public class ModelAluno {

	//Fongo fongo = new Fongo("app");

	MongoClient mongoClient = new MongoClient( "127.0.0.1" );
	MongoDatabase db = mongoClient.getDatabase("app");
	
	public String search(String chave, String valor) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document(chave, valor));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		// System.out.println(foundJson);
		return foundJson;
	}

	public String buscaPorDono(String emailAluno) {

		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document("responsavel-aluno", emailAluno));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		System.out.println(foundJson);
		return foundJson;
	}

	public String buscaSemDono() {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		FindIterable<Document> found = projects.find(new Document("responsavel-aluno", ""));
		String foundJson = StreamSupport.stream(found.spliterator(), false).map(Document::toJson)
				.collect(Collectors.joining(", ", "[", "]"));
		// System.out.println(foundJson);
		return foundJson;
	}
	
	public Document atribuirAluno(String emailAluno, String _id) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		Document found = projects.find(new Document("_id", _id)).first();
		BasicDBObject searchQuery = new BasicDBObject().append("_id", _id);
		found.put("responsavel-professor", emailAluno);
		projects.replaceOne(searchQuery, found);
		return found;
	}
	
	public Document atribuir(String emailAluno, String _id) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		Document found = projects.find(new Document("_id", _id)).first();
		BasicDBObject searchQuery = new BasicDBObject().append("_id", _id);
		found.put("responsavel-aluno", emailAluno);
		projects.replaceOne(searchQuery, found);
		return found;
	}
	
	public Document getProject(String _id) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projects = db.getCollection("projeto");
		Document found = projects.find(new Document("_id", _id)).first();
		return found;
	}

	public void addAluno(Document doc) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> researches = db.getCollection("alunos");
		researches.insertOne(doc);
	}

	public void addProjeto(Document doc) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projeto = db.getCollection("projeto");
		projeto.insertOne(doc);
	}

	public Document login(String email, String senha) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> alunos = db.getCollection("alunos");
		Document found = alunos.find(new Document("email", email).append("senha", senha)).first();

		return found;

	}

	public FindIterable<Document> listaProjetos() {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projetos = db.getCollection("projeto");
		FindIterable<Document> found = projetos.find();
		return found;
	}

	public Document updateProjeto(Document projeto) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}
	
	public Document submitProject(Document projeto, String autores, String desc, String link) {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> projetos = db.getCollection("projeto");
		BasicDBObject query = new BasicDBObject();
		query.append("_id", projeto.get("_id"));
		Bson newDocument = new Document("$set", projeto);
		return projetos.findOneAndUpdate(query, newDocument, (new FindOneAndUpdateOptions()).upsert(true));
	}
	
	public List<String> listAlunos() {
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> alunos = db.getCollection("alunos");
		FindIterable<Document> alunosF = alunos.find();
		List<String> listAlunos = new ArrayList<String>();
		for(Document proj:alunosF) {
			listAlunos.add(proj.toJson());
		}
		return listAlunos;
	}

	public void alterarId (String id, Document alteracao){
		Document filter = new Document("id", id);
		//MongoDatabase db = fongo.getDatabase("app");
		MongoCollection<Document> alunos = db.getCollection("alunos");
		alunos.updateOne(filter, alteracao);
		}

}
