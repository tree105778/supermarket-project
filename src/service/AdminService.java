package service;

import domain.Product;
import domain.buyHistory;
import repository.AdminRepository;

import java.util.ArrayList;
import java.util.List;

import static ui.CommonUI.*;

public class AdminService {

    ProductService productService;
    AdminRepository adminRepository;

    public static void startAdminScreen() {
        System.out.println("================= 관리자 계정입니다. =================");
        System.out.println("### 1. 제품 등록");
        System.out.println("### 2. 제품 삭제");
        System.out.println("### 3. 고객 조회");
        System.out.println("### 4. 구매, 환불 내역 조회");
        System.out.println("### 5. 총매출, 수익 조회");
        System.out.println("### 6. 관리자 모드 종료");

        makeLine();
    }

    // 회원 목록 검색

    // 제품 등록

    // 제품 조회

    // 제품 삭제

    public void start() {
        while (true) {
            startAdminScreen();
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1: addProduct(); break;
                case 2: deleteProduct(); break;
                case 3: searchAllCustomer(); break;
                case 4: showBuyHistory(); break;
                case 5: showProfit(); break;
                case 6: startCustomerScreen(); break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");

            }
        }
    }

    private void showProfit() {

    }

    private void showBuyHistory() {
        System.out.println("\n============== 관리자 모드입니다. ===============");
        System.out.println("\n============== 구매 내역 ================");
        try {
            List<buyHistory> histories = adminRepository.searchBuyHistory();
            int count = histories.size();
            if(count > 0) {
                System.out.printf("\n======================================= 검색 결과(총 %d건) =======================================\n", count);
                for (buyHistory history : histories) {
                    System.out.println(history);
                }
            } else {
                System.out.println("\n### 검색 결과가 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct() {
        productService.deleteProductData();
    }

    private void addProduct() {
        productService.insertProductData();
    }

    private void searchAllCustomer() {
        productService.searchProductDataForAdmin();
    }


}
