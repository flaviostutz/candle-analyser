package br.com.candleanalyser.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CandleFactory {

	private static List<Candle> loadCandlesYahooFinances(String stock, File csvFile, int qtty) throws IOException, NumberFormatException, ParseException {
		List<Candle> candles = new ArrayList<Candle>();
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//skip header
		String line = br.readLine();
		
		//read lines
		while((line = br.readLine())!=null && candles.size()<qtty) {
			if(line.startsWith("#")) {
				continue;
			}
			String[] f = line.split(",");
			Candle candle = new Candle(stock, sdf.parse(f[0]), Float.parseFloat(f[1]), Float.parseFloat(f[2]), Float.parseFloat(f[3]), Float.parseFloat(f[4]), Float.parseFloat(f[5]));
			candles.add(candle);
		}
		Collections.reverse(candles);
		return candles;
	}

	public static StockPeriod getStockHistoryFromYahoo(String stock, Date fromDate, Date toDate) throws IOException, NumberFormatException, ParseException {
		List<Candle> candles = new ArrayList<Candle>();
		StockPeriod period = getStockHistoryFromYahoo(stock, null, Integer.MAX_VALUE);
		if(!Helper.dayAfterEquals(fromDate, period.getCandles().get(0).getDate())) {
			throw new IllegalArgumentException("Stock " + stock + " doesn't have candles from " + fromDate);
		}
		if(!Helper.dayBeforeEquals(toDate, period.getCandles().get(period.getCandles().size()-1).getDate())) {
			throw new IllegalArgumentException("Stock " + stock + " doesn't have candles by " + toDate);
		}
		for(Candle candle : period.getCandles()) {
			if(Helper.dayBetweenDates(candle.getDate(), fromDate, toDate)) {
				candles.add(candle);
			}
		}
		StockPeriod r = new StockPeriod(stock, Integer.MAX_VALUE);
		r.addCandles(candles);
		return r;
	}
	public static StockPeriod getStockHistoryFromYahoo(String stock, int qtty) throws IOException, NumberFormatException, ParseException {
		return getStockHistoryFromYahoo(stock, null, qtty);
	}
	public static StockPeriod getStockHistoryFromYahoo(String stock, Date finalDate, int qtty) throws IOException, NumberFormatException, ParseException {
		File csvFile = new File("samples/yahoo/" + stock + ".csv");
		
		//download if it doesnt exist
		if(!csvFile.exists()) {
			System.out.println(stock + ".csv not found. Downloading it...");
			
			//setup proxy
	        //System.setProperty("http.proxyHost", "cache");
//	        System.setProperty("http.proxyPort", "80");
			
			URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + stock + "&g=d&a=4&b=13&c=1992&ignore=.csv");
	        URLConnection con = url.openConnection();
	        
	        //proxy user and pass
//	        con.setRequestProperty(
//	        	"Proxy-Authorization", 
//	        	"Basic " + new sun.misc.BASE64Encoder().encode(
//	        		("F3310193:00000000").getBytes()
//	        	)
//	        );
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile));

			try {
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					if(!csvFile.exists()) csvFile.createNewFile();
					bw.write(inputLine + "\n");
				}
			} finally {
				in.close();
				bw.close();
			}
			
			if(!csvFile.exists()) throw new RuntimeException("It was not possible to download " + stock + " from Yahoo finances");
			System.out.println("done.");
		}
		return loadCandlesFromCSV(stock, csvFile, finalDate, qtty);
	}
	
	public static StockPeriod loadCandlesFromCSV(String stock, File csvFile, Date finalDate, int qtty) throws NumberFormatException, IOException, ParseException {
		List<Candle> candles = CandleFactory.loadCandlesYahooFinances(stock, csvFile, Integer.MAX_VALUE);
		StockPeriod s = new StockPeriod(stock, qtty);
		for (Candle candle : candles) {
			if(finalDate!=null && candle.getDate().after(finalDate)) {
				break;
			} else {
				s.addCandle(candle);
			}
		}
		if(s.getCandles().size()==0) {
			throw new RuntimeException("There are no candles before " + finalDate);
		}
		return s;
	}
	
}
