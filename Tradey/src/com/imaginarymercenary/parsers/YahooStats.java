package com.imaginarymercenary.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.JSONObject;

import com.imaginarymercenary.tradey.Tradey;
import com.imaginarymercenary.tradey.Tradey.OnDataReceivedListener;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class YahooStats {

	private JSONObject json;
	private String symbol;

	private OnDataReceivedListener listener;

	public YahooStats(OnDataReceivedListener listener, String symbol){
		this.symbol = symbol;
		this.listener = listener;
		
		try {
			download(symbol);
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setOnDataReceivedListener(OnDataReceivedListener listener){
		this.listener = listener;
	}

	public String getSymbol(){
		return symbol;
	}

	private void download(String symbol) throws IOException, InterruptedException, ExecutionException{

		String query = "SELECT * FROM yahoo.finance.keystats WHERE symbol='"+symbol+"'";
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
							listener.onStatsDataReceived(YahooStats.this);
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
	
	private JSONObject getData(){
		return json.optJSONObject("query").optJSONObject("results").optJSONObject("stats");
	}
	
	public double getMarketCap(){
		return getData().optJSONObject("MarketCap").optDouble("content");
	}
	
	public double getEnterpriseValue(){
		return getData().optJSONObject("EnterpriseValue").optDouble("content");
	}
	
	public double getTrailingPE(){
		return getData().optJSONObject("TrailingPE").optDouble("content");
	}
	
	public double getForwardPE(){
		return getData().optJSONObject("ForwardPE").optDouble("content");
	}
	
	public String getPEGRatioTerm(){
		return getData().optJSONObject("PGERatio").optString("term");
	}
	
	public double getPEGRatio(){
		return getData().optJSONObject("PGERatio").optDouble("content");
	}
	
	public double getPriceSales(){
		return getData().optJSONObject("PriceSales").optDouble("content");
	}
	
	public double getPriceBook(){
		return getData().optJSONObject("PriceBook").optDouble("content");
	}
	
	public double getEnterpriseValueRevenue(){
		return getData().optJSONObject("EnterpriseValueRevenue").optDouble("content");
	}
	
	public double getEnterpriseValueEBITDA(){
		return getData().optJSONObject("EnterpriseValueEBITDA").optDouble("content");
	}
	
	public Date getMostRecentQuarter() throws ParseException{
		String d = getData().optJSONObject("MostRecentQuarter").optString("content");
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy",Locale.US);
		return formatter.parse(d);
	}
	
	public double getProfitMargin(){
		String percentage = getData().optJSONObject("ProfitMargin").optString("content");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getOperatingMargin(){
		String percentage = getData().optJSONObject("OperatingMargin").optString("content");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getReturnOnAssets(){
		String percentage = getData().optJSONObject("ReturnonAssets").optString("content");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getReturnOnEquity(){
		String percentage = getData().optJSONObject("ReturnonEquity").optString("content");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getRevenue(){
		return getData().optJSONObject("Revenue").optDouble("content");
	}
	
	public double getRevenuePerShare(){
		return getData().optJSONObject("RevenuePerShare").optDouble("content");
	}
	
	public double getQuarterlyRevenueGrowth(){
		String percentage = getData().optJSONObject("QtrlyRevenueGrowth").optString("content");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getGrossProfit(){
		return getData().optJSONObject("GrossProfit").optDouble("content");
	}
	
	public double getEBITDA(){
		return getData().optJSONObject("EBITDA").optDouble("content");
	}
	
	public double getNetIncomeAvailableToCommon(){
		return getData().optJSONObject("NetIncomeAvltoCommon").optDouble("content");
	}
	
	public double getDilutedEPS(){
		return getData().optJSONObject("DilutedEPS").optDouble("content");
	}
	
	public double getQuarterlyEarningsGrowth(){
		String percentage = getData().optJSONObject("QtrlyEarningsGrowth").optString("content");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getTotalCash(){
		return getData().optJSONObject("TotalCash").optDouble("content");
	}
	
	public double getTotalCashPerShare(){
		return getData().optJSONObject("TotalCashPerShare").optDouble("content");
	}
	
	public double getTotalDebt(){
		return getData().optJSONObject("TotalDebt").optDouble("content");
	}
	
	public double getTotalDebtEquity(){
		return getData().optJSONObject("TotalDebtEquity").optDouble("content");
	}
	
	public double getCurrentRatio(){
		return getData().optJSONObject("CurrentRatio").optDouble("content");
	}
	
	public double getBookValuePerShare(){
		return getData().optJSONObject("BookValuePerShare").optDouble("content");
	}
	
	public double getOperatingCashFlow(){
		return getData().optJSONObject("OperatingCashFlow").optDouble("content");
	}
	
	public double getLeveredFreeCashFlow(){
		return getData().optJSONObject("LeveredFreeCashFlow").optDouble("content");
	}
	
	public double getBeta(){
		return getData().optDouble("Beta");
	}
	
	public double get52WeekChange(){
		String percentage = getData().optString("p_52_WeekChange");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double get52WeekHigh(){
		return getData().optJSONObject("p_52_WeekHigh").optDouble("content");
	}
	
	public double get52WeekLow(){
		return getData().optJSONObject("p_52_WeekLow").optDouble("content");
	}
	
	public double getSMA50(){
		return getData().optDouble("p_50_DayMovingAverage");
	}
	
	public double getSMA200(){
		return getData().optDouble("p_200_DayMovingAverage");
	}
	
	public double getSharesOutstanding(){
		return getData().optDouble("SharesOutstanding");
	}
	
	public double getFloat(){
		return getData().optDouble("Float");
	}
	
	public double getPercentageHeldByInsiders(){
		String percentage = getData().optString("PercentageHeldbyInsiders");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getPercentageHeldByInstitutions(){
		String percentage = getData().optString("PercentageHeldbyInstitutions");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getForwardAnnualDividendRate(){
		String percentage = getData().optString("ForwardAnnualDividendRate");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getForwardAnnualDividendYield(){
		String percentage = getData().optString("ForwardAnnualDividendYield");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double get5YearAverageDividendYield(){
		String percentage = getData().optString("p_5YearAverageDividendYield");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public double getPayoutRatio(){
		String percentage = getData().optString("PayoutRatio");
		BigDecimal d = new BigDecimal(percentage.trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		return d.doubleValue();
	}
	
	public Date getDividendDate() throws ParseException{
		String d = getData().optString("DividendDate");
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy",Locale.US);
		return formatter.parse(d);
	}
	
	public Date getExDividendDate() throws ParseException{
		String d = getData().optString("Ex_DividendDate");
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy",Locale.US);
		return formatter.parse(d);
	}
		

}
