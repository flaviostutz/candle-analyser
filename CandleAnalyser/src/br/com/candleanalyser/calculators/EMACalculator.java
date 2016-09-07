package br.com.candleanalyser.calculators;

public class EMACalculator extends SamplingCalculator<Double> {

	private SMACalculator smaCalculator;
	
	private double multiplier = 0;
	private double lastEma = Double.NaN;

	public EMACalculator(int maxSamples) {
		super(maxSamples);
		multiplier = 2.0 / (1.0 + (double)maxSamples);
		smaCalculator = new SMACalculator(maxSamples);
	}

	@Override
	public void addSample(Double value) {
		if(!isFull()) {
			smaCalculator.addSample(value);
			lastEma = smaCalculator.getCurrentValue();
		} else {
			lastEma = ((value - lastEma) * multiplier) + lastEma;
//			lastEma = PrecisionHelper.add(PrecisionHelper.multiply(PrecisionHelper.subtract(value, lastEma), multiplier), lastEma);
		}
		super.addSample(value);
	}
	
	@Override
	protected double calculateValue() {
		return lastEma;
	}

}
