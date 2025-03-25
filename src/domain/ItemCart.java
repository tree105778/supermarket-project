package domain;

public class ItemCart {
    private int product_id;
    private int count;
    private int price;

    public ItemCart(int product_id, int count, int price) {
        this.product_id = product_id;
        this.count = count;
        this.price = price;
    }

}
