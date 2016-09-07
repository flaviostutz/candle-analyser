package br.com.candleanalyser.calculators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.util.FixedQueue;
import br.com.candleanalyser.util.Line;
import br.com.candleanalyser.util.LinearRegression;

public class TrendChannelCalculator {

	private LinearRegression linearRegression;
	private FixedQueue<Candle> candles;
	private float containedCandlesRatio;
	private boolean considerOnlyClose;
	
	private double lastChannelRadius;
	private boolean dirtyRadius = true;

	public TrendChannelCalculator(int numberOfCandles) {
		this(numberOfCandles, 0.95F, false);
	}
	
	/**
	 * Creates a new TrendChannel
	 * @param numberOfCandles - number of candles to maintain in channel. New candles as put in head of queue and the oldest is discarded.
	 * @param containedCandlesRatio - 0-1 percent of candles that will be inside channel
	 * @param considerOnlyClose - if true, consider only the close value to evaluate a candle inside channel, otherwise, consider the whole body
	 */
	public TrendChannelCalculator(int numberOfCandles, float containedCandlesRatio, boolean considerOnlyClose) {
		if(containedCandlesRatio<0 || containedCandlesRatio>1) throw new IllegalArgumentException("containedCandlesRatio must be between 0 and 1. value=" + containedCandlesRatio);
		this.linearRegression = new LinearRegression(numberOfCandles);
		this.candles = new FixedQueue<Candle>(numberOfCandles);
		this.containedCandlesRatio = containedCandlesRatio;
		this.considerOnlyClose = considerOnlyClose;
	}

	public void addCandle(Candle candle) {
		dirtyRadius = true;
		linearRegression.addSample(candle.getDate().getTime(), (float) candle.getClose());
		candles.add(candle);
	}
	
	/**
	 * Returns the trend line in the form y=a+bx, where a=return[0]; b=return[1]
	 * @return
	 */
	public Line getMainTrendLine() {
		return linearRegression.regress();
	}
	
	/**
	 * Returns the upper channel line in the form y=a+bx, where a=return[0]; b=return[1]
	 * @return
	 */
//	public Line getUpperChannelLine() {
//		Line mtl = getMainTrendLine();
//		return new Line(mtl.getA()+calculateChannelRadius(), mtl.getB());
//	}
	
	/**
	 * Returns the lower channel line in the form y=a+bx, where a=return[0]; b=return[1]
	 * @return
	 */
//	public Line getLowerChannelLine() {
//		Line mtl = getMainTrendLine();
//		return new Line(mtl.getA()-calculateChannelRadius(), mtl.getB());
//	}
	
	public double getChannelRadius() {
		if(!dirtyRadius) return lastChannelRadius;
		
		//order candles by error
		Line mtl = getMainTrendLine();
		List<CandleError> errors = new ArrayList<CandleError>();
		for (Candle candle : candles) {
			errors.add(new CandleError(candle, calcError(candle, mtl)));
		}
		Collections.sort(errors);
		
		int pos = (int)((errors.size()-1) * containedCandlesRatio);
		lastChannelRadius = errors.get(pos).getError();
		return lastChannelRadius;
	}
	
	private double calcError(Candle candle, Line mtl) {
		double ideal = mtl.getYForX(candle.getDate().getTime());
		if(considerOnlyClose) {
			return Math.abs(ideal-candle.getClose());
		} else {
			return Math.max(Math.abs(ideal-candle.getMin()), Math.abs(ideal-candle.getMax()));
		}
	}

	public FixedQueue<Candle> getCandles() {
		return candles;
	}

	public long getTime1() {
		return getCandles().get(0).getDate().getTime();
	}
	
	public long getTime2() {
		return getCandles().get(getCandles().getSize()-1).getDate().getTime();
	}
	
	public double getMainPrice1() {
		return getMainTrendLine().getYForX(getTime1());
	}
	
	public double getMainPrice2() {
		return getMainTrendLine().getYForX(getTime2());
	}

	public boolean isFull() {
		return candles.isFull();
	}

	public double getUpperPrice1() {
		return getUpperPrice(getTime1());
	}
	
	public double getLowerPrice1() {
		return getLowerPrice(getTime1());
	}
	
	public double getUpperPrice2() {
		return getUpperPrice(getTime2());
	}
	
	public double getLowerPrice2() {
		return getLowerPrice(getTime2());
	}
	
	public double getUpperPrice(long millis) {
		return getMainTrendLine().getYForX(millis)+getChannelRadius();
	}

	public double getLowerPrice(long millis) {
		return getMainTrendLine().getYForX(millis)-getChannelRadius();
	}

}
