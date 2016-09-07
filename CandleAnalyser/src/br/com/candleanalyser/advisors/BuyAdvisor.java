package br.com.candleanalyser.advisors;

public interface BuyAdvisor extends Advisor {

	/**
	 * Value between -1 and 1 to indicate a possible buy
	 * -1 indicates WORST BUY
	 * 0 indicates NEUTRAL
	 * 1 indicates BEST BUY
	 * @return
	 */
	public float getBuyStrength();

	public void onBuy(float value);
	public void onSell(float close);
	
}
