import java.io.*;
import java.util.*;

/**
 * @author Tabassum Fabiha -- tf2478
 *
 * This Class implements the compresser method of editing a file. It stores all
 * info into compressedContent, which is a LinkedList<ArrayList<Integer>> and the
 * reason for such is explained below at the end of the file.
 * 
 * The class is able to get both CMP and TXT files, create new files, print the compressed 
 * contents, remove lines from the content, remove words from the content, replace words 
 * in the content, replace lines in a content, and save the content to a file, either TXT
 * or CMP.
 * 
 * For saving if the user specifies that they would like to save as a .txt then it is 
 * Immediately saved as such. For all other file names it is saved as a .cmp. If the user
 * does not specify the name of what the file should be saved as then the file is saved
 * with the same name that it was created with.
 * 
 * If at any point the methods throw an Exception then that means the user has not provided
 * a valid command. Examples of when this will happen: removing a line that does not exist,
 * trying to add a line outside of the given options, and not providing all the information
 * that is required to run a command. 
 */
public class Compresser {
	
	/**
	 * This method does the setup for opening a new file.
	 */
	public Compresser() {
		fileName = DEFAULTFNAME;
		dict = new Dictionary();
		dict.setCompressedText(compressedContent);
	}
	
	/**
	 * @param fName name of file to open
	 * 
	 * This method first fixes the file extension of the given file and then attempts
	 * to open it depending on if it's a CMP file or a TXT file. If it is successful in 
	 * opening the file then it copies the contents of the file to the database. If we
	 * are looking for a CMP file and it does not exist then we see if the TXT extension
	 * of the file name exists. If not then we assume that we are creating a new
	 * file of that name. Then, regardless of the results of the boolean we set  
	 * fileName to fName and set the dictionary's reference to the compressed text.
	 */
	public Compresser(String fName) {
		fName = fixFileName(fName);
		
		try {
			File dir = new File(dirPath);
			dir.mkdirs();
			File file = new File(dir, fName);
			
		    String line = "";
		    
		    if (isCMPFile(fName)) {
		    	try {
		    		getCMPFile(fName);
		    	}
		    	catch (Exception e) {
		    		String txtName = fName.substring(0, fName.length() - 4) + ".txt";
		    		getTXTFile(txtName);
		    	}
		    }
		    else {
		    	getTXTFile(fName);
		    }
		    
		}
		catch (Exception e) {
			dict = new Dictionary();
		}
		
		fileName = fName;
		dict.setCompressedText(compressedContent);
	}
	
	/**
	 * @param fName name of a file with the extension CMP
	 * @throws IOException if the file cannot be read/opened
	 * 
	 * This method opens the file and creates a new Dictionary with the first line
	 * of the opened file. It then reads the rest of the file into compressedContent.
	 */
	private void getCMPFile(String fName) throws IOException {
		File dir = new File(dirPath);
		dir.mkdirs();
		File file = new File(dir, fName);
		
	    String line = "";
	    
	    FileReader reader = new FileReader(file);
    	BufferedReader br = new BufferedReader(reader);
    	line = br.readLine();
    	
    	dict = new Dictionary(line);
    	
    	line = br.readLine();
    	while (line != null) {
    		
    		ArrayList<Integer> temp = new ArrayList<Integer>();
    		String[] wordIndicies = line.split(" ");
    		for (int i = 0; i < wordIndicies.length; i++) {
    			int index = Integer.parseInt( wordIndicies[i] );
    			temp.add(index);
    		}
    		
    		compressedContent.add(temp);
    		line = br.readLine();
    	}
    	
    	br.close();
	}
	
	/**
	 * @param fName name of a file with the extension CMP
	 * @throws FileNotFoundException if the file cannot be read/opened
	 * 
	 * This method opens the file and creates a new Dictionary with the entire text
	 * of the opened file. It then reads the entire file into compressedContent.
	 */
	private void getTXTFile(String fName) throws FileNotFoundException {
		File dir = new File(dirPath);
		dir.mkdirs();
		File file = new File(dir, fName);
		
		String line = "";
		
		Scanner sc = new Scanner(file); 
    	
    	while (sc.hasNextLine()) {
	    	line += sc.nextLine() + " ";
	    }
    	dict = new Dictionary(line);
    	sc.close();
    	
    	sc = new Scanner(file);
    	
    	while (sc.hasNextLine()) {
    		line = sc.nextLine();
    		
    		ArrayList<Integer> compressedLine = compressLine(line);
    		compressedContent.add(compressedLine);
    	}
    	
    	sc.close();
	}
	
