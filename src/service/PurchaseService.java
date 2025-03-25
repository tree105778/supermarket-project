package service;

import domain.*;
import repository.CustomerRepository;
import repository.OrderRepository;

import java.util.List;

import static ui.CommonUI.inputInteger;
import static ui.CommonUI.inputString;

public class PurchaseService {
    private final ProductService productService = new ProductService();
    private final OrderRepository orderRepository = new OrderRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    public void startPurchaseScreen(CustomerDTO customer) {
        while (true) {
            System.out.println("==================== 제품 구매 서비스 ====================");
            System.out.println("### 1. 제품 목록 조회 및 장바구니 담기");
            System.out.println("### 2. 환불");

            int selectNum = inputInteger(">>> ");

            switch (selectNum) {
                case 1:
                    showProductListAndPurChase(customer);
                    break;
                case 2:
                    refundProcess(customer);
                    break;
                default:
                    System.out.println("메뉴 번호를 다시 입력해주세요.");
            }
        }
    }

    private void refundProcess(CustomerDTO customer) {
//        String username = inputString("### 회원 이름을 입력하세요: ");
////        List<CustomerDTO> users = customerRepository.searchByUserName(username);
////        if (users.isEmpty()) {
////            System.out.println("### 조건을 만족하는 회원 이름이 없습니다. 다시 입력해주세요.");
////            return;
////        }
////        for (int i = 0; i < users.size(); i++) {
////            System.out.print(i + ". ");
////            System.out.println(users.get(i));
////        }
////        int userIndex = inputInteger("### 회원 번호를 입력해주세요 >> ");
        List<BuyHistory> buyHistories =
                orderRepository.getBuyHistoryByUserId(customer.getUserId());
        if (buyHistories.isEmpty()) {
            System.out.println("### 주문 내역이 없습니다.");
            return;
        }

        for (BuyHistory buyHistory : buyHistories) {
            System.out.println("### 주문번호: " + buyHistory.getBuyId()
                    + ", 주문날짜: " + buyHistory.getBuyTime());
        }
        int buyId = inputInteger("### 환불할 주문 번호를 입력하세요 >> ");
        if (!buyHistories.contains(buyId)) {
            System.out.println("# 주문 번호를 잘못 입력하셨습니다 다시 입력해주세요");
            return;
        }

        List<BuyList> buyLists = orderRepository.getBuyListByBuyId(buyId);

        int totalRefundPrice = 0;

        while (true) {
            for (int i = 0; i < buyLists.size(); i++) {
                System.out.printf("%d. 상품 이름: %s, 개수: %d\n"
                        , i, buyLists.get(i).getProduct().getProductId(),
                        buyLists.get(i).getProduct().getProductName(),
                        buyLists.get(i).getCount());
            }
            String productId = inputString("환불할 상품의 번호를 입력해주세요(종료하려면 q를 눌러주세요!): ");
            if (productId.equals("q")) break;
            int integerProductId = Integer.parseInt(productId);
            int refundCount = inputInteger("환불할 상품의 개수를 입력하세요: ");
            Product product = buyLists.get(integerProductId).getProduct();
            if (refundCount > buyLists.get(integerProductId).getCount()) break;
            totalRefundPrice += product.getPrice() * refundCount;
            orderRepository.refundProcess(buyId, integerProductId, refundCount);
        }
        orderRepository.updateTotalPriceInBuyHistory(totalRefundPrice);
    }

    private void showProductListAndPurChase(CustomerDTO customer) {
        List<ItemCart> itemCarts = productService.shopProductCart();
        String yes = inputString("### 구매를 계속 진행하시겠습니까? (Y/n): ");
        if (!yes.equals("Y")) return;
        if (itemCarts.isEmpty()) {
            System.out.println("장바구니가 비었습니다. 구매를 진행할 수 없습니다.");
            return;
        }
        orderRepository.purchaseProcess(customer.getUserId(), itemCarts);
    }
}
