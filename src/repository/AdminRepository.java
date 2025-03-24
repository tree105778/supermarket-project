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
                    // Date 타입을 LocalDateTime으로 변환하는데 문제가 있음.
                    temp.setBuyTime(rs.getDate("buy_time").toLocalDate().atTime(6,30));
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
