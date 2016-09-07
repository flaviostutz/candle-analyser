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
import br.com.candleanalyser.matchers.IndicatorMatcher;

public class IndicatorMatcherSelectedSamplesTest extends TestCase {

	public void testMatcherByAllSamples() throws IOException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		File samples = new File("samples/candlestick-results-selected.csv");
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
						IndicatorMatcher im = IndicatorMatcherAllSamplesTest.getMatcher(l[2], l[4], l[5]);
						boolean ok = im.matches(stockPeriod);
						if(ok) {
							System.out.println("Found a match for indicator '" + im.getIndicator().getName() + "' for stock '" + stock + "' at '" + sdf.format(date));
						} else {
							//if(im.getIndicator().getName().equals("Belt Hold Bearish")) {
							//	System.out.println("Ignoring failed test for " + im.getIndicator().getName());
							//	continue;
							//}
							System.out.println("Failed to match indicator '" + im.getIndicator().getName() + "' for stock '" + stock + "' at '" + sdf.format(date));
							failedTests.add("\nFailed to match indicator '" + im.getIndicator().getName() + "' for stock '" + stock + "' at '" + sdf.format(date) + "'\n\n" + stockPeriod + "\n");
							//System.out.println("\nFailed to match indicator '" + im.getIndicator().getName() + "' for stock '" + stock + "' at '" + sdf.format(date) + "'\n\n" + stockPeriod + "\n");
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

}
