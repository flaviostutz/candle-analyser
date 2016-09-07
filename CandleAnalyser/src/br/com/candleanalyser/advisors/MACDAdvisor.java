package br.com.candleanalyser.advisors;

import br.com.candleanalyser.calculators.MACDCalculator;
import br.com.candleanalyser.calculators.TrendDivergenceCalculator;
import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.genetics.trainner.Neuron;

public class MACDAdvisor implements BuyAdvisor, SellAdvisor {

	private MACDCalculator macdCalculator;
	private TrendDivergenceCalculator trendDivergenceCalculator;

	private Neuron neuron;
	private static final int DIVERGENCE = 1;
	private static final int CENTER_CROSSOVER = 2;
	private static final int AVERAGE_CROSSOVER = 3;

	public MACDAdvisor() {
		this(9, 12, 26, 1, 1, 1);
	}
	
	public MACDAdvisor(int macdEmaSamples, int fasterEmaSamples, int slowerEmaSamples, float weightCenterCrossover, float weightMovingAverageCrossover, float weightDivergence) {
		this.macdCalculator = new MACDCalculator(macdEmaSamples, fasterEmaSamples, slowerEmaSamples);
		this.trendDivergenceCalculator = new TrendDivergenceCalculator(15, 15);
		neuron = new Neuron();
		neuron.addInput(DIVERGENCE, weightDivergence);
		neuron.addInput(CENTER_CROSSOVER, weightCenterCrossover);
		neuron.addInput(AVERAGE_CROSSOVER, weightMovingAverageCrossover);
	}
	
	@Override
	public void nextCandle(Candle candle) {
		macdCalculator.addSample(candle.getClose());
		trendDivergenceCalculator.addSamples(candle.getClose(), macdCalculator.getMACDValue());
	}

	@Override
	public float getBuyStrength() {
		neuron.resetInputValues();
		if(trendDivergenceCalculator.areDivergentTrends()) {
			neuron.setInputValue(DIVERGENCE, 1F);
		}
		if(macdCalculator.isMACDMovingAboveMACDEMA()) {
			neuron.setInputValue(AVERAGE_CROSSOVER, 1F);
		}
		if(macdCalculator.isMACDMovingAboveCenter()) {
			neuron.setInputValue(CENTER_CROSSOVER, 1F);
		}
		return neuron.getOutputValue();
	}

	@Override
	public float getSellStrength() {
		neuron.resetInputValues();
		if(trendDivergenceCalculator.areDivergentTrends()) {
			neuron.setInputValue(DIVERGENCE, 1F);
		}
		if(macdCalculator.isMACDMovingBelowMACDEMA()) {
			neuron.setInputValue(AVERAGE_CROSSOVER, 1F);
		}
		if(macdCalculator.isMACDMovingBelowCenter()) {
			neuron.setInputValue(CENTER_CROSSOVER, 1F);
		}
		return neuron.getOutputValue();
	}

	@Override
	public String getCurrentInfo() {
		return "buy=" + Helper.formatNumber(getBuyStrength()) + "; sell=" + Helper.formatNumber(getSellStrength());
	}

	@Override
	public void onBuy(float value) {
	}
	@Override
	public void onSell(float value) {
	}
	
}
