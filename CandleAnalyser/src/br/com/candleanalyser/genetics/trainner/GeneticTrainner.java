package br.com.candleanalyser.genetics.trainner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.genetics.GeneticAdvisor;
import br.com.candleanalyser.genetics.GeneticAdvisorFactory;
import br.com.candleanalyser.simulation.MultiPeriodResults;
import br.com.candleanalyser.simulation.PeriodResult;
import br.com.candleanalyser.simulation.Simulator;

public class GeneticTrainner {

	private static Random random = new Random();

	private List<AdvisorResult> advisorResults;
	private AdvisorResult bestResult;
	
	private int generations;
	private int childrenPerGeneration;
	private float minAdvisorStrength;
	private int maxNumberOfAdvisorsInResult;
	private float newRandomElementRate;
	private double fixedCostPerOperation;
	private double initialMoney;
	private float buyThreshold;
	private float sellThreshold;
	private GeneticTrainnerListener listener;
	private GeneticAdvisorFactory geneticAdvisorFactory;
	
	public GeneticTrainner(int generations, int childrenPerGeneration, int maxNumberOfAdvisorsInResult, float minAdvisorStrength, float newRandomElementRate, double initialMoney, double fixedCostPerOperation, GeneticAdvisorFactory geneticAdvisorFactory, float buyThreshold, float sellThreshold, GeneticTrainnerListener listener) {
		this.childrenPerGeneration = childrenPerGeneration;
		this.fixedCostPerOperation = fixedCostPerOperation;
		this.generations = generations;
		this.geneticAdvisorFactory = geneticAdvisorFactory;
		this.initialMoney = initialMoney;
		this.listener = listener;
		this.minAdvisorStrength = minAdvisorStrength;
		this.newRandomElementRate = newRandomElementRate;
		this.maxNumberOfAdvisorsInResult = maxNumberOfAdvisorsInResult;
		this.buyThreshold = buyThreshold;
		this.sellThreshold = sellThreshold;
	}

	public void evolveAdvisors(List<StockPeriod> periods, int numberOfNewRandomAdvisors) {
		evolveAdvisors(periods, numberOfNewRandomAdvisors, null);
	}
		
	public void evolveAdvisors(List<StockPeriod> periods, int numberOfNewRandomAdvisors, List<GeneticAdvisor> initialAdvisors) {
		List<GeneticAdvisor> randomAdvisors = new ArrayList<GeneticAdvisor>();
		if(initialAdvisors!=null) {
			randomAdvisors.addAll(initialAdvisors);
		}
		for (int i = 0; i < numberOfNewRandomAdvisors; i++) {
			randomAdvisors.add(geneticAdvisorFactory.createNewRandomGeneticAdvisor());
		}
		evolveAdvisors(periods, randomAdvisors);
	}
	
	public void evolveAdvisors(List<StockPeriod> periods, List<GeneticAdvisor> newAdvisors) {
		if(newAdvisors!=null) {
			evaluateAdvisors(newAdvisors, periods);
		}
		
		for (int i = 0; i < generations; i++) {
			if(getResults().size()<2) break;
			
			//reproduce advisors
			AdvisorResult result1 = pollFirstAdvisorResult();
			AdvisorResult result2 = getRandomAdvisorResult();
			if (listener != null) {
				listener.onNewGeneration(i+1, result1, result2);
			}
			reproduceAndProcessChildrenAdvisors(result1.getAdvisor(), result2.getAdvisor(), periods);
		}
	}

	public void reproduceAndProcessChildrenAdvisors(GeneticAdvisor advisor1, GeneticAdvisor advisor2, List<StockPeriod> periods) {
		//reproduce from advisor1 and advisor2
		List<GeneticAdvisor> children = new ArrayList<GeneticAdvisor>();
		for (int i = 0; i < childrenPerGeneration; i++) {
			GeneticAdvisor ga = geneticAdvisorFactory.createNewGeneticAdvisor(advisor1, advisor2);
			children.add(ga);
		}
		
		//add new random element
		float r = random.nextFloat();
		if(r>=0 && r<=newRandomElementRate) {
			GeneticAdvisor ga = geneticAdvisorFactory.createNewRandomGeneticAdvisor();
			children.add(ga);
		}

		evaluateAdvisors(children, periods);
	}
	
	public void evaluateAdvisors(List<GeneticAdvisor> advisors, List<StockPeriod> periods) {
		for (GeneticAdvisor advisor : advisors) {
			MultiPeriodResults mpr = new MultiPeriodResults();
			
			//operate with advisor on all periods
			for (StockPeriod stockPeriod : periods) {
				PeriodResult or = Simulator.operateUsingAdvisor(fixedCostPerOperation, initialMoney, stockPeriod, advisor, advisor, buyThreshold, sellThreshold, null);
				mpr.addPeriodResult(or);
			}
			
			//add advisor to results
			AdvisorResult gor = new AdvisorResult(advisor, mpr);
			listener.onNewAdvisorResult(gor);
			getResults().add(gor);
			
			//determine best result (FITNESS FUNCTION)
			if(bestResult==null || gor.getOperationResult().getYield()>bestResult.getOperationResult().getYield()) {
				bestResult = gor;
				if(listener!=null) {
					listener.onBestUpdated(bestResult);
				}
			}
		}
		cleanupAdvisorResults();
	}

	protected void cleanupAdvisorResults() {
		if(getResults().size()<=2) return;
		
		//remove poor results and select best advisor
		double yieldSum = 0;
		for (AdvisorResult ar : getResults()) {
			yieldSum += ar.getOperationResult().getYield();
		}
//		double yieldAvg = yieldSum/(double)getResults().size();
		
		List<AdvisorResult> toRemove = new ArrayList<AdvisorResult>();
		for (AdvisorResult advisorResult : getResults()) {
//			if(advisorResult.getOperationResult().getYield()<((yieldAvg-0.01F)*minAdvisorStrength)) {
			//remove according to probability
			//see http://en.wikipedia.org/wiki/Fitness_proportionate_selection
			if((advisorResult.getOperationResult().getYield()/yieldSum)<minAdvisorStrength) {
				toRemove.add(advisorResult);
			}
		}
		for (AdvisorResult ar : toRemove) {
			if(getResults().size()>=2) {
				getResults().remove(ar);
//			} else {
//				throw new IllegalStateException("Cannot remove items");
			}
		}
	}

	public void resetAdvisorResults() {
		this.advisorResults = null;
		this.bestResult = null;
	}
	
	private List<AdvisorResult> getResults() {
		if(advisorResults==null) {
			advisorResults = new ArrayList<AdvisorResult>();
		}
		return advisorResults;
	}
	
	public List<AdvisorResult> getAdvisorResults() {
		//limit the number of advisors
		if(getResults().size()>maxNumberOfAdvisorsInResult) {
			advisorResults = getResults().subList(0, maxNumberOfAdvisorsInResult);
		}
		Collections.sort(getResults());
		return getResults();
	}
	
	public AdvisorResult getBestAdvisorResult() {
		if(getAdvisorResults().size()>0) {
			return getAdvisorResults().get(0);
		} else {
			return null;
		}
	}
	
	private AdvisorResult pollFirstAdvisorResult() {
		AdvisorResult result = getResults().get(0);
		if(result!=bestResult) {
			getResults().remove(0);
		}
		return result;
	}

	private AdvisorResult getRandomAdvisorResult() {
		int i = random.nextInt(getResults().size());
		return getResults().get(i);
	}
	
}
