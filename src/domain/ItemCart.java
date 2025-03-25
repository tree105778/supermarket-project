package domain;

public class ItemCart {
    private int productId;
    private int count;
    private int price;


    public ItemCart(int product_id, int count, int price) {
        this.productId = product_id;
        this.count = count;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
