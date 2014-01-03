package com.imaginarymercenary.tradey;

import com.imaginarymercenary.parsers.YahooHistorical;
import com.imaginarymercenary.parsers.YahooQuote;
import com.imaginarymercenary.parsers.YahooStats;


public class Tradey {
	
	public static final String YAHOO_URL_BASE = "http://query.yahooapis.com/v1/public/yql?q=";
	public static final String YAHOO_URL_END = "&format=json&diagnostics=false&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	
	
	public static void getHistoricalData(String symbol, String startDate, String endDate,OnDataReceivedListener listener){
		new YahooHistorical(listener,symbol,startDate,endDate);
	}
	
	public static void getStats(String symbol, OnDataReceivedListener listener){
		new YahooStats(listener,symbol);
	}
	
	public static void getQuotes(String[] symbols, OnDataReceivedListener listener){
		new YahooQuote(listener, symbols);
	}
	
	public interface OnDataReceivedListener{
		public void onHistoricalDataReceived(YahooHistorical history);
		public void onStatsDataReceived(YahooStats stats);
		public void onQuoteDataReceived(YahooQuote quotes);
	}

}
