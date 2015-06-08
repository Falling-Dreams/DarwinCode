/**
 * LDPC code implementation
 * @author Joshua Manuel
 */
public class LDPCCode extends Code {
	
	private static final long serialVersionUID = 3874234847726153634L;
	
	/**
	 * Constructor based on the LDPC code's suggested density
	 * @param CWLength code word length
	 * @param suggestedDensity the suggested density; will vary, but the PC matrix will try to be close
	 */
	LDPCCode(int CWLength, double suggestedDensity) {
		super("Low Density Parity Check", CWLength);
		createPC(suggestedDensity);
		initialize();
	}
	
	/**
	 * Constructor based on a parent LDPCCode, with a chance for mutation
	 * @param CWLength code word length
	 * @param mutateChance the chance that a given bit will flip
	 * @param parent the parent node
	 */
	LDPCCode(int CWLength, double mutateChance, LDPCCode parent) {
		super("Low Density Parity Check", CWLength);
		H = parent.getH();
		suggestedDensity = parent.suggestedDensity;
		mutatePC(mutateChance);
		initialize();
	}
	
	/**
	 * Creates the parity check matrix for the LDPC code,
	 * Simply creates a one at the PC matrix spot based on a RNG
	 * @param suggestedDensity the suggested density
	 */
	public void createPC(double suggestedDensity) {
		H = new int[n-k][n];
		for (int row = 0; row < H.length; row++) {
			for (int col = 0; col < H[row].length; col++) {
				if (Math.random() <= suggestedDensity) { //creates the PC matrix with the suggested density
					H[row][col] = 1;
				}
				else {
					H[row][col] = 0;
				}
			}
		}
	}
	
	/**
	 * Mutates the PC matrix based on mutateChance
	 * @param mutateChance chance a given bit will flip
	 */
	public void mutatePC(double mutateChance) {
		for(int row = 0; row < H.length; row++) { //for every bit in the PC matrix
			for(int col = 0; col < H[row].length; col++) {
				if (Math.random() <= mutateChance) { //will flip bits with given frequency
					H[row][col] = (H[row][col] + 1) % 2; //flips the bit
				}
			}
		}
	}
}