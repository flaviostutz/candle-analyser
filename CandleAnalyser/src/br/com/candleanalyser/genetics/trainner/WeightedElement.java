package br.com.candleanalyser.genetics.trainner;


public class WeightedElement<T> {

	private T element;
	private float weight;
	
	public WeightedElement(T element, float weight) {
		this.element = element;
		this.weight = weight;
	}

	public T getElement() {
		return element;
	}
	
	public void setElement(T element) {
		this.element = element;
	}

	public float getWeight() {
		return weight;
	}
	
	
	
}
