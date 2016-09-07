package br.com.candleanalyser.simulation;

import java.util.ArrayList;
import java.util.List;

import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.engine.StockPeriod;

public class PeriodResult extends OperationResult {

	private StockPeriod stockPeriod;
	private List<Operation> operations = new ArrayList<Operation>();

	private double initialMoney;
	private double finalMoney;

	public PeriodResult(double initialMoney) {
		this.initialMoney = initialMoney;
		this.finalMoney = initialMoney;
	}
	
	public double getInitialMoney() {
		return initialMoney;
	}

	public double getFinalMoney() {
		return finalMoney;
	}
	public void setFinalMoney(double finalMoney) {
		this.finalMoney = finalMoney;
	}

	public void addOperation(Operation operation) {
		operations.add(operation);
	}

	public double getTotalCost() {
		double total = 0;
		for (Operation op : operations) {
			total += op.getCost();
		}
		return total;
	}
	
	public List<Operation> getOperations() {
		return operations;
	}

	public StockPeriod getStockPeriod() {
		return stockPeriod;
	}
	public void setStockPeriod(StockPeriod stockPeriod) {
		this.stockPeriod = stockPeriod;
	}
	
	@Override
	public String getStockName() {
		return stockPeriod.getStockName();
	}
	
	public String toString() {
		String r = "";
		for (Operation op : operations) {
			r += "\n" + op;
		}
		String str = ">> OperationResult for "+ stockPeriod.getStockName() +":\n";
		str += "          Without operating: " + Helper.formatNumber(((stockPeriod.getLastPrice() / stockPeriod.getFirstPrice()) - 1) * 100) + "%; firstPrice=" + Helper.formatNumber(stockPeriod.getFirstPrice()) + "; lastPrice=" + Helper.formatNumber(stockPeriod.getLastPrice()) + "\n";
		str += "           Doing operations: " + super.toString();
		str += r + "\n\n";
		return str;
	}

}
