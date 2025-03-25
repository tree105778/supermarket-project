package repository;

import domain.Customer;
import jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BuyHistoryRepository {
    public void buyProducts(Customer customer) {
        String sql = "INSERT INTO BUY_HISTORY (BUY_ID, USER_ID) " +
                "VALUES (BUY_HISTORY_SEQ, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
