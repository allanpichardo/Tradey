package com.imaginarymercenary.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

import com.imaginarymercenary.tradey.Tradey;
import com.imaginarymercenary.tradey.Tradey.OnDataReceivedListener;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class YahooQuote {

	private JSONObject json;

	private OnDataReceivedListener listener;

	public YahooQuote(OnDataReceivedListener listener, String[] symbols){
		this.listener = listener;
		try {
			download(symbols);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setOnDataReceivedListener(OnDataReceivedListener listener){
		this.listener = listener;
	}

	private void download(String[] symbols) throws IOException, InterruptedException, ExecutionException{

		String query = "select * from yahoo.finance.quote where symbol in (";
		for(int i = 0; i < symbols.length; ++i){
			query += "'"+symbols[i]+"'";
			query += (i < symbols.length-1) ? ", " : "";
		}
		query += ")";
		
		try {
			query = URLEncoder.encode(query, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String url = Tradey.YAHOO_URL_BASE + query + Tradey.YAHOO_URL_END;

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		Future<Integer> f = asyncHttpClient.prepareGet(url).execute(
				new AsyncCompletionHandler<Integer>(){

					@Override
					public Integer onCompleted(Response response) throws Exception{
						String body = response.getResponseBody();
						json = new JSONObject(body);
						if(listener != null){
							listener.onQuoteDataReceived(YahooQuote.this);
						}
						return response.getStatusCode();
					}

					@Override
					public void onThrowable(Throwable t){
						// Something wrong happened.
					}
				});

		int statusCode = f.get();
	}

	private JSONArray getData(){
		return this.json.optJSONObject("query").optJSONObject("results").optJSONArray("quote");
	}
	
	public int getSize(){
		return getData().length();
	}
	
	public QuoteData get(int position){
		JSONObject j = getData().optJSONObject(position);
		return new QuoteData(j);
	}
	
	@Override
	public String toString() {
		return this.json.toString();
	}
	
	public class QuoteData{
		
		private JSONObject json;
		
		public QuoteData(JSONObject json){
			this.json = json;
		}
		
		public String getSymbol(){
			return this.json.optString("Symbol");
		}
		
		public double getAverageDailyVolume(){
			return this.json.optDouble("AverageDailyVolume");
		}
		
		public double getChange(){
			String c = this.json.optString("Change");
			c = c.replace("+", "");
			c.trim();
			return Double.parseDouble(c);
		}
		
		public double getDaysLow(){
			return this.json.optDouble("DaysLow");
		}
		
		public double getDaysHigh(){
			return this.json.optDouble("DaysHigh");
		}
		
		public double getYearLow(){
			return this.json.optDouble("YearLow");
		}
		
		public double getYearHigh(){
			return this.json.optDouble("YearHigh");
		}
		
		public String getMarkerCapitalization(){
			return this.json.optString("MarketCapitalization");
		}
		
		public double getLastTradePrice(){
			return this.json.optDouble("LastTradePriceOnly");
		}
		
		public String getDaysRange(){
			return this.json.optString("DaysRange");
		}
		
		public String getName(){
			return this.json.optString("Name");
		}
		
		public double getVolume(){
			return this.json.optDouble("Volume");
		}
		
		public String getStockExchange(){
			return this.json.optString("NasdaqNM");
		}
		
		@Override
		public String toString() {
			return this.json.toString();
		}
	}

}
