package service;

import static ui.CommonUI.*;

public class AdminService {

    ProductService productService;

    public static void startAdminScreen() {
        System.out.println("================= 관리자 계정입니다. =================");
        System.out.println("### 1. 제품 등록");
        System.out.println("### 2. 제품 삭제");
        System.out.println("### 3. 고객 조회");
        System.out.println("### 4. 구매, 환불 내역 조회");
        System.out.println("### 5. 총매출, 수익 조회");
        System.out.println("### 5. 관리자 모드 종료");

        makeLine();
    }

    // 회원 목록 검색

    // 제품 등록

    // 제품 조회

    // 제품 삭제

    /*public void start() {
        while (true) {
            startAdminScreen();
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1: searchAllCustomers(); break;
                case 2: productService.; break;
                case 3: ; break;
                case 4: searc; break;
                case 5: connectToAdmin(); break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");

            }
        }
    }*/


}
