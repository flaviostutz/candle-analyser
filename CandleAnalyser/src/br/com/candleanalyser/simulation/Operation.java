package br.com.candleanalyser.simulation;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import br.com.candleanalyser.engine.Candle;

public class Operation {
	
	private long qtty;
	private Candle buyCandle;
	private Candle sellCandle;
	private double buyCost;
	private String buyInfo;
	private double sellCost;
	private String sellInfo;
	
	public Operation(long qtty, Candle buyCandle, double buyCost, String buyInfo) {
		this.qtty = qtty;
		this.buyCandle = buyCandle;
		this.buyCost = buyCost;
		this.buyInfo = buyInfo;
	}
	
	public void registerSell(Candle sellCandle, double sellCost, String sellInfo) {
		this.sellCandle = sellCandle;
		this.sellCost = sellCost;
		this.sellInfo = sellInfo;
	}

	public String getBuyInfo() {
		return buyInfo;
	}
	public String getSellInfo() {
		return sellInfo;
	}
	
	public long getQtty() {
		return qtty;
	}

	public Candle getBuyCandle() {
		return buyCandle;
	}

	public Candle getSellCandle() {
		return sellCandle;
	}

	public double getCost() {
		return sellCost + buyCost;
	}
	
	private void assertState() {
		if(sellCandle==null) throw new IllegalStateException("This operation was not finished yet. Some operations are not permited.");
	}
	
	public double getBuyDebit() {
		return qtty*buyCandle.getClose() + buyCost;
	}
	
	public double getSellCredit() {
		assertState();
		return qtty*sellCandle.getClose() - sellCost;
	}
	
	public double getYield() {
		assertState();
		return (getSellCredit()/getBuyDebit())-1;
	}
	
	public double getYieldMoney() {
		assertState();
		return getSellCredit() - getBuyDebit();
	}
	
	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String result = "Operation: buyDate=" + sdf.format(getBuyCandle().getDate()) + "; buyDebit=" + nf.format(getBuyDebit()) + "; buyValue=" + nf.format(getBuyCandle().getClose()) + "; qtty="+ getQtty();
		if(sellCandle!=null) {
			result += "\n           sellDate=" + sdf.format(getSellCandle().getDate()) + "; sellCredit=" + nf.format(getSellCredit()) + "; sellValue=" + nf.format(getSellCandle().getClose()) + "; yield=" + nf.format(getYield()*100) + "%; yieldMoney=" + nf.format(getYieldMoney()); 
		} else {
			result += "\n           No sell in this operation";
		}
		return result;
	}
	
}
