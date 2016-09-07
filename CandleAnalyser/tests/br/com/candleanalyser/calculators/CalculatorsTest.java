package br.com.candleanalyser.calculators;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jfree.chart.JFreeChart;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.CandleFactory;
import br.com.candleanalyser.engine.ChartBuilder;
import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.engine.StockPeriod;

public class CalculatorsTest extends TestCase {

	public void testMACDCalculator() throws NumberFormatException, IOException, ParseException {
		//test1
		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(1, 1, 2007), Helper.createDate(1, 8, 2008));
		MACDCalculator c1 = new MACDCalculator(9, 12, 26);
		List<Double> macds = new ArrayList<Double>();
		List<Double> macdHistograms = new ArrayList<Double>();
		for (Candle candle : p1.getCandles()) {
			c1.addSample(candle.getClose());
			macds.add(c1.getMACDValue());
			macdHistograms.add(c1.getHistogramValue());
		}
		
		assertEquals(-2.0139587041271696, c1.getMACDValue());
		assertEquals(0.21040881939371925, c1.getHistogramValue());

		//test2
		StockPeriod p2 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(1, 7, 2008), Helper.createDate(31, 12, 2008));
		MACDCalculator c2 = new MACDCalculator(9, 12, 26);
		for (Candle candle : p2.getCandles()) {
			c2.addSample(candle.getClose());
		}
		assertEquals(-0.06563427438189251, c2.getMACDValue());
		assertEquals(-0.04858503546275371, c2.getHistogramValue());
		
	}
	
	public void testEMACalculator() throws NumberFormatException, IOException, ParseException {
		EMACalculator ec = new EMACalculator(10);
		ec.addSample(60.33);
		ec.addSample(59.44);
		ec.addSample(59.38);
		ec.addSample(59.38);
		ec.addSample(59.22);
		ec.addSample(59.88);
		ec.addSample(59.55);
		ec.addSample(59.50);
		ec.addSample(58.66);
		ec.addSample(59.05);
		assertEquals(59.439, ec.getCurrentValue());
		ec.addSample(57.15);
		assertEquals(59.02281818181818, ec.getCurrentValue());
		ec.addSample(57.32);
		assertEquals(58.71321487603306, ec.getCurrentValue());
		ec.addSample(57.65);
		assertEquals(58.519903080390684, ec.getCurrentValue());
	}
	
	public void testStochasticCalculator() throws NumberFormatException, IOException, ParseException {
		//test 1
		StochasticCalculator sc = new StochasticCalculator(14);
		sc.addSample(new Candle(0, 119.50, 116.0, 119.13, 0));
		sc.addSample(new Candle(0, 119.94, 116.0, 116.75, 0));
		sc.addSample(new Candle(0, 118.44, 111.63, 113.50, 0));
		sc.addSample(new Candle(0, 114.19, 110.06, 111.56, 0));
		sc.addSample(new Candle(0, 112.81, 109.63, 112.25, 0));
		sc.addSample(new Candle(0, 113.44, 109.13, 110.0, 0));
		sc.addSample(new Candle(0, 115.81, 110.38, 113.50, 0));
		sc.addSample(new Candle(0, 117.50, 114.06, 117.13, 0));
		sc.addSample(new Candle(0, 118.44, 114.81, 115.63, 0));
		sc.addSample(new Candle(0, 116.88, 113.13, 114.13, 0));
		sc.addSample(new Candle(0, 119.0, 116.19, 118.81, 0));
		sc.addSample(new Candle(0, 119.75, 117.0, 117.38, 0));
		sc.addSample(new Candle(0, 119.13, 116.88, 119.13, 0));
		sc.addSample(new Candle(0, 119.44, 114.56, 115.38, 0));
		assertEquals(57.8168362627197, sc.getCurrentValue());

		//test 2
		StochasticCalculator sc2 = new StochasticCalculator(14);
		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(4, 12, 2007), Helper.createDate(1, 3, 2008));
		for (Candle candle : p1.getCandles()) {
			sc2.addSample(candle);
		}
		assertEquals(58.68545161482896, sc2.getCurrentValue());
	}

	public void testRSICalculator() throws NumberFormatException, IOException, ParseException {
		//test 1 (CLASSIC RSI)
		RSICalculator c1 = new RSICalculator(14, RSICalculatorStrategy.CLASSIC_RSI, false);
		c1.addSample(46.125);
		c1.addSample(47.125);
		c1.addSample(46.4375);
		c1.addSample(46.9375);
		c1.addSample(44.9375);
		c1.addSample(44.25);
		c1.addSample(44.625);
		c1.addSample(45.75);
		c1.addSample(47.8125);
		c1.addSample(47.5625);
		c1.addSample(47.0);
		c1.addSample(44.5625);
		c1.addSample(46.3125);
		c1.addSample(47.6875);
		c1.addSample(46.6875);
		assertEquals(51.77865612648221, c1.getCurrentValue());
		c1.addSample(45.6875);
		assertEquals(48.47708511243952, c1.getCurrentValue());
		c1.addSample(43.0625);
		c1.addSample(43.5625);
		c1.addSample(44.8750);
		c1.addSample(43.6875);
		assertEquals(43.992110594000444, c1.getCurrentValue());
		
		//test 2 (CLASSIC RSI)
		RSICalculator c2 = new RSICalculator(14, RSICalculatorStrategy.CLASSIC_RSI, false);
		c2.addSample(22.44);
		c2.addSample(22.61);
		c2.addSample(22.67);
		c2.addSample(22.88);
		c2.addSample(23.36);
		c2.addSample(23.23);
		c2.addSample(23.08);
		c2.addSample(22.86);
		c2.addSample(23.17);
		c2.addSample(23.69);
		c2.addSample(23.77);
		c2.addSample(23.84);
		c2.addSample(24.32);
		c2.addSample(24.80);
		assertEquals(85.11904761904762, c2.getCurrentValue());
		
		//test 3 (SMA BASED)
		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(1, 10, 2008), Helper.createDate(2, 1, 2009));
		RSICalculator c3 = new RSICalculator(14, RSICalculatorStrategy.SMA_BASED, false);
		for (Candle candle : p1.getCandles()) {
			c3.addSample(candle.getClose());
		}
		assertEquals(61.52417005962591, c3.getCurrentValue());
	}

	public void testStochRSICalculator() throws NumberFormatException, IOException, ParseException {
		StochRSICalculator sc = new StochRSICalculator(14, RSICalculatorStrategy.CLASSIC_RSI, false);
		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(1, 10, 2008), Helper.createDate(2, 1, 2009));
		for (Candle candle : p1.getCandles()) {
			sc.addSample(candle.getClose());
		}
		assertEquals(75.02530787987615, sc.getCurrentValue());
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
		//stochastics
