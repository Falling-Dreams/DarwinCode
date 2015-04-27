/*
 * Bugfixes:
 * + Array index out of bounds for some instances
 * + Reliable Generator matrix generation
 * 
 * TODO:
 * + Allow for BEC decoding?
 * + Make decoding algorithm recursive? Just 'cause.
 * + Add noise simulator
 * + Evolve things.
 */


public class Code implements java.io.Serializable {
	private static final long serialVersionUID = 7711339048320293596L; //Random UID
	public int n;
	public int k;
	
	public int[][] G; //generator
	public int[][] H; //Parity check
	public int[][] abr; //abbreviated generator
	public int[] p; //encoded message
	
	Code(int n, int k) { //Hamming code
		//TODO use HARDCODED hamming matrix for now: 7, 4
				H = new int [][] { {1,0,1,0,1,0,1},
					{0,1,1,0,0,1,1},
					{0,0,0,1,1,1,1} };
	}
	
	/**
	 * Constructor
	 * @param n = length of message
	 * @param k = number of data bits
	 * @param density = double value representing the probability of 1
	 */
	Code(int n, int k, double density) {
		this.n = n;
		this.k = k;
		
		this.H = new int[n-k][n]; //parity check matrix
		for (int row = 0; row < H.length; row++) {
			for (int col = 0; col < H[row].length; col++) {
				if (Math.random() <= density) {
					H[row][col] = 1;
				}
				else {
					H[row][col] = 0;
				}
			}
		}
		
		
		//create abbreviated table for generator
		abr = new int[n-k][k];
		int aRow = 0;
		for(int row = 0; row < H.length; row++) {
			int aCol = 0;
			//System.out.println("Hrow length" + H[row].length);
			for(int col = 0; col <= H[row].length; col++) {
				if(!((col & -col) == col)) {
					this.abr[aRow][aCol] = H[row][col-1]; //TODO BUG
					aCol++;
				}
			}
			aRow++;
		}
		
		this.G = new int[n][k]; //generator matrix
		int row = 0;
		int c = 0;
		for (int gRow = 1; gRow < 7; gRow++) { //TODO
			//System.out.println("gSum     ->  " + gRow);
			if ((gRow & -gRow) == gRow) { //TODO nope
				//System.out.println("it worked!");
				G[gRow-1] = abr[row];
				row++;
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
	}
	
	Code(Code mom, Code dad) { //TODO
		
	}
	
	public int[] encode(int[] p) {
		int[] ans = new int[G.length];
		for (int row = 0; row < G.length; row++) {
			for (int col = 0; col < G[row].length; col++) {
				ans[row] += G[row][col] * p[col];
			}
		}
		System.out.print("Encoded:\t");
		for (int row = 0; row < ans.length; row++) {
			ans[row] = ans[row] % 2;
			System.out.print(ans[row]);
		}
		this.p = ans;
		return ans;
	}
	
	public int[] decode(int[] y) { //runs one cycle of the HDD algorithm TODO
		/*
		int[] ans = new int [y.length];
		for (int col = 0; col < H[0].length; col++) {
			int sum = 0;
			for (int row = 0; col < H.length; col++) {
				sum += H[row][col] * y[col];
			}
			ans[col] = sum % 2;
		}
		return ans;
		
		
		int[] cycle = new int[y.length];
		for (int row = 0; row < H.length; row++) {
			int sum = 0;
			for (int col = 0; col < H[0].length; col++) {
				//System.out.println(row + " " + col);
				sum += (y[col]*H[row][col]);
				//checks the multiplication math: System.out.println("sum: " + y[col] + " * " + H[row][col] + " = " + y[col]*H[row][col]);
				if (H[row][col] == 1) {
					cycle[col] = cycle[col] + sum - (y[col]*H[row][col]);
					//System.out.println("cycle: " + cycle[col]);
				}
			}
		}
		
		for(int i = 0; i < cycle.length; i++) {
			cycle[i] = cycle[i] % 2;
		}
		return cycle;*/
		
		int[][] response = new int[H.length][p.length];
		int[] ans = new int[p.length];
		
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
		
		System.out.println("\nRESPONSE");
		
		for(int row = 0; row < response.length; row++) {
			for(int col = 0; col < response[row].length; col++) {
				System.out.print(response[row][col]);
			}
			System.out.println();
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
		
		/*
		for(int row = 0; row < response.length; row++) {
			int num1 = 0;
			int num0 = 0;
			for(int col = 0; col < response[row].length; col++) {
				if(response[row][col] == 1) {
					num1++;
				}
				else
			}
		}
		*/
	}
	
	public void show() {
		System.out.println("Code Rate: " + this.n + "/" + this.k);
		System.out.println("Generator:");
		for(int row = 0; row < G.length; row++) {
			for(int col = 0; col < G[0].length; col++) {
				System.out.print(G[row][col]);
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Parity Check:");
		for(int row = 0; row < H.length; row++) {
			for(int col = 0; col < H[0].length; col++) {
				System.out.print(H[row][col]);
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("abbr: ");
		for(int row = 0; row < abr.length; row++) {
			for(int col = 0; col < abr[0].length; col++) {
				System.out.print(abr[row][col]);
			}
			System.out.println();
		}
	}
	

		
}