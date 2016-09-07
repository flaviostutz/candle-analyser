package br.com.candleanalyser.genetics.trainner;

import br.com.candleanalyser.genetics.GeneticAdvisor;
import br.com.candleanalyser.genetics.GeneticAdvisorFactory;

public class NeuralAdvisorFactory implements GeneticAdvisorFactory {

	private float mutationRate;
	
	/**
	 * @param buyThreshold Value between 0 and 1 to be used as a minimum value in neuron for a positive buy
	 * @param sellThreshold Value between 0 and 1 to be used as a minimum value in neuron for a positive sell
	 * @param mutationRate Value between 0 and 1 to indicate the rate of mutation
	 */
	public NeuralAdvisorFactory(float mutationRate) {
		this.mutationRate = mutationRate;
	}

	@Override
	public GeneticAdvisor createNewGeneticAdvisor(GeneticAdvisor advisor1, GeneticAdvisor advisor2) {
		return new NeuralGeneticAdvisor(advisor1, advisor2, mutationRate);
	}

	@Override
	public GeneticAdvisor createNewRandomGeneticAdvisor() {
		return new NeuralGeneticAdvisor();
	}

}
