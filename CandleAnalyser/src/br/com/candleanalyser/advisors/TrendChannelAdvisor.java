package br.com.candleanalyser.advisors;

import br.com.candleanalyser.calculators.TrendChannelCalculator;
import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.util.FixedQueue;

public class TrendChannelAdvisor implements SellAdvisor, BuyAdvisor {

	private TrendChannelCalculator trendChannel;
	private Candle lastCandle;
	private FixedQueue<Candle> delayedQueue;
	private int direction;
	private int confirmationCandles;
	
	public TrendChannelAdvisor(int channelNumberOfCandles, float containedCandlesRatio, int trendDelayDays, int confirmationCandles) {
		this.trendChannel = new TrendChannelCalculator(channelNumberOfCandles, containedCandlesRatio, false);
		this.delayedQueue = new FixedQueue<Candle>(trendDelayDays);
		this.confirmationCandles = confirmationCandles;
	}
	
	@Override
	public void nextCandle(Candle candle) {
		//keep trend channel at 3 days ago
		delayedQueue.add(candle);
		if(delayedQueue.isFull()) {
			trendChannel.addCandle(delayedQueue.get(0));
		}
		//define direction
		if(lastCandle!=null) {
			if(candle.getClose()>lastCandle.getClose()) {
				if(direction>0) direction++;
				else direction = 1;
			} else if(candle.getClose()<lastCandle.getClose()) {
				if(direction<0) direction--;
				else direction = -1;
			} else {
				direction = 0;
			}
		}
		lastCandle = candle;
	}

	@Override
	public float getBuyStrength() {
		if(!trendChannel.isFull()) return 0;
		//breaking tendency strategy
		double diffToUpper = lastCandle.getClose() - trendChannel.getUpperPrice(lastCandle.getDate().getTime());
		if(diffToUpper>0) {
			if(direction>confirmationCandles) {
				return 1;
			}
		}
		return 0;
		
		//top/bottom channel strategy
//		double potential = trendChannel.getMainPrice2() - lastCandle.getClose();
//		return (float)Helper.clamp(potential/trendChannel.getChannelWidth(), -1, 1);
	}

	@Override
	public float getSellStrength() {
		if(!trendChannel.isFull()) return 0;
		
		//breaking tendency strategy
		double diffToUpper = lastCandle.getClose() - trendChannel.getLowerPrice(lastCandle.getDate().getTime());
		if(diffToUpper<0) {
			if(direction<-confirmationCandles) {
				return 1;
			}
		}
		return 0;
		
		//if trend line is 30 degrees (down trend), sell is 1!
		//top/bottom channel strategy
//		return (float)Helper.clamp(-getBuyStrength() + ((float)trendChannel.getMainTrendLine().getB()/0.57F), -1, 1);
	}
	
	@Override
	public String getCurrentInfo() {
		if(trendChannel.getMainTrendLine()==null) return null;
		return "buy=" + Helper.formatNumber(getBuyStrength()) + "; sell=" + Helper.formatNumber(getBuyStrength()) + "; b=" + Helper.formatNumber(trendChannel.getMainTrendLine().getB());
	}
	
	public TrendChannelCalculator getTrendChannel() {
		return trendChannel;
	}

	@Override
	public void onBuy(float value) {
	}
	@Override
	public void onSell(float value) {
	}
	
}
