import java.io.*;


public class Runner {
	
	private ObjectInputStream obj_in;

	public Code load(String name) throws ClassNotFoundException, IOException {
		
		try {
			FileInputStream f_in = new FileInputStream(name);
			obj_in = new ObjectInputStream (f_in);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
		Object obj = obj_in.readObject(); //read the object

		if (obj instanceof Code)
		{
			Code ans = (Code) obj;
			System.out.println("Load successful: " + name);
			return ans;
		}
		return null;
	}
	
	public void save(Code obj, String name) throws IOException, FileNotFoundException {
		FileOutputStream f_out = new FileOutputStream(name);
		ObjectOutputStream obj_out;
		obj_out = new ObjectOutputStream(f_out);
		obj_out.writeObject(obj);
		obj_out.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
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