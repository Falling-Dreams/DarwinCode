import java.util.ArrayList;

public class Genetic {
	
	ArrayList<Code> pop = new ArrayList<Code>(); //population of Codes
	
	Genetic(int popSize, int CWLength, double density) {
		populate(this, popSize, CWLength, density);
	}
	
	//if density is < 1, use a random density for each code
	public static void populate(Genetic gen, int popSize, int CWLength, double density) {
		for(int i = 0; i < popSize; i++) {
			if (density < 0) {
				gen.pop.add(new LDPCCode(CWLength, Math.random()));
			}
			else {
				gen.pop.add(new LDPCCode(CWLength, density));
			}
		}
	}
	
	public static void printPop(Genetic gen) {
		for(int i = 0; i < gen.pop.size(); i++) {
			System.out.print("\t\t " + i  + "\t");
			gen.pop.get(i).printStats();
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
	
}