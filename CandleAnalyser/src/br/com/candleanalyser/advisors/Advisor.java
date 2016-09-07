package br.com.candleanalyser.advisors;

import br.com.candleanalyser.engine.Candle;

public interface Advisor {

	public void nextCandle(Candle candle);
	public String getCurrentInfo();

}
