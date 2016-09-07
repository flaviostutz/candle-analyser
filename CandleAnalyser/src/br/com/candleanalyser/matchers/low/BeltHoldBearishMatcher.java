package br.com.candleanalyser.matchers.low;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class BeltHoldBearishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(BeltHoldBearishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Belt Hold Bearish", 
							Indicator.Trend.BEARISH, 
							Indicator.Pattern.REVERSAL, 
							Indicator.Reliability.LOW);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		Candle candle = stockPeriod.getLast(0);
		if(stockPeriod.isUpTrend(1)) {
			if(candle.isBlack() && candle.hasNoTopShadow()) {
				logger.fine("Found a match ("+ candle +")");
				return true;
			}
		}
		return false;
	}

}
 
