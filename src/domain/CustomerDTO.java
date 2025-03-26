package domain;

public class CustomerDTO {
    private int userId;
    private String userName;
    private String phoneNumber;
    private int totalPay;
    private int userPoint;

    @Override
    public String toString() {
        return "회원 이름: " + userName + '\'' +
                ", 휴대폰번호: " + phoneNumber;
    }

    public CustomerDTO(String userName, int totalPay, int userPoint, String phoneNumber) {
        this.userName = userName;
        this.totalPay = totalPay;
        this.userPoint = userPoint;
        this.phoneNumber = phoneNumber;
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
        return totalPay/100;
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
