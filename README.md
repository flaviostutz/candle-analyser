# candle-analyser
This project uses stock candle figure analysis in order to determine when to buy or sell stocks on the Market. It comes with a simulator and a genetics algorithms tool in order to define the best approach for a particular stock. 

No, I didn't get rich with that!

## Approach
It was 2007 when a friend of mine came with that thing of CandleSticks (http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:introduction_to_candlesticks) and I got crazy. 

This software performs matches for candle figures over historical stock prices indicating when to buy or sell a particular item as seen on theory. After some tests, I realized that the scanning parameters are different from stock to stock, so I added a genetics algorithm to the system so that it changes those parameters, simulates performance over time (better gains overall) and gathers the best combination of strategies (candle match parameters, validity of a particular theory for the paper etc) for the stock.

## Findings
Overfitting is a real problem, but It was very fun to discover some sets that in a particular period of time would get 100% gain when the same period had -10% from the last to the first price.

Have fun!

## Next
Maybe in the future I would add other graphing or fundamental data for the genetics/simulator to have more variability.
