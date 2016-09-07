package br.com.candleanalyser.engine;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;
import br.com.candleanalyser.engine.CandleAnalyser.ReliabilityFilter;

public class CandleAnalyserTest extends TestCase {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public void testAnalyse() throws NumberFormatException, IOException, ParseException {
		
		/*
		 * ALTR:
		 * 20/08 - Doji Star Bullish
		 * 13/08 - Belt Hold Bearish
		 * 30/07 - Belt Hold Bullish
		 * 18/07 - Hanging Man Bearish
		 */
		
		StockPeriod sp = CandleFactory.getStockHistoryFromYahoo("ALTR", sdf.parse("20/08/2007"), 24);
		List<Result> results = CandleAnalyser.analysePeriod(sp, ReliabilityFilter.ALL);
		
		assertEquals(4, results.size());
		assertEquals(1, results.get(3).getIndicators().size());
		assertEquals("Doji Star Bullish", results.get(3).getIndicators().get(0).getName());
		assertEquals("Mon Aug 20 00:00:00 BRT 2007", results.get(3).getCandle().getDate().toString());
	}
	
	public void testAnalyseLastCandle() throws NumberFormatException, IOException, ParseException {
		StockPeriod sp = CandleFactory.getStockHistoryFromYahoo("DCO", sdf.parse("13/8/2007"), 30);
		Indicator indicator = CandleAnalyser.getBestIndicatorForLastCandle(sp, ReliabilityFilter.ALL);
		if(indicator!=null) {
			System.out.println("Found indicator " + indicator.getName());
		}
		assertNotNull(indicator);
		assertEquals("Three Inside Up Bullish", indicator.getName());
	}
}