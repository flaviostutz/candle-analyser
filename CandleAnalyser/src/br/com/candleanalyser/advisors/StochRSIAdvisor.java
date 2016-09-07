package br.com.candleanalyser.advisors;

import br.com.candleanalyser.calculators.RSICalculatorStrategy;
import br.com.candleanalyser.calculators.StochRSICalculator;
import br.com.candleanalyser.calculators.TrendDivergenceCalculator;
import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Helper;

public class StochRSIAdvisor implements BuyAdvisor, SellAdvisor {

	private StochRSICalculator stochRsiCalculator;
	private TrendDivergenceCalculator trendDivergenceCalculator;
	
//	private Neuron neuron;
	private boolean basedOn2080;
//	private static final int DIVERGENCE = 1;
//	private static final int RSI_VALUE = 2;

	private static final float THRESHOLD_BUY = 20F;
	private static final float THRESHOLD_SELL = 80F;
	
	public StochRSIAdvisor() {
		this(14, RSICalculatorStrategy.CLASSIC_RSI, false, true);
	}
	
	public StochRSIAdvisor(int numberOfCandles, RSICalculatorStrategy strategy, boolean useVolume, boolean basedOn2080) {
		this.stochRsiCalculator = new StochRSICalculator(numberOfCandles, strategy, useVolume);
		this.trendDivergenceCalculator = new TrendDivergenceCalculator(14, 14);
		this.basedOn2080 = basedOn2080;
//		this.neuron = new Neuron();
//		neuron.addInput(DIVERGENCE, weightTrendDivergence);
//		neuron.addInput(RSI_VALUE, weightRsiValue);
	}
	
	@Override
	public void nextCandle(Candle candle) {
		stochRsiCalculator.addSample(candle);
		trendDivergenceCalculator.addSamples(candle.getClose(), stochRsiCalculator.getCurrentValue());
	}

	@Override
	public float getBuyStrength() {
		if(!stochRsiCalculator.isFull()) return 0;
		if(!basedOn2080) {
			float rsi = (float)stochRsiCalculator.getCurrentValue();
			return 1F - rsi/50F;
		} else {
			if(stochRsiCalculator.hasValuePassedUpBy(THRESHOLD_BUY)) {
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * Returns 1 for buy potential and -1 for sell potential
	 * @return
	 */
	private float getRsiPotential() {
		return 1F - (float)(stochRsiCalculator.getCurrentValue()/50.0);
	}

	@Override
	public float getSellStrength() {
		if(!stochRsiCalculator.isFull()) return 0;
		if(!basedOn2080) {
			float rsi = (float)stochRsiCalculator.getCurrentValue();
			return rsi/50F - 1F;
		} else {
			if(stochRsiCalculator.hasValuePassedDownBy(THRESHOLD_SELL)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getCurrentInfo() {
		String rsiStr = "undefined";
		if(stochRsiCalculator.isFull()) rsiStr = Helper.formatNumber(stochRsiCalculator.getCurrentValue());
		return "buy=" + Helper.formatNumber(getBuyStrength()) + "; sell=" + Helper.formatNumber(getSellStrength()) + "; s-rsi=" + rsiStr;
	}

	@Override
	public void onBuy(float value) {
	}
	@Override
	public void onSell(float value) {
	}
	
}
