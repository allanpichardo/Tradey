package com.imaginarymercenary.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

import com.imaginarymercenary.tradey.Tradey;
import com.imaginarymercenary.tradey.Tradey.OnDataReceivedListener;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class YahooHistorical {
	
	private JSONObject json;
	private String symbol;
	
	private OnDataReceivedListener listener;
	
	public YahooHistorical(OnDataReceivedListener listener, String symbol, String startDate, String endDate){
		this.listener = listener;
		this.symbol = symbol;
		try {
			download(symbol,startDate,endDate);
		} catch (IOException | InterruptedException | ExecutionException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setOnDataReceivedListener(OnDataReceivedListener listener){
		this.listener = listener;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	private void download(String symbol, String startDate, String endDate) throws IOException, InterruptedException, ExecutionException{
		
		String query = "select * from yahoo.finance.historicaldata where symbol = '"+symbol+
						"' and startDate = '"+startDate+"' and endDate = '"+endDate+"'";
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
		        	listener.onHistoricalDataReceived(YahooHistorical.this);
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
	
	public Day get(int position){
		JSONObject dayJson = getData().optJSONObject(position);
		Day day = new Day(dayJson);
		return day;
	}
	
	@Override
	public String toString() {
		return this.json.toString();
	}
	
	public class Day{
		
		private JSONObject json;
		
		public Day(JSONObject json){
			this.json = json;
		}
		
		public String getSymbol(){
			return this.json.optString("Symbol");
		}
		
		public Date getDate(){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String dateString = this.json.optString("Date");
			Date date = null;
			try {
				date = formatter.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return date;
		}
		
		public double getOpen(){
			return this.json.optDouble("Open");
		}
		
		public double getHigh(){
			return this.json.optDouble("High");
		}
		
		public double getLow(){
			return this.json.optDouble("Low");
		}
		
		public double getClose(){
			return this.json.optDouble("Close");
		}
		
		public double getVolume(){
			return this.json.optDouble("Volume");
		}
		
		public double getAdjustedClose(){
			return this.json.optDouble("Adj_Close");
		}
		@Override
		public String toString() {
			return this.json.toString();
		}
	}

}
