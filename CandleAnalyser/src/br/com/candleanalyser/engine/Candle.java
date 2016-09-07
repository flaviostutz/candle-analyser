package br.com.candleanalyser.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Candle {

	//tolerance to describe an equal value related to the medium price (max-min/2)
	private static float PC_EQUAL_TOLERANCE = 0.007F;
	
	private String stock;
	private Date date;
	private double open;
	private double max;
	private double min;
	private double close;
	private double volume;

	public Candle(String stock, Date date, double open, double max, double min, double close, double volume) {
		this.stock = stock;
		this.date = date;
		this.open = open;
		this.max = max;
		this.min = min;
		this.close = close;
		this.volume = volume;
	}

	public Candle(double open, double max, double min, double close, double volume) {
		this(null, null, open, max, min, close, volume);
	}
	
	public String getStock() {
		return stock;
	}
	
	public Date getDate() {
		return date;
	}

	public double getOpen() {
		return open;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public double getClose() {
		return close;
	}
	
	public double getVolume() {
		return volume;
	}

	public boolean isWhite() {
		return open<close;
	}

	public boolean isBlack() {
		return open>close;
	}

	public boolean isDoji() {
		//test equal values with minimal tolerance
		return Math.abs(open-close) < (getMediumPrice()*PC_EQUAL_TOLERANCE);
	}

	public boolean isMorubozu() {
		return false;
	}

	public double getBodySize() {
		return Math.abs(getClose() - getOpen());
	}

	public double getTopShadow() {
		return Math.min(getMax()-getOpen(), getMax()-getClose());
	}

	public double getBottomShadow() {
		return Math.min(getOpen()-getMin(), getClose()-getMin());
	}
	
	public boolean isGappingDown(Candle otherCandle) {
		return otherCandle.getClose()>getOpen();
	}

	public boolean isGappingUp(Candle otherCandle) {
		return otherCandle.getClose()<getOpen();
	}

	public boolean hasNoTopShadow() {
		return getTopShadow() <= (getMediumPrice()*PC_EQUAL_TOLERANCE);
	}

	public boolean hasNoBottomShadow() {
		return getBottomShadow() <= (getMediumPrice()*PC_EQUAL_TOLERANCE);
	}

	public double getMediumPrice() {
		return (getMax()+getMin())/2;
	}
	
	public double getBodyMidPoint() {
		return (getOpen()+getClose())/2;
	}

	public boolean isValueInsideBody(double value) {
		return value>=getOpen() && value<=getClose();
	}

	public boolean isBodySmallerThan(Candle anotherCandle) {
		return getBodySize()<anotherCandle.getBodySize();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Candle other = (Candle) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		return true;
	}

	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return "Candle: stock="+ stock +"; date=" + sdf.format(date) + "; open=" + open + "; max=" + max + "; min=" + min + "; close=" + close + "; volume=" + volume + "\n" +
				"        topShadow="+ getTopShadow() + ", bottomShadow="+ getBottomShadow() +"\n" +
				"        morubozu=" + isMorubozu() + "; doji=" + isDoji() + "; black=" + isBlack() + "; white=" + isWhite() + "\n" +
				"        noTopShadow=" + hasNoTopShadow() + "; mediumPrice=" + getMediumPrice();
	}

}
