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
	
	public void debug() {
		Code a = new HammingCode(16, 11);
		System.out.println(a.toString());
		int[] p = {1,1,1,1,1,1,1,1,1,0,1};
		int[] ans = a.encode(p);
		int[] dec = a.decode(ans);
		/*System.out.println("Decoded:\n");
		for(int i = 0; i < dec.length; i++) {
			System.out.print(dec[i]);
		}*/
		

		this.save(a, "blorp.data");
		Code b = this.loadCode("blorp.data");
		if (b != null) {
			System.out.println(b.toString());
		}
		
		System.out.println("Hey");

	}
	
	public Double getNumber(String message) {
		BufferedReader input = new BufferedReader (new InputStreamReader(System.in));
		Double ans = null;
		try {
			ans = Double.parseDouble(input.readLine());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return ans;
		
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
	
	public static void main(String[] args) throws IOException {
		Runner run = new Runner();
		if (run.loadStrings("strings.txt")) {
			for(int i = 0; i < run.strings.length; i++) {
				System.out.print(run.strings[i]);
			}
		}
		else {
			System.out.println("The needed strings were not loaded.");
		}
	}
	
}