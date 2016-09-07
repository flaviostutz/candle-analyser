package br.com.candleanalyser.matchers.high;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;
import br.com.candleanalyser.matchers.low.HaramiBullishMatcher;

public class ThreeInsideUpBullishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(ThreeInsideUpBullishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Three Inside Up Bullish", 
				Indicator.Trend.BULLISH, 
				Indicator.Pattern.REVERSAL, 
				Indicator.Reliability.HIGH);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		
		//test harami occurrence before this day
		StockPeriod p = stockPeriod.getStockPeriod(1);
		boolean hasHaramiBefore = new HaramiBullishMatcher().matches(p);
		
		if(hasHaramiBefore) {
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
