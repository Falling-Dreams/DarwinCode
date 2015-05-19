import java.io.*;

public class Runner {
	
	boolean isRunning = true;
	private String[] strings;
	
	public Code loadCode(String name) {
		Object obj;
		
		try {
			ObjectInputStream obj_in;
			FileInputStream f_in = new FileInputStream(name);
			obj_in = new ObjectInputStream (f_in);
			obj = obj_in.readObject();
			obj_in.close();
		} catch (IOException | ClassNotFoundException ex) {
			ex.getMessage();
			//ex.printStackTrace();
			return null;
		}
		
		if (obj instanceof Code)
		{
			Code ans = (Code) obj;
			System.out.println("Load successful: " + name);
			return ans;
		}
		return null;
	}
	
	public void save(Code obj, String name) {
		try {
			FileOutputStream f_out = new FileOutputStream(name);
			ObjectOutputStream obj_out;
			obj_out = new ObjectOutputStream(f_out);
			obj_out.writeObject(obj);
			obj_out.close();
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
	
	public Double getNumber(String message) {
		BufferedReader input = new BufferedReader (new InputStreamReader(System.in));
		Double ans = null;
		do {
			System.out.print("\t\t" + message);
			try {
				ans = Double.parseDouble(input.readLine());
			}
			catch (NumberFormatException | IOException ex) {
				//System.err.print(ex.getMessage() + "\n");
			}
		} while (ans == null);
		
		return ans;
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
	
	public void runSim() {
		
	}
	
	public void modParam() {
		//Choose LDPC density
	}
	
	public void compareCodes() {
		
	}
	
	public void mainMenu() {
		System.out.print(this.strings[0]);
		do {
			System.out.print(this.strings[1] + this.strings[2]);
			Double ans = this.getNumber("What shall you choose? ");
			System.out.print(this.strings[ans.intValue() + 2]);
			int input = (int) ans.doubleValue();
			switch (input) {
			case 1:
				runSim();
				break;
			case 2:
				//modParam();
				break;
			case 3: //save
				int index = getNumber("Enter the index of the code you want: ").intValue();
				String name = getString("Save as? ");
				//save(population.get(index), name);
				break;
			case 4:
				//import
				break;
			case 5:
				//compare code
			case 6:
				this.isRunning = false;
			}
		} while(this.isRunning);
	}
	
	public void debug() {
		Genetic g = new Genetic(100, 7, -1);
		Genetic.printPop(g);
		/*
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
		run.debug();
		
		if (run.loadStrings("strings.txt")) {
			run.mainMenu();
		}
		else {
			System.out.println("The needed strings were not loaded; the program cannot start.");
		}
	}
	
}