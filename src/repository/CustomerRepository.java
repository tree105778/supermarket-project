package repository;

import domain.Customer;
import domain.CustomerDTO;
import jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    // 회원 추가 기능
    public void addUser(String userName, String phoneNumber, String userPw) {
        String sql = "INSERT INTO CUSTOMER " +
                "(USER_ID, USER_NAME, PHONE_NUMBER, USER_PW) VALUES " +
                "(CUSTOMER_SEQ.NEXTVAL, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, userPw);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 회원 삭제 로직
    public void deleteUser(String username, String phNum, String userPw) {
        String sql = "UPDATE CUSTOMER SET ACTIVE = 'N' WHERE USER_NAME = ? AND PHONE_NUMBER = ? AND USER_PW = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, phNum);
            pstmt.setString(3, userPw);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("탈퇴에 실패했습니다!");
        }
    }

    // 전체 회원 조회 로직(관리자 전용)
    public List<Customer> showAllUsers() {
        String sql = "SELECT * FROM CUSTOMER";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                boolean active = rs.getString("active").equals("Y");
                Customer customer = new Customer(rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getInt("total_pay"),
                        rs.getInt("user_point"),
                        rs.getString("phone_number"),
                        active,
                        rs.getString("user_pw")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    // 특정 이름으로 고객을 조회해서 DTO 객체 List로 반환
    public List<CustomerDTO> searchByUserName(String username) {
        String sql = "SELECT * FROM CUSTOMER WHERE ACTIVE = 'Y' AND user_name like ?";
        List<CustomerDTO> customers = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + username + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CustomerDTO customer = new CustomerDTO(rs.getString("user_name"),
                        rs.getInt("total_pay"),
                        rs.getInt("user_point"),
                        rs.getString("phone_number")
                );
                customer.setUserId(rs.getInt("user_id"));
                customer.setUserId(rs.getInt("user_id"));
                customers.add(customer);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }


    // buy_history 객체에 Customer가 있어서 user_id를 바탕으로 Customer를 리턴해주는 메소드 추가 작성
    // buy_history 객체에 Customer가 아니라 user_id를 가지게 되면, 필요없어지는 메소드
    public Customer getCustomerByUserId(int user_id) {
        // 리턴한 Customer 객체 임의로 작성
        Customer user = new Customer(0, "", 0,
                0, "", false, "");
        String sql = "SELECT * FROM customer WHERE user_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                user.setUserId(user_id);
                user.setUserName(rs.getString("user_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setTotalPay(rs.getInt("total_pay"));
                user.setActive(rs.getString("active").equals("Y"));
                user.setUserPw(rs.getString("user_pw"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // abs true면 환불이라 + , false면 구매라 -
    public void updateCustomerTotalPay(int newTotalPrice, int user_id, boolean abs) {
        String sql = "";
        if (abs) {
            sql += "UPDATE customer SET total_pay = total_pay + ? WHERE user_id = ?";
        } else {
            sql += "UPDATE customer SET total_pay = total_pay - ? WHERE user_id = ?";
        }

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newTotalPrice);
            pstmt.setInt(2, user_id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
