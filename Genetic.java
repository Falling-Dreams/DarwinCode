import java.util.ArrayList;

public class Genetic {
	
	private ArrayList<Code> pop = new ArrayList<Code>(); //population of Codes
	
	int decodeCycles; //the number of times to run the HDD function per Code
	private int popSize; //population size of Genetic
	int numToSave; //the number of codes to save per iteration of run
	int CWLength; //user-supplied codeword length
	int[] message; //a random message to encode
	int[] messageBits; //the message bits of the decoded message
	double mutateChance; //chance the code will mutate from parent
	
	Genetic(int decodeCycles, int popSize, int numToSave, int CWLength, double density, double mutateChance) {
		this.decodeCycles = decodeCycles;
		this.popSize = popSize;
		this.numToSave = numToSave;
		this.CWLength = CWLength;
		this.message = new int[CWLength];
		this.mutateChance = mutateChance;
		
		//creates the initial message
		for(int i = 0; i < CWLength; i++) {
			message[i] = (int) (Math.random()*2); 
		}
		
		//gets the non-encoded message bits
		messageBits = getDataBits(message);
		//populates pop with LDPC codes
		populate(density);
	}
	
	/**
	 * Populates pop until the the population size is reached
	 * @param density the ideal density of the LDPC code to create
	 */
	public void populate(double density) {
		for(int i = 0; i < popSize; i++) {
			if (density < 0) { //if density is < 1, use a random density for each code
				this.pop.add(new LDPCCode(CWLength, (((int) (Math.random()*100)))/100.0));
			}
			else {
				this.pop.add(new LDPCCode(CWLength, density));
			}
		}
	}
	
	/**
	 * Adds a code to the Genetic object's population
	 * Used to import an LDPC code or add a Hamming Code
	 * @param c the code to add
	 */
	public void popAdd(Code c) {
		this.pop.add(c);
		popSize++;
	}
	
	/**
	 * Prints the quick statistics of every code in population
	 */
	public void printPop() {
		int viable = 0;
		for(int i = 0; i < this.pop.size(); i++) {
			System.out.print("\t\t " + i  + "\t");
			this.pop.get(i).printStats();
			System.out.println();
			if (pop.get(i).getEfficiency() > 0) {
				viable++;
			}
		}
		if (viable == 0) {
			System.out.println("\t\tNo viable codes were found!");
		}
	}
	
	/**
	 * Sets the efficiency for every code in the population
	 */
	public void judgePop() {
		for(int i = 0; i < pop.size(); i++) {
			Code code = pop.get(i);
			if (code.getEfficiency() == -1) {
				code.setEfficiency(1/judgeCode(code)); //sets efficiency to the reciprocal of judgeCode. So higher efficiencies are better
			} 
		}
	}
	
	/**
	 * Judges how well each code object can decode its encoded message
	 * @param code
	 * @return
	 */
	public int judgeCode(Code code) {
		//the encoded message
		int[] enc = code.encode(message);
		
		int[] dec;
		int i = 0;
		
		do {
			dec = code.decode(enc);
			i++;
		} while(i < decodeCycles && !arraysEqual(getDataBits(dec), messageBits)); //while we the decoded aren't equal to the message bits or we haven't exceeded our decodeCycles (which is user supplied input)
		return i;
	}
	
	/**
	 * Checks if two arrays are equal
	 * Helper method for judgeCode
	 * @param first the first array to compare
	 * @param second the second array
	 * @return
	 */
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
	
	/**
	 * Gets the data bits of the message
	 * Used in judgeCode
	 * @param p the message
	 * @return an array of the data bits
	 */
	public int[] getDataBits(int[] p) {
		int[] ans = new int[CWLength - Code.numParityBits(CWLength)];
		
		int mbCounter = 0;
		for(int i = 1; i <= p.length; i++) {
			if (!((i & -i) == i)) { //get any bits that aren't under "columns" of powers of two
				ans[mbCounter] = p[i-1];
				mbCounter++;
			}
		}
		return ans;
	}
	
	/**
	 * Gets the specified code from the population
	 * @param i the index to get
	 * @return the code from the population
	 */
	public Code get(int i) {
		if (i >= 0 && i < pop.size()) {
			return this.pop.get(i);
		}
		return null;
	}
	
	/**
	 * Adds the code to the end of the population list
	 * @param e the code to add
	 * @return the new population size
	 */
	public int add(Code e) { //adds and returns the index of the code
		this.pop.add(e);
		return pop.size() -1;
	}
	
	/**
	 * Runs the genetic algorithm!
	 */
	public void run() {
		//gets the number of generations to run
		int genCap;
		do {
			genCap = Util.getInt("Enter the number of generations you want to evolve: ");
		} while (genCap < 1);
		
		System.out.println();
		
		int gen = 0;
		
		//While we haven't run enough generations
		while (gen < genCap) {
			System.out.println("\t\tGeneration " + (gen+1) + ":");
			judgePop();
			printPop();
			ArrayList<Code> array;
			array = getHighestScoring(pop, numToSave);
			
			for(int i = numToSave; i < pop.size(); i++) { //add new Codes based off of the ones that passed for the rest of the population until population size is reached
				int index = (int) (Math.random()*numToSave);
				array.add(new LDPCCode(CWLength, mutateChance, (LDPCCode) pop.get(index)));
			}
			pop = array; //update the population
			gen++;
		}
	}
	
	/**
	 * Gets the codes with the highest efficiencies
	 * @param c an arrayList of codes
	 * @param howMany how many to of the highest to get.
	 * @return
	 */
	private ArrayList<Code> getHighestScoring(ArrayList<Code> c, int howMany) {
		ArrayList<Code> ans = new ArrayList<Code>();
		
		//fill the answer with the first few Codes of the population in case they aren't enough
		for(int i = 0; i < howMany; i++) {
			ans.add(c.get(i));
		}
		//Goes over the entire code, replacing the ones that aren't effective
		for(int i = 0; i < c.size(); i++) {
			if (howMany > 0) {
				for(int j = 0; j < ans.size(); j++) {
					if (c.get(i).getEfficiency() > ans.get(j).getEfficiency()){ //replace only one code if it has a higher efficiency.
						ans.set(j, c.get(i));
						break; //TODO test this
					}
				}
			}
		}
		return ans;
	}
	
	/**
	 * Gets the population size
	 * @return population size
	 */
	public int getPopSize() {
		return this.popSize;
	}
}