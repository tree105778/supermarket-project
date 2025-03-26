package repository;

import domain.BuyHistory;
import domain.BuyList;
import domain.ItemCart;
import domain.Product;
import jdbc.DBConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private Connection conn;

    public boolean purchaseProcess(int userId, List<ItemCart> itemCarts) {
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);

            int totalPrice = itemCarts.stream()
                    .mapToInt(ic -> ic.getCount() * ic.getPrice())
                    .sum();

            buyProducts(conn, userId, totalPrice);
            int buyId = getBuyId(conn);
            if (buyId == -1) {
                System.out.println("구매에 실패했습니다 다시 시도해주세요.");
                return false;
            }
            for (ItemCart itemCart : itemCarts) {
                buyListPurchase(conn, buyId, itemCart);
                updateProductStock(conn, itemCart, false);
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println("구매에 실패했습니다 다시 시도해주세요.");
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("롤백에 실패했습니다");
            } finally {
                return false;
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
        // abs true면 환불, false면 구매
    private void updateProductStock(Connection conn, ItemCart itemCart, boolean abs) throws SQLException {
        String sql = "";

        if (!abs) {
            sql +=   "UPDATE PRODUCT " +
                    "SET stock = stock - ? WHERE PRODUCT_ID = ?";
        }
        else{
            sql +=   "UPDATE PRODUCT " +
                    "SET stock = stock + ? WHERE PRODUCT_ID = ?";
        }

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, itemCart.getCount());
        pstmt.setInt(2, itemCart.getProductId());
        pstmt.executeUpdate();
    }

    public boolean refundProcess(int buyId, int productId, int refundCount) {
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);
            System.out.println("refundProcess");

            refundBuyList(conn, buyId, productId, refundCount);
            ItemCart temp = new ItemCart(productId, refundCount, 0);
            updateProductStock(conn, temp, true);
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                System.out.println("환불 절차가 실패했습니다.");
                conn.rollback();
                return false;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private int getBuyId(Connection conn) {
        String sql = "SELECT BUY_HISTORY_SEQ.CURRVAL FROM DUAL";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt("CURRVAL");
        } catch (SQLException e) {
            return -1;
        }
    }

    private void buyListPurchase(Connection conn, int buyId, ItemCart itemCart) {
        String sql = "INSERT INTO BUY_LIST VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buyId);
            pstmt.setInt(2, itemCart.getProductId());
            pstmt.setInt(3, itemCart.getCount());
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refundBuyList(
            Connection conn, int buyId, int productId, int refundCount) throws SQLException {
        String sql = "UPDATE BUY_LIST " +
                "SET refund_count = refund_count + ? " +
                "WHERE BUY_ID = ? AND PRODUCT_ID = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, refundCount);
            pstmt.setInt(2, buyId);
            pstmt.setInt(3, productId);
            System.out.println("refundBuyList");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void updateTotalPriceInBuyHistory(int totalRefundPrice) {
        String sql = "UPDATE BUY_HISTORY " +
                "SET total_price = total_price - " + totalRefundPrice;

        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private int getBuyHistoryTotalPrice(int buy_id, Product product) {
        String sql = "SELECT * FROM BUY_LIST WHERE BUY_ID = " + buy_id;
        int total = 0;
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                total += (product.getPrice() *
                        (rs.getInt("count") - rs.getInt("refund_count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<BuyList> getBuyListByBuyId(int buyId) {
        List<BuyList> buyLists = new ArrayList<>();
        String sql = "SELECT " +
                "bl.buy_id, bl.product_id, bl.count, bl.refund_count, " +
                "p.product_name, p.price, p.stock, p.active, p.category_id " +
                "FROM BUY_LIST bl " +
                "JOIN PRODUCT p ON p.product_id = bl.product_id " +
                "WHERE buy_id = " + buyId;

        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("product_id");


                Product product = new Product(
                        productId,
                        rs.getString("product_name"),
                        rs.getInt("price"),
                        rs.getInt("stock"),
                        rs.getInt("category_id"),
                        rs.getString("active").equals("Y")
                );
                BuyList buyList = new BuyList(
                        rs.getInt("buy_id"),
                        productId,
                        rs.getInt("count"),
                        rs.getInt("refund_count")
                );
                buyList.setProduct(product);
                buyLists.add(buyList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return buyLists;
    }

    public List<BuyHistory> getBuyHistoryByUserId(int userId) {
        List<BuyHistory> buyHistories = new ArrayList<>();

        String sql = "SELECT * FROM BUY_HISTORY WHERE USER_ID = " + userId;

        try (Connection connection = DBConnectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                BuyHistory buyHistory = new BuyHistory(
                        rs.getInt("buy_id"),
                        rs.getTimestamp("buy_time").toLocalDateTime(),
                        rs.getInt("user_id"),
                        rs.getInt("total_price")
                );
                buyHistories.add(buyHistory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return buyHistories;
    }

    private void buyProducts(Connection conn, int userId, int totalPrice) {
        String sql = "INSERT INTO BUY_HISTORY (BUY_ID, USER_ID, BUY_TIME, TOTAL_PRICE) " +
                "VALUES (BUY_HISTORY_SEQ.NEXTVAL, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, totalPrice); // 나중에 UPDATE로 총액 수정

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 총 매출 확인하는 메소드
    private int getTotalPrice(int option) {
        int result = 0;
        String sql = "";
        // 오늘 하루
        if (option == 1) {
            sql += "SELECT SUM(total_price) AS today_total" +
                    " FROM buy_history" +
                    " WHERE TRUNC(buy_time) = TRUNC(SYSDATE)";
        } // 최근 7일
        else if (option == 2) {
            sql += "SELECT SUM(total_price) AS today_total" +
                    " FROM buy_history" +
                    " WHERE buy_time >= TRUNC(SYSDATE) - 6";
        }// 최근 30일
        else {
            sql += "SELECT SUM(total_price) AS last_30_days_total" +
                    " FROM buy_history" +
                    " WHERE buy_time >= TRUNC(SYSDATE) - 29";
        }


        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result += rs.getInt("total_price");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
