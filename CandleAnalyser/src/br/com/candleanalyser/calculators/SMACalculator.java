package br.com.candleanalyser.calculators;

public class SMACalculator extends SamplingCalculator<Double> {

	public SMACalculator(int maxSamples) {
		super(maxSamples);
	}

	@Override
	protected double calculateValue() {
		double sum = 0;
		for (double value : getSamples()) {
			sum += value;
		}
		return sum/(double)getSamples().getSize();
	}

	
}
