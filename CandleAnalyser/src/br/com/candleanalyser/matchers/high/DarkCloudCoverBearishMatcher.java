package br.com.candleanalyser.matchers.high;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;
import br.com.candleanalyser.matchers.low.HaramiBullishMatcher;

public class DarkCloudCoverBearishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(DarkCloudCoverBearishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Dark Cloud Cover Bearish", 
				Indicator.Trend.BEARISH, 
				Indicator.Pattern.REVERSAL, 
				Indicator.Reliability.HIGH);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		Candle candle1 = stockPeriod.getLast(1);
		Candle candle2 = stockPeriod.getLast(0);
		
		if(stockPeriod.isUpTrend(2)) {
			if(stockPeriod.isLongDay(candle1) && candle1.isWhite()) {
				if(candle2.isBlack()
					&& candle2.getOpen()>=candle1.getMax()) {
					if(candle2.getClose()<=candle1.getBodyMidPoint()) {
						logger.fine("Found a match (" + candle2 + ")");
						return true;
					}
				}
			}
		}
		return false;
	}

}
