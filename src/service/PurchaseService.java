package service;

import domain.CustomerDTO;
import ui.CommonUI;

public class PurchaseService {
    public static void startPurchaseScreen(CustomerDTO customer) {
        while (true) {
            System.out.println("==================== 제품 구매 서비스 ====================");
            System.out.println("### 1. 제품 목록 조회 및 구매");
            System.out.println("### 2. 환불");
        }
    }

}
