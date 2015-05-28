import java.util.ArrayList;

public class Genetic {
	
	private ArrayList<Code> pop = new ArrayList<Code>(); //population of Codes
	
	int decodeCycles;
	private int popSize;
	int numToSave;
	int CWLength;
	int[] message;
	int[] messageBits;
	double mutateChance;
	
	Genetic(int decodeCycles, int popSize, int numToSave, int CWLength, double density, double mutateChance) {
		this.decodeCycles = decodeCycles;
		this.popSize = popSize;
		this.numToSave = numToSave;
		this.CWLength = CWLength;
		this.message = new int[CWLength];
		this.mutateChance = mutateChance;
		
		for(int i = 0; i < CWLength; i++) {
			message[i] = (int) (Math.random()*2); //encodes the message
		}
		
		messageBits = getDataBits(message);
		populate(density);
	}
	
	//if density is < 1, use a random density for each code
	public void populate(double density) {
		for(int i = 0; i < popSize; i++) {
			if (density < 0) {
				this.pop.add(new LDPCCode(CWLength, (((int) (Math.random()*100)))/100.0));
			}
			else {
				this.pop.add(new LDPCCode(CWLength, density));
			}
		}
	}
	
	public void repopulate(int start) {
		for(int i = start; i < pop.size(); i++) {
			pop.set(i, new LDPCCode(this.CWLength, this.mutateChance, (LDPCCode) pop.get((int) Math.random()*start)));
		}
	}
	
	public void popAdd(Code c) {
		this.pop.add(c);
		popSize++;
	}
	
	public void printPop() {
		for(int i = 0; i < this.pop.size(); i++) {
			System.out.print("\t\t " + i  + "\t");
			this.pop.get(i).printStats();
			System.out.println();
		}
	}
	
	public void judgePop() {
		for(int i = 0; i < pop.size(); i++) {
			Code code = pop.get(i);
			if (code.getEfficiency() == -1) {
				code.setEfficiency(1/judgeCode(code)); //sets efficiency to the reciprocal of judgeCode. So higher efficiencies.
			} 
		}
	}
	
	public int judgeCode(Code code) {
		/*
		 * TODO
		 * compare the actual message nodes, not the message.
		 * Also, add convolution!
		 */
		int[] enc = code.encode(message);
		
		int[] dec;
		int i = 0;
		
		do {
			dec = code.decode(enc);
			i++;
			
		} while(i < decodeCycles && !arraysEqual(getDataBits(dec), messageBits));
		return i;
	}
	
	private boolean arraysEqual(int[] first, int[] second) {
		boolean ans = true;
		for(int i = 0; i < first.length; i++) {
			if(first[i] != second[i]) {
				ans = false;
				break;
			}
		}
		return ans;
	}
	
	public int[] getDataBits(int[] p) {
		int[] ans = new int[CWLength - Code.numParityBits(CWLength)];
		
		int mbCounter = 0;
		for(int i = 1; i <= p.length; i++) {
			if (!((i & -i) == i)) {
				ans[mbCounter] = p[i-1];
				mbCounter++;
			}
		}
		return ans;
	}
	
	public Code get(int i) {
		return this.pop.get(i);
	}
	
	public int add(Code e) { //adds and returns the index of the code
		this.pop.add(e);
		return pop.size() -1;
	}
	
	public void run() {
		int genCap = 3;
		System.out.println();
		
		int gen = 0;
		
		while (gen < genCap) {
			System.out.println("\t\tGeneration " + (gen+1) + ":");
			judgePop();
			printPop();
			ArrayList<Code> array;
			array = getHighestScoring(pop, numToSave);
			
			for(int i = numToSave; i < pop.size(); i++) {
				int index = (int) (Math.random()*numToSave);
				array.add(new LDPCCode(CWLength, mutateChance, (LDPCCode) pop.get(index)));
			}
			pop = array;
			gen++;
		}
	}
	
	private ArrayList<Code> getHighestScoring(ArrayList<Code> c, int howMany) {
		ArrayList<Code> ans = new ArrayList<Code>();
		for(int i = 0; i < howMany; i++) {
			ans.add(c.get(i));
		}
		for(int i = 0; i < c.size(); i++) {
			if (howMany > 0) {
				for(int j = 0; j < ans.size(); j++) {
					if (c.get(i).getEfficiency() > ans.get(j).getEfficiency()){
						ans.set(j, c.get(i));
					}
				}
			}
		}
		return ans;
	}
	
	public int getPopSize() {
		return this.popSize;
	}
}