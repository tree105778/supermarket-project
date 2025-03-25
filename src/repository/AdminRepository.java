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
    public List<buyHistory> searchBuyHistory(){
            List<buyHistory> buyHistoryList = new ArrayList<>();
            String sql = "SELECT * FROM buy_history b JOIN customer c ON b.user_id = c.user_id ORDER BY b.buy_time DESC";

            try(Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
            ){
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
