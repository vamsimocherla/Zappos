public class ZappSearch {

	private String statusCode;
	private ZappProduct[] results;

	public ZappSearch(String statusCode, ZappProduct[] results) {
		this.statusCode = statusCode;
		this.results = results;
	}

	// Status Code
	public String getStatusCode() {
		return statusCode;
	}
	
	// Product List
	public ZappProduct[] getProducts() {
		return results;
	}
}