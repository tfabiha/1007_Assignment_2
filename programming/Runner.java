/**
 * @author Tabassum Fabiha -- tf2478
 * 
 * test0 --> /commands/test0
 * 		 --> /results/test0.txt
 * 				size: 58 bytes
 *       --> /results/test0.cmp
 *       		size: 58 bytes
 * 		Tested: trying to replace a line that does not exist
 * 				trying to replace a line that does exist
 * 				getting a file with no extensions
 * 				quitting without saving
 * 				getting a new file without saving
 * 				quitting a file after saving
 * 				printing
 *       
 * test1 --> /commands/test1
 * 		 --> /results/test1.txt
 * 				size: 22 bytes
 *       --> /results/test0.cmp
 *       		size: 27 bytes
 *       Tested: creating a new file
 *       		 replacing a word with another word
 *       		 replacing a word with nothing
 *       		 trying a command that doesn't exist
 *       		 
 * test2 --> /commands/test2
 * 		 --> /results/test2.txt
 * 				size: 204 bytes
 *       --> /results/test0.cmp
 *       		size: 125 bytes
 *       Tested: opening specifically a .txt file
 *       		 replacing a word with multiple words
 *       		 saving specifically to a .txt file
 *
 */
public class Runner {

	public static void main(String[] args) {
		
		Talker myTalker = new Talker();
		myTalker.talk();
	}
}
