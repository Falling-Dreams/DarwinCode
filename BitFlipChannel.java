
public class BitFlipChannel extends Channel {
	
	double chance;
	
	BitFlipChannel (double chance) {
		this.chance = chance;
	}
	
	@Override
	public int[] flip(int feed[]) {
		int[] ans = new int[feed.length];
		
		for (int i = 0; i < feed.length; i++) {
			if (Math.random() < 1) {
				feed[i] = (feed[i] + 1) % 2;
			}
		}
		return ans;
	}
	
	@Override
	public int[] flip(int feed[], int numToFlip) {
		int index = (int) Math.random() * feed.length;
		if (numToFlip == 0) {
			return feed;
		}
		else if (feed[index] == 3) {
			flip(feed, numToFlip);
		}
		else {
			feed[index] = (feed[index] + 1) % 2;
			flip(feed, numToFlip--);
		}
		
		return feed;
	}
	
}
