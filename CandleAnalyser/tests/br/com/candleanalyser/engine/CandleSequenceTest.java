package br.com.candleanalyser.engine;

import java.util.Date;

import junit.framework.TestCase;

public class CandleSequenceTest extends TestCase {

	public void testAddCandle() {
		StockPeriod s = new StockPeriod(4);
		assertEquals(0, s.getCandles().size());
		s.addCandle(new Candle("", new Date(), 0F, 0F, 0F, 0F, 0F));
		assertEquals(1, s.getCandles().size());
		s.addCandle(new Candle("", new Date(), 0F, 0F, 0F, 0F, 0F));
		assertEquals(2, s.getCandles().size());
		s.addCandle(new Candle("", new Date(), 0F, 0F, 0F, 0F, 0F));
		assertEquals(3, s.getCandles().size());
		s.addCandle(new Candle("", new Date(), 0F, 0F, 0F, 0F, 0F));
		assertEquals(4, s.getCandles().size());
		s.addCandle(new Candle("", new Date(), 0F, 0F, 0F, 0F, 0F));
		assertEquals(4, s.getCandles().size());
		s.addCandle(new Candle("", new Date(), 0F, 0F, 0F, 0F, 0F));
		assertEquals(4, s.getCandles().size());
	}
	
}
