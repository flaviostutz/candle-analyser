package br.com.candleanalyser.calculators;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Helper;


public class RSICalculator extends SamplingCalculator<Candle> {

	private int maxSamples;
	private boolean useVolume;
	private RSICalculatorStrategy strategy;
	private static final int POINTS_FOR_PRECISION = 250;
	
	public RSICalculator() {
		this(14, RSICalculatorStrategy.CLASSIC_RSI, false);//default value
	}
	
	public RSICalculator(int maxSamples, RSICalculatorStrategy strategy, boolean useVolume) {
		super(POINTS_FOR_PRECISION + maxSamples);
		this.maxSamples = maxSamples;
		this.useVolume = useVolume;
		this.strategy = strategy;
	}

	public void addSample(double value) {
		if(useVolume) throw new IllegalStateException("Use addSample(Candle) because it is needed stock's volume");
		addSample(new Candle(Double.NaN,Double.NaN,Double.NaN,value,Double.NaN));
	}
	
	@Override
	public boolean isFull() {
		return getSamples().getSize()>=maxSamples;
	}
	
	@Override
	protected double calculateValue() {
		//average calculators
		double lastAvgGain=0;
		double lastAvgLoss=0;
		SamplingCalculator<Double> gainAvgCalc = new SMACalculator(maxSamples);
		SamplingCalculator<Double> lossAvgCalc = new SMACalculator(maxSamples);
		if(strategy.equals(RSICalculatorStrategy.EMA_BASED)) {
			gainAvgCalc = new EMACalculator(maxSamples);
			lossAvgCalc = new EMACalculator(maxSamples);
		}
		
		double lastValue = Double.NaN;
		for (Candle candle : getSamples()) {
			if(!Double.isNaN(lastValue)) {//first pass
				//verify gain/loss since last sample
				double diff = candle.getClose() - lastValue;
				double gain = Helper.clamp(diff, 0, Double.MAX_VALUE);
				double loss = Helper.clamp(-diff, 0, Double.MAX_VALUE);
				if(useVolume) {
					gain *= candle.getVolume();
					loss *= candle.getVolume();
				}
				
				//average calculation
				gainAvgCalc.addSample(gain);
				lossAvgCalc.addSample(loss);
				
				//CLASSIC RSI
				if(strategy.equals(RSICalculatorStrategy.CLASSIC_RSI)) {
					//use average only
					if(!gainAvgCalc.isFull()) {
						lastAvgGain = gainAvgCalc.getCurrentValue();
						lastAvgLoss = lossAvgCalc.getCurrentValue();
					
					//use iteration
					} else {
						lastAvgGain = (lastAvgGain*((double)maxSamples-1.0) + gain)/(double)maxSamples;
						lastAvgLoss = (lastAvgLoss*((double)maxSamples-1.0) + loss)/(double)maxSamples;
					}
					
				//MOVING AVERAGE only
				} else {
					lastAvgGain = gainAvgCalc.getCurrentValue();
					lastAvgLoss = lossAvgCalc.getCurrentValue();
				}
			}
			
			lastValue = candle.getClose();
		}

		//calculate rsi
		if(lastAvgLoss==0) return 100;
		double rsi = 1.0 + (lastAvgGain/lastAvgLoss);
		rsi = 100.0/rsi;
		rsi = 100.0 - rsi;
		return rsi;
	}
	
}
