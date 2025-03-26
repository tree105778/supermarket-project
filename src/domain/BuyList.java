package domain;

import java.util.List;

public class BuyList {
    private int buyId;
    private int productId;
    private int count;
    private int refundCount;
    private Product product;

    public BuyList(int buyId, int productId, int count, int refundCount) {
        this.buyId = buyId;
        this.productId = productId;
        this.count = count;
        this.refundCount = refundCount;
    }

    public int getBuyId() {
        return buyId;
    }

    public void setBuyId(int buyId) {
        this.buyId = buyId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }
}
