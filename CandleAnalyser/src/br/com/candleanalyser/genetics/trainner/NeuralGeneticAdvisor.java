package br.com.candleanalyser.genetics.trainner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.candleanalyser.advisors.BuyAdvisor;
import br.com.candleanalyser.advisors.CandleIndicatorAdvisor;
import br.com.candleanalyser.advisors.MACDAdvisor;
import br.com.candleanalyser.advisors.RSIAdvisor;
import br.com.candleanalyser.advisors.SellAdvisor;
import br.com.candleanalyser.advisors.StochRSIAdvisor;
import br.com.candleanalyser.advisors.TrendChannelAdvisor;
import br.com.candleanalyser.engine.Candle;
import br.com.candleanalyser.engine.Helper;
import br.com.candleanalyser.genetics.Gene;
import br.com.candleanalyser.genetics.GeneticAdvisor;

public class NeuralGeneticAdvisor extends GeneticAdvisor {

	private static final long serialVersionUID = 1L;

	transient private List<BuyAdvisor> buyAdvisors;
	transient private Neuron buyNeuron;

	transient private List<SellAdvisor> sellAdvisors;
	transient private Neuron sellNeuron;

	transient private String info;
	
	/**
	 * @param buyThreshold
	 *            Value between 0 and 1 to be used as a minimum value in neuron
	 *            for a positive buy
	 * @param sellThreshold
	 *            Value between 0 and 1 to be used as a minimum value in neuron
	 *            for a positive sell
	 */
	public NeuralGeneticAdvisor() {
		super();
		setupAdvisors();
	}

	public NeuralGeneticAdvisor(GeneticAdvisor advisor1, GeneticAdvisor advisor2, float mutationRate) {
		super(advisor1, advisor2, mutationRate);
		setupAdvisors();
	}

