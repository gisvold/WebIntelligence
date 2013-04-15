package src.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import src.dataClasses.ATC;
import src.dataClasses.Constants;
import src.dataClasses.ICD10;

public class Indexer {

	private IndexWriter indexWriter = null;
	private Directory index;
	private Analyzer analyzer;
	private IndexWriterConfig config;
	private File file;
	
	public Indexer() throws IOException{
		file = new File(Constants.ATCINDEX);
		
		//To make sure you use correct analyzer, specify text tokenizer
		analyzer = new SimpleAnalyzer(Version.LUCENE_35);
		config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
		//Creating the file and storing the index to HDD
		index = FSDirectory.open(file);
	}
	
	public Directory getIndex(){
		return index;
	}
	
	public Analyzer getAnalyzer(){
		return analyzer;
	}
	
	public void closeIndexWriter() throws IOException{
		if(indexWriter != null){
			indexWriter.close();
		}
	}
	
	public IndexWriter getIndexWriter() throws IOException{
		if(indexWriter == null){
			indexWriter = new IndexWriter(index, config);
		}
		return indexWriter;
	}
	
	public void addICDDocument(ICD10 code) throws IOException{
		IndexWriter indexWriter = getIndexWriter();
		Document icd = new Document();
		icd.add(new Field("label", code.getLabel(), Field.Store.YES, Field.Index.ANALYZED));
		icd.add(new Field("code", code.getIcdCode(), Field.Store.YES, Field.Index.ANALYZED));
		for (String syn : code.getSynonyms()){
			icd.add(new Field("Synonyms", syn, Store.YES, Index.ANALYZED));
		}
		try {
			indexWriter.addDocument(icd);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		indexWriter.commit();
	}
	
	public void addATCDocument(ATC code) throws IOException {
		IndexWriter indexWriter = getIndexWriter();
		Document atc = new Document();
		atc.add(new Field("label", code.getLabel(), Field.Store.YES, Field.Index.ANALYZED));
		atc.add(new Field("code", code.getCode(), Field.Store.YES, Field.Index.ANALYZED));
		try {
			indexWriter.addDocument(atc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		indexWriter.commit();
	}
	
	
	
}
