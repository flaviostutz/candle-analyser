package br.com.candleanalyser.calculators;

import br.com.candleanalyser.util.FixedQueue;

public abstract class SamplingCalculator<T> {

	private FixedQueue<T> samples;
	private boolean dirty;
	private double lastLastValue = Double.NaN;
	private double lastValue = Double.NaN;

	public SamplingCalculator(int maxSamples) {
		dirty = true;
		samples = new FixedQueue<T>(maxSamples);
	}
	
	public void reset() {
		dirty = true;
		samples = new FixedQueue<T>(samples.getMaxSize());
		lastLastValue = Double.NaN;
		lastValue = Double.NaN;
	}
	
	public void addSample(T value) {
		if(samples.getMaxSize()==0) throw new IllegalStateException("Cannot add sample directly. maxSize=0");
		dirty = true;
		samples.add(value);
		lastLastValue = lastValue;
		if(isFull()) {
			lastValue = getCurrentValue();
		}
	}

	protected abstract double calculateValue();
	
	public double getCurrentValue() {
		if(dirty) {
			lastValue = calculateValue();
		}
		return lastValue;
	}
	
	protected FixedQueue<T> getSamples() {
		return samples;
	}
	
	public boolean isFull() {
		return samples.getSize() == samples.getMaxSize();
	}

	public boolean hasValuePassedUpBy(float value) {
		return isValueGoingUp() && lastLastValue<=value && value<=lastValue;
	}

	public boolean hasValuePassedDownBy(float value) {
		return isValueGoingDown() && lastLastValue>=value && value>=lastValue;
	}

	public boolean isValueGoingUp() {
		if(Double.isNaN(lastValue) || Double.isNaN(lastLastValue)) {
			return false;
		} else {
			return lastValue > lastLastValue;
		}
	}

	public boolean isValueGoingDown() {
		if(Double.isNaN(lastValue) || Double.isNaN(lastLastValue)) {
			return false;
		} else {
			return lastValue < lastLastValue;
		}
	}
	
}
