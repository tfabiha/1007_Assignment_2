import java.io.*;
import java.util.*;

public class Compresser {
	
	public Compresser() {
		fileName = DEFAULTFNAME;
		dict = new Dictionary();
		dict.setCompressedText(compressedContent);
	}
	
	/**
	 * @param fName name of file to open
	 * 
	 * This method first fixes the file extension of the given file and then attempts
	 * to open it. If it is successful in opening the file then it copies the contents
	 * of the file to the database. If not then we assume that we are creating a new
	 * file of that name. Thus regardless of the results of the boolean we set  
	 * fileName to fName.
	 * 
	 * TODO HAVE TO ADD RANDOM NAMES
	 * CHANGE FROM SCANNER TO FILEREADER
	 */
	public Compresser(String fName) {
		fName = fixFileName(fName);
		System.out.println(fName);
		System.out.println("about to open file");
		try {
			File dir = new File(dirPath);
			dir.mkdirs();
			File file = new File(dir, fName);
			
		    String line = "";
		    
		    System.out.println("got to open file");
		    
		    if (isCmpFile(fName)) {
		    	try {
		    		getCMPFile(fName);
		    	}
		    	catch (Exception e) {
		    		System.out.println("on the exception");
		    		String txtName = fName.substring(0, fName.length() - 4) + ".txt";
		    		System.out.println(txtName);
		    		getTXTFile(txtName);
		    		fName = txtName;
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
	
	private void getCMPFile(String fName) throws IOException {
		File dir = new File(dirPath);
		dir.mkdirs();
		File file = new File(dir, fName);
		
	    String line = "";
	    
	    FileReader reader = new FileReader(file);
    	BufferedReader br = new BufferedReader(reader);
    	line = br.readLine();
    	System.out.println(line);
    	
    	dict = new Dictionary(line);
    	
    	line = br.readLine();
    	while (line != null) {
    		System.out.println(line);
    		
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
	
	private boolean isCmpFile(String name) {
		return name.length() < 5 || !( name.substring(name.length() - 4).equals(".txt"));
	}
	
	/**
	 * @param name file name provided
	 * @return new file name with appropriate extension
	 * 
	 * This method fixes the given file name. It removes all spaces at the ends of the 
	 * name and fixes the file extension if necessary. If the file already ends
	 * in .txt then it's left as is. Else it appends a .txt to the end of the 
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
	 * This method removes the line of the given index
	 */
	public void remove(int index) throws Exception {
		compressedContent.remove(index - 1);
		lastEditSaved = false;
	}
	
	/**
	 * @param index
	 * @param line
	 * @throws Exception
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
	 * @throws IOException if the file cannot be saved
	 * 
	 * Saves the file if no name is given by the user
	 */
	public void save() throws IOException {
		save(fileName);
	}
	
	public void save(String fName) throws IOException {
		fName = fixFileName(fName);
		dict.adjustDictionary();
		
		File dir = new File(dirPath);
		dir.mkdirs();
		
		File file = new File(dir, fName);
		file.createNewFile();
		
		FileWriter fw = new FileWriter(file);
		
		ListIterator<ArrayList<Integer>> fileIterator = compressedContent.listIterator(0); 
		if (isCmpFile(fName)) {
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
	
	private String wordifyLine(ArrayList<Integer> line) {
		String stringified = "";
		
		for (int i = 0; i < line.size(); i++) {
			int indexOfWord = line.get(i);
			stringified += dict.getWordAtIndex(indexOfWord) + " ";
		}
		
		return stringified;
	}
	
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
	private String dirPath = "./bin";
	private String DEFAULTFNAME = "NEWFILE";

	private LinkedList<ArrayList<Integer>> compressedContent = new LinkedList<ArrayList<Integer>>();
	private Dictionary dict;
}
