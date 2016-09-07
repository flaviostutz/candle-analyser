package br.com.candleanalyser.genetics;

public interface GeneticAdvisorFactory {

	public GeneticAdvisor createNewGeneticAdvisor(GeneticAdvisor advisor1, GeneticAdvisor advisor2);
	public GeneticAdvisor createNewRandomGeneticAdvisor();

}
