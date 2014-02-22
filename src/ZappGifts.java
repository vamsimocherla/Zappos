import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;


public class ZappGifts {

	private static final String API_SEARCH = "http://api.zappos.com/Search";
	private static final String TERM = "?term=\"gift\"";
	private static final String LIMIT = "&limit=100";
	private static final String INCLUDES = "&includes=[\"onSale\"]";
	private static final String FILTERS = "&filters={\"onSale\":[\"true\"]}";
	private static final String SORT = "&sort={\"goLiveDate\":\"desc\"}";
	// API_KEY is the API key
	// it is omitted here for security reasons
	private static final String API_KEY = "&key=<YOUR_API_KEY_GOES_HERE>";

	private static int numTotalProducts = 100;
	private static ZappProduct[] productList = new ZappProduct[numTotalProducts];
	private static int numResults = 5;
	
	public static void main(String[] args) {
		// get the list of products
		productList = getProductList();
		new ZappGui();		
	}

	private static String httpGet(String urlStr) throws IOException {

		URL url = new URL(urlStr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// keep track of the number of API calls
		System.out.println("Number of API Calls made today: "
				+ String.valueOf(2500-Integer.parseInt((connection.getHeaderField("X-RateLimit-Long-RateRemaining")))));
		System.out.println("Number of API Calls remaining: "
				+ connection.getHeaderField("X-RateLimit-Long-RateRemaining"));
		
		if (connection.getResponseCode() != 200) {
			throw new IOException(connection.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder responseString = new StringBuilder();

		String line;
		while ((line = rd.readLine()) != null) 
			responseString.append(line);
		rd.close();

		connection.disconnect();
		return responseString.toString();
	}

	public static ZappProduct[] getProductList() {
		String searchURL = API_SEARCH + TERM + INCLUDES + FILTERS + SORT + LIMIT + API_KEY;
		
		Gson gson = new Gson();
		ZappProduct[] products = new ZappProduct[numTotalProducts];
		
		try {
			// get HTTP response from the Search URL
			String response = httpGet(searchURL);
			
			// parse the HTTP response
			ZappSearch zappResponse = gson.fromJson(response, ZappSearch.class);
			
			// get the product list from search results
			products = zappResponse.getProducts();
		}
		catch (IOException exc) {
			System.out.println("[ERROR]" + exc.toString());
		}
		return products;
	}
	
	// Find the subsets with exactly Quantity elements whose Sum is closest to Cost
	public static ZappProduct[][] searchForGifts(int quantity, double cost) {
		long lStartTime = new Date().getTime();
		
		// multiply the values with 100 to take care of the decimal places
		int sum = (int)Math.round(cost * 100);
		int N = numTotalProducts;
		int K = quantity;
		ZappProduct[][] results = new ZappProduct[numResults][K];
		
		// initialize the DP array
		boolean[][][] dp = new boolean[N][sum+1][K+1];
		
		// dp[i][m][l] is true if a sum of 'm' can be formed from the
		// first 'i' elements in the array using exactly 'l' elements
		for(int i=0;i<N;i++) {
			for(int m=0;m<=sum;m++) {
				for(int l=0;l<=K;l++) {
					dp[i][m][l] = false;
				}
			}
		}
		for(int i=0;i<N;i++) {
			// zero sum can be formed by selecting no elements
			dp[i][0][0] = true;
		}

		// a sum of arr[0] can be formed from the first (0+1) elements
		// using exactly 1 element -- the first element
		int val = 0;
		// multiply the values with 100 to take care of the decimal places
		val = (int) Math.round(Double.parseDouble(productList[0].getPrice().substring(1)) * 100);
		if(val <= sum) {
			dp[0][val][1] = true;
		}

		for(int i=1;i<N;i++) {
			for(int m=1;m<=sum;m++)	{
				for(int l=1;l<=K;l++) {
					// dp[i][m][l] is true if dp[i-1][m][l] is true
					// as if a sum of 'm' can be formed from the first 'i-1' elements
					// it can be formed from the first 'i' elements as well
					dp[i][m][l] = dp[i-1][m][l];
					
					// multiply the values with 100 to take care of the decimal places
					val = (int) Math.round(Double.parseDouble(productList[i].getPrice().substring(1)) * 100);
					if(m >= val) {
						// dp[i][m][l] is true if dp[i-1][m-val][l-1] is true
						// as val can be added to the subset to form a sum of 'm'
						dp[i][m][l] = dp[i][m][l] || dp[i-1][m-val][l-1];
					}
				}
			}
		}

		int count = 0;
		for(int m=sum;m>=0;m--) {
			if(count == numResults)
				break;
			double totalCost = 0;
			int j = m;
			int k = K;
			boolean flag = false;
			for(int i=N-1;i>0;i--) {
				// print all sums formed from the first i elements
				// using exactly K elements
				val = (int) Math.round(Double.parseDouble(productList[i].getPrice().substring(1)) * 100);
				if(j>0 && dp[i][j][k] && !dp[i-1][j][k]) {
					System.out.println(productList[i].getPrice() + " - " + productList[i].getProductName());
					results[count][K-k] = productList[i];
					j -= val;
					k--;
					flag = true;
					totalCost += Double.parseDouble(productList[i].getPrice().substring(1));
				}
			}
			if(j>0 && dp[0][j][k]) {
				System.out.println(productList[0].getPrice() + " - " + productList[0].getProductName());
				results[count][K-k] = productList[0];
				flag = true;
				totalCost += Double.parseDouble(productList[0].getPrice().substring(1));
			}
			if(flag) {
				System.out.println("Total Cost of the products = $" + totalCost);
				System.out.println("------------------------------------");
				count++;
			}
		}
		
		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		System.out.println("Elapsed milliseconds: " + difference);
		
		return results;
	}
}
