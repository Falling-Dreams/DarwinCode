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
	private double density;
	LDPCCode(int n, int k, double density) {
		super("Low Density Parity Check", n, k);
		this.density = density;
		
		//create parity check matrix, then initialize from super
		
		createPC();
		initialize();
	}
	
	LDPCCode(int CWLength, double density) {
		super("Low Density Parity Check", CWLength);
		this.density = density;
		createPC();
		initialize();
	}
	
	@Override
	public void createPC() {
		H = new int[n-k][n];
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
	}
	
	@Override
	public void printStats() {
		super.printStats();
		System.out.print("Density: " + this.density);
	}
}