package repository;

import common.Condition;
import domain.Product;
import jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // admin이 true면 관리자, false면 고객입장 -> 삭제된 물품의 검색 포함 여부
    public List<Product> searchProductList(Condition condition, String keyword, boolean admin){

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
        if(!admin) {
            if (condition == Condition.NAME) {
                sql += " WHERE product_name = ? AND active = 'Y'";
            } else if (condition == Condition.CATEGORY) {
                sql += " p JOIN category c ON p.category_id = c.category_id" +
                        " WHERE p.category_id = ? AND p.active = 'Y'";
            } else {
                sql += " WHERE active = 'Y'";
            }
        }
        // 관리자용 -> 삭제된 물품도 전부 확인
        else{
            if (condition == Condition.NAME) {
                sql += " WHERE product_name = ?";
            } else if (condition == Condition.CATEGORY) {
                sql += " p JOIN category c ON p.category_id = c.category_id" +
                        " WHERE p.category_id = ?";
            }
        }

        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            if(condition != Condition.ALL){
                pstmt.setString(1, keyword);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Product pr = new Product(
                  rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("price"),
                        rs.getInt("stock"),
                        rs.getInt("category_id"),
                        rs.getString("active").equals("Y")
                );

                products.add(pr);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }


}
