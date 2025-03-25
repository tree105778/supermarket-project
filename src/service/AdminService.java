package service;

import domain.buyHistory;
import repository.AdminRepository;
import repository.BuyHistoryRepository;
import java.util.List;

import static ui.CommonUI.*;

public class AdminService {

    AdminRepository adminRepository = new AdminRepository();
    ProductService productService = new ProductService();
    BuyHistoryRepository buyHistoryRepository = new BuyHistoryRepository();

    public static void startAdminScreen() {
        System.out.println("================= 관리자 계정입니다. =================");
        System.out.println("### 1. 제품 등록");
        System.out.println("### 2. 제품 삭제");
        System.out.println("### 3. 고객 조회");
        System.out.println("### 4. 구매, 환불 내역 조회");
        System.out.println("### 5. 총매출, 수익 조회");
        System.out.println("### 6. 관리자 모드 종료");

    }

    public void startAdminUI() {
        while (true) {
            startAdminScreen();
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    deleteProduct();
                    break;
                case 3:
                    searchAllCustomer();
                    break;
                case 4:
                    showBuyHistory();
                    break;
                case 5:
                    showProfit();
                    break;
                case 6:
                    //customerService.start();
                    break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");

            }
        }
    }


        private void showProfit() {
            System.out.println("\n============== 관리자 모드입니다. ===============");
            System.out.println("\n============== 옵션을 선택하세요. ===============");
            System.out.println("[ 1. 오늘 매출 | 2. 최근 7일 매출 | 3. 최근 30일 매출 | 4. 전 화면으로 돌아가기 ]");
            int option = inputInteger("### 옵션: ");
            int profit = 0; String str = "";
            switch (option) {
                case 1:
                    profit = buyHistoryRepository.getTotalPrice(1);
                    str += "오늘";
                    break;
                case 2:
                    profit = buyHistoryRepository.getTotalPrice(2);
                    str += "최근 7일";
                    break;
                case 3:
                    profit = buyHistoryRepository.getTotalPrice(3);
                    str += "최근 30일";
                    break;
                case 4:
                    startAdminUI();
                    break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");

            }
            System.out.printf("\n============== %s 총 매출 ================\n", str);

            // 나중에
            // System.out.println("\n============== 순 수익 ===============");

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
