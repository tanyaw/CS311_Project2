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

	private static ArrayList<String> reservedWords = new ArrayList<String>();

	public static void main(String[] args) throws FileNotFoundException {
		//Set switchArray values to -1
		Arrays.fill(switchArr, -1);
		
		//Set NextArr values values to -1
		Arrays.fill(nextArr, -1);

		/** READ FIRST INPUT FILE */
		BufferedReader br = new BufferedReader(new FileReader("Project2_Input1.txt"));

		try {
			String line = "";
			/** READ RESERVED WORDS */
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


		/** TRANSITION MECHANISM */
		//Pointer points at 0 index of symbol array
		int symbolPtr = 0;
		int lastReference = -1;
		
		for(int i=0; i < reservedWords.size(); i++){
			System.out.println("run #" + i);
			String word = reservedWords.get(i);
			//first character of word
			int symbol = word.charAt(0);

			if(switchArr[(symbol-71)] == -1) {	//Word is undefined
				//Set character to index symbolPtr is at symbol array
				switchArr[(symbol-71)] = symbolPtr;

				//Put the rest of word in symbol array
				for(int j=1; j < word.length(); j++) {
					symbolArr[symbolPtr] = word.charAt(j);
					symbolPtr += 1;
				}

				//Concatenate end_marker '*'
				symbolArr[symbolPtr] = '*';
				symbolPtr += 1;
				
				//symbolPtr holds next available index
				lastReference = symbolPtr;
			} /**else {	//Word shares same 1st character
				symbolPtr = switchArr[(symbol-71)];	//Index of where 1st instance of switch word begins
				System.out.println("Jump to index: " + symbolPtr);
				
				Boolean exit = false;
				while(!exit) {
					//if (word.charAt is same as symbol[pointer])
						//if (symbol is not *) {
							//increment pointer
							//get next word.charAt
						//else
							//print endmarker;
							//exit = true;
					
					//
				}
			}*/
			
			else {	//Word shares AT LEAST same 1st character
				symbolPtr = switchArr[(symbol-71)];	//Index of same 1st character
				char w = word.charAt(1);	//2nd character
				//System.out.println("Changed symbolPtr: " + symbolPtr);
				Boolean exit = false;
				while(!exit) {
									
					for(int j=1; j < word.length(); j++) {
						//System.out.println("symbolArr[symbolPtr]" + symbolArr[symbolPtr]);
						if(symbolArr[symbolPtr] == w) {
							if(symbolArr[symbolPtr] != '*') {
								symbolPtr += 1;
								w = word.charAt(j+1);
							} else {
								symbolArr[symbolPtr] = '*';
								symbolPtr += 1;
								exit = true;
								break;
							}
						} else {
							if (nextArr[symbolPtr] != -1) {
								//Update symbolPtr to lastReference
								symbolPtr = nextArr[symbolPtr];
								break;
							} else {
								//Update nextArr
								nextArr[symbolPtr] = lastReference;
								symbolPtr = lastReference;
								//System.out.println("Last reference: " + lastReference);
								//Put the rest of word in symbol array
								//System.out.println("k: " + j);
								for(int k=j; k < word.length(); k++) { 
									symbolArr[symbolPtr] = word.charAt(k);
									symbolPtr += 1;
								}
								symbolArr[symbolPtr] = '*';
								symbolPtr += 1;
								lastReference = symbolPtr;
								exit= true;
								break;
							}
						}
					}
				}
			}
			
			System.out.println("symbolPtr: " + symbolPtr);
			//System.out.println("Arrya: " + Arrays.toString(symbolArr));
			System.out.print("        ");
			for(int k=0; k<100; k++) {
				System.out.print(k + " ");
			}
			System.out.println();
			System.out.println("Arrya: " + Arrays.toString(symbolArr));
		}
		
		
	}
	

	/**
	 * Helper Method - Prints Switch array contents
	 */
	private static void printSwitchArr() {
		System.out.print("          ");
		for(int i=0; i < 20; i++) {
			System.out.print((char) (i + 65) + "   ");
		}

		System.out.print("\nswitch:  ");
		for(int i=0; i < 20; i++) {
			System.out.print(switchArr[i] + "  ");
		}
		System.out.println("\n");

		//20 to 40
		System.out.print("          ");
		for(int i=20; i < 26; i++) {
			System.out.print((char) (i + 65) + "   ");
		}

		for(int i=26; i < 40; i++) {
			System.out.print((char) (i + 71) + "   ");
		}

		System.out.print("\nswitch:  ");
		for(int i=20; i < 40; i++) {
			System.out.print(switchArr[i] + "  ");
		}
		System.out.println("\n");

		//40 to 54
		System.out.print("          ");
		for(int i=40; i < 52; i++) {
			System.out.print((char) (i + 71) + "   ");
		}
		System.out.println("_   $");

		System.out.print("\nswitch:  ");
		for(int i=40; i < 54; i++) {
			System.out.print(switchArr[i] + "  ");
		}
		System.out.println();
	}
}
