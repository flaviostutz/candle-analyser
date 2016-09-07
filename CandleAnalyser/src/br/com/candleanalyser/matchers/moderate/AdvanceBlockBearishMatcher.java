package br.com.candleanalyser.matchers.moderate;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class AdvanceBlockBearishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(AdvanceBlockBearishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Advance Block Bearish", 
							Indicator.Trend.BEARISH, 
							Indicator.Pattern.REVERSAL, 
							Indicator.Reliability.MODERATE);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		
		Candle candle1 = stockPeriod.getLast(2);
		Candle candle2 = stockPeriod.getLast(1);
		Candle candle3 = stockPeriod.getLast(0);
		
		
		if(stockPeriod.isUpTrend(3)) {
			if(candle1.isWhite()
					&& candle2.isWhite()
					&& candle3.isWhite()) {
				
				if(stockPeriod.isLongDay(candle1) 
					&& stockPeriod.isLongDay(candle2) 
					&& stockPeriod.isLongDay(candle3)) {
					
					if(candle2.getClose()>candle1.getClose()
							&& candle3.getClose()>candle2.getClose()) {
						
						if(candle1.isValueInsideBody(candle2.getOpen())
								&& candle2.isValueInsideBody(candle3.getOpen())) {
							
							if(candle2.isBodySmallerThan(candle1)
								&& candle3.isBodySmallerThan(candle1)) {
								
								if(stockPeriod.hasMediumOrLongTopShadow(candle2)
									&& stockPeriod.hasMediumOrLongTopShadow(candle3)) {
									logger.fine("Found a match (" + candle3 + ")");
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
}