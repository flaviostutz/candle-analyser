package br.com.candleanalyser.genetics.trainner;

public interface GeneticTrainnerListener {

	public void onNewGeneration(int number, AdvisorResult result1, AdvisorResult result2);
	public void onBestUpdated(AdvisorResult bestResult);
	public void onNewAdvisorResult(AdvisorResult advisorResult);

}
