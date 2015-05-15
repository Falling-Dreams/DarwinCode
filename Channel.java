
public abstract class Channel {
	
	double prob;
	
	Channel(double prob) {
		this.prob = prob;
	}
	
	public abstract int[] flip(int feed[]);
	
}