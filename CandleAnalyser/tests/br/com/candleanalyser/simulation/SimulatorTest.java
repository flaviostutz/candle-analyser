package br.com.candleanalyser.simulation;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jfree.chart.JFreeChart;

import br.com.candleanalyser.advisors.CandleIndicatorAdvisor;
import br.com.candleanalyser.advisors.MACDAdvisor;
import br.com.candleanalyser.advisors.RSIAdvisor;
import br.com.candleanalyser.advisors.StochRSIAdvisor;
import br.com.candleanalyser.advisors.StopGainLossAdvisor;
import br.com.candleanalyser.advisors.TrendChannelAdvisor;
import br.com.candleanalyser.calculators.RSICalculatorStrategy;
import br.com.candleanalyser.engine.CandleAnalyser;
import br.com.candleanalyser.engine.CandleAnalyser.ReliabilityFilter;
import br.com.candleanalyser.engine.CandleFactory;
import br.com.candleanalyser.engine.ChartBuilder;
import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.engine.Result;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.genetics.simulation.GeneticAdvisorSimulator;
import br.com.candleanalyser.genetics.trainner.AdvisorResult;
import br.com.candleanalyser.genetics.trainner.GeneticTrainner;
import br.com.candleanalyser.genetics.trainner.GeneticTrainnerListenerConsole;
import br.com.candleanalyser.genetics.trainner.GeneticTrainnerRunner;
import br.com.candleanalyser.genetics.trainner.NeuralAdvisorFactory;

public class SimulatorTest extends TestCase {

	public void testOperateUsingCandleIndicatorAdvisor() throws NumberFormatException, IOException, ParseException {
		CandleIndicatorAdvisor advisor = new CandleIndicatorAdvisor();
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("DCO", 180);
		OperationResult result = Simulator.operateUsingAdvisor(10D, 1000D, period, advisor, advisor, 0.5F, 0.5F, null);
		System.out.println(result);
	}

	public static void main(String[] args) throws NumberFormatException, IOException, ParseException, ClassNotFoundException {
//		trainUntilSuccessful();
//		trainOnMultipleStocksAndOperate();
//		executeTrendChannelAdvisor();
//		executeCandleIndicatorAdvisor();
//		executeMACDAdvisor();
//		executeRSIAdvisor();
//		executeStopGainLossAdvisor();
//		executeStochRSIAdvisor();
		executeTestAdvisor();
	}

	private static void executeTestAdvisor() throws NumberFormatException, IOException, ParseException {
		StopGainLossAdvisor sellAdvisor = new StopGainLossAdvisor(0.1F, 0.1F, true);
		StochRSIAdvisor buyAdvisor = new StochRSIAdvisor(14, RSICalculatorStrategy.SMA_BASED, false, true);
		
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("PETR4.SA", Helper.createDate(15, 1, 2007), Helper.createDate(15, 1, 2008));
		PeriodResult result = Simulator.operateUsingAdvisor(10D, 10000D, period, buyAdvisor, sellAdvisor, 0.5F, 0.5F, null);

		JFreeChart chart = ChartBuilder.createCandleChart(period);
		ChartBuilder.addOperations(chart, result);
		ChartBuilder.showGraph(chart, result.getStockPeriod().getStockName());

		System.out.println(result);
	}

	private static void executeStopGainLossAdvisor() throws NumberFormatException, IOException, ParseException {
		StopGainLossAdvisor advisor = new StopGainLossAdvisor(0.05F, 0.05F, true);
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(15, 1, 2007), Helper.createDate(15, 1, 2009));
		PeriodResult result = Simulator.operateUsingAdvisor(10D, 10000D, period, advisor, advisor, 0.5F, 0.5F, null);

		JFreeChart chart = ChartBuilder.createCandleChart(period);
		ChartBuilder.addOperations(chart, result);
		ChartBuilder.showGraph(chart, result.getStockPeriod().getStockName());

