package domain;

public class Product {
    private int productId;
    private String productName;
    private int price;
    private int stock;
    private int categoryId;
    private boolean active;

    public Product(int productId, String productName, int price, int stock, int categoryId, boolean active) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.active = active;
    }

    @Override
    public String toString() {
        return "### 제품번호: " + productId +
                ", 제품 이름: " + productName +
                ", 제품 가격: " + price + "원"+
                ", 재고 개수: " + stock + "개" +
                ", 카테고리 번호: " + categoryId ;
    }

    public String toString2() {
        return "### 제품번호: " + productId +
                ", 제품 이름: " + productName +
                ", 제품 가격: " + price + "원"+
                ", 재고 개수: " + stock + "개" +
                ", 카테고리 번호: " + categoryId +
                ", 삭제 여부: " + active;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
