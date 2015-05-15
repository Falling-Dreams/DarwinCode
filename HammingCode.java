
public class HammingCode extends Code{

	private static final long serialVersionUID = 3849979856791666909L;

	HammingCode(int n, int k) {
		super("Hamming", n, k);
		
		H = new int[n-k][n];
		
		for (int row = 0; row < H.length; row++) {
			
			if (row == 0) {
				H[row][0] = 1;
			}
			else {
				H[row][0] = 0;
			}
			
			for (int col = 2; col <= H[row].length; col++) {
				String colBinary = Integer.toBinaryString(col);
				if(colBinary.length() < row + 1) {
					H[row][col-1] = 0;
				}
				else {
					H[row][col-1] = Character.getNumericValue(colBinary.charAt(colBinary.length() - (row + 1)));
				}
			}
		}

		initialize();
	}
}