		System.out.println(result);
	}

	public static void trainUntilSuccessful() throws NumberFormatException, IOException, ParseException, ClassNotFoundException {
		// EVOLVE ADVISORS
		System.out.println("TRAINNING WITH MULTIPLE STOCKS");
		List<String> trainningStocks = new ArrayList<String>();
		trainningStocks.add("VALE5.SA");
//		trainningStocks.add("BBDC4.SA");
//		trainningStocks.add("TMAR5.SA");

		// prepare trainner
		boolean found = false;
		while(!found) {
			NeuralAdvisorFactory factory = new NeuralAdvisorFactory(0.01F);
//			GeneticAdvisorFactory factory = new SelectiveAdvisorFactory(0.01F);
			GeneticTrainner gs = new GeneticTrainner(15, 30, 1, 1F, 0.1F, 10000F, 10F, factory, 0.5F, 0.5F, new GeneticTrainnerListenerConsole());
	
			// execute trainning
			GeneticTrainnerRunner.performTrainning(gs, trainningStocks, Helper.createDate(1, 1, 2004), Helper.createDate(31, 12, 2005), 0.5F, 30, new File("/advisors.ser"));
			AdvisorResult bestResult = gs.getBestAdvisorResult();
			System.out.println(bestResult.getOperationResult());
	
			// OPERATE WITH BEST ADVISOR AFTER THE DATE OF TRAINNING
			System.out.println("SIMULATING ON A DATE AFTER TRAINNING");
			AdvisorResult result = GeneticAdvisorSimulator.executeSimulation(bestResult.getAdvisor(), 0.5F, 0.5F, "ITAU4.SA", Helper.createDate(1, 1, 2006), Helper.createDate(31, 12, 2006), 10000, 10, null);
			System.out.println(result.getOperationResult());
			found = result.getOperationResult().getYield()>0.28F;
		}
	}
	
	public static void trainOnMultipleStocksAndOperate() throws NumberFormatException, IOException, ParseException, ClassNotFoundException {

		// EVOLVE ADVISORS
		System.out.println("TRAINNING WITH MULTIPLE STOCKS");
		List<String> trainningStocks = new ArrayList<String>();
//		trainningStocks.add("IBOVESPA.SA");
//		trainningStocks.add("DCO");
		trainningStocks.add("VALE5.SA");
		trainningStocks.add("ITAU4.SA");
//		trainningStocks.add("BBDC4.SA");
//		trainningStocks.add("BRTO4.SA");
//		trainningStocks.add("TNLP4.SA");
		trainningStocks.add("TMAR5.SA");

		// prepare trainner
//		GeneticAdvisorFactory factory = new NeuralAdvisorFactory(0.5F, 0.5F, 0.01F);
//		GeneticAdvisorFactory factory = new SelectiveAdvisorFactory(0.01F);
//		GeneticTrainner gs = new GeneticTrainner(20, 30, 10, 0.01F, 0.02F, 10000F, 10F, factory, 0.5F, 0.5F, new GeneticTrainnerListenerConsole());

		// execute trainning
//		GeneticTrainnerRunner.performTrainning(gs, trainningStocks, Helper.createDate(1, 1, 2003), Helper.createDate(31, 12, 2006), 0.5F, 30, new File("/advisors.ser"));
//		System.out.println("Total advisors: " + gs.getAdvisorResults().size());
//		AdvisorResult bestResult = gs.getBestAdvisorResult();
//		System.out.println(bestResult.getOperationResult());

		// OPERATE WITH BEST ADVISOR AFTER THE DATE OF TRAINNING
//		System.out.println("SIMULATING ON A DATE AFTER TRAINNING");
//		GeneticAdvisorSimulator.executeSimulationAndShowResults(bestResult.getAdvisor(), 0.5F, 0.5F, "VALE5.SA", Helper.createDate(1, 1, 2008), Helper.createDate(31, 12, 2008), 10000, 10, null);
//		GeneticAdvisorSimulator.executeSimulationAndShowResults(bestResult.getAdvisor(), 0.5F, 0.5F, "ITAU4.SA", Helper.createDate(1, 1, 2006), Helper.createDate(31, 12, 2006), 10000, 10, null);
//		GeneticAdvisorSimulator.executeSimulationAndShowResults(bestResult.getAdvisor(), "PETR4.SA", Helper.createDate(1, 1, 2006), Helper.createDate(31, 12, 2006), 10000, 10, null);
//		GeneticAdvisorSimulator.executeSimulationAndShowResults(bestResult.getAdvisor(), "POSI3.SA", Helper.createDate(1, 1, 2007), Helper.createDate(31, 12, 2007), 10000, 10, null);
//		GeneticAdvisorSimulator.executeSimulationAndShowResults(bestResult.getAdvisor(), "IBOVESPA.SA", Helper.createDate(1, 1, 2006), Helper.createDate(31, 12, 2006), 10000, 10, null);
	}

	public static void executeMACDAdvisor() throws NumberFormatException, IOException, ParseException {
		MACDAdvisor advisor = new MACDAdvisor(9, 12, 26, 1, 1, 1);
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(15, 1, 2008), Helper.createDate(15, 1, 2009));
		PeriodResult result = Simulator.operateUsingAdvisor(10D, 1000D, period, advisor, advisor, 0.5F, 0.5F, null);

		JFreeChart chart = ChartBuilder.createCandleChart(period);
		ChartBuilder.addOperations(chart, result);
		ChartBuilder.showGraph(chart, result.getStockPeriod().getStockName());

		System.out.println(result);
	}

	public static void executeRSIAdvisor() throws NumberFormatException, IOException, ParseException {
		RSIAdvisor advisor = new RSIAdvisor(14, RSICalculatorStrategy.SMA_BASED, false, true);
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(15, 1, 2006), Helper.createDate(15, 1, 2009));
		PeriodResult result = Simulator.operateUsingAdvisor(10D, 10000D, period, advisor, advisor, 0.5F, 0.5F, null);

		JFreeChart chart = ChartBuilder.createCandleChart(period);
		ChartBuilder.addOperations(chart, result);
		ChartBuilder.showGraph(chart, result.getStockPeriod().getStockName());

		System.out.println(result);
	}

	public static void executeStochRSIAdvisor() throws NumberFormatException, IOException, ParseException {
		StochRSIAdvisor advisor = new StochRSIAdvisor(14, RSICalculatorStrategy.SMA_BASED, false, true);
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(15, 1, 2007), Helper.createDate(15, 1, 2009));
		PeriodResult result = Simulator.operateUsingAdvisor(10D, 10000D, period, advisor, advisor, 0.5F, 0.5F, null);

		JFreeChart chart = ChartBuilder.createCandleChart(period);
		ChartBuilder.addOperations(chart, result);
		ChartBuilder.showGraph(chart, result.getStockPeriod().getStockName());

		//show stoch rsi graph
