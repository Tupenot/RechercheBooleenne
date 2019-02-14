package searchEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriWord {
	
	public static List<String> tri (String query) {
        List<String> mots = new ArrayList<String>();
        mots.addAll(Arrays.asList(query.split(" ")));
        return mots;
		
	}

}
