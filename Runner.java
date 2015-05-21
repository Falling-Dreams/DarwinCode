import java.io.*;

public class Runner {
	
	boolean isRunning = true;
	private String[] strings;
	boolean paramsAreSet = false;
	private Genetic gen;
	
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
	
	public void export(Code input, String name) {
		
		try {
			FileOutputStream f_out = new FileOutputStream(name);
			ObjectOutputStream obj_out;
			obj_out = new ObjectOutputStream(f_out);
			obj_out.writeObject(input.toString());
			obj_out.close();
		} catch (IOException ex) {
			ex.getMessage();
			//ex.printStackTrace();
		}
	}
	
	public double getNumber(String message) {
		BufferedReader input = new BufferedReader (new InputStreamReader(System.in));
		Double ans = null;
		do {
			System.out.print("\t\t" + message);
			try {
				ans = Double.parseDouble(input.readLine());
			}
			catch (NumberFormatException | IOException ex) {
				System.err.print(ex.getMessage() + "\n");
			}
		} while (ans == null);
		
		return ans;
	}
	
	public int getInt(String message) {
		return (int) Math.round((float)getNumber(message));
	}
	
	public String getString(String message) {
		BufferedReader input = new BufferedReader (new InputStreamReader(System.in));
		System.out.print("\t\t" + message);
		try {
			return input.readLine();
		}
		catch (IOException ex) {
			ex.getMessage();
		}
		return null;
		
	}
	
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
			this.strings = all.split("###");
		} catch (IOException ex) {
			ex.getMessage();
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void runSim() { //TODO
		
	}
	
	public void compareCodes() { //TODO
		
	}
	
	public void setParams() {
		paramsAreSet = true;
		String encodeMessage = getString("Enter the message to encode. A default will be used if you enter nothing: ");
		int decodeCycles = getInt("Enter the number of decode cycles (Will affect how long the algorithm runs): ");
		int popSize = getInt("What should the genetic algorithm's population size be? ");
		int numToSave = getInt("How many Codes should we save per iteration? ");
		
		int CWLength;
		do {
			CWLength = getInt("How long should each codeword be? (Any integer greater than 4)");
		} while (CWLength < 5);
		
		double density;
		do {
			density = getNumber("Enter a decimal number to set the LDPC code's density (or -1 for random ones)");
		} while ( (density < 0 || density >= 1) && density != -1);
		gen = new Genetic(encodeMessage, decodeCycles, popSize, numToSave, CWLength, density);
	}
	
	public void mainMenu() {
		System.out.print(this.strings[0]);
		gen = null;
		do {
			System.out.print(this.strings[1] + this.strings[2]);
			Double ans = this.getNumber("What shall you choose? ");
			System.out.print(this.strings[ans.intValue() + 2]);
			int input = (int) ans.doubleValue();
			if (gen == null && input > 3) {
				System.out.println("\t\tYou have to actually either import a code or set a population size first!");
			}
			else {
				int index;
				switch (input) {
				case 1:
					if (!paramsAreSet) {
						setParams();
					}
					gen.run();
					break;
				case 2:
					setParams();
					break;
				case 3: //save
					Code loadCode = load(getString("Enter the name of the file: "));
					if (loadCode != null) {
						System.out.println("\tThe code was added at index " + gen.add(loadCode));
					}
					else {
						System.out.println("\tThe code couldn't be loaded!");
					}
					break;
				case 4:
					gen.printPop();
					index = getInt("\nEnter the index of the code you want: ");
					String name = getString("Save as? ");
					save(gen.get(index), name);
					System.out.println("Save successful!");
					break;
				case 5: //compare code
					break;
				case 6: //inspect a code
					gen.printPop();
					index = getInt("\nEnter the index of the code you want: ");
					System.out.println(gen.get(index).toString());
					break;
				case 7: //export a code
					gen.printPop();
					index = getInt("\nEnter the index of the code you want: ");
					System.out.println(gen.get(index).toString());
					break;
				case 8: //quit
					this.isRunning = false;
					break;
				}
			}

		} while(this.isRunning);
	}
	
	public void debug() {
		/*Genetic g = new Genetic(100, 7, -1);
		g.printPop();
		
		Code a = new HammingCode(16, 11);
		System.out.println(a.toString());
		int[] p = {1,1,1,1,1,1,1,1,1,0,1};
		int[] ans = a.encode(p);
		int[] dec = a.decode(ans);
		System.out.println("Decoded:\n");
		for(int i = 0; i < dec.length; i++) {
			System.out.print(dec[i]);
		}
		

		this.save(a, "blorp.data");
		Code b = this.loadCode("blorp.data");
		if (b != null) {
			System.out.println(b.toString());
		}
		
		System.out.println("Hey");
		*/
	}
	
	public static void main(String[] args) throws IOException {
		Runner run = new Runner();
		//run.debug();
		
		if (run.loadStrings("strings.txt")) {
			run.mainMenu();
		}
		else {
			System.out.println("The needed strings were not loaded; the program cannot start.");
		}
	}
	
}