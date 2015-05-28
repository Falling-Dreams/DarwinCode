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
	
	LDPCCode(int CWLength, double suggestedDensity) {
		super("Low Density Parity Check", CWLength);
		createPC(suggestedDensity);
		initialize();
	}
	
	LDPCCode(int CWLength, double mutateChance, LDPCCode parent) {
		super("Low Density Parity Check", CWLength);
		H = parent.getH();
		suggestedDensity = parent.suggestedDensity;
		mutatePC(mutateChance);
		initialize();
	}
	
	public void createPC(double suggestedDensity) {
		H = new int[n-k][n];
		for (int row = 0; row < H.length; row++) {
			for (int col = 0; col < H[row].length; col++) {
				if (Math.random() <= suggestedDensity) {
					H[row][col] = 1;
				}
				else {
					H[row][col] = 0;
				}
			}
		}
	}
	
	public void mutatePC(double mutateChance) {
		for(int row = 0; row < H.length; row++) {
			for(int col = 0; col < H[row].length; col++) {
				if (Math.random() <= mutateChance) {
					H[row][col] = (H[row][col] + 1) % 2;
				}
			}
		}
	}
}