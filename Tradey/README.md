Tradey
======

A Simple Yahoo Finance API for Java


Usage:

```java
public class Example implements OnDataReceivedListener(){

  public Example(){
    //simply use the static methods from Tradey to get
    
    //Historical data
    Tradey.getHistoricalData("MSFT","2012-01-01","2012-12-31",this);
    
    //Stats
    Tradey.getStats("MSFT", this);
    
    //Quotes
    String[] symbols = {"MSFT","AAPL","GOOG"};
    Tradey.getQuotes(symbols,this);
    
    //The data requests are asynchronous, so you must implement
    //the OnDataReceivedListener to use the information
  }
  
  @Override
	public void onStatsDataReceived(YahooStats stats) {
		//Do something
	}
			
	@Override
	public void onHistoricalDataReceived(YahooHistorical history) {
		//Do something
	}

	@Override
	public void onQuoteDataReceived(YahooQuote quote) {
		//Do something
	}
	
}
```
