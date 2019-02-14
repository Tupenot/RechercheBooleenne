package searchEngine;

import java.util.Scanner;
import java.util.TreeMap;

public class Main {
	
	public static void main(String[] args) {
	
		@SuppressWarnings("resource")
		Scanner saisie = new Scanner(System.in);
		
		
		TreeMap<Integer, Document> documents = Index.loadDocuments();
		Index.loadVocabulary();
		
		System.out.print("Veuillez saisir votre requête : ");
		String requete = saisie.nextLine();
		
		Index.queryTreatment(requete);
		

	}
}