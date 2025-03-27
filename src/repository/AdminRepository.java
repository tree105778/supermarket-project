package repository;

import domain.BuyHistory;
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
    public List<BuyHistory> searchBuyHistory(int option, int month, int day, String name){
            List<BuyHistory> BuyHistoryList = new ArrayList<>();
            String sql = "";

            // 구매내역은 모두 최신순으로 출력
            // 고객 이름을 기준으로 구매내역 조회
            if(option == 1){
                sql += "SELECT * FROM buy_history b JOIN customer c ON b.user_id = c.user_id" +
                        " WHERE c.user_name = ? ORDER BY b.buy_time DESC";

            } // 특정 날짜를 기준으로 구매내역 조회
            else if (option == 2) {
                sql += "SELECT * FROM buy_history WHERE TO_CHAR(buy_time, 'MM') = ? AND " +
                        "TO_CHAR(buy_time, 'DD') = ? ORDER BY buy_time DESC";
            } // 모든 구매내역 조회
            else if (option == 3) {
            sql += "SELECT * FROM buy_history ORDER BY buy_time DESC";
        }

        try(Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
            ){
                if(option == 1){
                    pstmt.setString(1, name);
                }
                else if(option == 2){
                    pstmt.setInt(1, month);
                    pstmt.setInt(2, day);
                }
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()){
                    BuyHistory temp = new BuyHistory(0, null, 0, 0);
                    temp.setBuyId(rs.getInt("buy_id"));

                    temp.setBuyTime(rs.getTimestamp("buy_time").toLocalDateTime());
                    temp.setUserId(rs.getInt("user_id"));
                    temp.setTotalPrice(rs.getInt("total_price"));
                    BuyHistoryList.add(temp);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

            return BuyHistoryList;
    }





}
