import java.util.*;

public class Dictionary {
	public Dictionary(String text) {
		addUniqueWordsToWords(text);
	}
	
	public Dictionary() {
	}

	private void addUniqueWordsToWords(String text) {
		String[] allWords = text.split("\\s+");
		
		System.out.println( Arrays.toString(allWords) );
		for (int i = 0; i < allWords.length; i++) {
			if ( !exists(allWords[i]) )
				addNewWord(allWords[i]);
		}
		//System.out.println( words.toString() );
	}
	
	public void addNewWord(String word) {
		words.add(word);
	}
	
	public void adjustDictionary() {
		for (int i = 0; i < words.size(); i++) {
			boolean appeared = false;
			
			ListIterator<ArrayList<Integer>> iter = compressedText.listIterator(0);
			while (iter.hasNext()) {
				ArrayList<Integer> line = iter.next();
				
				appeared = appeared || line.contains(i);
			}
			
			if (!appeared) {
				adjustRemovedAt(i);
				words.remove(i);
			}
		}
	}
	
	private void adjustRemovedAt(int index) {
		ListIterator<ArrayList<Integer>> i = compressedText.listIterator(0);
		
		while (i.hasNext()) {
			ArrayList<Integer> line = i.next();
			//System.out.println(line.toString());
			
			for (int j = 0; j < line.size(); j++) {
				if (line.get(j) > index)
					line.set(j, line.get(j) - 1);
			}
		}
	}
	
	public boolean exists(String word) {
		return words.contains(word);
	}
	
	public String getWordAtIndex(int index) {
		return words.get(index);
	}
	
	public int getIndexOfWord(String word) {
		return words.indexOf(word);
	}
	
	public void setCompressedText(LinkedList<ArrayList<Integer>> compressed) {
		compressedText = compressed;
	}
	
	public String toString() {
		String stringified = "";
		
		for (int i = 0; i < words.size(); i++) {
			stringified += words.get(i) + " ";
		}
		return stringified;
	}
	
	private ArrayList<String> words = new ArrayList<String>();
	private LinkedList<ArrayList<Integer>> compressedText;
}
