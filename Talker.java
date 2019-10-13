import java.util.*;

/**
 * @author Tabassum Fabiha tf2478
 *
 */
public class Talker {

	/**
	 * This method handles the process of talking to the user and obtaining their
	 * input. For each input from the user it checks to see if the input needs an
	 * action. If it does, then it checks to see if we should quit based on the
	 * command given. If we should not quit than we process the command further.
	 * If there is an error caught at any point of this process we tell the user
	 * that they did not give a valid command, since that is the only condition in 
	 * which there will be an error.
	 */
	public void talk() {
		myScanner = new Scanner(System.in);
		
		System.out.print("> ");
		while (myScanner.hasNextLine()) {
			try {
				goodline = myScanner.nextLine();
				
				if (needsAction(goodline)) {
					if (shouldQuit(goodline)) {
						quit();
						break;
					}
					else {
						process(goodline);
					}
				}
			}
			catch(Exception e) {
				complain();
			}
			
			System.out.print("> ");
		}
	}
	
	/**
	 * @param inLine input passed in by a user
	 * @return if the input is a command to be acted upon
	 * @throws Exception when an invalid command is given
	 * 
	 * This method checks to see if the input contains the key to call a command.
	 * It throws an exception to say that a valid command has not been given.
	 * If the input is empty however, then we assume that the user only entered
	 * space and no warning is given.
	 */
	private boolean needsAction(String inLine) throws Exception {
		String[] command = inLine.split(" ");
		
		if (inLine.length() < 1) {
			return false;
		}
		
		for (int i = 0; i < commandKeys.length; i++) {
			if (commandKeys[i].equals(command[0])) {
				return true;
			}
		}
		
		throw new Exception();
	}
	
	// do
	private boolean shouldQuit(String inLine) {
		String[] command = inLine.split(" ");
		
		if (command[0].equals("q")) {
			if (!myCompresser.isLastEditSaved()) {
				System.out.println("PLEASE SAVE FIRST");
			}
			return myCompresser.isLastEditSaved();
		}
		return false;
	}
	
	/**
	 * This method handles letting the user know that the program is being quit.
	 */
	private void quit() {
		System.out.println("GOODBYE");
	}
	
	// do
	private void process(String inLine) throws Exception {
		String[] command = inLine.split(" ");
		
		// get
		if (command[0].equals("g")) {
			try {
				if (!myCompresser.isLastEditSaved()) {
					System.out.println("PLEASE SAVE FIRST");
					return;
				}
			}
			catch (Exception e) {
				
			}
			
			if (inLine.trim().length() < 2)
				myCompresser = new Compresser();
			else
				myCompresser = new Compresser( inLine.substring(2) );
		}
		
		//print
		else if (command[0].equals("p")) {
			myCompresser.print();
		}
		
		//replace
		else if (command[0].equals("r")) {
			if (command.length < 3)
				myCompresser.remove(Integer.parseInt(command[1]));
			else
				myCompresser.replace(Integer.parseInt(command[1]), inLine.substring(4));		
		}
		
		//save
		else if (command[0].equals("s")) {
			if (inLine.trim().length() < 2)
				myCompresser.save();
			else
				myCompresser.save(inLine.substring(2));
		}
	}
	
	/**
	 * This method lets the user know that they have entered an invalid command.
	 */
	private void complain() {
		System.out.println("THAT IS NOT A VALID COMMAND");
	}
	
	private static String[] commandKeys = {"g", "p", "r", "s", "q"};
	//private Uncompresser myCompresser = new Uncompresser();
	private Compresser myCompresser;
	private Scanner myScanner;
	private String goodline;
}
