package searchEngine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StopWord {
	
	private ArrayList<String> stopWords;
	
	public StopWord () {
		this.stopWords = new ArrayList<String>();
		init();
	}
	
	public boolean estPresent(String mot) {
		if(stopWords.contains(mot))
			return true;
		return false;
	}
	
	public void  init() {
		
		try {
			InputStream flux = new FileInputStream("datas/stopwords.txt");
			InputStreamReader lecture = new InputStreamReader(flux);
			BufferedReader buff = new BufferedReader(lecture);
			String ligne;
			
			while ((ligne = buff.readLine()) != null){
				stopWords.add(ligne);
			}
			buff.close(); 
			}		
		catch (Exception e){
			System.out.println(e.toString());
			}
	}

}
