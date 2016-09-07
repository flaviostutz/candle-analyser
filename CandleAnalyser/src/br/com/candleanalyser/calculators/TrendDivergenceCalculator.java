package br.com.candleanalyser.calculators;

import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.util.Line;
import br.com.candleanalyser.util.LinearRegression;

public class TrendDivergenceCalculator {

	private LinearRegression trend1;
	private LinearRegression trend2;
	
	public TrendDivergenceCalculator(int trend1MaxCandles, int trend2MaxCandles) {
		this.trend1 = new LinearRegression(trend1MaxCandles);
		this.trend2 = new LinearRegression(trend2MaxCandles);
	}
	
	public void addSamples(Double value1, Double value2) {
		trend1.addSample(value1);
		trend2.addSample(value2);
	}
	
	/**
	 * Returns a number between 0 and 1 regarding to the difference between the two trends
	 *  1 if the difference between trends is equals or more than 120 degrees
	 *  0 if there is no difference
	 * @return
	 */
	public double getTrendDiff() {
		float max = 1.73F * 2F;//120 degrees -> 1
		double diff = Math.abs(trend1.regress().getB()-trend2.regress().getB());
		return Helper.clamp(diff/max, 0, 1);
	}
	
	/**
	 * Returns whatever the two trends are on opposite directions (bullish or bearish) 
	 */
	public boolean areDivergentTrends() {
		if(trend1.regress()!=null && trend2.regress()!=null) {
			return (trend1.regress().getB()>0 != trend2.regress().getB()>0);
		} else {
			return false;
		}
	}
	
	public Line getTrend1() {
		return trend1.regress();
	}
	public Line getTrend2() {
		return trend2.regress();
	}
	
}
