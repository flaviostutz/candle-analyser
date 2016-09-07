package br.com.candleanalyser.calculators;

public class MACDCalculator {

	private EMACalculator fasterEmaCalculator;
	private EMACalculator slowerEmaCalculator;
	private EMACalculator macdEmaCalculator;
	private double lastHistogramValue = Double.NaN;
	private double lastMACDValue = Double.NaN;
	
	public MACDCalculator() {
		this(9, 12, 26);//default values
	}
	public MACDCalculator(int macdEmaSamples, int fasterEmaSamples, int slowerEmaSamples) {
		if(fasterEmaSamples>slowerEmaSamples) throw new IllegalArgumentException("fasterEmaSamples must be less than slowerEmaSamples");
		slowerEmaCalculator = new EMACalculator(slowerEmaSamples);
		fasterEmaCalculator = new EMACalculator(fasterEmaSamples);
		macdEmaCalculator = new EMACalculator(macdEmaSamples);
	}

	public void addSample(double value) {
		if(fasterEmaCalculator.isFull() && slowerEmaCalculator.isFull()) {
			lastHistogramValue = getHistogramValue();
			lastMACDValue = getMACDValue();
		}
		fasterEmaCalculator.addSample(value);
		slowerEmaCalculator.addSample(value);
		if(fasterEmaCalculator.isFull() && slowerEmaCalculator.isFull()) {
			macdEmaCalculator.addSample(getMACDValue());
		}
	}
	
	public double getMACDValue() {
		return getFasterEmaValue()-getSlowerEmaValue();
	}
	
	public double getFasterEmaValue() {
		return fasterEmaCalculator.getCurrentValue();
	}
	
	public double getSlowerEmaValue() {
		return slowerEmaCalculator.getCurrentValue();
	}
	
	public double getMACDEmaValue() {
		return macdEmaCalculator.getCurrentValue();
	}

	public double getHistogramValue() {
		return getMACDValue() - getMACDEmaValue();
	}
	
	public boolean isMACDMovingAboveCenter() {
		if(lastMACDValue<0) {
			return (getMACDValue()>=0);
		}
		return false;
	}
	
	public boolean isMACDMovingBelowCenter() {
		if(lastMACDValue>0) {
			return (getMACDValue()<=0);
		}
		return false;
	}
	
	public boolean isMACDMovingAboveMACDEMA() {
		if(lastHistogramValue<0) {
			return (getHistogramValue()>=0);
		}
		return false;
	}

	public boolean isMACDMovingBelowMACDEMA() {
		if(lastHistogramValue>0) {
			return (getHistogramValue()<=0);
		}
		return false;
	}

}
