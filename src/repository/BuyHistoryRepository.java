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
                "VALUES (BUY_HISTORY_SEQ.NEXTVAL, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // 총 매출 확인하는 메소드
    public int getTotalPrice(int option){
        int result = 0;
        String sql = "";
        // 오늘 하루
        if(option == 1){
            sql += "SELECT SUM(total_price) AS today_total" +
                    " FROM buy_history" +
                    " WHERE TRUNC(buy_time) = TRUNC(SYSDATE)";
        } // 최근 7일
        else if (option == 2) {
            sql += "SELECT SUM(total_price) AS today_total" +
                    " FROM buy_history" +
                    " WHERE buy_time >= TRUNC(SYSDATE) - 6";
        }// 최근 30일
        else{
            sql += "SELECT SUM(total_price) AS last_30_days_total" +
                    " FROM buy_history" +
                    " WHERE buy_time >= TRUNC(SYSDATE) - 29";
        }


        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result += rs.getInt("total_price");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
