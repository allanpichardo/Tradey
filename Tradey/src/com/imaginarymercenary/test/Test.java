package com.imaginarymercenary.test;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.imaginarymercenary.parsers.YahooHistorical;
import com.imaginarymercenary.parsers.YahooHistorical.Day;
import com.imaginarymercenary.parsers.YahooQuote;
import com.imaginarymercenary.parsers.YahooStats;
import com.imaginarymercenary.tradey.Tradey;
import com.imaginarymercenary.tradey.Tradey.OnDataReceivedListener;

public class Test{
	
	public static void main(String[] args){
		
		System.out.println("Starting test:");
		
		
		Tradey.getHistoricalData("MSFT","2012-01-01","2012-12-31",new OnDataReceivedListener() {
			
			@Override
			public void onHistoricalDataReceived(YahooHistorical history) {
				System.out.println();
				System.out.println(history.getSize() + " entries received");
				System.out.println("Generating statistics");
				
				System.out.print(".");
				// Get a DescriptiveStatistics instance
				DescriptiveStatistics stats = new DescriptiveStatistics();

				// Add the data from the array
				for( int i = 0; i < history.getSize(); i++) {
					Day day = history.get(i);
				    stats.addValue(day.getAdjustedClose());
				    
				    System.out.print(".");
				}
				
				System.out.println();

				// Compute some statistics
				double mean = stats.getMean();
				double std = stats.getStandardDeviation();
				double median = stats.getPercentile(50);
				double variance = stats.getVariance();
				
				System.out.println("Mean: "+mean);
				System.out.println("Std Dev: "+std);
				System.out.println("Median: "+median);
				System.out.println("Variance: "+variance);
			}

			@Override
			public void onStatsDataReceived(YahooStats stats) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onQuoteDataReceived(YahooQuote quote) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Tradey.getStats("MSFT", new OnDataReceivedListener() {
			
			@Override
			public void onStatsDataReceived(YahooStats stats) {
				System.out.println("Stats for "+stats.getSymbol());
				System.out.println("Beta: "+stats.getBeta());
				System.out.println("sma 50: "+stats.getSMA50());
				System.out.println("sma 200: "+stats.getSMA200());
			}
			
			@Override
			public void onHistoricalDataReceived(YahooHistorical history) {
				
			}

			@Override
			public void onQuoteDataReceived(YahooQuote quote) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
