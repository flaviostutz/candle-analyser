package br.com.candleanalyser.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CandleSequence {
	
	private int maxCandles;
	private List<Candle> candles;
	
	public CandleSequence(int maxCandles) {
		this.maxCandles = maxCandles;
		this.candles = new ArrayList<Candle>();
	}
	public CandleSequence(int maxCandles, List<Candle> candles) {
		this(maxCandles);
		for (Candle candle : candles) {
			add(candle);
		}
	}
	public void add(Candle candle) {
		candles.add(candle);
		if(candles.size()>maxCandles) {
			candles.remove(0);
		}
	}
	public List<Candle> getCandles() {
		return candles;
	}
	public Candle getLast(int n) {
		int p = candles.size()-n-1;
		if(p<0) throw new IllegalArgumentException("'n' is greater than values.size()");
		return candles.get(p);
	}
	public int getSize() {
		return candles.size();
	}
	
	public int getMaxCandles() {
		return maxCandles;
	}
	
	public double[] getOpens() {
		double[] result = new double[candles.size()];
		for (int i=0; i<candles.size(); i++) {
			result[i] = candles.get(i).getOpen();
		}
		return result;
	}
	public double[] getCloses() {
		double[] result = new double[candles.size()];
		for (int i=0; i<candles.size(); i++) {
			result[i] = candles.get(i).getClose();
		}
		return result;
	}
	public double[] getMaxes() {
		double[] result = new double[candles.size()];
		for (int i=0; i<candles.size(); i++) {
			result[i] = candles.get(i).getMax();
		}
		return result;
	}
	public double[] getMins() {
		double[] result = new double[candles.size()];
		for (int i=0; i<candles.size(); i++) {
			result[i] = candles.get(i).getMin();
		}
		return result;
	}
	public double[] getVolumes() {
		double[] result = new double[candles.size()];
		for (int i=0; i<candles.size(); i++) {
			result[i] = candles.get(i).getVolume();
		}
		return result;
	}
	public Date[] getDates() {
		List<Date> result = new ArrayList<Date>();
		for (Candle candle : candles) {
			result.add(candle.getDate());
		}
		return result.toArray(new Date[0]);
	}
	public List<Candle> getLastCandles(int lastCandle) {
		int p = candles.size()-lastCandle-1;
		if(p<0) throw new IllegalArgumentException("'lastCandle' is greater than values.size()");
		return new ArrayList<Candle>(candles.subList(0, p+1));
	}
	
}
