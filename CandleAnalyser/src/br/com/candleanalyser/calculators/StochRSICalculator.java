package br.com.candleanalyser.calculators;

import br.com.candleanalyser.engine.Candle;


public class StochRSICalculator extends SamplingCalculator<Candle> {

	private int maxSamples;
	private RSICalculatorStrategy strategy;
	private boolean useVolume;
	
	public StochRSICalculator() {
		this(14, RSICalculatorStrategy.CLASSIC_RSI, false);
	}
	
	public StochRSICalculator(int maxSamples, RSICalculatorStrategy strategy, boolean useVolume) {
		super(maxSamples);
		this.maxSamples = maxSamples;
		this.strategy = strategy;
		this.useVolume = useVolume;
	}

	public void addSample(Double value) {
		if(useVolume) throw new IllegalStateException("Use addSample(Candle) because it is needed stock's volume");
		addSample(new Candle(0,0,0,value,0));
	}
	
	@Override
	protected double calculateValue() {
		RSICalculator rsiCalculator = new RSICalculator(maxSamples, strategy, useVolume);
		StochasticCalculator stochCalculator = new StochasticCalculator(maxSamples);
		for (Candle candle : getSamples()) {
			rsiCalculator.addSample(candle);
			double rsi = rsiCalculator.getCurrentValue();
			Candle rsiCandle = new Candle(rsi, rsi, rsi, rsi, Double.NaN);
			stochCalculator.addSample(rsiCandle);
		}
		return stochCalculator.getCurrentValue();
		
	}

}
