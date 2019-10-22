import java.util.*;

/**
 * @author Tabassum Fabiha -- tf2478
 *
 * This Class contains the dictionary of unique words that are in the content, and 
 * the indices of those words.
 */
public class Dictionary {
	/**
	 * @param text words to add to the dictionary
	 * 
	 * This method creates a new instance of Dictionary and sets the dictionary
	 * with all the unique words in the given text.
	 */
	public Dictionary(String text) {
		addUniqueWordsToWords(text);
	}
	
	/**
	 * This method creates a new instance of Dictionary
	 */
	public Dictionary() {
	}

	/**
	 * @param text text containing all words that show up in the text
	 * 
	 * This method finds all the unique words in the given text and then adds them
	 * to words.
	 */
	private void addUniqueWordsToWords(String text) {
		String[] allWords = text.split("\\s+");
		
		//System.out.println( Arrays.toString(allWords) );
		for (int i = 0; i < allWords.length; i++) {
			if ( !exists(allWords[i]) )
				addNewWord(allWords[i]);
		}
		//System.out.println( words.toString() );
	}
	
	/**
	 * @param word new word
	 * 
	 * Adds a new word to the dictionary.
	 */
	public void addNewWord(String word) {
		words.add(word);
	}
	
	/**
	 * This method adjusts the compressed content and dictionary to only contain words
	 * that exist in the text. If a word in the dictionary is found to not be in the text
	 * then it removes the word from the dictionary and adjusts the compressed text to
	 * contain the correct indices for the newly changed dictionary.
	 */
	public void adjustCompressedContent() {
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
	
	/**
	 * @param index index of the dictionary word that was removed
	 * 
	 * This method adjusts the compressed text to fit the new dictionary. If an index
	 * in a line is greater than the removed index then that index must be subtracted 
	 * 1 to appropriatly fit the new dictionary.
	 */
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
	
	/**
	 * @param word word
	 * @return if the word exists in the dictionary
	 */
	public boolean exists(String word) {
		return words.contains(word);
	}
	
	/**
	 * @param index index of the word in the dictionary
	 * @return the word at the given index
	 */
	public String getWordAtIndex(int index) {
		return words.get(index);
	}
	
	/**
	 * @param word word
	 * @return the index of the word in the dictionary
	 */
	public int getIndexOfWord(String word) {
		return words.indexOf(word);
	}
	
	/**
	 * @param compressed reference to the compressed text used by the Compresser instance
	 * 
	 * This method sets the reference compressed text shared by both the dictionary and
	 * the compresser instances.
	 */
	public void setCompressedText(LinkedList<ArrayList<Integer>> compressed) {
		compressedText = compressed;
	}
	
	/**
	 * @return all the words in the dictionary in a single String separated by spaces
	 */
	public String toString() {
		String stringified = "";
		
		for (int i = 0; i < words.size(); i++) {
			stringified += words.get(i) + " ";
		}
		return stringified;
	}
	
	/**
	 * This needed to be an ArrayList, because it was the easiest to traverse through,
	 * and immediately get things at specific indices. 
	 */
	private ArrayList<String> words = new ArrayList<String>();
	
	/**
	 * The Dictionary needs a reference to this so it can adjust itself to get rid of
	 * words that aren't in use.
	 */
	private LinkedList<ArrayList<Integer>> compressedText;
}
