package service;

import domain.Category;
import domain.Customer;
import domain.buyHistory;
import repository.AdminRepository;
import repository.BuyHistoryRepository;
import repository.CategoryRepository;
import repository.CustomerRepository;

import java.util.List;

import static ui.CommonUI.*;

public class AdminService {

    AdminRepository adminRepository = new AdminRepository();
    ProductService productService = new ProductService();
    BuyHistoryRepository buyHistoryRepository = new BuyHistoryRepository();
    CategoryRepository categoryRepository = new CategoryRepository();
    CustomerRepository customerRepository = new CustomerRepository();

    public static void startAdminScreen() {
        System.out.println("================= 관리자 계정입니다. =================");
        System.out.println("### 1. 제품 관리");
        System.out.println("### 2. 제품 삭제");
        System.out.println("### 3. 카테고리 등록");
        System.out.println("### 4. 제품 조회");
        System.out.println("### 5. 고객 조회");
        System.out.println("### 6. 구매, 환불 내역 조회");
        System.out.println("### 7. 총매출, 수익 조회");
        System.out.println("### 8. 관리자 모드 종료");

    }

    public void startAdminUI() {
        CustomerService customerService = new CustomerService();
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
                    addCategory();
                    break;
                case 4:
                    searchAllProduct();
                    break;
                case 5:
                    searchAllCustomer();
                    break;
                case 6:
                    showBuyHistory();
                    break;
                case 7:
                    showProfit();
                    break;
                case 8:
                    customerService.start();
                    break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");

            }
        }
    }

    private void addCategory() {
        System.out.println("\n============== 관리자 모드입니다. ===============");
        System.out.println("\n============== 카테고리 추가 ===============");
        System.out.println("### 종료를 원하시면 0을 입력");
        List<String> categoryList = categoryRepository.findAllCategoryNames();
        while(true){
            String cateName = inputString("### 카테고리명: ");
            boolean isExist = false;
            if(cateName.equals("0")){
                return;
            }
            else{
                for (String s : categoryList) {
                    if(cateName.equals(s)){
                        System.out.println("이미 존재하는 카테고리입니다.");
                        isExist = true;
                    }
                }
            }
            if(isExist){
                continue;
            }

            categoryRepository.insertCategory(cateName);
            System.out.println(cateName +" 카테고리가 추가되었습니다.");
            break;
        }

    }

    private void searchAllProduct() {
        productService.showSearchProductData(true);
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
            System.out.println("\n============== 검색 옵션 선택 ===============");
            System.out.println("[ 1. 회원 이름 | 2. 날짜 검색 | 3. 전체 검색 ]");
            int option = inputInteger("### 옵션 입력: ");
            int month = 0; int day = 0; String name = "";

            switch(option){
                case 1:
                    name += inputString("### 회원 이름 입력: ");
                    System.out.println("\n## 이름으로 검색합니다.");
                    break;
                case 2:
                    month = inputInteger("### 월 입력: ");
                    day = inputInteger("### 일 입력: ");
                    System.out.println("\n## 날짜로 검색합니다.");
                    break;
                case 3:
                    break;
                default:
                    System.out.println("\n### 해당 메뉴가 존재하지 않습니다. 전체 정보로 검색합니다.");
            }

            System.out.println("\n============== 구매 내역 ================");
            try {
                List<buyHistory> histories = adminRepository.searchBuyHistory(option, month, day, name);
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
            List<Customer> customers = customerRepository.showAllUsers();
            System.out.println("\n============== 관리자 모드입니다. ===============");
            System.out.println("\n============== 검색 옵션 선택 ===============");
            System.out.println("[ 1. 회원 이름 | 2. 전체 검색 ]");
            int option = inputInteger("### 옵션 입력: ");

            if(option == 1){
                String name = inputString("### 회원 이름 입력: ");
                boolean isExist = false;
                for (Customer customer : customers) {
                    if(customer.getUserName().equals(name)){
                        isExist = true;
                        String act = "";
                        if(customer.isActive()){
                            act += "Y";
                        }
                        else{
                            act += "N";
                        }
                        String phonenumber = "#".repeat(7) + customer.getPhoneNumber().substring(7);
                        System.out.printf("### 회원명: %s, 전화번호: %s, 포인트: %d, 탈퇴 여부: %s\n",
                                customer.getUserName(), phonenumber, customer.getUserPoint(), act);
                    }
                }
                if (!isExist) {
                    System.out.println("존재하지 않는 회원명입니다.");
                }
                return;
            }
            else{
                for (Customer customer : customers) {
                    String act = "";
                    if(customer.isActive()){
                        act += "Y";
                    }
                    else{
                        act += "N";
                    }
                    System.out.printf("### 회원명: %s, 전화번호: %s, 포인트: %d, 탈퇴 여부: %s\n",
                            customer.getUserName(), customer.getPhoneNumber(), customer.getUserPoint(), act);
                }
            }
        }



}
