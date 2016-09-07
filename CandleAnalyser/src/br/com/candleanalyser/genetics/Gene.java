package br.com.candleanalyser.genetics;

public enum Gene {

	BUY_ADVISOR(0, 0, true),
	SELL_ADVISOR(0, 0, true),
	
	RSI_ADVISOR_BUY_WEIGHT(0, 0),
	RSI_ADVISOR_BUY_NR_CANDLES(14, 14),
	
	RSI_ADVISOR_SELL_WEIGHT(0, 0),
	RSI_ADVISOR_SELL_NR_CANDLES(14, 14),
	
	STOCH_RSI_ADVISOR_BUY_WEIGHT(1, 1),
	STOCH_RSI_ADVISOR_BUY_NR_CANDLES(14, 14),
	STOCH_RSI_ADVISOR_BUY_DIVERGENCE_WEIGHT(0.5F, 0.5F),
	STOCH_RSI_ADVISOR_BUY_RSI_VALUE_WEIGHT(0.5F, 0.5F),
	
	STOCH_RSI_ADVISOR_SELL_WEIGHT(1, 1),
	STOCH_RSI_ADVISOR_SELL_NR_CANDLES(14, 14),
	STOCH_RSI_ADVISOR_SELL_DIVERGENCE_WEIGHT(0.5F, 0.5F),
	STOCH_RSI_ADVISOR_SELL_RSI_VALUE_WEIGHT(0.5F, 0.5F),
	
	CHANNEL_ADVISOR_BUY_WEIGHT(0, 0),
	CHANNEL_ADVISOR_BUY_NR_CANDLES(4, 360),
	CHANNEL_ADVISOR_BUY_CONTAINED_CANDLES_RATIO(0, 1),

	CHANNEL_ADVISOR_SELL_WEIGHT(0, 0),
	CHANNEL_ADVISOR_SELL_NR_CANDLES(4, 360),
	CHANNEL_ADVISOR_SELL_CONTAINED_CANDLES_RATIO(0, 1),

	CANDLE_INDICATOR_BUY_WEIGHT(0, 0),
	CANDLE_INDICATOR_SELL_WEIGHT(0, 0),
	
	MACD_ADVISOR_SELL_WEIGHT(0, 0),
	MACD_ADVISOR_SELL_MACD_EMA(9, 9),
	MACD_ADVISOR_SELL_FASTER_EMA(12, 12),
	MACD_ADVISOR_SELL_SLOWER_EMA(26, 26),

	MACD_ADVISOR_BUY_WEIGHT(0, 0),
	MACD_ADVISOR_BUY_MACD_EMA(9, 9),
	MACD_ADVISOR_BUY_FASTER_EMA(12, 12),
	MACD_ADVISOR_BUY_SLOWER_EMA(26, 26);

	private float minValue;
	private float maxValue;
	private String name;
	private boolean isInteger;

	private Gene(float minValue, float maxValue) {
		this(minValue, maxValue, false);
	}
	private Gene(float minValue, float maxValue, boolean isInteger) {
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.isInteger = isInteger;
	}

	public float getValue(float raw) {
		if(!isInteger) {
			return minValue + raw*(maxValue-minValue);
		} else {
			return getIntegerValue(raw);
		}
	}

	private int getIntegerValue(float raw) {
		return (int)(minValue + raw*(maxValue-minValue+0.99F));
	}
	
	public float getMinValue() {
		return minValue;
	}
	public float getMaxValue() {
		return maxValue;
	}
	
	public String getName() {
		return name;
	}

}
