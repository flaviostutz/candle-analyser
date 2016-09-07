package br.com.candleanalyser.genetics;

import java.io.Serializable;

import br.com.candleanalyser.advisors.BuyAdvisor;
import br.com.candleanalyser.advisors.SellAdvisor;

public abstract class GeneticAdvisor implements BuyAdvisor, SellAdvisor, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Genetics genetics;

	public GeneticAdvisor() {
		this.genetics = new Genetics();
	}

	public GeneticAdvisor(GeneticAdvisor advisor1, GeneticAdvisor advisor2, float mutationRate) {
		this.genetics = new Genetics(advisor1.genetics, advisor2.genetics, mutationRate);
	}
	
	public Genetics getGenetics() {
		return genetics;
	}
	
}
