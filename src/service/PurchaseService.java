package service;

import domain.*;
import repository.CustomerRepository;
import repository.OrderRepository;

import java.util.List;

import static ui.CommonUI.*;

public class PurchaseService {
    private final ProductService productService = new ProductService();
    private final OrderRepository orderRepository = new OrderRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    public void startPurchaseScreen(CustomerDTO customer) {
        while (true) {
            System.out.println("==================== 제품 구매 서비스 ====================");
            System.out.println("### 1. 제품 목록 조회 및 장바구니 담기");
            System.out.println("### 2. 환불");
            System.out.println("### 3. 구매 종료");

            int selectNum = inputInteger(">>> ");

            switch (selectNum) {
                case 1:
                    showProductListAndPurChase(customer);
                    break;
                case 2:
                    refundProcess(customer);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("메뉴 번호를 다시 입력해주세요.");
            }
        }
    }

    private void refundProcess(CustomerDTO customer) {
        String inputPw = inputString("### 비밀번호를 입력하세요 >>> ");
        Customer user = customerRepository.getCustomerByUserId(customer.getUserId());
        if (!user.getUserPw().equals(inputPw)) {
            System.out.println("### 비밀번호가 올바르지 않습니다.");
            return;
        }
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
        boolean flag = false;
        int userID = 0;
        for (BuyHistory buyHistory : buyHistories) {
            if (buyHistory.getBuyId() == buyId) {
                flag = true; userID = buyHistory.getUserId();
                break;
            }
        }
        if (!flag) {
            System.out.println("환불할 주문 번호를 잘못 입력하셨습니다. 다시 입력해주세요!");
            return;
        }

        List<BuyList> buyLists = orderRepository.getBuyListByBuyId(buyId);

        int totalRefundPrice = 0;

        while (true) {
            for (int i = 0; i < buyLists.size(); i++) {
                System.out.printf("%d. 상품 이름: %s, 개수: %d\n"
                        , i + 1,
                        buyLists.get(i).getProduct().getProductName(),
                        buyLists.get(i).getCount());
            }
            String productId = inputString("### 환불할 상품의 번호를 입력해주세요(종료하려면 q를 눌러주세요!): ");
            char[] charID = productId.toCharArray();
            if (productId.equals("q")) break;
            int integerProductId = Integer.parseInt(productId) - 1;
            if (integerProductId < 0 || integerProductId >= buyLists.size() || Character.isLetter(charID[0])) {
                System.out.println("### 잘못된 번호입니다. 다시 입력해주세요");
                continue;
            }
            int refundCount = inputInteger("### 환불할 상품의 개수를 입력하세요: ");
            BuyList buyList = buyLists.get(integerProductId);
            Product product = buyLists.get(integerProductId).getProduct();
            if (refundCount > buyLists.get(integerProductId).getCount()) {
                System.out.println("### 잘못된 입력입니다.");
                break;
            }
            buyList.setCount(buyList.getCount() - refundCount);
            totalRefundPrice += product.getPrice() * refundCount;
            boolean isRefund = orderRepository.
                    refundProcess(buyId, product.getProductId(), refundCount);
            if (isRefund) System.out.println("### 환불 절차가 성공했습니다.");
            else System.out.println("### 환불 절차가 실패했습니다.");
        }
        orderRepository.updateTotalPriceInBuyHistory(totalRefundPrice);
        customerRepository.updateCustomerTotalPay(totalRefundPrice,userID ,false);
    }

    private void showProductListAndPurChase(CustomerDTO customer) {
        List<ItemCart> itemCarts = productService.shopProductCart();
        String yes = inputString("### 구매를 완료하시겠습니까? (Y/N): ");
        if (!yes.equals("Y")) {
            System.out.println("### 구매가 취소 되었습니다.");
            return;
        }
        if (itemCarts.isEmpty()) {
            System.out.println("### 장바구니가 비었습니다. 구매를 진행할 수 없습니다.");
            return;
        }
        String inputPw = inputString("### 비밀번호를 입력하세요 >> ");
        Customer user = customerRepository.getCustomerByUserId(customer.getUserId());
        if (!user.getUserPw().equals(inputPw)) {
            System.out.println("비밀번호가 올바르지 않습니다.");
            return;
        }

        boolean isPurchase =
                orderRepository.purchaseProcess(customer.getUserId(), itemCarts);
        if (isPurchase) {
            System.out.println("### 구매가 성공적으로 진행되었습니다.");
            int totalPrice = itemCarts.stream()
                    .mapToInt(ic -> ic.getCount() * ic.getPrice())
                    .sum();
            customerRepository.updateCustomerTotalPay(totalPrice, customer.getUserId(), true);
        }
        else System.out.println("### 구매가 실패했습니다.");
    }
}
