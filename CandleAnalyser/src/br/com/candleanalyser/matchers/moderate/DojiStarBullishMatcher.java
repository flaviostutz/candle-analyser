package br.com.candleanalyser.matchers.moderate;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class DojiStarBullishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(DojiStarBullishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Doji Star Bullish", 
							Indicator.Trend.BULLISH, 
							Indicator.Pattern.REVERSAL, 
							Indicator.Reliability.MODERATE);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		Candle candle1 = stockPeriod.getLast(1);
		Candle candle2 = stockPeriod.getLast(0);
		//FIXME test stockPeriod.isDownTrend(1) or stockPeriod.isDownTrend(2)
		if(stockPeriod.isDownTrend(1) && candle2.isGappingDown(candle1)) {
			if(candle1.isBlack() && stockPeriod.isLongDay(candle1)) {
				if(candle2.isDoji() 
						&& !stockPeriod.hasLongTopShadow(candle2) 
						&& !stockPeriod.hasLongBottomShadow(candle2)) {
					logger.fine("Found a match (" + candle2 + ")");
					return true;
				}
			}
		}
		return false;
	}
	
}
 
