package br.com.candleanalyser.util;

public class LinearRegression {

	private FixedQueue<CompoundXY> fixedList;
	private double lastx;
	private Line lastResult = null;
	private boolean dirty = true;//optimization. don't regress again if no new sample was added

	public LinearRegression(int numberOfSamples) {
		fixedList = new FixedQueue<CompoundXY>(numberOfSamples);
	}

	/**
	 * Performs a regression on samples and returns an array containing the
	 * elements "a" and "b" for the form "y = a + bx".
	 * 
	 * @return
	 */
	public Line regress() {
		if(!dirty) return lastResult;
		
		int n = fixedList.getSize();
		if (n == 0) {
			throw new IllegalStateException("No sample was added to this calculator");
		}
		
		double sumx=0, sumy=0, sumxx=0, sumxy=0;
		for (int i = 0; i < n; i++) {
			CompoundXY cv = fixedList.get(i);
			double x = cv.getX();
			double y = cv.getY();
			sumx += x;
			sumy += y;
			sumxx += x*x;
			sumxy += x*y;
		}

		double sxx = sumxx-(sumx*sumx/n);
		double sxy = sumxy-(sumx*sumy/n);
		if(sxx==0) return lastResult;
		
		double b = sxy/sxx;
		double a = (sumy-(b*sumx))/n;
		
		lastResult = new Line(a,b);
		return lastResult;
	}

	public void addSample(double x, double y) {
		dirty = true;
		lastx = x;
		fixedList.add(new CompoundXY(x,y));
	}
	
	public void addSample(double y) {
		addSample(lastx+1, y);
	}
	
	public int getNumberOfSamples() {
		return fixedList.getSize();
	}

}
