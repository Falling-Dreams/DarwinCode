import java.io.Serializable;

public abstract class Code implements Serializable {

	private static final long serialVersionUID = -5270488753848121945L;

	protected int n;
	protected int k;
	
	protected String type;
	
	protected int[][] G; //generator
	protected int[][] H; //Parity check
	protected int[][] abr; //abbreviated generator
	protected int[] p; //encoded message
	
	public Code(String type, int n, int k) {
		this.type = type;
		this.n = n;
		this.k = k;
		
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

	public void initialize() {
		//Subclasses take care of creating the parity check matrix
		//create abbreviated table for generator
		abr = new int[n-k][k];
		int aRow = 0;
		H[1][1] = 0; //TODO the array wasn't initialized?!?!?!?
		for(int row = 0; row < H.length; row++) {
			int aCol = 0;
			//System.out.println("Hrow length" + H[row].length);
			for(int col = 0; col <= H[row].length; col++) {
				if(!((col & -col) == col)) {
					abr[aRow][aCol] = H[row][col-1]; //TODO BUG
					aCol++;
				}
			}
			aRow++;
		}
		
		G = new int[n][k]; //generator matrix
		int row = 0;
		int c = 0;
		for (int gRow = 1; gRow < 7; gRow++) { //TODO FIX FOR MORE
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
		p = ans;
		return ans;
	}
	
	public abstract int[] decode(int[] y); //different for LDPC and Hamming
	
	public void show() {
		System.out.println("Code Rate: " + n + "/" + k);
		System.out.println("Generator:");
		for(int row = 0; row < G.length; row++) {
			for(int col = 0; col < G[0].length; col++) {
				System.out.print(G[row][col]);
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println(type + ":");
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
