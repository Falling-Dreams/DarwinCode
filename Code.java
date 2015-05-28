import java.io.Serializable;
import java.text.DecimalFormat;

public abstract class Code implements Serializable {

	private static final long serialVersionUID = -5270488753848121945L;

	protected int n;
	protected int k;
	
	protected String type;
	protected double efficiency = -1;
	
	protected int[][] G; //generator
	protected int[][] H; //Parity check matrix
	protected int[][] abr; //abbreviated generator
	protected int[] p; //encoded message
	
	protected int index;
	
	protected double density;
	protected double suggestedDensity;
	
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
	
	public static int numParityBits(int CWLen) { //public and static to allow Genetic to get the message bits
		int ans = 0;
		int num = 0;
		while(num <= CWLen) {
			ans++;
			num = (int) Math.pow(2, ans);
		}
		return ans;
	}
	
	public void initialize() {
		//Subclasses take care of creating the parity check matrix
		//create abbreviated table for generator
		abr = new int[n-k][k];

		int aRow = 0;
		for(int row = 0; row < H.length; row++) {
			int aCol = 0;
			for(int col = 0; col <= H[row].length; col++) {
				if(!((col & -col) == col)) { //if the col isn't a power of two
					abr[aRow][aCol] = H[row][col-1]; //TODO Need to feed in correct length message
					aCol++;
				}
			}
			aRow++;
		}
		
		G = new int[n][k]; //generator matrix
		int r = 0;
		int c = 0;
		for (int gRow = 1; gRow < n; gRow++) { //TODO FIX FOR MORE
			//System.out.println("gSum     ->  " + gRow);
			if ((gRow & -gRow) == gRow) { //TODO nope
				//System.out.println("it worked!");
				G[gRow-1] = abr[r];
				r++;
			}
			else {
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
		calculateDensity();
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
		this.density = numOnes / (H[0].length * H.length);
}
	
	public int[] encode(int[] p) {
		int[] ans = new int[G.length];
		for (int row = 0; row < G.length; row++) {
			for (int col = 0; col < G[row].length; col++) {
				ans[row] = (G[row][col] * p[col]) % 2;
			}
		}
		this.p = ans;
		return ans;
	}
	
	public int[] decode(int[] y) { //runs one cycle of the HDD algorithm TODO
		
		int[][] response = new int[H.length][y.length];
		int[] ans = new int[y.length];
		
		for(int row = 0; row < H.length; row++) {
			int sum = 0;
			for (int col = 0; col < H[row].length; col++) {
				if (H[row][col] == 1) {
					sum += y[col];
				}
			}
			for(int col = 0; col < H[row].length; col++) {
				if(H[row][col] == 1) {
					int tempSum = (sum-y[col]) %2;
					if (tempSum == 0) {
						response[row][col] = 2;
					}
					else {
						
						response[row][col] = tempSum;
					}
				}
			}
			
		}
		
		for(int col = 0; col < response[0].length; col++) {
			int num1 = 0;		
			int num0 = 0;
			for(int row = 0; row < response.length; row++) {
				if(response[row][col] == 1) {
					num1++;
				}
				if(response[row][col] == 2) {
					num0++;
				}
			}
			
			if(num1 > num0) {
				ans[col] = 1;
			}
			if(num1 < num0) {
				ans[col] = 0;
			}
			if (num1 == num0) { //flip the bit
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
		 DecimalFormat df = new DecimalFormat("#0.000");
		 
		 System.out.print("\tScore: " + efficiency + " Density: " + df.format(density));
	}
	
	public double getDensity() {
		return density;
	}
}
