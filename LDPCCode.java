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


public class LDPCCode extends Code {
	
	private static final long serialVersionUID = 3874234847726153634L;

	LDPCCode(int n, int k, double density) {
		super("Low Density Parity Check", n, k);
		
		//create parity check matrix, then initialize from super
		H = new int[n-k][n]; //TODO RIGHT HERE
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
		
		initialize();
	}

	public int[][] getG() {
		return this.G;
	}
	
	public int[][] getH() {
		return this.H;
	}
	
	public int[] getP() {
		return this.p;
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
	
}