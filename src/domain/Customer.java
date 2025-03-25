package domain;

public class Customer {
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PW = "admin";
    private int userId;
    private String userName;
    private int totalPay;
    private int userPoint;
    private String phoneNumber;
    private boolean active;
    private String userPw;

    public Customer(int userId, String userName, int totalPay, int userPoint, String phoneNumber, boolean active, String userPw) {
        this.userId = userId;
        this.userName = userName;
        this.totalPay = totalPay;
        this.userPoint = userPoint;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.userPw = userPw;
    }

    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }
}