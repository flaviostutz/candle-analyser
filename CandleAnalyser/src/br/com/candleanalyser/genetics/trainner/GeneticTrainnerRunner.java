package br.com.candleanalyser.genetics.trainner;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import br.com.candleanalyser.engine.CandleFactory;
import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.engine.StockPeriod;
import br.com.candleanalyser.genetics.GeneticAdvisor;

public class GeneticTrainnerRunner {

	private static Random random = new Random();
	
	public static void performTrainning(GeneticTrainner trainner, List<String> stockNames, Date fromDate, Date toDate, float percentTranningDaysForEachStock, int numberOfNewRandomAdvisors, File advisorsFile) throws IOException, ClassNotFoundException, NumberFormatException, ParseException {
		List<StockPeriod> stockPeriods = new ArrayList<StockPeriod>();
	
		//distribute total days to stock periods
		int daysPeriod = (int)((toDate.getTime()-fromDate.getTime())/(1000*60*60*24));
		int daysEachPeriod = (int)(percentTranningDaysForEachStock*daysPeriod);
		
		for (String stock : stockNames) {
			//define beginning date
			Calendar c1 = GregorianCalendar.getInstance();
			c1.setTime(fromDate);
			int dayBegin = (int)((daysPeriod-daysEachPeriod)*random.nextFloat());
			c1.add(Calendar.DAY_OF_MONTH, dayBegin);
			
			//define end date
			Calendar c2 = GregorianCalendar.getInstance();
			c2.setTime(c1.getTime());
			c2.add(Calendar.DAY_OF_YEAR, daysEachPeriod);
			if(c2.getTime().after(toDate)) {
				throw new IllegalStateException("End stock date cannot be after " + toDate);
			}
			
			//get stocks
			StockPeriod stockPeriod = CandleFactory.getStockHistoryFromYahoo(stock, c1.getTime(), c2.getTime());
			stockPeriods.add(stockPeriod);
		}
		
		if(advisorsFile!=null) {
			//load advisors from disk
			List<GeneticAdvisor> advisors = null;
			try {
				advisors = (List<GeneticAdvisor>)Helper.loadObject(advisorsFile);
			} catch (Exception e) {
				System.out.println("Skipping advisors loading: " + e);
			}
			if(advisors!=null) {
				//evolve loaded advisors
				System.out.println("Found " + advisors.size() + " advisors in disk");
				if(advisors.size()>200) {
					advisors = advisors.subList(0, 199);
					System.out.println("Too many advisors in disk. Using only 200.");
				}
				trainner.evolveAdvisors(stockPeriods, numberOfNewRandomAdvisors, advisors);
			} else {
				//evolve random advisors
				trainner.evolveAdvisors(stockPeriods, numberOfNewRandomAdvisors);
			}
		} else {
			//evolve random advisors
			trainner.evolveAdvisors(stockPeriods, numberOfNewRandomAdvisors);
		}

		//save advisors to disk
		if(advisorsFile!=null) {
			ArrayList<GeneticAdvisor> advisors = new ArrayList<GeneticAdvisor>();
			for (AdvisorResult ar : trainner.getAdvisorResults()) {
				if(ar.getAdvisor() instanceof Serializable) {
					advisors.add(ar.getAdvisor());
				}
			}
			try {
				Helper.saveObject(advisorsFile, advisors);
			} catch (Exception e) {
				System.out.println("Skipping advisors saving: " + e);
			}
		}
	}
	
//EVOLUI CADA PAPEL
//	public static void performTrainning(GeneticTrainner trainner, List<String> stockNames, Date fromDate, Date toDate, int numberOfNewRandomAdvisors, File advisorsFile) throws IOException, ClassNotFoundException, NumberFormatException, ParseException {
//		boolean first = true;
//		for (String stock : stockNames) {
//			StockPeriod stockPeriod = CandleFactory.getStockHistoryFromYahoo(stock, fromDate, toDate);
//			if(first) {
//				if(advisorsFile!=null) {
//					Object obj = Helper.loadObject(advisorsFile);
//					trainner.evolveAdvisorResults(stockPeriod, (List<GeneticAdvisor>)obj);
//				} else {
//					trainner.evolveAdvisorResults(stockPeriod, numberOfNewRandomAdvisors);
//				}
//			} else {
//				trainner.evolveAdvisorResults(stockPeriod, 0);
//			}
//		}
//		if(advisorsFile!=null) {
//			ArrayList<AdvisorResult> results = new ArrayList<AdvisorResult>(trainner.getAdvisorResults());
//			Helper.saveObject(advisorsFile, results);
//		}
//	}
	
}
