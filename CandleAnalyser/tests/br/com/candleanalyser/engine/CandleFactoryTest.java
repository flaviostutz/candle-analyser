package br.com.candleanalyser.engine;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import junit.framework.TestCase;

public class CandleFactoryTest extends TestCase {

	public void testLoadYahooFinances() throws NumberFormatException, IOException, ParseException {
		List<Candle> candles = CandleFactory.getStockHistoryFromYahoo("ALTR", 500).getCandles();
		assertEquals(500, candles.size());
		assertEquals(21.74F, candles.get(0).getOpen());
		assertEquals(22.95F, candles.get(499).getOpen());
	}
	
}
