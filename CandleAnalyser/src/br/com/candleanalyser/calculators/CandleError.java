package br.com.candleanalyser.calculators;

import br.com.candleanalyser.engine.Candle;

public class CandleError implements Comparable<CandleError> {

	private Candle candle;
	private double error;
	
	public CandleError(Candle candle, double error) {
		this.candle = candle;
		this.error = error;
	}
	
	public Candle getCandle() {
		return candle;
	}
	public double getError() {
		return error;
	}
	@Override
	public int compareTo(CandleError other) {
		if(getError()<other.getError()) {
			return -1;
		} else if(getError()>other.getError()) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
}
