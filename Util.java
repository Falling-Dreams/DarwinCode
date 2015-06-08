import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility classes for the DarwinCode program, may be used for others.
 * @author hal
 */

public final class Util {
    /**
     * Gets a double from the user after printing out a message
     * @param message the message to print
     * @return a user-entered double
     */
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
	
	/**
	 * Gets an integer from the user
	 * @param message the message to print out
	 * @return user-entered integer
	 */
	public static int getInt(String message) {
		return (int) Math.round((float)getNumber(message));
	}
	
	/**
	 * Gets a string from the user
	 * @param message the message to request a string
	 * @return user-entered string
	 */
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