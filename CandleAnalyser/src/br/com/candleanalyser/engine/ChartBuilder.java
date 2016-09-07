package br.com.candleanalyser.engine;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

import br.com.candleanalyser.calculators.TrendChannelCalculator;
import br.com.candleanalyser.simulation.Operation;
import br.com.candleanalyser.simulation.OperationResult;

public class ChartBuilder {

	public static void addIndicators(JFreeChart chart, List<Result> indicatorResults) {
		for (Result result : indicatorResults) {
			String label = "";
			for (Indicator indicator : result.getIndicators()) {
				label += ", " + indicator.getName();
			}
			XYTextAnnotation annotation = new XYTextAnnotation(label.substring(2), result.getCandle().getDate().getTime(), result.getCandle().getMediumPrice());
			annotation.setFont(new Font("arial", Font.PLAIN, 6));
			chart.getXYPlot().addAnnotation(annotation);
		}
	}

	public static void addOperations(JFreeChart chart, OperationResult result) {
		// add markers for in/out market
		if (result != null) {
			for (Operation operation : result.getOperations()) {
				if (operation.getSellCandle() == null)
					continue;
				Marker marker1 = new IntervalMarker(operation.getBuyCandle().getDate().getTime(), operation.getSellCandle().getDate().getTime());
				marker1.setLabel("yield: " + Helper.formatNumber(operation.getYield() * 100) + "%");
				marker1.setLabelFont(new Font("arial", Font.PLAIN, 6));
				chart.getXYPlot().addDomainMarker(marker1);

				// show buy info
				// int offset = 10;
				if (operation.getBuyInfo() != null) {
					Marker marker = new ValueMarker(operation.getBuyCandle().getDate().getTime());
					marker.setLabel(operation.getBuyInfo());
					// offset += 10;
					marker.setLabelOffset(new RectangleInsets(10, 0, 0, 60));
					marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
					// if (offset > 200)
					// offset = 0;
					marker.setLabelFont(new Font("Arial", Font.PLAIN, 6));
					chart.getXYPlot().addDomainMarker(marker);
				}

				// show sell info
				if (operation.getSellInfo() != null) {
					Marker marker = new ValueMarker(operation.getSellCandle().getDate().getTime());
					marker.setLabel(operation.getSellInfo());
					// offset += 10;
					marker.setLabelOffset(new RectangleInsets(20, 0, 0, 60));
					marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
					// if (offset > 200)
					// offset = 0;
					marker.setLabelFont(new Font("Arial", Font.PLAIN, 6));
					chart.getXYPlot().addDomainMarker(marker);
				}
			}
		}
	}

	public static void addTrendChannel(JFreeChart chart, TrendChannelCalculator trendChannel) {
		XYLineAnnotation upperLine = new XYLineAnnotation(trendChannel.getTime1(), trendChannel.getUpperPrice1(), trendChannel.getTime2(), trendChannel.getUpperPrice2());
		XYLineAnnotation mainLine = new XYLineAnnotation(trendChannel.getTime1(), trendChannel.getMainPrice1(), trendChannel.getTime2(), trendChannel.getMainPrice2());
		XYLineAnnotation lowerLine = new XYLineAnnotation(trendChannel.getTime1(), trendChannel.getLowerPrice1(), trendChannel.getTime2(), trendChannel.getLowerPrice2());
		chart.getXYPlot().addAnnotation(upperLine);
		chart.getXYPlot().addAnnotation(lowerLine);
		chart.getXYPlot().addAnnotation(mainLine);
	}

	public static JFreeChart createTimeLineChart(String stock) {
		TimeSeriesCollection dataset1 = new TimeSeriesCollection();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(stock, "Time", "Value", dataset1, true, true, false);
		return chart;
	}
	
	public static void addTimeSeries(JFreeChart chart, String name, List<Double> values) {
		TimeSeries series = new TimeSeries(name);
		Day d = new Day();
		for (Double value : values) {
			series.add(d, value);
			d = (Day)d.next();
		}
		((TimeSeriesCollection)chart.getXYPlot().getDataset()).addSeries(series);
	}

	public static void showGraph(JFreeChart chart, String stock) {
		// removeWeekendFromTimeline(chart.getXYPlot());
		ChartFrame frame1 = new ChartFrame(stock, chart);
		frame1.setSize(620, 354);
		RefineryUtilities.centerFrameOnScreen(frame1);
		frame1.setVisible(true);
	}

	public static JFreeChart createCandleChart(StockPeriod stockPeriod) {
		CandleSequence seq = new CandleSequence(99999, stockPeriod.getCandles());
		DefaultHighLowDataset dataset = new DefaultHighLowDataset(stockPeriod.getStockName(), seq.getDates(), seq.getMaxes(), seq.getMins(), seq.getOpens(), seq.getCloses(), seq.getVolumes());
		JFreeChart chart = ChartFactory.createCandlestickChart(stockPeriod.getStockName(), "Time", "Value", dataset, true);
		return chart;
	}

	public static void showGraphForCandles(StockPeriod stockPeriod) {
		JFreeChart chart = createCandleChart(stockPeriod);
		showGraph(chart, stockPeriod.getStockName());
	}

	private static void removeWeekendFromTimeline(XYPlot xyplot) {
		SegmentedTimeline stl = new SegmentedTimeline(SegmentedTimeline.DAY_SEGMENT_SIZE, 5, 2);

		DateAxis domainAxis = (DateAxis) xyplot.getDomainAxis();
		Date d1 = domainAxis.getMinimumDate();
		Date d2 = domainAxis.getMaximumDate();

		// go to first monday before minimum date
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		while (gc.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			gc.add(Calendar.DATE, -1);
		}

		// get weekend days
		List<Date> weekendDays = new ArrayList<Date>();
		while (gc.getTime().before(d2)) {
			if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				weekendDays.add(gc.getTime());
			}
			gc.add(Calendar.DATE, 1);
		}

		stl.setStartTime(gc.getTime().getTime());
		stl.addExceptions(weekendDays);
		domainAxis.setTimeline(stl);
	}

}
