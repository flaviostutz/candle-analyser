package br.com.candleanalyser.genetics.trainner;

import br.com.candleanalyser.engine.Helper;

public class GeneticTrainnerListenerConsole implements GeneticTrainnerListener {

	private AdvisorResult bestResult;
	private AdvisorResult bestInGeneration;
	
	@Override
	public void onBestUpdated(AdvisorResult bestResult) {
		this.bestResult = bestResult;
		System.out.println((Helper.formatNumber(bestResult.getOperationResult().getYield()*100)) + "% <");
	}

	@Override
	public void onNewAdvisorResult(AdvisorResult advisorResult) {
		if(bestInGeneration==null || advisorResult.getOperationResult().getYield()>bestInGeneration.getOperationResult().getYield()) {
			bestInGeneration = advisorResult;
			System.out.println((Helper.formatNumber(bestInGeneration.getOperationResult().getYield()*100)) + "%");
		} else {
			System.out.print(".");
		}
	}

	@Override
	public void onNewGeneration(int number, AdvisorResult result1, AdvisorResult result2) {
		System.out.println("\n=========================");
		if(bestResult!=null) {
			System.out.println(">> Generation " + number + " - " + (Helper.formatNumber(bestResult.getOperationResult().getYield()*100)) + "%");
		} else {
			System.out.println(">> Generation " + number + " - no results yet");
		}
		System.out.println("=========================");
		bestInGeneration = null;
	}

}
