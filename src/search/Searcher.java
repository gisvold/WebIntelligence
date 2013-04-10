package search;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;

import parser.CaseReader;

import dataClasses.PatientCase;

public class Searcher {
	
	private IndexSearcher searcher;
	private QueryParser parser;
	private IndexReader reader;
	private Indexer indexer;
	
	public Searcher() throws IOException{
		indexer = new Indexer();
		reader = IndexReader.open(indexer.getIndex());
		searcher = new IndexSearcher(reader);
		parser = new QueryParser(Version.LUCENE_35, "label", indexer.getAnalyzer());
	}

	
	public ScoreDoc[] search(String searchQuery) throws IOException, ParseException{
		
		System.out.println(searchQuery);
		String s = "label: "+searchQuery + " OR "+ "code: "+searchQuery;
		Query query = parser.parse(s);
		
		int hitsPerPage = 5;
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		getIndexSearcher().search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		return hits;
	}
	
	public IndexSearcher getIndexSearcher(){
		return searcher;
	}
	
	public void closeIndexReader() throws IOException{
		reader.close();
	}
	
	private static void patientATC() throws IOException, ParseException, CorruptIndexException{
		Searcher searcher = new Searcher();
		CaseReader caseReader = new CaseReader();
		for (PatientCase p : caseReader.getPatients()){
			for (int i = 0; i < p.getNumberOfSentences(); i++){
				ScoreDoc[] hits = searcher.search(p.getSentence()[i]);
				System.out.println("Found" + hits.length + " hits.");
				for (int j = 0; j < hits.length; j++){
					int docID = hits[j].doc;
					Document d = searcher.getIndexSearcher().doc(docID);
					System.out.println(docID);
					System.out.println((j + 1) + ". " + d.get("label"));
					System.out.println((j + 1) + ". " + d.get("code"));
				}
			}
		}
		searcher.closeIndexReader();
	}
}
