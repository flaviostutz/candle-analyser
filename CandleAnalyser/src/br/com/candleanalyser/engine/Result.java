package br.com.candleanalyser.engine;

import java.util.List;

public class Result {
 
	private Candle candle;
	private List<Indicator> indicators;
	
	public Result(Candle candle, List<Indicator> indicators) {
		this.candle = candle;
		this.indicators = indicators;
	}
	
	public Candle getCandle() {
		return candle;
	}

	public List<Indicator> getIndicators() {
		return indicators;
	}
	
}
 
