/**
 * CS 311 Formal Languages and Automata
 * Project #2 - Build Dynamic FSA
 * Winter Quarter 2017
 * Dr. Daisy Sang
 * Author: Tanya Wanwatanakool
 *
 * In this project, we have two input files, Project2_Input1.txt
 * and Project2_Input2.txt, that hold Java reserved words and a 
 * Java program respectively.
 * 
 * DynamicFSA simulates a basic symbol table manager to 
 * recognize Java reserved words and identifiers using a trie 
 * data structure that is implemented with three arrays.
 *  
 *
 * How to compile, link, and run this program:
 *   1) Please make sure Project2_Input1.txt and Project2_Input2.txt are in the directory before running the program
 *   2) javac DynamicFSA.java
 *   3) java DynamicFSA
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DynamicFSA {
	//Used for trie logic iteration
	private static ArrayList<String> reservedWords = new ArrayList<String>();
	private static int lastReference = 0;	
	
	//Implement trie data structure with 3 arrays
	private static int[] switchArr = new int[54];
	private static char[] symbolArr = new char[2000];
	private static int[] nextArr = new int[2000];
	
	public static void main(String[] args) throws FileNotFoundException {
		//Set switchArr and nextArr values to -1
		Arrays.fill(switchArr, -1);
		Arrays.fill(nextArr, -1);

		//READ FIRST INPUT FILE
		readInput1(reservedWords);
		parseFile(reservedWords);

		//READ SECOND INPUT FILE
		ArrayList<String> javaProgram = new ArrayList<String>();
		readInput2(javaProgram);
		parseFile2(javaProgram);

		//PRINT TRIE CONTENTS
		printSwitchArr();
		printSymbolNextArr();
	}

	/**
	 * Helper Method - Parses through Project2_Input1.txt
	 *                 Extracts and stores Java reserved words in trie
	 */
	private static void parseFile(ArrayList<String> s) {
		for(int k = 0; k < s.size(); k++) {
			boolean dupe = false;
			String word = s.get(k);
			int index = indexToSwitch(word.charAt(0));	//Set index to ascii value of 1st character of word

			//If 1st character is not defined in switchArr
			if(switchArr[index] == -1) {
				//Set index to next available location
				switchArr[index] = lastReference;

				//Add the rest of new word
				for (int j = 1; j < word.length(); j++) {
					symbolArr[lastReference] = word.charAt(j);
					lastReference++;
				}

				//Append * for unique reserved word
				symbolArr[lastReference] = '*';
				lastReference++;
			} 

			//If 1st character is defined in switchArr
			else {	
				int i = 1;	//Pointer to compare word
				int j = switchArr[index];	//Pointer to compare symbolArr

				//LOGIC TO NAVIGATE TRIE 
				//Compare word to symbol array
				while(i < word.length()) {

					//IF NO MATCH
					if (word.charAt(i) != symbolArr[j]) {	
						//nextArr doesn't have an index to jump to
						if(nextArr[j] == -1) {	
							//Set nextArr to next available index (lastReference)
							nextArr[j] = lastReference;

							//Update to nextArr index
							j = nextArr[j];
							break;
						} 

						//nextArr has an index to jump to
						else {	
							//Update if there is an index to jump to
							j = nextArr[j];							
						}
					} 

					//IF MATCH, increment pointers
					//Keep iterating through word and symbolArr
					else {	
						i++;
						j++;
					}
				}

				//Duplicate handling
				if ((word.length() == i && symbolArr[j] == '*') || dupe == true) {
					System.out.println("DUPE FOUND = " + word);
				}		

				else {	//Not a duplicate

					if (i == word.length()) {	//Word contains a prefix and we still need to record nextArr
						nextArr[j] = lastReference; 
					} 

					while(i < word.length()) {	//Add the rest of new word
						symbolArr[lastReference] = word.charAt(i);
						lastReference++;	
						i++;
					}	

					//Append a * for unique reserved word
					symbolArr[lastReference] = '*';
					lastReference++;
				}
			}
		}
	}

	/**
	 * Helper Method - Parses through Project2_Input2.txt
	 *                 Extracts and stores reserved words and identifiers in trie
	 */
	private static void parseFile2(ArrayList<String> s) {
		for(int k = 0; k < s.size(); k++) {
			boolean dupe = false;
			String word = s.get(k);
			word.trim();

			//Prevent reserved word dupes
			if(reservedWords.contains(word)) {
				System.out.print(word + "* ");
				continue;
			}

			//Preserves format of Java Program
			if(word.equals("%")) {
				System.out.println();
				continue;
			}

			//Set index to ascii value of 1st character of word
			int index = indexToSwitch(word.charAt(0));	

			//If 1st character is not defined in switchArr
			if(switchArr[index] == -1) {
				//Set index to next available location
				switchArr[index] = lastReference;

				//Add rest of new word
				for (int j = 1; j < word.length(); j++) {
					symbolArr[lastReference] = word.charAt(j);
					lastReference++;
				}

				//Append ? for unique identifier
				symbolArr[lastReference] = '?';
				lastReference++;
				System.out.print(word + "? ");
			} 

			else {	//If 1st character is defined in switchArr
				int i = 1;	//Pointer to compare word
				int j = switchArr[index];	//Pointer to compare symbolArr

				//LOGIC TO NAVIGATE TRIE 
				//Compare word to symbol array
				while(i < word.length()) {

					//IF NO MATCH
					if (word.charAt(i) != symbolArr[j]) {	
						//nextArr doesn't have an index to jump to
						if(nextArr[j] == -1) {	
							//Set nextArr to next available index (lastReference)
							nextArr[j] = lastReference;

							//Update to nextArr index
							j = nextArr[j];
							break;
						} 

						//nextArr has an index to jump to
						else {	
							//Update if there is an index to jump to
							j = nextArr[j];						
						}
					} 

					//IF MATCH, increment pointers
					//Keep iterating through word and symbolArr
					else {	
						i++;
						j++;
					}
				}

				//Single letter handling
				if (word.length() == 1) { 
					while (true) {	//Until there is no index to jump to
						if (symbolArr[j] == '*' || symbolArr[j] == '?' || symbolArr[j] == '@') { //Duplicate
							dupe = true;
							break;
						}

						if (nextArr[j] == -1) {
							break;
						}

						j = nextArr[j];
					}	

					//Append ? for unique identifier
					if (!dupe) {
						symbolArr[lastReference] = '?';
						nextArr[j] = lastReference;
						lastReference++;
						System.out.print(word + "? ");
						continue;
					}	
				}

				//Prefix word handling
				if (i == word.length() && word.length() != 1 ) {
					while (true) {	//Until there is no index to jump to
						if (symbolArr[j] == '*' || symbolArr[j] == '?' || symbolArr[j] == '@') { //Duplicate
							dupe = true;
							break;
						}

						if (nextArr[j] == -1) {
							break;
						}

						j = nextArr[j];
					}	

					//Append ? for unique identifier
					if (!dupe) {
						symbolArr[lastReference] = '?';
						nextArr[j] = lastReference;
						lastReference++;
						System.out.print(word + "? ");
						continue;
					}
				}

				//Duplicate handling
				if (dupe == true || (word.length() == i && symbolArr[j] == '?') || (word.length() == i && symbolArr[j] == '@')) {
					//Append @ for repeated identifier
					symbolArr[j] = '@';
					System.out.print(word + "@ ");
					continue;
				}		

				else {	//Not a duplicate

					if (i == word.length()) { nextArr[j] = lastReference; } //Word contains prefix and we still need to record nextArr

					while(i < word.length()) {	//Add the rest of new word
						symbolArr[lastReference] = word.charAt(i);
						lastReference++;	
						i++;
					}

					//Append ? for unique identifier
					symbolArr[lastReference] = '?';
					lastReference++;
					System.out.print(word + "? ");
				}
			}
		}
	}


	/**
	 * Helper Method - Indexes character to switch array using ascii values
	 */
	private static int indexToSwitch(char symbol) {
		if (symbol >= 97) {
			//Symbol is a lower case character 
			return (symbol -97) + 26;
		} else if (symbol == 95){
			//Symbol is _
			return 52;
		} else if (symbol == 36){
			//Symbol is $
			return 53;
		} else {
			//Symbol is an upper case character
			return (symbol - 65);
		}
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
			while((line = br.readLine()) != null) {
				//Replace all non-alphanumberic, _, and $ with spaces
				line = line.replaceAll("[^A-Za-z0-9_$]", " ");

				//Split the line using spaces as delimiter
				String[] split = line.split(" ");

				//Add % to indicate new line
				String[] lineSplit = new String[(split.length + 1)];
				for(int i=0; i < split.length; i++) {
					lineSplit[i] = split[i];
				}
				lineSplit[(split.length)] = "%";

				//Store word ONLY if it is a reserved word or identifier
				for(int i=0; i < lineSplit.length; i++) {

					if((lineSplit[i]).equals("")) {
						//If empty string, don't add to Array List
					} else if(Character.isDigit(lineSplit[i].charAt(0)) ) {
						//Word begins with integer, it is NOT an identifier
					} else if(lineSplit[i].matches("[0-9]")) {
						//If word is integer, it is NOT an identifier
					} else {
						//It is a reserved word or identifier, add to Array List
						javaProgram.add(lineSplit[i]);	
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Formatting Method - Prints Symbol and Next Array contents
	 */
	private static void printSymbolNextArr() {
		for(int j=0; j <= (lastReference / 20); j++) {
			System.out.print("       ");
			for(int i=0; i < 20; i++) {
				System.out.printf("%5d", (i + (j*20)));
			}

			System.out.print("\nsymbol:");
			for(int i=0; i < 20; i++) {
				System.out.printf("%5s", symbolArr[(i + (j*20))]);
			}

			System.out.print("\n  next:");
			for(int i=0; i < 20; i++) {
				if(nextArr[(i+(j*20))] == -1) {
					System.out.printf("%5s", " ");
				} else {
					System.out.printf("%5s", nextArr[(i + (j*20))]);
				}
			}
			System.out.println("\n");
		}
	}

	/**
	 * Formatting Method - Prints Switch array contents
	 */
	private static void printSwitchArr() {
		//Indices 0 to 20
		System.out.print("       ");
		for(int i=0; i < 20; i++) {
			System.out.printf("%6s", (char) (i + 65));
		}

		System.out.print("\nswitch:");
		for(int i=0; i < 20; i++) {
			System.out.printf("%6s", switchArr[i]);
		}
		System.out.println("\n");

		//Indices 20 to 40
		System.out.print("       ");
		for(int i=20; i < 26; i++) {
			System.out.printf("%6s",(char) (i + 65));
		}

		for(int i=26; i < 40; i++) {
			System.out.printf("%6s",(char) (i + 71));
		}

		System.out.print("\nswitch:");
		for(int i=20; i < 40; i++) {
			System.out.printf("%6s", switchArr[i]);
		}
		System.out.println("\n");

		//Indices 40 to 54
		System.out.print("       ");
		for(int i=40; i < 52; i++) {
			System.out.printf("%6s",(char) (i + 71));
		}
		System.out.printf("%6s%6s", '_', '$');

		System.out.print("\nswitch:");
		for(int i=40; i < 54; i++) {
			System.out.printf("%6s", switchArr[i]);
		}
		System.out.println("\n\n");
	}
}
