import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Util {
    // Example Utility method
	public static double getNumber(String message) {
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
	
	public static int getInt(String message) {
		return (int) Math.round((float)getNumber(message));
	}
	
	public static String getString(String message) {
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
}