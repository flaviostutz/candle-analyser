package br.com.candleanalyser.matchers.low;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class HaramiBullishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(HaramiBullishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Harami Bullish", 
							Indicator.Trend.BULLISH, 
							Indicator.Pattern.REVERSAL, 
							Indicator.Reliability.LOW);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		Candle candle1= stockPeriod.getLast(1);
		Candle candle2= stockPeriod.getLast(0);
		if(stockPeriod.isDownTrend(2)) {
			if(candle1.isBlack() && stockPeriod.isLongDay(candle1)) {
				if(candle2.isWhite()
					&& candle1.getClose()<candle2.getOpen()
					&& candle1.getOpen()>candle2.getClose()) {
					logger.fine("Found a match ("+ candle2 +")");
					return true;
				}
			}
		}
		return false;
	}

}
 
