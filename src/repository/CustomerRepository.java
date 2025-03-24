package repository;

import jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerRepository {

    // 회원 추가 기능
    public void addUser() {
        String sql = "INSERT INTO CUSTOMER " +
                "(USER_ID, USER_NAME, PHONE_NUMBER, USER_PW) VALUES " +
                "(?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
