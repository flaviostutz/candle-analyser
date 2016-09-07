package br.com.candleanalyser.matchers.high;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;
import br.com.candleanalyser.matchers.moderate.EngulfingBullishMatcher;

public class ThreeOutsideUpBullishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(ThreeOutsideUpBullishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Three Outside Up Bullish", 
				Indicator.Trend.BULLISH, 
				Indicator.Pattern.REVERSAL, 
				Indicator.Reliability.HIGH);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		
		//test engulfing pattern occurence before this day
		StockPeriod p = stockPeriod.getStockPeriod(1);
		boolean hasEngulfingBefore = new EngulfingBullishMatcher().matches(p);
		
		if(hasEngulfingBefore) {
			Candle candle1 = stockPeriod.getLast(1);
			Candle candle2 = stockPeriod.getLast(0);
			if(candle2.isWhite()
				&& candle2.getClose()>candle1.getClose()) {
				return true;
			}
		}
		return false;
	}

}
