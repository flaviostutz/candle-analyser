package br.com.candleanalyser.engine;

import java.util.List;

import br.com.candleanalyser.engine.threelinebreak.ThreeLineBreakStudy;

public class StockPeriod {

	// percentage of element (real body, shadow etc) related to max/min diff
	// among all candles in sequece
	private static final double PC_LONG_DAY = 0.09;
	private static final double PC_LONG_SHADOW = 0.13;// more than this PC will
														// be long
	private static final double PC_MEDIUM_SHADOW = 0.08;// more than this PC

	private CandleSequence candleSequencer;
	private CandleSequence trendSequencer;
	private String stockName;

	public StockPeriod() {
		this(null, Integer.MAX_VALUE);
	}
	
	public StockPeriod(int periodMaxCandles) {
		this(null, periodMaxCandles);
	}
	
	public StockPeriod(String stockName, int periodMaxCandles) {
		this.candleSequencer = new CandleSequence(periodMaxCandles);
		this.trendSequencer = new CandleSequence(periodMaxCandles + 30);
		this.stockName = stockName;
	}

	public void addCandle(Candle candle) {
		candleSequencer.add(candle);
		trendSequencer.add(candle);
	}

	public void addCandles(List<Candle> candles) {
		for (Candle candle : candles) {
			candleSequencer.add(candle);
			trendSequencer.add(candle);
		}
	}

	public Candle getLast(int n) {
		return candleSequencer.getLast(n);
	}

	public List<Candle> getCandles() {
		return candleSequencer.getCandles();
	}

	public boolean isShortDay(Candle candle) {
		double p = candle.getBodySize() / getMaxMinPeriodDiff();
		return p < PC_LONG_DAY;
	}

	public boolean isLongDay(Candle candle) {
		double p = candle.getBodySize() / getMaxMinPeriodDiff();
		return p >= PC_LONG_DAY;
	}

	private double getMax() {
		double max = -1;
		for (Candle candle : candleSequencer.getCandles()) {
			if (candle.getMax() > max) {
				max = candle.getMax();
			}
		}
		return max;
	}

	private double getMin() {
		double min = Double.MAX_VALUE;
		for (Candle candle : candleSequencer.getCandles()) {
			if (candle.getMin() < min) {
				min = candle.getMin();
			}
		}
		return min;
	}

	private double getMaxMinPeriodDiff() {
		return getMax() - getMin();
	}

	public boolean hasLongTopShadow(Candle candle) {
		return (candle.getTopShadow() / getMaxMinPeriodDiff()) >= PC_LONG_SHADOW;
	}

	public boolean hasMediumOrLongTopShadow(Candle candle) {
		return (candle.getTopShadow() / getMaxMinPeriodDiff()) >= PC_MEDIUM_SHADOW;
	}

	public boolean hasLongBottomShadow(Candle candle) {
		return (candle.getBottomShadow() / getMaxMinPeriodDiff()) >= PC_LONG_SHADOW;
	}

	public boolean isDownTrend(int lastCandle) {
		ThreeLineBreakStudy tlb = new ThreeLineBreakStudy(trendSequencer.getCandles());
		return !tlb.getLastCandleBox(lastCandle).isUpTrend();
	}

	public boolean isUpTrend(int lastCandle) {
		ThreeLineBreakStudy tlb = new ThreeLineBreakStudy(trendSequencer.getCandles());
		return tlb.getLastCandleBox(lastCandle).isUpTrend();
	}

	public StockPeriod getStockPeriod(int untilLastCandle) {
		StockPeriod p = new StockPeriod(stockName, candleSequencer.getMaxCandles());
		List<Candle> cs = candleSequencer.getLastCandles(untilLastCandle);
		for (Candle c : cs) {
			p.addCandle(c);
		}
		return p;
	}

	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	
	public double getFirstPrice() {
		return getCandles().get(0).getClose();
	}
	
	public double getLastPrice() {
		return getCandles().get(getCandles().size()-1).getClose();
	}
	
	public String toString() {
		String candlesStr = "";
		int c = 0;
		for (Candle candle : candleSequencer.getCandles()) {
			candlesStr += "\n[" + (c++) + "]" + toString(candle) + ":\n";
		}
		return "=== StockPeriod (maxCandles=" + candleSequencer.getMaxCandles() + "):\n" + candlesStr + "\n===";
	}

	public String toString(Candle candle) {
		return candle.toString() + "\n" + "        shortDay=" + isShortDay(candle) + "; longDay=" + isLongDay(candle) + "; longTopShadow=" + hasLongTopShadow(candle) + "; longBottomShadow=" + hasLongBottomShadow(candle);
	}

}
