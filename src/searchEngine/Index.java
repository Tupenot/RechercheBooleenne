package searchEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

public class Index {

   static final String pathDocs= "datas/documents.data";
   static final String pathVoc = "datas/vocabulary.data";

   static TreeMap<String,Keyword> keywords;
   static TreeMap<Integer,Document> documents;

    public Index() {
        documents = new TreeMap<Integer,Document>();
    }
    
    public static boolean isNotOperator(String mot) {
    	
    	if (mot.charAt(0) != mot.toUpperCase().charAt(0) && mot.charAt(1) != mot.toUpperCase().charAt(1)
    													|| 
			(mot.charAt(0) == mot.toUpperCase().charAt(0) && mot.charAt(1) != mot.toUpperCase().charAt(1))){
    		return true;
    	}
    	
    	return false;
    }
    
    public static LinkedHashMap<String, Keyword> verifMots(List<String> listesmots) {
    	
    	LinkedHashMap<String, Keyword> treeKey = new LinkedHashMap<>();
    	
    	for (int i = 0; i < listesmots.size(); i++) {
    		
    		if ( keywords.containsKey(listesmots.get(i))) {	
    			Keyword keyword = keywords.get(listesmots.get(i));
    			treeKey.put(listesmots.get(i), keyword);
    		}
    		
    		if ( listesmots.get(i).equals("AND") || listesmots.get(i).equals("OR") || listesmots.get(i).equals("NOT")) 
    			treeKey.put(listesmots.get(i), null);
    		
    	}	   
    	return treeKey;
    }
    
    public static boolean queryIsOneWord(HashMap<String, Keyword> motsrequetes) {
    	
    	System.out.println("Mots contenus dans la requête : " + motsrequetes.keySet());
    	
    	boolean contientqunmot = true;
    	
    	for (String mot : motsrequetes.keySet()) {
    		if(!isNotOperator(mot))
    			contientqunmot = false;
    	}
    	
    	return contientqunmot;
    }
    
    public static ArrayList<Document> oneWordQueryTreatment(String mot, ArrayList<Document> resultatDocs) {
    	
		for (Document d : documents.values()) {	
			if (d.getText() != null) {
    			if ( d.getText().contains(mot)) 
    				resultatDocs.add(d);	
			}
		}	
		
		return resultatDocs;
    }
    
    public static ArrayList<Document> queryTreatment(String requeteUser) {
    	
       	if(requeteUser.length()==0) 
    		return null;  
    	
    	List<String> motsrequetes = new ArrayList<String>(TriWord.tri(requeteUser));

    	LinkedHashMap<String, Keyword> motmo = verifMots(motsrequetes);
    	
    	boolean requeteUnMot = queryIsOneWord(motmo);
    	
    	ArrayList<Document> resultatDocs = new ArrayList<>();
    	
    	if (requeteUnMot) 
    		return oneWordQueryTreatment(requeteUser, resultatDocs);
    		
       	oneWordQueryTreatment(motmo.entrySet().iterator().next().getKey(), resultatDocs);
	    
	    for (int j = 0 ; j < motmo.keySet().size() ; j++) {
	    	if (new Vector<>(motmo.keySet()).get(j).equals("AND")) {
	    		resultatDocs = AND(resultatDocs, new Vector<>(motmo.keySet()).get(j+1));
	    	}
	    	else if (new Vector<>(motmo.keySet()).get(j).equals("NOT")) {
	    		resultatDocs = NOT(resultatDocs, new Vector<>(motmo.keySet()).get(j+1));
	    	}
	    	else if ( new Vector<>(motmo.keySet()).get(j).equals("OR")) {
	    		resultatDocs = OR(resultatDocs,  new Vector<>(motmo.keySet()).get(j+1));
	    	}
	    }
	    
	    return resultatDocs;
    	
    }
    
    public static ArrayList<Document> OR(ArrayList<Document> resultatDocs, String mot) {
    
    	for (Document d : documents.values()) {
    		if (d.getText() != null) {
	    		if ( d.getText().contains(mot) && !resultatDocs.contains(d)) {
	    			resultatDocs.add(d);
	    		}
    		}
    	}
    	
    	System.out.println("Affichage des identifiants de document : ");
    	for (Document d : resultatDocs) {
    		System.out.println(d.getId());
    	}
    	
    	return resultatDocs;
    }

    public static ArrayList<Document> NOT(ArrayList<Document> resultatDocs, String motinterdit) {
 
    	Iterator<Document> docu = resultatDocs.iterator();
    	
    	while (docu.hasNext()) {		
    		
			Document u = docu.next();			
			if (u.getText().contains(motinterdit)) {
				docu.remove();	
			}
		}
    	
    	System.out.println("Affichage des identifiants de document : ");
    	for (Document d : resultatDocs) {
    		System.out.println(d.getId());
    	}
    	return resultatDocs;
    	
    }


    public static ArrayList<Document> AND(ArrayList<Document> resultatDocs, String m) {
    	    	
    	Iterator <Document> it = resultatDocs.iterator();
    	
    	while (it.hasNext()) {
    		Document d = it.next();
    		if (!d.getText().contains(m)) {
    			it.remove();
    		}
    	}
    	
    	System.out.println("Affichage des identifiants de document après traitement : ");
    	for (Document d : resultatDocs) {
    		System.out.println(d.getId());
    	}
    	 	
        return resultatDocs;
    }
       
    public Keyword getKeyword(String key) {
        return keywords.get(key);
    }

    public void addKeyword(String key, Keyword keyword){
        keywords.put(key, keyword);
    }

    public Document getDocument(Integer id){
        return documents.get(id);
    }

    public void addDocument(Integer id, Document doc){
        documents.put(id, doc);
    }

    public static void saveVocabulary(){
    	
        try{
            File fileTemp = new File(pathVoc);
            if (fileTemp.exists()){
                fileTemp.delete();
            }
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream(pathVoc);

            // Write object with ObjectOutputStream
            @SuppressWarnings("resource")
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

            // Write object out to disk

            obj_out.writeObject (keywords);

        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
	public static TreeMap<String,Keyword> loadVocabulary(){
        try{
            System.out.println("Lecture du vocabulaire..");
            // Read from disk using FileInputStream
             FileInputStream f_in = new FileInputStream(pathVoc);

            // Read object using ObjectInputStream
            @SuppressWarnings("resource")
			ObjectInputStream obj_in = new ObjectInputStream (f_in);

            // Read an object
            keywords = (TreeMap<String,Keyword>)obj_in.readObject();
            System.out.println("Lecture terminée");
            return keywords;

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    return null;
    }

    public static void saveDocuments(){

        try{

            File fileTemp = new File(pathDocs);
            if (fileTemp.exists()){
                fileTemp.delete();
            }
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream(pathDocs);

            // Write object with ObjectOutputStream
            @SuppressWarnings("resource")
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

            // Write object out to disk

            obj_out.writeObject (documents);

        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
	public static TreeMap<Integer,Document>loadDocuments() {
        try{
            // Read from disk using FileInputStream
            System.out.println("Lecture des documents..");
            FileInputStream f_in = new FileInputStream(pathDocs);

            // Read object using ObjectInputStream
            @SuppressWarnings("resource")
			ObjectInputStream obj_in = new ObjectInputStream (f_in);

            // Read an object
            documents = (TreeMap<Integer,Document>)obj_in.readObject();
            System.out.println("Lecture terminée");
            return documents;

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
