package br.com.candleanalyser.genetics.trainner;

import java.util.HashMap;
import java.util.Map;

public class Neuron {

	private Map<Object, WeightedElement<Float>> values = new HashMap<Object,WeightedElement<Float>>();
	
	public void addInput(Object inputId, float weight) {
		values.put(inputId, new WeightedElement<Float>(0F, weight));
	}
	
	public void setInputValue(Object inputId, float value) {
		values.get(inputId).setElement(value);
	}
	
	public float getOutputValue() {
		float sum = 0;
		float weightSum = 0;
		for (WeightedElement<Float> value : values.values()) {
			sum += value.getWeight() * value.getElement();
			weightSum += value.getWeight();
		}
		return sum/weightSum;
	}

	public void resetInputValues() {
		for (Object key : values.keySet()) {
			values.get(key).setElement(0F);
		}
	}

}