	protected void setupAdvisors() {
		// buy advisors
		buyAdvisors = new ArrayList<BuyAdvisor>();
		buyNeuron = new Neuron();

//		//RSI
//		BuyAdvisor rsiBuyAdvisor = new RSIAdvisor((int) getGenetics().getValue(Gene.RSI_ADVISOR_BUY_NR_CANDLES));
//		buyAdvisors.add(rsiBuyAdvisor);
//		buyNeuron.addInput(rsiBuyAdvisor, getGenetics().getValue(Gene.RSI_ADVISOR_BUY_WEIGHT));
//
//		//STOCH RSI
//		BuyAdvisor stochRsiBuyAdvisor = new StochRSIAdvisor((int) getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_BUY_NR_CANDLES), getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_BUY_DIVERGENCE_WEIGHT), getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_BUY_RSI_VALUE_WEIGHT));
//		buyAdvisors.add(stochRsiBuyAdvisor);
//		buyNeuron.addInput(stochRsiBuyAdvisor, getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_BUY_WEIGHT));
//
//		//CHANNEL TREND
//		BuyAdvisor channelBuyAdvisor = new TrendChannelAdvisor((int) getGenetics().getValue(Gene.CHANNEL_ADVISOR_BUY_NR_CANDLES), getGenetics().getValue(Gene.CHANNEL_ADVISOR_BUY_CONTAINED_CANDLES_RATIO));
//		buyAdvisors.add(channelBuyAdvisor);
//		buyNeuron.addInput(channelBuyAdvisor, getGenetics().getValue(Gene.CHANNEL_ADVISOR_BUY_WEIGHT));
//
//		//CANDLE INDICATOR
//		BuyAdvisor candleIndicatorBuyAdvisor = new CandleIndicatorAdvisor();
//		buyAdvisors.add(candleIndicatorBuyAdvisor);
//		buyNeuron.addInput(candleIndicatorBuyAdvisor, getGenetics().getValue(Gene.CANDLE_INDICATOR_BUY_WEIGHT));
//
//		//MACD
//		BuyAdvisor macdBuyAdvisor = new MACDAdvisor((int)getGenetics().getValue(Gene.MACD_ADVISOR_BUY_MACD_EMA), (int)getGenetics().getValue(Gene.MACD_ADVISOR_BUY_FASTER_EMA), (int)getGenetics().getValue(Gene.MACD_ADVISOR_BUY_SLOWER_EMA));
//		buyAdvisors.add(macdBuyAdvisor);
//		buyNeuron.addInput(macdBuyAdvisor, getGenetics().getValue(Gene.MACD_ADVISOR_BUY_WEIGHT));
//
//		// sell advisors
//		sellAdvisors = new ArrayList<SellAdvisor>();
//		sellNeuron = new Neuron();
//
//		//RSI
//		SellAdvisor rsiSellAdvisor = new RSIAdvisor((int) getGenetics().getValue(Gene.RSI_ADVISOR_SELL_NR_CANDLES));
//		sellAdvisors.add(rsiSellAdvisor);
//		sellNeuron.addInput(rsiSellAdvisor, getGenetics().getValue(Gene.RSI_ADVISOR_SELL_WEIGHT));
//
//		//STOCH RSI
//		SellAdvisor stochRsiSellAdvisor = new StochRSIAdvisor((int) getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_SELL_NR_CANDLES), getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_SELL_DIVERGENCE_WEIGHT), getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_SELL_RSI_VALUE_WEIGHT));
//		sellAdvisors.add(stochRsiSellAdvisor);
//		sellNeuron.addInput(rsiSellAdvisor, getGenetics().getValue(Gene.STOCH_RSI_ADVISOR_SELL_WEIGHT));
//
//		//CHANNEL TREND
//		SellAdvisor channelSellAdvisor = new TrendChannelAdvisor((int) getGenetics().getValue(Gene.CHANNEL_ADVISOR_SELL_NR_CANDLES), getGenetics().getValue(Gene.CHANNEL_ADVISOR_SELL_CONTAINED_CANDLES_RATIO));
//		sellAdvisors.add(channelSellAdvisor);
//		sellNeuron.addInput(channelSellAdvisor, getGenetics().getValue(Gene.CHANNEL_ADVISOR_SELL_WEIGHT));
//
//		//CANDLE INDICATOR
//		SellAdvisor candleIndicatorSellAdvisor = new CandleIndicatorAdvisor();
//		sellAdvisors.add(candleIndicatorSellAdvisor);
//		sellNeuron.addInput(candleIndicatorSellAdvisor, getGenetics().getValue(Gene.CANDLE_INDICATOR_SELL_WEIGHT));
//		
//		//MACD
//		SellAdvisor macdSellAdvisor = new MACDAdvisor((int)getGenetics().getValue(Gene.MACD_ADVISOR_SELL_MACD_EMA), (int)getGenetics().getValue(Gene.MACD_ADVISOR_SELL_FASTER_EMA), (int)getGenetics().getValue(Gene.MACD_ADVISOR_SELL_SLOWER_EMA));
//		sellAdvisors.add(macdSellAdvisor);
//		sellNeuron.addInput(macdSellAdvisor, getGenetics().getValue(Gene.MACD_ADVISOR_SELL_WEIGHT));

	}

	@Override
	public void nextCandle(Candle candle) {
		for (BuyAdvisor buyAdvisor : buyAdvisors) {
			buyAdvisor.nextCandle(candle);
		}
		for (SellAdvisor sellAdvisor : sellAdvisors) {
			sellAdvisor.nextCandle(candle);
		}
	}

	@Override
	public float getBuyStrength() {
		info = "";
		for (BuyAdvisor buyAdvisor : buyAdvisors) {
			buyNeuron.setInputValue(buyAdvisor, buyAdvisor.getBuyStrength());
			info += buyAdvisor.getClass().getSimpleName() + ": " + (buyAdvisor.getCurrentInfo() != null ? buyAdvisor.getCurrentInfo() : "buy") + " (" + Helper.formatNumber(buyAdvisor.getBuyStrength()) + ")\n";
		}
		return buyNeuron.getOutputValue();
	}

	@Override
	public float getSellStrength() {
		info = "";
		for (SellAdvisor sellAdvisor : sellAdvisors) {
			sellNeuron.setInputValue(sellAdvisor, sellAdvisor.getSellStrength());
			info += sellAdvisor.getClass().getSimpleName() + ": " + (sellAdvisor.getCurrentInfo() != null ? sellAdvisor.getCurrentInfo() : "sell") + " (" + Helper.formatNumber(sellAdvisor.getSellStrength()) + ")\n";
		}
		return sellNeuron.getOutputValue();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject(); // Read the array normally.
		setupAdvisors();
	}

	@Override
	public String getCurrentInfo() {
		return info;
	}

	@Override
	public void onBuy(float value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSell(float close) {
		// TODO Auto-generated method stub
		
	}

}
