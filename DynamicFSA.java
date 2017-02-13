import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DynamicFSA {
	private static int[] switchArr = new int[54];
	private static char[] symbolArr = new char[500];
	private static int[] nextArr = new int[500];

	public static void main(String[] args) throws FileNotFoundException {
		//Set switchArr and nextArr values to -1
		Arrays.fill(switchArr, -1);
		Arrays.fill(nextArr, -1);

		/** READ FIRST INPUT FILE - contains Java reserved words*/
		ArrayList<String> reservedWords = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("Project2_Input1.txt"));

		try {	//Store java reserved words in reservedWords
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

		/** LOGIC TO STORE VALUES IN TRIE */
		int symbolPtr = 0;	//Pointer set at 0 index of symbol array
		int lastReference = -1;

		//Iterates through reservedWords array list
		for(int i=0; i < reservedWords.size(); i++){
			String word = reservedWords.get(i);
			int symbol = word.charAt(0);	//Set symbol to 1st character of word

			//If 1st character is not defined in switchArr
			if(switchArr[(symbol-71)] == -1) {	
				//Set index to next available location (symbolPtr)
				switchArr[(symbol-71)] = symbolPtr;

				//Put the rest of word in symbol array
				for(int counter=1; counter < word.length(); counter++) {
					symbolArr[symbolPtr] = word.charAt(counter);
					symbolPtr += 1;
				}

				//Concatenate reserved word end_marker '*'
				symbolArr[symbolPtr] = '*';
				symbolPtr += 1;

				//Update symbolPtr to next available index
				lastReference = symbolPtr;

			} else {	//If 1st character is defined in switchArr
				//Update index to first instance where word begins (symbolPtr)
				symbolPtr = switchArr[(symbol-71)];	

				//Set to 2nd character
				int counter = 1;	 

				/** LOGIC TO NAVIGATE TRIE */
				//Compare word to symbol array
				while(counter < word.length()) {
					//IF MATCH, keep iterating symbolArr
					if (word.charAt(counter) == symbolArr[symbolPtr]) {	
						counter++;
						symbolPtr++;
					} else {	//IF NO MATCH
						if(nextArr[symbolPtr] != -1) {	
							//Update symbolPtr to index to jump to
							symbolPtr = nextArr[symbolPtr];
						} else {	
							//Set nextArr to next available spot (lastReference)
							nextArr[symbolPtr] = lastReference;

							//Update symbolPtr nextArr index
							symbolPtr = nextArr[symbolPtr];
							break;
						}
					}
				}
				//Put the rest of word in symbol array
				for(; counter < word.length(); counter++) {
					symbolArr[symbolPtr] = word.charAt(counter);
					symbolPtr += 1;
				}

				//Concatenate end_marker '*'
				//If we're at the end of the word and there is a star, then we have a duplicate. 
				//And we dont' add another star
				//if(j == word.length() && symbolArr[symbolPtr] != '*') {
				symbolArr[symbolPtr] = '*';
				symbolPtr += 1;
				//}

				//Update symbolPtr to next available index
				lastReference = symbolPtr;
			}
		}

		//System.out.println("Switch: " + Arrays.toString(switchArr));
		printSwitchArr();
		System.out.print("        ");
		for(int k=0; k<100; k++) {
			System.out.print(k + "  ");
		}

		System.out.println();
		System.out.println("Symb : " + Arrays.toString(symbolArr));
		System.out.println("Next : " + Arrays.toString(nextArr));
	}

	/**
	 * Helper Method - Prints Switch array contents
	 */
	private static void printSwitchArr() {
		System.out.print("          ");
		for(int i=0; i < 20; i++) {
			System.out.print((char) (i + 65) + "    ");
		}

		System.out.print("\nswitch:  ");
		for(int i=0; i < 20; i++) {
			System.out.print(switchArr[i] + "   ");
		}
		System.out.println("\n");

		//20 to 40
		System.out.print("          ");
		for(int i=20; i < 26; i++) {
			System.out.print((char) (i + 65) + "   ");
		}

		for(int i=26; i < 40; i++) {
			System.out.print((char) (i + 71) + "    ");
		}

		System.out.print("\nswitch:  ");
		for(int i=20; i < 40; i++) {
			System.out.print(switchArr[i] + "   ");
		}
		System.out.println("\n");

		//40 to 54
		System.out.print("          ");
		for(int i=40; i < 52; i++) {
			System.out.print((char) (i + 71) + "    ");
		}
		System.out.print("_     $");

		System.out.print("\nswitch:  ");
		for(int i=40; i < 54; i++) {
			System.out.print(switchArr[i] + "   ");
		}
		System.out.println();
	}
}
