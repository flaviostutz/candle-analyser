package br.com.candleanalyser.simulation;

public interface SimulatorListener {

	public void onSell(Operation currentOperation);
	public void onBuy(Operation currentOperation);

}
