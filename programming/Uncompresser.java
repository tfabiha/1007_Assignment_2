import java.io.*;
import java.util.*;

/**
 * @author Tabassum Fabiha -- tf2478
 * 
 * This Class implements the uncompresser method of editing a file. It stores all
 * info into compressedContent, which is a LinkedList<String> and the
 * reason for such is explained below at the end of the file.
 * 
 * The class is able to get files, create new files, print the compressed contents, remove
 * lines from the content, remove words from the content, replace words in the content,
 * replace lines in a content, and save the content to a file.
 * 
 * For saving if the user specifies that they would like to save as a .txt. If the user
 * does not specify the name of what the file should be saved as then the file is saved
 * with the same name that it was created with.
 * 
 * If at any point the methods throw an Exception then that means the user has not provided
 * a valid command. Examples of when this will happen: removing a line that does not exist,
 * trying to add a line outside of the given options, and not providing all the information
 * that is required to run a command. 
 */
public class Uncompresser {
	
	/**
	 * This method creates a new file with the name DEFAULTNAME.
	 */
	public Uncompresser() {
		fileName = DEFAULTFNAME;
	}
	
	/**
	 * @param fName name of file to open
	 * 
	 * This method first fixes the file extension of the given file and then attempts
	 * to open it. If it is successful in opening the file then it copies the contents
	 * of the file to the database. If not then we assume that we are creating a new
	 * file of that name. Thus regardless of the results of the boolean we set  
	 * fileName to fName.
	 */
	public Uncompresser(String fName) {
		
		fName = fixFileName(fName);
		
		try {
			uncompressedContent.clear();
			
			File dir = new File(dirPath);
			dir.mkdirs();
			File file = new File(dir, fName);
			
			Scanner sc = new Scanner(file); 
		    String line;
		    
		    while (sc.hasNextLine()) {
		    	line = sc.nextLine();
		    	uncompressedContent.add(line);
		    }
		    sc.close();
		}
		catch (Exception e) {
		}
		
		fileName = fName;
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
				!( name.substring(name.length() - 4).equals(".txt") )) {
			name += ".txt";
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
		
		ListIterator<String> fileIterator = uncompressedContent.listIterator(0); 
		System.out.println("0");
		while (fileIterator.hasNext()) {
			int index = fileIterator.nextIndex() + 1;
			String line = (String) fileIterator.next();
			
			String output = index + " " + line;
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
		uncompressedContent.remove(index - 1);
		lastEditSaved = false;
	}
	
	/**
	 * @param index index of the line in the perspective of the user to be replaced
	 * @param line new line at the given index
	 * @throws Exception if an invalid command is given
	 * 
	 * This method replaces line given by the index with the word form of the
	 * new line. It also sets lastEditSaved to false after all changes because a new
	 * edit has been made. 
	 */
	public void replace(int index, String line) throws Exception {
		if (index == 0) {
			uncompressedContent.addFirst(line);
			lastEditSaved = false;
		}
		else if (index == uncompressedContent.size() + 1) {
			uncompressedContent.addLast(line);
			lastEditSaved = false;
		}
		else {
			uncompressedContent.set(index - 1, line);
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
		write(inLine);
	}
	
	/**
	 * @param inLine string containing the word to be replaced and all the words to
	 * replace it
	 * 
	 * The method replaces all instances of the given word in the text with the new
	 * given words. It iterates through each line in the text and if it sees the word
	 * to be replaced, the replacement is made. If the changed line after being edited
	 * is empty then the entire line is removed.
	 */
	public void write(String inLine) {
		inLine = inLine.trim();
		
		String oldWord = inLine.split(" ")[0];
		String newWords = inLine.substring(oldWord.length() ).trim();
		
		ListIterator<String> iter = uncompressedContent.listIterator(0);
		while (iter.hasNext()) {
			String line = iter.next();
			
			String newLine = line.replace(oldWord, newWords);
			
			iter.remove();
			
			newLine = newLine.trim();
			if (!newLine.equals("")) {
				iter.add(newLine);
			}
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
	
	/**
	 * @param fName name to save the file as
	 * @throws IOException if the file cannot be opened or written to
	 * 
	 * This method saves the text into a file. It first fixes the file extension and then
	 * saves the text to that file name.
	 */
	public void save(String fName) throws IOException {
		fName = fixFileName(fName);
		
		File dir = new File(dirPath);
		dir.mkdirs();
		
		File file = new File(dir, fName);
		file.createNewFile();
		
		FileWriter fw = new FileWriter(file);
		
		ListIterator<String> fileIterator = uncompressedContent.listIterator(0); 
		while (fileIterator.hasNext()) {
	    	fw.write(fileIterator.next() + "\n");
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

	private boolean lastEditSaved = true;
	private String fileName = "";
	private String dirPath = "./";
	private String DEFAULTFNAME = "NEWFILE";

	/**
	 * The LinkedList is used to store the compressed content because we have the
	 * need to constantly add or remove lines from the content and since we will need
	 * to traverse the entirety of it for many of the times that we access the content
	 * the flaw with LinkedLists having to traverse through from the very beginning is
	 * not an issue. Each node is a String in order to capture the entire line in
	 * word format.
	 */
	private LinkedList<String> uncompressedContent = new LinkedList<String>();
}
