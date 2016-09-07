package br.com.candleanalyser.matchers;
import br.com.candleanalyser.engine.Indicator;
import br.com.candleanalyser.engine.StockPeriod;

public interface IndicatorMatcher {
	
	public boolean matches(StockPeriod stockPeriod);
	public Indicator getIndicator();

}
 
