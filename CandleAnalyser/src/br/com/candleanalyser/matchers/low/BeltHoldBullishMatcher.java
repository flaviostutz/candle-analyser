package br.com.candleanalyser.matchers.low;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class BeltHoldBullishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(BeltHoldBullishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Belt Hold Bullish", 
							Indicator.Trend.BULLISH, 
							Indicator.Pattern.REVERSAL, 
							Indicator.Reliability.LOW);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		Candle candle = stockPeriod.getLast(0);
		if(stockPeriod.isDownTrend(1)) {
			if(candle.isWhite() && candle.hasNoBottomShadow()) {
				logger.fine("Found a match ("+ candle +")");
				return true;
			}
		}
		return false;
	}

}
 
