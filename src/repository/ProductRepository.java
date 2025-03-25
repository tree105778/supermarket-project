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

    CategoryRepository categoryRepository = new CategoryRepository();

    // admin이 true면 관리자, false면 고객입장 -> 삭제된 물품의 검색 포함 여부
    public List<Product> searchProductList(Condition condition, String keyword, boolean admin){

        List<Product> products = new ArrayList<>();
        int categoryId = categoryRepository.getCategoryId(keyword);
        String sql = "SELECT * FROM product";
        // 고객용 -> 활성화된 제품만 확인
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
            if(condition == Condition.CATEGORY){
                pstmt.setInt(1, categoryId);
            } else if (condition == Condition.NAME) {
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

    
    public void addProductData(String productName, String categoryName, int stock, int productPrice, boolean isExist){

        int categoryId = categoryRepository.getCategoryId(categoryName);
        System.out.println(categoryId);
        String sql = "";

        // 이미 존재하는 제품인 경우
        if (isExist) {
            sql += "UPDATE product SET active = 'Y', stock = ?" +
                    " WHERE product_name = ?";
        }
        // 새로운 제품을 등록하는 경우
        else{
            sql += "INSERT INTO product VALUES (product_seq.NEXTVAL, " +
                    "?, ?, ?, 'Y', ?)";
        }

        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        )   {
            if (isExist) {
                pstmt.setInt(1, stock);
                pstmt.setString(2, productName);
            }
            else{
                pstmt.setString(1, productName);
                pstmt.setInt(2, productPrice);
                pstmt.setInt(3, stock);
                pstmt.setInt(4, categoryId);
            }
            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product deleteProductData(int product_id){
        String sql = "UPDATE product SET active = 'N' WHERE product_id = ?";
        String sear = "SELECT * FROM product WHERE product_id = ?";

        Product temp = new Product(0, "", 0, 0, 0, false);

        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sear);){

            pstmt.setInt(1, product_id);
            pstmt2.setInt(1, product_id);
            ResultSet rs = pstmt2.executeQuery();
            while(rs.next()){
                temp.setProductId(rs.getInt("product_id"));
                temp.setProductName(rs.getString("product_name"));
                temp.setPrice(rs.getInt("price"));
                temp.setStock(rs.getInt("stock"));
                temp.setActive(false);
                temp.setCategoryId(rs.getInt("category_id"));
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }


    // 제품이 이미 있는 지 없는 확인
    public boolean isDeletedProduct(String productName, String categoryName){
        boolean result = false;
        String sql = "SELECT * FROM product p JOIN category c " +
                "ON p.category_id = c.category_id " +
                "WHERE p.product_name = ?" +
                "AND c.name = ?";

        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setString(1, productName);
            pstmt.setString(2, categoryName);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result = true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    // 구매, 환불 시 재고 업데이트 메소드
    // count -> 변화하는 재고량, abs -> true면 구매 -> 재고+, false면 환불 -> 재고 -
    public void updateProductStock(int product_id, int count, boolean abs){
        String sql = "";
        if(abs){
            sql += "UPDATE product SET stock = stock + ? WHERE product_id = ?";
        }
        else{
            sql += "UPDATE product SET stock = stock - ? WHERE product_id = ? AND stock > 0";
        }


        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
