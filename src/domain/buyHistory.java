package domain;

import java.time.LocalDateTime;
import java.util.List;

public class buyHistory {
    private int buyId;
    private LocalDateTime buyTime;
    private Customer user;
    private int totalPrice;
    private List<buyList> buylist;

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

    public Customer getUser() {
        return user;
    }

    public void setUser(Customer user) {
        this.user = user;
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
                ", 고객ID: " + user.getUserId() +
                ", 구매 시각: " + buyTime;
    }
}
