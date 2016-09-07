package br.com.candleanalyser.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import br.com.candleanalyser.engine.threelinebreak.ThreeLineBreakStudy;
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class IndicatorMatcherAllSamplesTest extends TestCase {

	public void testMatcherByAllSamples() throws IOException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		File samples = new File("samples/candlestick-results.csv");
		BufferedReader br = new BufferedReader(new FileReader(samples));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		//loop over each sample and test all
		List<String> failedTests = new ArrayList<String>();
		
		try {
			String line;
			while((line=br.readLine())!=null) {
				if(!line.startsWith("#")) {
					String[] l = line.split(";");
					Date date = sdf.parse(l[0]);
					String stock = l[1];
					StockPeriod stockPeriod = CandleFactory.getStockHistoryFromYahoo(stock, date, 10);
					try {
						IndicatorMatcher im = getMatcher(l[2], l[4], l[5]);
						boolean ok = im.matches(stockPeriod);
						if(ok) {
							System.out.println("Found a match for indicator '" + im.getIndicator().getName() + "' for stock '" + stock + "' at '" + sdf.format(date));
						} else {
							//if(im.getIndicator().getName().equals("Belt Hold Bearish")) {
							//	System.out.println("Ignoring failed test for " + im.getIndicator().getName());
							//	continue;
							//}
							failedTests.add("\nFailed to match indicator '" + im.getIndicator().getName() + "' for stock '" + stock + "' at '" + sdf.format(date) + "'\n\n" + stockPeriod + "\n");
							//break;
						}
					} catch (ClassNotFoundException e) {
						System.out.println("Matcher class for "+ l[2] + "-" + l[4] +" (" + e.getMessage() + ") not found. Skipping the test to this matcher.");
					}
				}
			}
			br.close();
			
		} finally {
			assertEquals("Some tests have failed: " + failedTests, 0, failedTests.size());
		}		
	}

	public static IndicatorMatcher getMatcher(String indicator, String trend, String reliability) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String p = "br.com.candleanalyser.matchers." + reliability.toLowerCase() + ".";
		String in = p + indicator.replaceAll(" ", "");
		try {
			//try without trend in name
			Class<IndicatorMatcher> ind = (Class<IndicatorMatcher>)IndicatorMatcherAllSamplesTest.class.forName(in + "Matcher");
			return (IndicatorMatcher)ind.newInstance();
		} catch (ClassNotFoundException e) {
			//try with trend in name
			in = in + trend;
			Class<IndicatorMatcher> ind = (Class<IndicatorMatcher>)IndicatorMatcherAllSamplesTest.class.forName(in + "Matcher");
			return (IndicatorMatcher)ind.newInstance();
		}
	}

	public static void main(String[] args) throws IOException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String stock = "DCO";
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("14/12/2006");
		StockPeriod stockPeriod = CandleFactory.getStockHistoryFromYahoo(stock, date, 30);
		ChartBuilder.showGraphForCandles(stockPeriod);
		
		ThreeLineBreakStudy t = new ThreeLineBreakStudy(stockPeriod.getCandles());
		StockPeriod sp = new StockPeriod();
		//draw three line break graph
		for (Candle candle : stockPeriod.getCandles()) {
			sp.addCandle(new Candle("ThreeLineBreak", candle.getDate(), t.getBox(candle).getValue1(), t.getBox(candle).getTop(), t.getBox(candle).getBottom(), t.getBox(candle).getValue2(), 0));
		}	
		ChartBuilder.showGraphForCandles(sp);
	}
	
}
