package br.com.candleanalyser.advisors;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.CandleAnalyser;
import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.engine.CandleAnalyser.ReliabilityFilter;
import br.com.candleanalyser.engine.Indicator.Pattern;
import br.com.candleanalyser.engine.Indicator.Reliability;
import br.com.candleanalyser.engine.Indicator.Trend;

public class CandleIndicatorAdvisor implements BuyAdvisor, SellAdvisor {

	private StockPeriod period = new StockPeriod(null, 30);
	private static final ReliabilityFilter reliabilityFilter = ReliabilityFilter.MODERATE_HIGH;
	private Indicator lastIndicator;

	@Override
	public void nextCandle(Candle candle) {
		period.addCandle(candle);
	}

	@Override
	public float getBuyStrength() {
		lastIndicator = CandleAnalyser.getBestIndicatorForLastCandle(period, reliabilityFilter);
		if(lastIndicator==null) return 0;
		
		//should buy on reversals
		if(lastIndicator.getTrend()==Trend.BULLISH) {
			if(lastIndicator.getPattern()==Pattern.REVERSAL) {
				if(lastIndicator.getReliability()==Reliability.HIGH) {
					return 1F;
				} else if(lastIndicator.getReliability()==Reliability.MODERATE) {
					return 0.6F;
				} else {
					return 0.3F;
				}
			}
			
		//shouldn't buy at all
		} else if(lastIndicator.getTrend()==Trend.BEARISH){
			return -getSellStrength();
		}
		
		return 0;
	}

	@Override
	public float getSellStrength() {
		lastIndicator = CandleAnalyser.getBestIndicatorForLastCandle(period, reliabilityFilter);
		if(lastIndicator==null) return 0;
		
		//should sell on reversals
		if(lastIndicator.getTrend()==Trend.BEARISH) {
			if(lastIndicator.getPattern()==Pattern.REVERSAL) {
				if(lastIndicator.getReliability()==Reliability.HIGH) {
					return 1F;
				} else if(lastIndicator.getReliability()==Reliability.MODERATE) {
					return 0.6F;
				} else {
					return 0.3F;
				}
			}
			
		//shouldn't sell at all
		} else if(lastIndicator.getTrend()==Trend.BULLISH){
			return -getBuyStrength();
		}
		
		return 0;
	}

	
	@Override
	public String getCurrentInfo() {
		return "buy=" + Helper.formatNumber(getBuyStrength()) + "; sell=" + Helper.formatNumber(getSellStrength()) + "; " + (lastIndicator!=null?lastIndicator.getName():"no indicator");
	}

	@Override
	public void onBuy(float value) {
	}
	@Override
	public void onSell(float value) {
	}

}
