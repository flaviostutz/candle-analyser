package br.com.candleanalyser.engine;
public class Indicator {

	public enum Pattern {CONTINUATION, REVERSAL}
	public enum Trend {BULLISH, BEARISH}
	public enum Reliability {LOW, MODERATE, HIGH}
	
	private String name;
	private Trend trend;
	private Pattern pattern;
	private Reliability reliability;
	
	public Indicator(String name, Trend trend, Pattern pattern, Reliability reliability) {
		this.name = name;
		this.trend = trend;
		this.pattern = pattern;
		this.reliability = reliability;
	}
	
	public String getName() {
		return name;
	}
	public Trend getTrend() {
		return trend;
	}
	public Pattern getPattern() {
		return pattern;
	}
	public Reliability getReliability() {
		return reliability;
	}

	
	
}
 
