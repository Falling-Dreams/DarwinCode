import java.util.ArrayList;

public class Genetic {
	
	private ArrayList<Code> pop = new ArrayList<Code>(); //population of Codes
	
	String encodeMessage;
	int decodeCycles;
	int popSize;
	int numToSave;
	int CWLength;
	
	Genetic(String encodeMessage, int decodeCycles, int popSize, int numToSave, int CWLength, double density) {
		this.encodeMessage = encodeMessage;
		this.decodeCycles = decodeCycles;
		this.popSize = popSize;
		this.numToSave = numToSave;
		this.CWLength = CWLength;
		populate(density);
	}
	
	//if density is < 1, use a random density for each code
	public void populate(double density) {
		for(int i = 0; i < popSize; i++) {
			if (density < 0) {
				this.pop.add(new LDPCCode(CWLength, (((int) (Math.random()*100)))/100.0) );
			}
			else {
				this.pop.add(new LDPCCode(CWLength, density));
			}
		}
	}
	
	public void printPop() {
		for(int i = 0; i < this.pop.size(); i++) {
			System.out.print("\t\t " + i  + "\t");
			this.pop.get(i).printStats();
			System.out.println();
		}
	}
	
	public void judge() {
		for(int i = 0; i < pop.size(); i++) {
			Code code = pop.get(i);
			if (code.getEfficiency() == -1) {
				//code.encode(an array of k length) //do more with decoding method. Make recursive?
			}
		}
	}
	
	public Code get(int i) {
		return this.pop.get(i);
	}
	
	public int add(Code e) { //adds and returns the index of the code
		this.pop.add(e);
		return pop.size() -1;
	}
	
	public void run() { //BIG CODE
		
	}
}