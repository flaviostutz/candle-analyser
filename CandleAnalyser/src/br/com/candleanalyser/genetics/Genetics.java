package br.com.candleanalyser.genetics;

import java.io.Serializable;
import java.util.Random;

public class Genetics implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Random random = new Random();

	private float[] geneticCode = new float[Gene.values().length];

	public Genetics(Genetics genetics1, Genetics genetics2, float mutationRate) {
	    // define the point for crossing over
	    int defaultPoint = random.nextInt(geneticCode.length);
	    
		// randomize the default genetic codes
		for (int i = 0; i < genetics1.geneticCode.length; i++) {
		    
		    //mutate genes
		    float r = random.nextFloat();
		    if(r>=0 && r<=mutationRate) {
				geneticCode[i] = random.nextFloat();
		        continue;
		    }
		    
		    //do the crossing over
		    if(i<defaultPoint) {
				geneticCode[i] = genetics1.geneticCode[i];
		    } else {
				geneticCode[i] = genetics2.geneticCode[i];
		    }
		}

	}

	public Genetics() {
		// randomize the default genetic codes
		for (int i = 0; i < geneticCode.length; i++) {
			geneticCode[i] = random.nextFloat();
		}
	}

	public float getValue(Gene gene) {
		return gene.getValue(geneticCode[gene.ordinal()]);
	}
	
	public String toString() {
		String str = "genes={\n";
		for (int i=0; i<geneticCode.length; i++) {
			str += "   " + Gene.values()[i].getName() + "=" + Gene.values()[i].getValue(geneticCode[i]) + " (" + geneticCode[i] + "),";
		}
		str = str.substring(0, str.length() - 1) + "}";
		return str;
	}

}
