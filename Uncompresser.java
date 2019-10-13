import java.io.*;
import java.util.*;

public class Uncompresser {
	
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
	 * 
	 * TODO 
	 * CHANGE FROM SCANNER TO FILEREADER
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
	 * This method removes the line of the given index
	 */
	public void remove(int index) throws Exception {
		uncompressedContent.remove(index - 1);
		lastEditSaved = false;
	}
	
	/**
	 * @param index
	 * @param line
	 * @throws Exception
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
	 * @throws IOException if the file cannot be saved
	 * 
	 * Saves the file if no name is given by the user
	 */
	public void save() throws IOException {
		save(fileName);
	}
	
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
	private String dirPath = "./bin";
	private String DEFAULTFNAME = "NEWFILE";

	private LinkedList<String> uncompressedContent = new LinkedList<String>();
}
