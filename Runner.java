import java.io.*;

/**
 * Main interface of the DarwinCode program.
 * @author Joshua Manuel
 *
 */
public class Runner {
	
	boolean isRunning = true;
	private String[] strings; //the strings for the program
	boolean paramsAreSet = false;
	private Genetic gen;
	private int CWLength;
	
	/*
	 * Loads the serialized code object from the filesystem with they specified name.
	 * @param name name of the file to load
	 * @return the initialized code object
	 */
	public Code load (String name) {
		Code obj = null;
		
		try {
			ObjectInputStream obj_in;
			FileInputStream f_in = new FileInputStream(name);
			obj_in = new ObjectInputStream (f_in);
			obj = (Code) obj_in.readObject();
			obj_in.close();
		} catch (IOException | ClassNotFoundException ex) {
			ex.getMessage();
		}
		if (obj instanceof Code)
		{
			Code ans = (Code) obj;
			return ans;
		}
		return obj;
	}
	
	/**
	 * Serializes the code, saving it to the filesystem.
	 * @param obj the object to serialize
	 * @param name the name to save obj under
	 */
	public void save(Code obj, String name) {
		try {
			if (obj != null) {
				FileOutputStream f_out = new FileOutputStream(name);
				ObjectOutputStream obj_out;
				obj_out = new ObjectOutputStream(f_out);
				obj_out.writeObject(obj);
				obj_out.close();
			}
			else {
				System.out.println("Not a valid index!");
			}
			
		} catch (IOException ex) {
			ex.getMessage();
			//ex.printStackTrace();
		}
		
	}
	
	/**
	 * Exports the code as plaintext, saving it to a file
	 * @param input the code to save as plaintext
	 * @param name filename to save the code under
	 */
	public void export(Code input, String name) {
		try {
			FileOutputStream f_out = new FileOutputStream(name);
			ObjectOutputStream obj_out;
			obj_out = new ObjectOutputStream(f_out);
			obj_out.writeObject(input.toString());
			obj_out.close();
		} catch (IOException ex) {
			ex.getMessage();
		}
	}
	
	/**
	 * Loads the strings needed for the program.
	 * @param name name of the text file they are stored under
	 * @return true if successful, false if not
	 */
	public boolean loadStrings(String name) {
		try {
			BufferedReader stream = new BufferedReader(new FileReader(name));
			
			String all = "";
			String input;
			do {
				input = stream.readLine();
				if (input != null) {
					all += input + "\n";
				}
			} while(input != null);
			stream.close();
			this.strings = all.split("###"); //splits the string according to this delimiter
		} catch (IOException ex) {
			ex.getMessage();
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Gets user-supplied parameters. This method could not be generalized due to the unique bounds each parameter.
	 * Afterwards, this method initializes the Genetic object used for the program.
	 */
	public void setParams() {
		paramsAreSet = true;
		int decodeCycles;
		do{
			decodeCycles = Util.getInt("Enter the number of decode cycles to test (Will affect how long the algorithm runs): ");
		} while (decodeCycles <= 0);
		
		int popSize;
		do {
			popSize = Util.getInt("Enter the size of the genetic algorithm's population: ");
		} while (popSize <=0);
		
		int numToSave;
		do {
			numToSave = Util.getInt("Enter the number of codes to save per iteration of the algorithm (Less than the population size): ");
		} while (numToSave > popSize);
		
		do {
			CWLength = Util.getInt("Enter the length of the codeword (a number greater than 4): ");
		} while (CWLength < 5);
		
		double density;
		do {
			density = Util.getNumber("Enter decimal between 0 and 1 to suggest the LDPC code's density (or -1 for random densities): ");
		} while ( (density < 0 || density >= 1) && density != -1);
		
		double mutateChance;
		
		do {
			mutateChance = Util.getNumber("Enter a number between 0 and 1 for the mutate chance: ");
		} while ( mutateChance > 1 || mutateChance < 0);
		
		gen = new Genetic(decodeCycles, popSize, numToSave, CWLength, density, mutateChance);
	}
	
	/**
	 * The main loop of DarwinCode
	 * Gets user input and redirects the program based on the user's choices.
	 */
	public void mainMenu() {
		System.out.print(this.strings[0]); //print the DarwinCode banner, welcome message
		gen = null;
		do {
			System.out.print(this.strings[1] + this.strings[2]); //print the menu and footer
			Double ans = Util.getNumber("What shall you choose? ");
			System.out.print(this.strings[ans.intValue() + 2]); //print the corresponding header for the part of the program
			int input = (int) ans.doubleValue();
			if (gen == null && (input >= 3 && input < 8)) { //Some options are locked until gen is initialized
				System.out.println("\t\tYou have to modify parameters first!");
			}
			else {
				int index;
				switch (input) {
				case 0: //Print the explanation
					System.out.print(strings[strings.length-1]);
					break;
				case 1: //try to run the simulation
					if (!paramsAreSet) { //only if parameters are set!
						setParams();
					}
					gen.run();
					break;
				case 2: //set the parameters of the genetic algorithm
					setParams();
					break;
				
				case 3: //import a code
					Code loadCode = load(Util.getString("Enter the name of the file: "));
					if (loadCode != null) {
						System.out.println("\n\tThe code was added at index " + gen.add(loadCode));
					}
					else {
						System.out.println("\n\tThe code couldn't be loaded!");
					}
					break;
				case 4: //add a Hamming code to the Genetic object's population
					gen.popAdd(new HammingCode(CWLength));
					System.out.println("\tSuccessful!\tSaved at index " + (gen.getPopSize()-1));
					gen.judgeCode(gen.get(gen.getPopSize()-1));
					break;
				case 5: //save a code
					gen.printPop();
					index = Util.getInt("\nEnter the index of the code you want: ");
					String name = Util.getString("Save as? ");
					save(gen.get(index), name);
					System.out.println("Save successful!");
					break;
				case 6: //inspect a code
					gen.printPop();
					index = Util.getInt("\nEnter the index of the code you want: ");
					System.out.println("\n" + gen.get(index));
					break;
				case 7: //export a code
					gen.printPop();
					index = Util.getInt("\nEnter the index of the code you want: ");
					System.out.println("Here's what your code looks like: ");
					System.out.println(gen.get(index).toString());
					export(gen.get(index), Util.getString("Enter the name to save the code under: "));
					break;
				case 8: //quit
					this.isRunning = false;
					break;
				}
				System.out.print(strings[2]);
			}

		} while(this.isRunning);
	}
	
	public static void main(String[] args) throws IOException {

		Runner run = new Runner();
		
		if (run.loadStrings("strings.txt")) { //the strings are essential for this program to run
			run.mainMenu();
		}
		else {
			System.out.println("The needed strings were not loaded; the program cannot start.");
		}
	}
}