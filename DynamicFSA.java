import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DynamicFSA {
	//Implement trie data structure with 3 arrays
	private static int[] switchArr = new int[54];
	private static char[] symbolArr = new char[500];
	private static int[] nextArr = new int[500];

	//Initialize pointers used in trie logic
	private static int symbolPtr = 0;	//Set at 0 index of symbol array
	private static int lastReference = -1;	//Not used yet, Set to -1

	public static void main(String[] args) throws FileNotFoundException {
		//Set switchArr and nextArr values to -1
		Arrays.fill(switchArr, -1);
		Arrays.fill(nextArr, -1);

		/** READ FIRST INPUT FILE */
		ArrayList<String> reservedWords = new ArrayList<String>();
		readInput1(reservedWords);

		/** LOGIC TO STORE JAVA RESERVED WORDS IN TRIE */
		//Iterates through reservedWords array list
		for(int i=0; i < reservedWords.size(); i++){
			String word = reservedWords.get(i);
			int symbol = word.charAt(0);	//Set symbol to 1st character of word

			//If 1st character is not defined in switchArr
			if(switchArr[(symbol-71)] == -1) {
				//Set index to next available location (symbolPtr)
				switchArr[(symbol-71)] = symbolPtr;

				//Store unique part of word in symbolArr
				createUniqueWord(word, 1);

			} else {	//If 1st character is defined in switchArr
				//Update index to first instance where word begins (symbolPtr)
				symbolPtr = switchArr[(symbol-71)];	

				//Pointer to iterate through word
				int counter = 1;	//Set to 2nd character 

				/** LOGIC TO NAVIGATE TRIE */
				//Compare word to symbol array
				while(counter < word.length()) {
					//IF MATCH, keep iterating through word and symbolArr
					if (word.charAt(counter) == symbolArr[symbolPtr]) {	
						counter++;
						symbolPtr++;
					} else {	//IF NO MATCH
						if(nextArr[symbolPtr] != -1) {	
							//Update symbolPtr if there is an index to jump to
							symbolPtr = nextArr[symbolPtr];
						} else {	
							//Set nextArr to next available spot (lastReference)
							nextArr[symbolPtr] = lastReference;

							//Update symbolPtr to nextArr index
							symbolPtr = nextArr[symbolPtr];
							break;
						}
					}
				}
				//Store unique part of word in symbolArr
				createUniqueWord(word, counter);
			}
		}

		/** READ SECOND INPUT FILE */
		ArrayList<String> javaProgram = new ArrayList<String>();
		readInput2(javaProgram);

		/** LOGIC TO EXTRACT RESERVED WORDS AND IDENTIFIERS*/
		for(int i=0; i < javaProgram.size(); i++) {
			String word = javaProgram.get(i);

			//If it is a reserved word, print right away
			if(isReservedWord(reservedWords, word)) {
				System.out.println(word + " *");
			} else {
				//Begin crazy logic

			}
		}

		//Print trie data structure
		printSwitchArr();
		printSymbolNextArr();
	}

	

	/**
	 * Helper Method - Checks if word is a Java reserved word
	 */
	private static boolean isReservedWord(ArrayList<String> reservedWords, String s) {
		boolean bool = false;
		if(reservedWords.contains(s)) {
			bool = true;
		} 
		return bool;
	}

	/**
	 * Helper Method - Stores unique characters of word in symbolArr
	 * 				   Iterates from given counter to word.length()
	 */
	private static void createUniqueWord(String word, int counter) {
		//Put the rest of word in symbol array
		for(; counter < word.length(); counter++) {
			symbolArr[symbolPtr] = word.charAt(counter);
			symbolPtr += 1;
		}

		//Concatenate end_marker '*'
		//If we're at the end of the word and there is a star, then we have a duplicate. 
		//Don't add another star
		if(counter == word.length() && symbolArr[symbolPtr] != '*') {
			symbolArr[symbolPtr] = '*';
			symbolPtr += 1;
		}

		//Update symbolPtr to next available index
		lastReference = symbolPtr;
	}

	/**
	 * Helper Method - Read Project2_Input1.txt
	 * 				   Store Java reserved words in reservedWords ArrayList
	 */
	private static void readInput1(ArrayList<String> reservedWords) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader("Project2_Input1.txt"));

		try {	
			String line = "";
			while( (line = br.readLine()) != null) {
				String[] lineSplit = line.split(" ");
				if(lineSplit.length == 0) {
					reservedWords.add(line);
				} else {
					for(int i=0; i < lineSplit.length; i++) {
						reservedWords.add(lineSplit[i]);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper Method - Read Project2_Input2.txt
	 * 				   Store Java program reserved words and identifiers in javaProgram Array List 
	 */
	private static void readInput2(ArrayList<String> javaProgram) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader("Project2_Input2.txt"));

		try {	
			String line = "";
			while( (line = br.readLine()) != null) {
				//Replace all non-alphanumberic, _, and $ with spaces
				line = line.replaceAll("[^A-Za-z0-9_$]", " ");

				//Split the line using spaces as delimiter
				String[] lineSplit = line.split(" ");

				//Store word ONLY if it is an identifier
				for(int i=0; i < lineSplit.length; i++) {

					if((lineSplit[i]).equals("")) {
						//If empty string, don't add to Array List
					} else if(lineSplit[i].matches("[0-9]")) {
						//If word begins with integer, it is NOT an identifier
					} else {
						//It is an identifier, add to Array List
						javaProgram.add(lineSplit[i]);	
					}
				}
			}
			//System.out.println(Arrays.toString(javaProgram.toArray()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Formatting Method - Prints Symbol and Next Array contents
	 */
	private static void printSymbolNextArr() {
		/** 
		System.out.print("       ");
		for(int i=0; i < 20; i++) {
			System.out.printf("%5s", (char) (i + 65));
		}

		System.out.print("\nswitch:");
		for(int i=0; i < 20; i++) {
			System.out.printf("%5s", switchArr[i]);
		}
		System.out.println("\n");
		*/
		
		System.out.print("        ");
		for(int k=0; k<100; k++) {
			System.out.print(k + "  ");
		}

		System.out.println();
		System.out.println("symbol: " + Arrays.toString(symbolArr));
		System.out.println("next: " + Arrays.toString(nextArr));
	}
	
	/**
	 * Formatting Method - Prints Switch array contents
	 */
	private static void printSwitchArr() {
		//0 to 20
		System.out.print("       ");
		for(int i=0; i < 20; i++) {
			System.out.printf("%5s", (char) (i + 65));
		}

		System.out.print("\nswitch:");
		for(int i=0; i < 20; i++) {
			System.out.printf("%5s", switchArr[i]);
		}
		System.out.println("\n");

		//20 to 40
		System.out.print("       ");
		for(int i=20; i < 26; i++) {
			System.out.printf("%5s",(char) (i + 65));
		}

		for(int i=26; i < 40; i++) {
			System.out.printf("%5s",(char) (i + 71));
		}

		System.out.print("\nswitch:");
		for(int i=20; i < 40; i++) {
			System.out.printf("%5s", switchArr[i]);
		}
		System.out.println("\n");

		//40 to 54
		System.out.print("       ");
		for(int i=40; i < 52; i++) {
			System.out.printf("%5s",(char) (i + 71));
		}
		System.out.printf("%5s%5s", '_', '$');

		System.out.print("\nswitch:");
		for(int i=40; i < 54; i++) {
			System.out.printf("%5s", switchArr[i]);
		}
		System.out.println("\n\n");
	}
}
