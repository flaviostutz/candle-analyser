package br.com.candleanalyser.engine.threelinebreak;

public class Box {
	
	private double value1;
	private double value2;
	
	public double getValue1() {
		return value1;
	}
	public void setValue1(double value1) {
		this.value1 = value1;
	}
	public double getValue2() {
		return value2;
	}
	public void setValue2(double value2) {
		this.value2 = value2;
	}
	public double getHeight() {
		return Math.abs(value2-value1);
	}
	public boolean isUpTrend() {
		return (value2-value1)>0;
	}
	public double getTop() {
		return Math.max(value1, value2);
	}
	public double getBottom() {
		return Math.min(value1, value2);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value1);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(value2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		final Box other = (Box) obj;
		if (Double.doubleToLongBits(value1) != Double
				.doubleToLongBits(other.value1))
			return false;
		if (Double.doubleToLongBits(value2) != Double
				.doubleToLongBits(other.value2))
			return false;
		return true;
	}
	
	
}
