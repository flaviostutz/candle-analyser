package br.com.candleanalyser.advisors;

public interface SellAdvisor extends Advisor {

	/**
	 * Value between -1 and 1 to indicate a possible sell
	 * -1 indicates WORST SELL
	 * 0 indicates NEUTRAL
	 * 1 indicates BEST SELL
	 * @return
	 */
	public float getSellStrength();
	
	public void onSell(float value);
	public void onBuy(float close);

}