//		StochasticCalculator sc = new StochasticCalculator(14);
//		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(1, 12, 2008), Helper.createDate(2, 1, 2009));
//		List<Double> rsis = new ArrayList<Double>();
//		for (Candle candle : p1.getCandles()) {
//			sc.addSample(candle);
//			if(candle.getDate().after(Helper.createDate(4, 12, 2007))) {
//				rsis.add(sc.getCurrentValue());
//			}
//		}
//		ChartBuilder.showGraphForCandles(p1);
//		JFreeChart chart = ChartBuilder.createTimeLineChart("VALE5.SA");
//		ChartBuilder.addTimeSeries(chart, "Stochastic", rsis);
//		ChartBuilder.showGraph(chart, "VALE5.SA");
		
		//RSI
		RSICalculator sc = new RSICalculator(14, RSICalculatorStrategy.SMA_BASED, false);
		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(15, 1, 2007), Helper.createDate(15, 1, 2009));
		List<Double> rsis = new ArrayList<Double>();
		for (Candle candle : p1.getCandles()) {
			sc.addSample(candle);
//			if(candle.getDate().after(Helper.createDate(15, 12, 2008))) {
				rsis.add(sc.getCurrentValue());
//			}
		}
		ChartBuilder.showGraphForCandles(p1);
		JFreeChart chart = ChartBuilder.createTimeLineChart("VALE5.SA");
		ChartBuilder.addTimeSeries(chart, "RSI", rsis);
		ChartBuilder.showGraph(chart, "VALE5.SA");

		//RSI stochastics
//		StochRSICalculator sc = new StochRSICalculator(14, RSICalculatorStrategy.EMA_BASED, false);
//		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(15, 10, 2008), Helper.createDate(15, 1, 2009));
//		List<Double> rsis = new ArrayList<Double>();
//		for (Candle candle : p1.getCandles()) {
//			sc.addSample(candle);
////			if(candle.getDate().after(Helper.createDate(15, 12, 2008))) {
//				rsis.add(sc.getCurrentValue());
////			}
//		}
//		ChartBuilder.showGraphForCandles(p1);
//		JFreeChart chart = ChartBuilder.createTimeLineChart("VALE5.SA");
//		ChartBuilder.addTimeSeries(chart, "RSI", rsis);
//		ChartBuilder.showGraph(chart, "VALE5.SA");
		
		//MACD
/*		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(4, 12, 2007), Helper.createDate(15, 1, 2009));
		MACDCalculator c1 = new MACDCalculator(9, 12, 26);
		List<Double> macds = new ArrayList<Double>();
		List<Double> macdHistograms = new ArrayList<Double>();
		for (Candle candle : p1.getCandles()) {
			c1.addSample(candle.getClose());
			macds.add(c1.getMACDValue());
			macdHistograms.add(c1.getMACDEmaValue());
		}
		ChartBuilder.showGraphForCandles(p1);
		JFreeChart chart = ChartBuilder.createTimeLineChart("VALE5.SA");
		ChartBuilder.addTimeSeries(chart, "MACD", macds);
		ChartBuilder.addTimeSeries(chart, "Histogram", macdHistograms);
		ChartBuilder.showGraph(chart, "VALE5.SA");
*/	}
	
}
