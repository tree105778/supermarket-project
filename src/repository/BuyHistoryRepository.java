package repository;

import domain.Customer;
import jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BuyHistoryRepository {
    public void buyProducts(Customer customer) {
        String sql = "INSERT INTO BUY_HISTORY (BUY_ID, USER_ID, BUY_TIME, TOTAL_PRICE) " +
                "VALUES (BUY_HISTORY_SEQ.NEXTVAL, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getUserId());
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, 0); // 나중에 UPDATE로 총액 수정

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();






        }
    }
}
