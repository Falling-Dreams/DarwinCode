/**
 * Hamming code implementation
 * @author Joshua Manuel
 *
 */
public class HammingCode extends Code{
	
	//for loading from serialization to identify the class
	private static final long serialVersionUID = 3849979856791666909L;
	
	HammingCode(int CWLength) {
		super("Hamming", CWLength);
		createPC();
		initialize();
	}
	
	/**
	 * Creates the parity check matrix for a Hamming code, which for this program all other matricies are based off of
	 */
	protected void createPC() {
		H = new int[n-k][n];
		
		for (int row = 0; row < H.length; row++) {
			
			//Pre-make the first column of the list, which simplifies the for loop
			if (row == 0) {
				H[row][0] = 1;
			}
			else {
				H[row][0] = 0;
			}
			/*
			 * The algorithm for creating the parity check matrix is as follows:
			 * 1) convert each column number to binary
			 * 2) create a one at the PC matrix location if there is a one at the corresponding parity check row of the binary string.
			 * 
			 * http://en.wikipedia.org/wiki/Hamming_code#General_algorithm
			 */
			for (int col = 2; col <= H[row].length; col++) {
				String colBinary = Integer.toBinaryString(col);
				if(colBinary.length() < row + 1) {
					H[row][col-1] = 0;
				}
				else {
					H[row][col-1] = Character.getNumericValue(colBinary.charAt(colBinary.length() - (row + 1))); //this is step 2
				}
			}
		}
	}
}