//		StochRSICalculator sc = new StochRSICalculator(14, RSICalculatorStrategy.SMA_BASED, false);
//		List<Double> rsis = new ArrayList<Double>();
//		for (Candle candle : period.getCandles()) {
//			sc.addSample(candle);
//			rsis.add(sc.getCurrentValue());
//		}
//		ChartBuilder.showGraphForCandles(period);
//		JFreeChart chart2 = ChartBuilder.createTimeLineChart("VALE5.SA");
//		ChartBuilder.addTimeSeries(chart2, "RSI", rsis);
//		ChartBuilder.showGraph(chart2, "VALE5.SA");

		System.out.println(result);
	}

	public static void executeTrendChannelAdvisor() throws NumberFormatException, IOException, ParseException {
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("VALE5.SA", Helper.createDate(1, 1, 2005), Helper.createDate(1, 1, 2007));
		final TrendChannelAdvisor advisor = new TrendChannelAdvisor(20, 0.95F, 1, 2);
		final JFreeChart chart = ChartBuilder.createCandleChart(period);

		PeriodResult result = Simulator.operateUsingAdvisor(10D, 1000D, period, advisor, advisor, 0.5F, 0.5F, new SimulatorListener() {
			public void onSell(Operation currentOperation) {
				ChartBuilder.addTrendChannel(chart, advisor.getTrendChannel());
				// System.out.println("SELL " +
				// currentOperation.getSellCandle().getClose());
			}

			public void onBuy(Operation currentOperation) {
				ChartBuilder.addTrendChannel(chart, advisor.getTrendChannel());
				// System.out.println("BUY " +
				// currentOperation.getBuyCandle().getClose());
			}
		});

		ChartBuilder.addOperations(chart, result);
		ChartBuilder.showGraph(chart, "DCO");

		System.out.println(result);
	}

	public static void executeCandleIndicatorAdvisor() throws NumberFormatException, IOException, ParseException {
		CandleIndicatorAdvisor advisor = new CandleIndicatorAdvisor();
		StockPeriod period = CandleFactory.getStockHistoryFromYahoo("DCO", 120);
		PeriodResult result = Simulator.operateUsingAdvisor(10D, 1000D, period, advisor, advisor, 0.5F, 0.5F, null);
		List<Result> indicatorResults = CandleAnalyser.analysePeriod(period, ReliabilityFilter.MODERATE_HIGH);

		JFreeChart chart = ChartBuilder.createCandleChart(period);
		ChartBuilder.addOperations(chart, result);
		ChartBuilder.addIndicators(chart, indicatorResults);
		ChartBuilder.showGraph(chart, "DCO");

		System.out.println(result);
	}

}