	/**
	 * @param name name of the file
	 * @return if the extension of the file name is CMP
	 */
	private boolean isCMPFile(String name) {
		return name.length() < 5 || !( name.substring(name.length() - 4).equals(".txt"));
	}
	
	/**
	 * @param name file name provided
	 * @return new file name with appropriate extension
	 * 
	 * This method fixes the given file name. It removes all spaces at the ends of the 
	 * name and fixes the file extension if necessary. If the file already ends
	 * in .txt or .cmp then it's left as is. Else it appends a .cmp to the end of the 
	 * name.
	 */
	private String fixFileName(String name) {
		name = name.trim();
		if ( name.length() < 5 ||
				(!( name.substring(name.length() - 4).equals(".txt") ) &&
				!( name.substring(name.length() - 4).equals(".cmp") ))) {
			name += ".cmp";
		}
		return name;
	}
	
	/**
	 * This method first sees if a file is open, and if it isn't then then it tells
	 * the user it isn't a valid command. Otherwise it prints out all the lines of 
	 * the file in the editor including two extra lines on the top and bottom.
	 */
	public void print() throws Exception {
		if (fileName == "") {
			throw new Exception();
		}
		
		ListIterator<ArrayList<Integer>> fileIterator = compressedContent.listIterator(0); 
		System.out.println("0");
		while (fileIterator.hasNext()) {
			int index = fileIterator.nextIndex() + 1;
			ArrayList<Integer> line = fileIterator.next();
			String stringified = wordifyLine(line);
			
			String output = index + " " + stringified;
			System.out.println(output);
		}
		System.out.println(fileIterator.nextIndex() + 1);
	}
	
	/**
	 * @param index index of the line from the interface perspective to remove 
	 * @throws Exception if the index doesn't exist or if no file has been opened
	 * 
	 * This method removes the line at the given index
	 */
	public void removeLine(int index) throws Exception {
		compressedContent.remove(index - 1);
		lastEditSaved = false;
	}
	
	/**
	 * @param index index of the line in the perspective of the user to be replaced
	 * @param line new line at the given index
	 * @throws Exception if an invalid command is given
	 * 
	 * This method replaces line given by the index with the compressed form of the
	 * new line. It also sets lastEditSaved to false after all changes because a new
	 * edit has been made. 
	 */
	public void replace(int index, String line) throws Exception {
		ArrayList<Integer> compressedLine = compressLine(line);
		
		if (index == 0) {
			compressedContent.addFirst(compressedLine);
			lastEditSaved = false;
		}
		else if (index == compressedContent.size() + 1) {
			compressedContent.addLast(compressedLine);
			lastEditSaved = false;
		}
		else {
			compressedContent.set(index - 1, compressedLine);
			lastEditSaved = false;
		}
	}
	
	/**
	 * @param inLine string containing the word to be replaced
	 * 
	 * This method removes all instances of the given word in the text. It iterates
	 * through every line in the text and if it sees the word then it removes it. If
	 * the changed line after being edited is empty then the entire line is removed.
	 */
	public void removeWord(String inLine) {
		inLine = inLine.trim();
		
		int oldWordIndex = dict.getIndexOfWord(inLine);
		
		ListIterator<ArrayList<Integer>> iter = compressedContent.listIterator(0);
		while (iter.hasNext()) {
			ArrayList<Integer> line = iter.next();
			
			int i = 0;
			while (i < line.size()) {
				if (line.get(i).equals(oldWordIndex))
					line.remove(i);
				else
					i += 1;
			}
			
			if (line.size() <= 0) {
				iter.remove();
			}
			
		}
	}
	
	/**
	 * @param inLine string containing the word to be replaced and all the words to
	 * replace it
	 * 
	 * The method replaces all instances of the given word in the text with the new
	 * given words. It iterates through each line in the text and if it sees the word
	 * to be replaced, the replacement is made.
	 */
	public void write(String inLine) {
		inLine = inLine.trim();
		
		String oldWord = inLine.split(" ")[0];
		String[] newWords = inLine.substring(oldWord.length() ).trim().split(" ");
		
		int oldWordIndex = dict.getIndexOfWord(oldWord);
		int[] newWordsIndicies = new int[newWords.length];
		
		for (int i = 0; i < newWords.length; i++) {
			if ( !dict.exists(newWords[i]) )
				dict.addNewWord( newWords[i] );
			newWordsIndicies[i] = dict.getIndexOfWord( newWords[i] );
		}
		
		ListIterator<ArrayList<Integer>> iter = compressedContent.listIterator(0);
		while (iter.hasNext()) {
			ArrayList<Integer> line = iter.next();
			
			int i = 0;
			while (i < line.size()) {
				if (line.get(i).equals(oldWordIndex)) {
					line.remove(i);
					
					for (int j = 0; j < newWordsIndicies.length; j++) {
						line.add(i, newWordsIndicies[j]);
						i += 1;
					}
				}
				else
					i += 1;
			}
			
			if (line.size() <= 0) {
				iter.remove();
			}
			
		}
	}
	
