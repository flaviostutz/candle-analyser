package br.com.candleanalyser.simulation;

import java.util.List;

import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.engine.StockPeriod;

public abstract class OperationResult implements Comparable<OperationResult> {

	private static final long serialVersionUID = 1L;

	public abstract double getTotalCost();
	public abstract String getStockName();
	public abstract double getInitialMoney();
	public abstract double getFinalMoney();
	public abstract List<Operation> getOperations();
	public abstract StockPeriod getStockPeriod();

	public double getYield() {
		return getFinalMoney()/getInitialMoney() - 1;
	}

	public int compareTo(OperationResult other) {
		if(other.getYield()>getYield()) {
			return 1;
		} else if(other.getYield()<getYield()) {
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return Helper.formatNumber(getYield()*100) + "%; initialMoney=" + Helper.formatNumber(getInitialMoney()) + "; finalMoney=" + Helper.formatNumber(getFinalMoney()) + "; totalCost="+ Helper.formatNumber(getTotalCost());
	}
	
}
 