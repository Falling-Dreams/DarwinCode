import java.io.*;


public class Runner {
	
	private ObjectInputStream obj_in;
	
	/*
	 * @param name	The name of the file to load
	 * @return		
	 */
	public Code load(String name) {
		Object obj;
		try {
			FileInputStream f_in = new FileInputStream(name);
			obj_in = new ObjectInputStream (f_in);
			obj = obj_in.readObject(); //read the object
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
	
	public static void main(String[] args) {
		Runner run = new Runner();
		//run.test();
		//Code a = new Code(7, 4, .5);
		//run.save(a, "blorp.data");
		Code b = run.load("blorp.data");
		if (b instanceof Code) {
			b.show();
		}
		System.out.println("Hey");
		
	}
	
}