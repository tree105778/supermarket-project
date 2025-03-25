package domain;

import java.time.LocalDateTime;
import java.util.List;

public class BuyHistory {
    private int buyId;
    private LocalDateTime buyTime;
    private int userId;
    private int totalPrice;
    private List<BuyList> buylist;

    public BuyHistory(int buyId, LocalDateTime buyTime, int userId, int totalPrice) {
        this.buyId = buyId;
        this.buyTime = buyTime;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public int getBuyId() {
        return buyId;
    }

    public void setBuyId(int buyId) {
        this.buyId = buyId;
    }

    public LocalDateTime getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(LocalDateTime buyTime) {
        this.buyTime = buyTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
         return "구매번호: " + buyId +
                ", 총 구매 금액: " + totalPrice +
                ", 고객ID: " + userId +
                ", 구매 시각: " + buyTime;
    }
}
