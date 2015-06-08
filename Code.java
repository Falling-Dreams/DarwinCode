import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Code superclass for the LDPCCode and HammingCode classes. Other linear block codes may be added in the future
 * @author Joshua Manuel
 * 
 */

public abstract class Code implements Serializable {

	private static final long serialVersionUID = -5270488753848121945L;

	protected int n; //codeword length
	protected int k; //number of data bits
	
	protected String type; //Hamming or LDPC?
	protected double efficiency = -1; //default score is zero. It will be modified by Genetic
	
	protected int[][] G; //generator
	protected int[][] H; //Parity check matrix
	protected int[][] abr; //abbreviated generator
	protected int[] p; //encoded message
	
	protected double density; //actual density of the code
	protected double suggestedDensity; //density to try to achieve
	
	/**
	 * Initializes the code's n and k values
	 * @param type Hamming or LDPC?
	 * @param CWLength the code word length for this code
	 */
	public Code(String type, int CWLength) {
		this.type = type;
		this.n = CWLength;
		this.k = CWLength - numParityBits(CWLength);
	}
	
	public int[][] getG() {
		return G;
	}
	
	public int[][] getH() {
		return H;
	}
	
	public int[] getP() {
		return p;
	}
	
	public double getEfficiency() {
		return this.efficiency;
	}
	
	public void setEfficiency(int i) {
		this.efficiency = i;
	}
	
	/**
	 * Returns the number of parity bits for the code
	 * @param CWLen
	 * @return
	 */
	public static int numParityBits(int CWLen) { //public and static to allow Genetic to get the message bits
		int ans = 0;
		int num = 0;
		while(num <= CWLen) { // the number of parity bits is calculated as the power of 2 closest to but not above the code word length
			ans++;
			num = (int) Math.pow(2, ans);
		}
		return ans;
	}
	
	/**
	 * Creates the generator matrix and calculates the actual density of the code
	 */
	public void initialize() {
		//Subclasses take care of creating the parity check matrix
		
		/*
		 * creates an abbreviated table for generator
		 * 
		 * Source: http://www.ece.umd.edu/~tta/resources/LDPC.pdf
		 */
		abr = new int[n-k][k];
		
		//Creates a table that only includes the data bits, not parity bits
		int aRow = 0;
		for(int row = 0; row < H.length; row++) {
			int aCol = 0;
			for(int col = 0; col <= H[row].length; col++) {
				if(!((col & -col) == col)) { //if the col isn't a power of two (the data bits are found at columns that aren't powers of two)
					abr[aRow][aCol] = H[row][col-1]; //add the column to the abbreviated table
					aCol++;
				}
			}
			aRow++;
		}
		
		//Creates the generator matrix
		G = new int[n][k]; 
		int r = 0;
		int c = 0;
		for (int gRow = 1; gRow < n; gRow++) {
			if ((gRow & -gRow) == gRow) { //if the row is a power of 2
				G[gRow-1] = abr[r]; //if the row is a parity bit (power of 2), add in the corresponding row from the earlier-created abbreviated table
				r++;
			}
			else { //creates a slant of ones, going to the next column if the row isn't a parity bit
				for (int i = 0; i < G[0].length; i++) {
					if (i == c) {
						G[gRow-1][i] = 1;
					}
					else {
						G[gRow-1][i] = 0;
					}
				}
				c++;
			}
		}
		G[G.length-1][G[0].length-1] = 1;
		
		calculateDensity(); //calculates the code's actual density
	}
	
	private void calculateDensity() {
	//simply set it to the number of ones over the total size of the PC matrix
		double numOnes = 0.0;
		for(int row = 0; row < H.length; row++) {
			for(int col = 0; col < H[row].length; col++) {
				if (H[row][col] == 1) {
					numOnes++;
				}
			}
		}
		this.density = numOnes / (H[0].length * H.length); //setting the density
}
	
	/**
	 * Encodes the message p, using the generator matrix.
	 * Encoding is achieved by multiplying the generator matrix and p.
	 * 
	 * Source: http://www.ece.umd.edu/~tta/resources/LDPC.pdf
	 * 
	 * @param p the message to encode
	 * @return the encoded message
	 */
	public int[] encode(int[] p) {
		int[] ans = new int[G.length];
		for (int row = 0; row < G.length; row++) {
			for (int col = 0; col < G[row].length; col++) {
				ans[row] = (G[row][col] * p[col]) % 2; //sets the current row to the corresponding multiplied matrix value
			}
		}
		this.p = ans;
		return ans;
	}
	
	
	/**
	 * A hard-decision decoder implementation for linear block codes.
	 * Runs one cycle of the HDD algorithm.
	 * 
	 * Cross-reference with this:
	 * Source: http://www.ece.umd.edu/~tta/resources/LDPC.pdf
	 * 
	 * Refer to the tanner graph explanation on page 5
	 * 
	 * @param y the received message, (in this version, unmodified from the parity check)
	 * @return the decoded message, but the data bits still need to be extracted
	 */
	public int[] decode(int[] y) {
		
		int[][] response = new int[H.length][y.length];
		int[] ans = new int[y.length];
		
		for(int row = 0; row < H.length; row++) {
			int sum = 0;
			for (int col = 0; col < H[row].length; col++) {
				if (H[row][col] == 1) {
					sum += y[col]; //calculates the sum of the ones in the row.
				}
			}
			for(int col = 0; col < H[row].length; col++) {
				if(H[row][col] == 1) { //if the node (col) is connected to the parity check node (row), represented by a 1
					int tempSum = (sum-y[col]) %2; //sets tempsum to whatever the sum minus whatever the value at the corresponding y index is, mod 2.
					if (tempSum == 0) {
						response[row][col] = 2;
					}
					else {
						
						response[row][col] = tempSum; //used for majority-rules decision of whichever bit is correct
					}
				}
			}
			
		}
		
		//Compare number of ones or zeroes. Which is higher?
		for(int col = 0; col < response[0].length; col++) {
			int num1 = 0;		
			int num0 = 0;
			//tally both
			for(int row = 0; row < response.length; row++) {
				if(response[row][col] == 1) {
					num1++;
				}
				if(response[row][col] == 2) {
					num0++;
				}
			}
			
			if(num1 > num0) { //keep the number for whichever has the higher count
				ans[col] = 1;
			}
			if(num1 < num0) {
				ans[col] = 0;
			}
			if (num1 == num0) { //flip the bit if number of ones and zeroes are equal
				ans[col] = (y[col]+1) % 2;
			}
		}
		
		return ans;
	}
	
	@Override
	public String toString() {
		//Code Rate
		String cr = "Code Rate: " + n + "/" + k + "\n";
		cr += "\n";
		
		//Generator
		String gen = "Generator Matrix:\n";
		for(int row = 0; row < G.length; row++) {
			for(int col = 0; col < G[0].length; col++) {
				gen += G[row][col];
			}
			gen += "\n";
		}
		gen += "\n";
		
		//Parity Check
		String pc = type + " Parity Check Matrix:\n";
		for(int row = 0; row < H.length; row++) {
			for(int col = 0; col < H[0].length; col++) {
				pc += H[row][col];
			}		
			pc += "\n";
		}
		pc += "\n";
		
		return cr + gen + pc;
	}
	
	public void printStats() {
		 DecimalFormat df = new DecimalFormat("#0.000"); //needed to format the code's actual density to 3 decimal places
		 
		 System.out.print("\tScore: " + efficiency + " Density: " + df.format(density));
	}
	
	/**
	 * Gets the code's actual density
	 * @return
	 */
	public double getDensity() {
		return density;
	}
}
