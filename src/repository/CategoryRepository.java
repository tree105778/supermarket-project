package repository;

import jdbc.DBConnectionManager;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private Connection conn;

    public CategoryRepository() {
        try {
            this.conn = DBConnectionManager.getConnection();
        } catch (Exception e) {
            e.printStackTrace(); // 연결 실패 시 콘솔에 에러 표시
        }
    }

    // 1. 전체 카테고리 이름 조회
    public List<String> findAllCategoryNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM category";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    // 2. 카테고리 이름으로 해당 상품 이름들 조회
    public List<String> findProductNamesByCategoryName(String categoryName) {
        List<String> productNames = new ArrayList<>();
        String sql = "SELECT p.product_name " +
                "FROM product p JOIN category c ON p.category_id = c.category_id " +
                "WHERE c.name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productNames.add(rs.getString("product_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productNames;
    }

    // 3. 새로운 카테고리 추가
    public boolean insertCategory(String name) {
        String sql = "INSERT INTO category VALUES (category_seq.NEXTVAL, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
