package br.com.candleanalyser.advisors;

import br.com.candleanalyser.calculators.RSICalculator;
import br.com.candleanalyser.calculators.RSICalculatorStrategy;
import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Helper;

public class RSIAdvisor implements BuyAdvisor, SellAdvisor {

	private static final float THRESHOLD_BUY = 20F;
	private static final float THRESHOLD_SELL = 80F;
	
	private RSICalculator rsiCalculator;
	private boolean basedOn2080;

	public RSIAdvisor() {
		this(14, RSICalculatorStrategy.CLASSIC_RSI, false, true);
	}
	
	public RSIAdvisor(int numberOfCandles, RSICalculatorStrategy strategy, boolean useVolume, boolean basedOn2080) {
		this.rsiCalculator = new RSICalculator(numberOfCandles, strategy, useVolume);
		this.basedOn2080 = basedOn2080;
	}

	public RSIAdvisor(int numberOfCandles) {
		this(numberOfCandles, RSICalculatorStrategy.CLASSIC_RSI, true, true);
	}
	
	@Override
	public void nextCandle(Candle candle) {
		rsiCalculator.addSample(candle);
	}

	@Override
	public float getBuyStrength() {
		if(!rsiCalculator.isFull()) return 0;
		if(!basedOn2080) {
			float rsi = (float)rsiCalculator.getCurrentValue();
			return 1F - rsi/50F;
		} else {
			if(rsiCalculator.hasValuePassedUpBy(THRESHOLD_BUY)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public float getSellStrength() {
		if(!rsiCalculator.isFull()) return 0;
		if(!basedOn2080) {
			float rsi = (float)rsiCalculator.getCurrentValue();
			return rsi/50F - 1F;
		} else {
			if(rsiCalculator.hasValuePassedDownBy(THRESHOLD_SELL)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getCurrentInfo() {
		String rsiStr = "undefined";
		if(rsiCalculator.isFull()) rsiStr = Helper.formatNumber(rsiCalculator.getCurrentValue());
		return "buy=" + Helper.formatNumber(getBuyStrength()) + "; sell=" + Helper.formatNumber(getSellStrength()) + "; rsi=" + rsiStr;
	}

	@Override
	public void onBuy(float value) {
	}
	@Override
	public void onSell(float value) {
	}
	
}
