package br.com.candleanalyser.engine;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.com.candleanalyser.matchers.IndicatorMatcher;

public class CandleAnalyser {
 
	private static final Logger logger = Logger.getLogger(CandleAnalyser.class.getName());
	
	private static List<IndicatorMatcher> lowMatchers = new ArrayList<IndicatorMatcher>();
	private static List<IndicatorMatcher> moderateMatchers = new ArrayList<IndicatorMatcher>();
	private static List<IndicatorMatcher> highMatchers = new ArrayList<IndicatorMatcher>();

	public enum ReliabilityFilter {LOW, LOW_MODERATE, MODERATE, MODERATE_HIGH, HIGH, ALL};
	
	private static final int MIN_CANDLES = 5;
	
	static {
		try {
			Class[] c = Helper.getClasses("br.com.candleanalyser.matchers.high");
			for (Class clazz : c) {
				highMatchers.add((IndicatorMatcher)clazz.newInstance());
			}
			
			c = Helper.getClasses("br.com.candleanalyser.matchers.moderate");
			for (Class clazz : c) {
				moderateMatchers.add((IndicatorMatcher)clazz.newInstance());
			}
			
			c = Helper.getClasses("br.com.candleanalyser.matchers.low");
			for (Class clazz : c) {
				lowMatchers.add((IndicatorMatcher)clazz.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
	}
	
	public static List<Result> analysePeriod(StockPeriod period, ReliabilityFilter reliability) {
		List<Result> result = new ArrayList<Result>();
		StockPeriod stockPeriod = new StockPeriod(period.getStockName(), 10);
		for (Candle candle : period.getCandles()) {
			stockPeriod.addCandle(candle);
			List<Indicator> indicators = getIndicatorsForLastCandle(stockPeriod, reliability);
			if(indicators.size()>0) {
				result.add(new Result(candle, indicators));
			}
		}
		return result;
	}
	
	public static Indicator getBestIndicatorForLastCandle(StockPeriod period, ReliabilityFilter reliability) {
		List<Indicator> indicators = getIndicatorsForLastCandle(period, reliability);
		if(indicators.size()==0) {
			return null;
		} else if(indicators.size()==1) {
			return indicators.get(0);
		} else {
			//select the best indicator by reliability
			Indicator result = null;
			for (Indicator indicator : indicators) {
				if(result==null) {
					result = indicator;
				} else {
					int i = indicator.getReliability().compareTo(result.getReliability());
					if(i>0) {
						//System.out.println(result.getName() + " replaced by " + indicator.getName());
						result = indicator;
					}
				}
			}
			return result;
		}
	}
	
	private static List<Indicator> getIndicatorsForLastCandle(StockPeriod period, ReliabilityFilter reliability) {
		List<IndicatorMatcher> matchers = getMatchers(reliability);
		StockPeriod stockPeriod = new StockPeriod(period.getStockName(), 10);
		List<Indicator> indicators = new ArrayList<Indicator>();
		long c = 1;
		for (Candle candle : period.getCandles()) {
			stockPeriod.addCandle(candle);
			boolean last = (c++==period.getCandles().size());
			if(last) {
				for (IndicatorMatcher matcher : matchers) {
					if(stockPeriod.getCandles().size()>=MIN_CANDLES) {
						if(matcher.matches(stockPeriod)) {
							indicators.add(matcher.getIndicator());
						}
					}
				}
			}
		}
		return indicators;
	}
	 
	private static List<IndicatorMatcher> getMatchers(ReliabilityFilter reliability) {
		List<IndicatorMatcher> matchers = new ArrayList<IndicatorMatcher>();
		
		//low matchers
		if(reliability==ReliabilityFilter.ALL
				|| reliability==ReliabilityFilter.LOW
				|| reliability==ReliabilityFilter.LOW_MODERATE) {
			matchers.addAll(lowMatchers);
		}
		
		//moderate matchers
		if(reliability==ReliabilityFilter.ALL
				|| reliability==ReliabilityFilter.MODERATE
				|| reliability==ReliabilityFilter.LOW_MODERATE
				|| reliability==ReliabilityFilter.MODERATE_HIGH) {
			matchers.addAll(moderateMatchers);
		}
		
		//high matchers
		if(reliability==ReliabilityFilter.ALL
				|| reliability==ReliabilityFilter.HIGH
				|| reliability==ReliabilityFilter.MODERATE_HIGH) {
			matchers.addAll(highMatchers);
		}
		
		return matchers;
	}
}
 
