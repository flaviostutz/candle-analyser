package br.com.candleanalyser.matchers.moderate;

import java.util.logging.Logger;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class EngulfingBullishMatcher implements IndicatorMatcher {

	private static final Logger logger = Logger.getLogger(EngulfingBullishMatcher.class.getName());
	
	@Override
	public Indicator getIndicator() {
		return new Indicator("Engulfing Bullish", 
				Indicator.Trend.BULLISH, 
				Indicator.Pattern.REVERSAL, 
				Indicator.Reliability.MODERATE);
	}

	@Override
	public boolean matches(StockPeriod stockPeriod) {
		Candle candle1 = stockPeriod.getLast(1);
		Candle candle2 = stockPeriod.getLast(0);
		//FIXME test stockPeriod.isDownTrend(1) or stockPeriod.isDownTrend(2)
		if(stockPeriod.isDownTrend(1)) {
			if(candle1.isBlack() && stockPeriod.isLongDay(candle1)) {
				if(candle2.isWhite()
						&& candle2.getOpen()<candle1.getClose()
						&& candle2.getClose()>candle1.getOpen()) {
					logger.fine("Found a match (" + candle2 + ")");
					return true;
				}
			}
		}
		return false;
	}

}
