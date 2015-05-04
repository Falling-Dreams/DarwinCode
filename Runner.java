import java.io.*;

public class Runner {
	
	public Code load(String name) {
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
	
	public void debug() {
		Code a = new LDPCCode(7, 4, .4);
		//a.show();
		int[] p = {1,0,1,1};
		int[] ans = a.decode(a.encode(p));
		
		System.out.println("Decoded:\n");
		for (int i = 0; i < ans.length; i++) {
			System.out.print(ans[i]);
		}
		
		this.save(a, "blorp.data");
		Code b = this.load("blorp.data");
		if (b instanceof Code) {
			b.show();
		}
		System.out.println("Hey");
	}
	
	public static void main(String[] args) {
		Runner run = new Runner();
		run.debug();
	}
	
}