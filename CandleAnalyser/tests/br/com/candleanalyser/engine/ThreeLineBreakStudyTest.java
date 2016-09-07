package br.com.candleanalyser.engine;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import br.com.candleanalyser.engine.threelinebreak.ThreeLineBreakStudy;

public class ThreeLineBreakStudyTest extends TestCase {

	public void testStudy() throws NumberFormatException, IOException, ParseException {
		List<Candle> candles = CandleFactory.getStockHistoryFromYahoo("AFF", 50).getCandles();
		ThreeLineBreakStudy t = new ThreeLineBreakStudy(candles);

		/* TEST CREATION STUFF
		int c = 0;
		for (Box box: t.getBoxes()) {
			//System.out.println("=====================================");
			//System.out.println("Candle: date="+ candle.getDate() +"; close=" + candle.getClose());
			//System.out.println("Box: upTrend="+ t.getBox(candle).isUpTrend() +"; value1=" + t.getBox(candle).getValue1() + "; value2=" + t.getBox(candle).getValue2());

			System.out.println("assertEquals("+box.getValue1()+", t.getBoxes().get("+c+").getValue1());");
			System.out.println("assertEquals("+box.getValue2()+", t.getBoxes().get("+c+").getValue2());\n");
			c++;
		}
		*/
		 
		assertEquals(24.520000457763672, t.getBoxes().get(0).getValue1());
		assertEquals(24.649999618530273, t.getBoxes().get(0).getValue2());

		assertEquals(24.649999618530273, t.getBoxes().get(1).getValue1());
		assertEquals(24.770000457763672, t.getBoxes().get(1).getValue2());

		assertEquals(24.770000457763672, t.getBoxes().get(2).getValue1());
		assertEquals(24.899999618530273, t.getBoxes().get(2).getValue2());

		assertEquals(24.899999618530273, t.getBoxes().get(3).getValue1());
		assertEquals(24.969999313354492, t.getBoxes().get(3).getValue2());

		assertEquals(24.969999313354492, t.getBoxes().get(4).getValue1());
		assertEquals(25.0, t.getBoxes().get(4).getValue2());

		assertEquals(24.969999313354492, t.getBoxes().get(5).getValue1());
		assertEquals(24.600000381469727, t.getBoxes().get(5).getValue2());

		assertEquals(24.969999313354492, t.getBoxes().get(6).getValue1());
		assertEquals(25.0, t.getBoxes().get(6).getValue2());

		assertEquals(24.969999313354492, t.getBoxes().get(7).getValue1());
		assertEquals(24.799999237060547, t.getBoxes().get(7).getValue2());

		assertEquals(24.799999237060547, t.getBoxes().get(8).getValue1());
		assertEquals(24.510000228881836, t.getBoxes().get(8).getValue2());

		assertEquals(24.510000228881836, t.getBoxes().get(9).getValue1());
		assertEquals(24.5, t.getBoxes().get(9).getValue2());

		assertEquals(24.5, t.getBoxes().get(10).getValue1());
		assertEquals(24.450000762939453, t.getBoxes().get(10).getValue2());

		assertEquals(24.5, t.getBoxes().get(11).getValue1());
		assertEquals(24.8799991607666, t.getBoxes().get(11).getValue2());

		assertEquals(24.5, t.getBoxes().get(12).getValue1());
		assertEquals(24.25, t.getBoxes().get(12).getValue2());

		assertEquals(24.5, t.getBoxes().get(13).getValue1());
		assertEquals(24.799999237060547, t.getBoxes().get(13).getValue2());

		assertEquals(24.5, t.getBoxes().get(14).getValue1());
		assertEquals(24.25, t.getBoxes().get(14).getValue2());

		assertEquals(24.5, t.getBoxes().get(15).getValue1());
		assertEquals(24.75, t.getBoxes().get(15).getValue2());

		assertEquals(24.5, t.getBoxes().get(16).getValue1());
		assertEquals(23.969999313354492, t.getBoxes().get(16).getValue2());

		assertEquals(23.969999313354492, t.getBoxes().get(17).getValue1());
		assertEquals(23.399999618530273, t.getBoxes().get(17).getValue2());

		assertEquals(23.399999618530273, t.getBoxes().get(18).getValue1());
		assertEquals(23.25, t.getBoxes().get(18).getValue2());

		assertEquals(23.25, t.getBoxes().get(19).getValue1());
		assertEquals(22.469999313354492, t.getBoxes().get(19).getValue2());

		assertEquals(22.469999313354492, t.getBoxes().get(20).getValue1());
		assertEquals(22.440000534057617, t.getBoxes().get(20).getValue2());

		assertEquals(22.469999313354492, t.getBoxes().get(21).getValue1());
		assertEquals(23.479999542236328, t.getBoxes().get(21).getValue2());

		assertEquals(23.479999542236328, t.getBoxes().get(22).getValue1());
		assertEquals(23.959999084472656, t.getBoxes().get(22).getValue2());

	}

	public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
		String stock = "ANST";
		StockPeriod p1 = CandleFactory.getStockHistoryFromYahoo(stock, 20);
		ThreeLineBreakStudy t = new ThreeLineBreakStudy(p1.getCandles());

		/*DRAW GRAPHS*/
		StockPeriod p2 = new StockPeriod();;
		//draw three line break graph
		for (Candle candle : p1.getCandles()) {
			p2.addCandle(new Candle("ABFS-Three", candle.getDate(), t.getBox(candle).getValue1(), t.getBox(candle).getTop(), t.getBox(candle).getBottom(), t.getBox(candle).getValue2(), 0));
		}	
		//stock candles
		ChartBuilder.showGraphForCandles(p1);
		//three line break boxes
		ChartBuilder.showGraphForCandles(p2);
	}
}
