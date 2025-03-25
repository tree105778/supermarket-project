package repository;

import domain.buyHistory;
import jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {

    CustomerRepository customerRepository;

    // 관리자모드에서 모든 buy_history를 날짜기준 최신순으로 모두 리스트로 묶어서 리턴
    public List<buyHistory> searchBuyHistory(int option, int month, int day, String name){
            List<buyHistory> buyHistoryList = new ArrayList<>();
            String sql = "";

            if(option == 1){
                sql += "SELECT * FROM buy_history b JOIN customer c ON b.user_id = c.user_id" +
                        " WHERE c.user_name = ? ORDER BY b.buy_time DESC";

            } else if (option == 2) {
                sql += "SELECT * FROM buy_history WHERE MONTH(buy_time) = ? AND " +
                        "DAY(buy_time) = ? ORDER BY buy_time DESC";
            } else if (option == 3) {
            sql += "SELECT * FROM buy_history ORDER BY buy_time DESC";
        }

        try(Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
            ){
                if(option == 1){

                }
                else if(option == 2){

                }

                ResultSet rs = pstmt.executeQuery();
                while(rs.next()){
                    buyHistory temp = new buyHistory();
                    temp.setBuyId(rs.getInt("buy_id"));

                    temp.setBuyTime(rs.getTimestamp("buy_time").toLocalDateTime());
                    temp.setUser(customerRepository.getCustomerByUserId(rs.getInt("user_id")));
                    temp.setTotalPrice(rs.getInt("total_price"));
                    buyHistoryList.add(temp);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

            return buyHistoryList;
    }





}
