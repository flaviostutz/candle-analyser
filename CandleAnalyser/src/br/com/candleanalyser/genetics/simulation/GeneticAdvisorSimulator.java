package br.com.candleanalyser.genetics.simulation;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.jfree.chart.JFreeChart;

import br.com.candleanalyser.engine.CandleFactory;
import br.com.candleanalyser.engine.ChartBuilder;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.genetics.GeneticAdvisor;
import br.com.candleanalyser.genetics.trainner.AdvisorResult;
import br.com.candleanalyser.simulation.PeriodResult;
import br.com.candleanalyser.simulation.Simulator;
import br.com.candleanalyser.simulation.SimulatorListener;

public class GeneticAdvisorSimulator {

	/**
	 * Select the best advisor considering its results on multiple stocks
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	/*
	 * public static GeneticAdvisor selectBestAdvisor(List<GeneticAdvisor>
	 * advisors, List<String> stockNames, Date fromDate, Date toDate, float
	 * initialMoney, float costPerOperation, SimulatorListener listener) throws
	 * NumberFormatException, IOException, ParseException { Map<GeneticAdvisor,
	 * Integer> results = new HashMap<GeneticAdvisor, Integer>();
	 * 
	 * List<AdvisorResult> allResults = new ArrayList<AdvisorResult>(); //call
	 * selectBestAdvisor for each stock for (String stock : stockNames) {
	 * System.out.println("Evaluating advisors performance on " + stock +
	 * "..."); List<AdvisorResult> advisorResults =
	 * executeAdvisorsOnStock(advisors, stock, fromDate, toDate, initialMoney,
	 * costPerOperation, listener); allResults.addAll(advisorResults);
	 * 
	 * //register strength (proportional to how much it appears and its
	 * performance) for (AdvisorResult ar : advisorResults) { Integer strength =
	 * results.get(ar.getAdvisor()); if(strength==null) {
	 * results.put(ar.getAdvisor(),
	 * (int)(ar.getOperationResult().getYield()100)); } else {
	 * results.put(ar.getAdvisor(),
	 * strength+(int)(ar.getOperationResult().getYield()100)); } }
	 * System.out.println("Best performance: " +
	 * Helper.formatNumber(advisorResults
	 * .get(0).getOperationResult().getYield()100)+"%"); }
	 * 
	 * //select strongest advisor GeneticAdvisor bestAdvisor = null; int
	 * highestStrength = 0; for (GeneticAdvisor ar: results.keySet()) { int
	 * strength = results.get(ar); if(bestAdvisor==null ||
	 * strength>highestStrength) { bestAdvisor = ar; highestStrength = strength;
	 * System.out.println("Found stronger advisor: " + strength); } } //TODO VER
	 * PQ ESSES VALORES NAO BATEM COM A SIMULACAO FEITA NO TESTE
	 * System.out.println(">>> BEST ADVISOR RESULTS"); for (AdvisorResult ar :
	 * allResults) { if(ar.getAdvisor().equals(bestAdvisor)) {
	 * System.out.println(ar + " - " +
	 * ar.getOperationResult().getStockPeriod().getStockName()); } }
	 * 
	 * return bestAdvisor; }
	 */
	/**
	 * Select the best advisor considering its results on a single stock
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	/*
	 * public static List<AdvisorResult>
	 * executeAdvisorsOnStock(List<GeneticAdvisor> advisors, String stockName,
	 * Date fromDate, Date toDate, float initialMoney, float costPerOperation,
	 * SimulatorListener listener) throws NumberFormatException, IOException,
	 * ParseException { List<AdvisorResult> results = new
	 * ArrayList<AdvisorResult>(); for (GeneticAdvisor advisor : advisors) {
	 * AdvisorResult ar = executeSimulation(advisor, stockName, fromDate,
	 * toDate, initialMoney, costPerOperation, listener); results.add(ar); }
	 * Collections.sort(results); return results; }
	 */
	/**
	 * Simulate operations on a stock using one advisor
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static AdvisorResult executeSimulation(GeneticAdvisor advisor, float buyThreshold, float sellThreshold, String stockName, Date fromDate, Date toDate, float initialMoney, float costPerOperation, SimulatorListener listener) throws NumberFormatException, IOException, ParseException {
		StockPeriod stockPeriod = CandleFactory.getStockHistoryFromYahoo(stockName, fromDate, toDate);
		PeriodResult result = Simulator.operateUsingAdvisor(costPerOperation, initialMoney, stockPeriod, advisor, advisor, buyThreshold, sellThreshold, listener);
		return new AdvisorResult(advisor, result);
	}

	public static AdvisorResult executeSimulationAndShowResults(GeneticAdvisor advisor, float buyThreshold, float sellThreshold, String stockName, Date fromDate, Date toDate, float initialMoney, float costPerOperation, SimulatorListener listener) throws NumberFormatException, IOException, ParseException {
		//simulate
		AdvisorResult simulationResult = GeneticAdvisorSimulator.executeSimulation(advisor, buyThreshold, sellThreshold, stockName, fromDate, toDate, initialMoney, costPerOperation, listener);
		//show simulation results
		JFreeChart chart = ChartBuilder.createCandleChart(simulationResult.getOperationResult().getStockPeriod());
		ChartBuilder.addOperations(chart, simulationResult.getOperationResult());
		ChartBuilder.showGraph(chart, simulationResult.getOperationResult().getStockPeriod().getStockName());
		System.out.println(simulationResult.getOperationResult());
		return simulationResult;
	}
}
