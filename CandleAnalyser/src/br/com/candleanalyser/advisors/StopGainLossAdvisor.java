package br.com.candleanalyser.advisors;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Helper;

public class StopGainLossAdvisor implements BuyAdvisor, SellAdvisor {

	private float maxValue = 0;
	private float lastBuyValue;
	private boolean up;
	private Candle lastCandle;
	private float stopGain;
	private float stopLoss;
	private boolean useMovingStopLoss;
	
	public StopGainLossAdvisor(float stopGain, float stopLoss, boolean useMovingStopLoss) {
		this.stopGain = stopGain;
		this.stopLoss = stopLoss;
		this.useMovingStopLoss = useMovingStopLoss;
	}
	
	@Override
	public void nextCandle(Candle candle) {
		if(lastCandle!=null) {
			up = candle.getClose()>lastCandle.getClose();
		}
		if(useMovingStopLoss) {
			maxValue = (float)Math.max(maxValue, candle.getClose());
		}
		lastCandle = candle;
	}

	@Override
	public float getBuyStrength() {
		return (up?1F:0F);
	}

	@Override
	public float getSellStrength() {
		//stop gain
		if(getGainFactor()>=stopGain) {
			return 1;
			
		//stop loss
		} else if(getLossFactor()>=stopLoss) {
			return 1;
		}
		return 0;
	}

	private float getGainFactor() {
		return ((float)lastCandle.getClose()/lastBuyValue) - 1;
	}
	
	private float getLossFactor() {
		return -(((float)lastCandle.getClose()/maxValue) - 1);
	}
	
	@Override
	public String getCurrentInfo() {
		return "gain=" + Helper.formatNumber(getGainFactor()) + "; loss=" + Helper.formatNumber(getLossFactor());
	}

	@Override
	public void onBuy(float value) {
		lastBuyValue = value;
		maxValue = value;
	}
	@Override
	public void onSell(float value) {
	}
	
}
