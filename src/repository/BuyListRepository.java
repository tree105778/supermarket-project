package repository;

import domain.Product;
import jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyListRepository {
    public void buyListPurchase(int buyId, int productId, int count) {
        String sql = "INSERT INTO BUY_LIST VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buyId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, count);
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBuyHistoryTotalPrice(int buy_id, Product product) {
        String sql = "SELECT * FROM WHERE BUY_ID = " + buy_id;
        int total = 0;
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                total += (product.getPrice() *
                        (rs.getInt("count") - rs.getInt("refund_count")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return total;
    }

    public void refundBuyList(int buyId, int refundCount) {
        String sql = "UPDATE BUY_LIST SET refund_count = ? WHERE BUY_ID = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, refundCount);
            pstmt.setInt(2, buyId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