	/**
	 * @throws IOException if the file cannot be saved
	 * 
	 * This method saves the file as the name of the file that was opened if no name 
	 * is given by the user.
	 */
	public void save() throws IOException {
		save(fileName);
	}
	
	/**
	 * @param fName name to save the file as
	 * @throws IOException if the file cannot be opened or written to
	 * 
	 * This method saves the compressed text into a file. It first fixes the file extension.
	 * If the file name is a CMP file then we save it in compressed form. If the file name
	 * is a TXT file then we save it in word form.
	 */
	public void save(String fName) throws IOException {
		fName = fixFileName(fName);
		dict.adjustCompressedContent();
		
		File dir = new File(dirPath);
		dir.mkdirs();
		
		File file = new File(dir, fName);
		file.createNewFile();
		
		FileWriter fw = new FileWriter(file);
		
		ListIterator<ArrayList<Integer>> fileIterator = compressedContent.listIterator(0); 
		if (isCMPFile(fName)) {
			fw.write(dict.toString() + "\n");
			
			while (fileIterator.hasNext()) {
				ArrayList<Integer> line = fileIterator.next();
				String stringified = "";
				
				for (int i = 0; i < line.size(); i++) {
					stringified += line.get(i) + " ";
				}
				
				stringified += "\n";
				
				fw.write(stringified);
			}
		}
		else {
			while (fileIterator.hasNext()) {
				String stringified = wordifyLine(fileIterator.next()) + '\n';
				fw.write(stringified);
			}
		}
		
	    fw.close();
				
	    lastEditSaved = true;
        System.out.println("SAVED FILE");
	}
	
	/**
	 * @return if the last edit made has been a save
	 */
	public boolean isLastEditSaved() {
		return lastEditSaved;
	}
	
	/**
	 * @param line the compressed form of a line
	 * @return the word form of a line
	 * 
	 * This method finds the word form of a line given the compressed form
	 * of the line.
	 */
	private String wordifyLine(ArrayList<Integer> line) {
		String stringified = "";
		
		for (int i = 0; i < line.size(); i++) {
			int indexOfWord = line.get(i);
			stringified += dict.getWordAtIndex(indexOfWord) + " ";
		}
		
		return stringified;
	}
	
	/**
	 * @param line the word form of a line
	 * @return the compressed form of a line
	 * 
	 * This method finds the compressed form of a line given the word form
	 * of the line.
	 */
	private ArrayList<Integer> compressLine(String line) {
		ArrayList<Integer> compressed = new ArrayList<Integer>();
		String[] wordsInLine = line.split(" ");
		
		for (int i = 0; i < wordsInLine.length; i++) {
			if (!dict.exists(wordsInLine[i])) {
				dict.addNewWord(wordsInLine[i]);
			}
			
			int index = dict.getIndexOfWord( wordsInLine[i] );
			compressed.add(index);
		}
		
		return compressed;
	}

	private boolean lastEditSaved = true;
	private String fileName = "";
	private String dirPath = "./";
	private String DEFAULTFNAME = "NEWFILE";

	/**
	 * The LinkedList is used to store the compressed content because we have the
	 * need to constantly add or remove lines from the content and since we will need
	 * to traverse the entirety of it for many of the times that we access the content
	 * the flaw with LinkedLists having to traverse through from the very beginning is
	 * not an issue.
	 * 
	 * The LinkedList is of type ArrayList<Integer> mostly in part to create an easily
	 * recognizable difference between what is the content as a whole and what is a 
	 * single line. I used an ArrayList instead of an array because I still needed each
	 * line in the content to be mutable to change if the user were to want to change a
	 * specific word in the line.
	 */
	private LinkedList<ArrayList<Integer>> compressedContent = new LinkedList<ArrayList<Integer>>();
	
	/**
	 * The dictionary will contain all the unique words that are in the content and the
	 * indices of all these words.
	 */
	private Dictionary dict;
}
