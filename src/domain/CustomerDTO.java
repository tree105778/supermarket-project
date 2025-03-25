package domain;

public class CustomerDTO {
    private String userName;
    private int totalPay;
    private int userPoint;
    private String phoneNumber;

    public CustomerDTO(String userName, int totalPay, int userPoint, String phoneNumber) {
        this.userName = userName;
        this.totalPay = totalPay;
        this.userPoint = userPoint;
        this.phoneNumber = phoneNumber;
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
}
