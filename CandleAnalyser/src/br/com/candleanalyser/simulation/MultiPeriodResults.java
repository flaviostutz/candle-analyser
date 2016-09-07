package br.com.candleanalyser.simulation;

import java.util.ArrayList;
import java.util.List;

import br.com.candleanalyser.engine.StockPeriod;

public class MultiPeriodResults extends OperationResult {

	private List<PeriodResult> periodResults = new ArrayList<PeriodResult>();
	
	public List<PeriodResult> getPeriodResults() {
		return periodResults;
	}
	
	public void addPeriodResult(PeriodResult result) {
		periodResults.add(result);
	}

	@Override
	public String getStockName() {
		String name = "";
		for (PeriodResult pr: periodResults) {
			name += pr.getStockName() + ", ";
		}
		return name.substring(0, name.length()-2);
	}

	@Override
	public double getTotalCost() {
		double total = 0;
		for (PeriodResult pr : periodResults) {
			total += pr.getTotalCost();
		}
		return total;
	}

	@Override
	public double getInitialMoney() {
		return periodResults.get(0).getInitialMoney();
	}

	@Override
	public double getFinalMoney() {
		return periodResults.get(periodResults.size()-1).getFinalMoney();
	}

	@Override
	public List<Operation> getOperations() {
		List<Operation> operations = new ArrayList<Operation>();
		for (PeriodResult pr : periodResults) {
			operations.addAll(pr.getOperations());
		}
		return operations;
	}
	
	@Override
	public StockPeriod getStockPeriod() {
		StockPeriod stockPeriod = new StockPeriod();
		stockPeriod.setStockName(getStockName());
		for (PeriodResult pr : periodResults) {
			stockPeriod.getCandles().addAll(pr.getStockPeriod().getCandles());
		}
		return stockPeriod;
	}
	
	@Override
	public String toString() {
		String str  = "==============================================\n";
		for (PeriodResult pr : periodResults) {
		       str += pr.toString();
		}
		       str += "==============================================\n";
		return str;
	}

}
