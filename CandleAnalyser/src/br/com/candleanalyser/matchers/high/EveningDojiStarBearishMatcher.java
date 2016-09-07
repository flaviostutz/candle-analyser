package br.com.candleanalyser.matchers.high;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;
import br.com.candleanalyser.matchers.low.HaramiBullishMatcher;

public class EveningDojiStarBearishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(EveningDojiStarBearishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Evening Doji Star Bearish", 
				Indicator.Trend.BEARISH, 
				Indicator.Pattern.REVERSAL, 
				Indicator.Reliability.HIGH);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		Candle candle1 = stockPeriod.getLast(2);
		Candle candle2 = stockPeriod.getLast(1);
		Candle candle3 = stockPeriod.getLast(0);
		
		if(stockPeriod.isUpTrend(3)) {
			if(stockPeriod.isLongDay(candle1) && candle1.isWhite()) {
				if(candle2.isDoji()
					&& candle2.isGappingUp(candle1)) {
					if(candle3.isBlack()) {
						logger.fine("Found a match (" + candle3 + ")");
						return true;
					}
				}
			}
		}
		return false;
	}

}
