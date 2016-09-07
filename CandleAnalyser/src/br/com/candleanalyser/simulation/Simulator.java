package br.com.candleanalyser.simulation;

import java.text.NumberFormat;

import br.com.candleanalyser.advisors.BuyAdvisor;
import br.com.candleanalyser.advisors.SellAdvisor;
import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.StockPeriod;

public class Simulator {

	/**
	 * Simulate operation using specific parameters
	 * @param fixedCostPerOperation Cost per buy/sell
	 * @param initialMoney
	 * @param stockPeriod
	 * @param buyAdvisor
	 * @param sellAdvisor
	 * @param buyThreshold Value between 0 and 1 for sensibility on buying (0-buy with minimum indication, 1-buy with maximum indication)
	 * @param sellThreshold Value between 0 and 1 for sensibility on buying (0-sell with minimum indication, 1-sell with maximum indication)
	 * @param listener
	 * @return
	 */
	public static PeriodResult operateUsingAdvisor(double fixedCostPerOperation, double initialMoney, StockPeriod stockPeriod, BuyAdvisor buyAdvisor, SellAdvisor sellAdvisor, float buyThreshold, float sellThreshold, SimulatorListener listener) {

		double operationCost = fixedCostPerOperation * 2;
		Operation currentOperation = null;
		PeriodResult result = new PeriodResult(initialMoney);
		result.setStockPeriod(stockPeriod);

		boolean inside = false;
		double currentMoney = initialMoney;

		for (Candle candle : stockPeriod.getCandles()) {
			buyAdvisor.nextCandle(candle);
			if(buyAdvisor!=sellAdvisor) {
				sellAdvisor.nextCandle(candle);
			}

			if (inside) {
				if (sellAdvisor.getSellStrength()>=sellThreshold) {

					currentOperation.registerSell(candle, fixedCostPerOperation, sellAdvisor.getCurrentInfo());
					sellAdvisor.onSell((float)candle.getClose());
					if(buyAdvisor!=sellAdvisor) {
						buyAdvisor.onSell((float)candle.getClose());
					}
					if(listener!=null) {
						listener.onSell(currentOperation);
					}

					currentMoney += currentOperation.getYieldMoney();
					inside = false;
				}

			} else {
				if (buyAdvisor.getBuyStrength()>=buyThreshold) {

					if (currentMoney < (candle.getClose() + operationCost)) {
						break;
					}

					long qtty = (long) Math.floor((currentMoney - operationCost) / candle.getClose());
					currentOperation = new Operation(qtty, candle, fixedCostPerOperation, buyAdvisor.getCurrentInfo());
					result.addOperation(currentOperation);
					buyAdvisor.onBuy((float)candle.getClose());
					if(buyAdvisor!=sellAdvisor) {
						sellAdvisor.onBuy((float)candle.getClose());
					}
					if(listener!=null) {
						listener.onBuy(currentOperation);
					}

					inside = true;
				}
			}
		}

		if (inside && currentOperation != null) {
			result.setFinalMoney((currentMoney - currentOperation.getBuyDebit()) + (currentOperation.getQtty() * stockPeriod.getLastPrice()) - operationCost);
		} else {
			result.setFinalMoney(currentMoney);
		}

		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);

		return result;
	}
}