public class ZappProduct {
	private String productId, productName, defaultProductUrl, defaultImageUrl, styleId, price, productUrl, imageUrl, thumbnailImageUrl;

	public ZappProduct() {
		this.productId = "";
		this.productName = "";
		this.defaultProductUrl = "";
		this.defaultImageUrl = "";
		this.styleId = "";
		this.price = "";
		this.productUrl = "";
		this.imageUrl = "";
		this.thumbnailImageUrl = "";
	}

	// Product Name
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	// Default Product URL
	public String getDefaultProductUrl() {
		return defaultProductUrl;
	}

	public void setDefaultProductUrl(String defaultProductUrl) {
		this.defaultProductUrl = defaultProductUrl;
	}

	// Default Image URL
	public String getDefaultImageUrl() {
		return defaultImageUrl;
	}

	public void setDefaultImageUrl(String defaultImageUrl) {
		this.defaultImageUrl = defaultImageUrl;
	}

	// Product ID
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	// Price
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	// Style ID
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	// Product URL
	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	// Image URL
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	// Thumbnail Image URL
	public String getThumbnailImageUrl() {
		return thumbnailImageUrl;
	}

	public void setThumbnailImageUrl(String thumbnailImageUrl) {
		this.thumbnailImageUrl = thumbnailImageUrl;
	}
}