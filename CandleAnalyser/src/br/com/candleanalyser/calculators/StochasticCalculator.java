package br.com.candleanalyser.calculators;

import br.com.candleanalyser.engine.Candle;

public class StochasticCalculator extends SamplingCalculator<Candle> {

	public StochasticCalculator(int maxSamples) {
		super(maxSamples);
	}

	@Override
	protected double calculateValue() {
		if(getSamples().getSize()<1) throw new IllegalStateException("It is needed at least 2 samples for this calculation");
		double lowestLow = Double.POSITIVE_INFINITY;
		double highestHigh = Double.NEGATIVE_INFINITY;
		double lastClose = 0;
		for (Candle candle : getSamples()) {
			if(candle.getMin()<lowestLow) {
				lowestLow = candle.getMin();
			}
			if(candle.getMax()>highestHigh) {
				highestHigh = candle.getMax();
			}
			lastClose = candle.getClose();
		}
		return 100.0 * ((lastClose-lowestLow)/(highestHigh-lowestLow));
	}

	
}
