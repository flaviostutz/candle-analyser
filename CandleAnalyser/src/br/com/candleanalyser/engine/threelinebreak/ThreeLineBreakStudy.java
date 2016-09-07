package br.com.candleanalyser.engine.threelinebreak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.CandleSequence;

public class ThreeLineBreakStudy {

	private static final int N = 3;
	private Map<Candle, Box> map;
	private List<Box> boxes;
	private List<Candle> candles;
	
	public ThreeLineBreakStudy(List<Candle> candles) {
		this.candles = candles;
		boxes = new ArrayList<Box>();
		CandleSequence sequencer = new CandleSequence(99999);
		boolean upTrend = true;
		Box currentBox = null;
		map = new HashMap<Candle, Box>();
		
		for (Candle candle : candles) {
			sequencer.add(candle);
			
			//first candle
			if(sequencer.getSize()==1) {
				currentBox = new Box();
				currentBox.setValue1(candle.getOpen());
				currentBox.setValue2(candle.getClose());
				boxes.add(currentBox);
				upTrend = candle.isWhite();

			//other candles
			} else {
				
				boolean createNewBox = false;
				
				//UP TREND
				if(upTrend) {
					//if going up, create new boxes
					if(candle.getClose()>currentBox.getValue2()) {
						createNewBox = true;
						
					} else {
						//consider the first box in same trend to decide for trend reversal. The distance is limited to N boxes.
						int fb = getFirstBoxInSameTrend(upTrend);
						fb = Math.min(fb, N);
						if(fb>0) {
							if(candle.getClose()<getLastBox(fb).getValue2()) {
								createNewBox = true;
								upTrend = false;
							}
						
						//if there are not enough boxes in same trend, consider the box bottom to reverse trend
						} else {
							if(candle.getClose()<currentBox.getValue1()) {
								createNewBox = true;
								upTrend = false;
							}
						}
					}
					
				//DOWN TREND
				} else {
					//if going down, create new boxes
					if(candle.getClose()<currentBox.getValue2()) {
						createNewBox = true;
						
					} else {
						//if there are more than N boxes with the same trend before this one, consider them to decide for reversal
						int fb = getFirstBoxInSameTrend(upTrend);
						fb = Math.min(fb, N);
						if(fb>0) {
							if(candle.getClose()>getLastBox(fb).getValue2()) {
								createNewBox = true;
								upTrend = true;
							}
							
						//if there are not enough boxes in same trend, consider the box top to reverse trend
						} else {
							if(candle.getClose()>currentBox.getValue1()) {
								createNewBox = true;
								upTrend = true;
							}
						}
					}
				}
				
				//CREATE A NEW BOX
				if(createNewBox) {
					Box newBox = new Box();
					if(upTrend) {
						newBox.setValue1(currentBox.getTop());
					} else {
						newBox.setValue1(currentBox.getBottom());
					}
					newBox.setValue2(candle.getClose());
					boxes.add(newBox);
					currentBox = newBox;
				}
			}
			map.put(candle, currentBox);
		}
	}
	
	/**
	 * Verify if there are a number (n) of boxes with the same trend before the current trend
	 * @param n
	 * @param upTrend
	 * @return
	 */
	public int getFirstBoxInSameTrend(boolean upTrend) {
		int result = -1;
		for(int i=0; i<boxes.size(); i++) {
			Box box = getLastBox(i);
			if(box!=null && box.isUpTrend()==upTrend) {
				result = i;
			} else {
				break;
			}
		}
		return result;
	}

	private Box getLastBox(int n) {
		int i = reverseIndex(boxes.size(), n);
		if(i<0) return null;
		return boxes.get(i);
	}
	
	public List<Box> getBoxes() {
		return boxes;
	}
	
	public Box getBox(Candle candle) {
		return map.get(candle);
	}
	
	public Box getLastCandleBox(int lastCandleNumber) {
		int i = reverseIndex(candles.size(), lastCandleNumber);
		if(i<0) return null;
		return map.get(candles.get(i));
	}

	private int reverseIndex(int size, int n) {
		return size-n-1;
	}
	
}
