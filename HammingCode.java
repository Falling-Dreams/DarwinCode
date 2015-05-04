
public class HammingCode extends Code{

	private static final long serialVersionUID = 3849979856791666909L;

	HammingCode(int n, int k) {
		super("Hamming", n, k);
		//TODO use HARDCODED hamming matrix for now: 7, 4
		H = new int [][] { {1,0,1,0,1,0,1},
			{0,1,1,0,0,1,1},
			{0,0,0,1,1,1,1} };
		initialize();
	}

	@Override
	public int[] decode(int[] y) { //TODO implement
		return null;
	}
}
