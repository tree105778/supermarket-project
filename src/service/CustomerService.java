package service;


import static ui.CommonUI.inputInteger;
import static ui.CommonUI.startCustomerScreen;

public class CustomerService {

    public void start() {
        while (true) {
            startCustomerScreen();
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1: purchase(); break;
                case 2: searchProduct(); break;
                case 3: findUser(); break;
                case 4: register(); break;
                case 5: connectToAdmin(); break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");

            }
        }
    }

    private void connectToAdmin() {
    }

    private void register() {

    }

    private void findUser() {
    }

    private void searchProduct() {
    }

    private void purchase() {
        String username = inputString("### 구매하기 전 회원 이름을 입력하세요 >> ");
        List<CustomerDTO> users = customerRepository.searchByUserName(username);
        if (users.isEmpty()) {
            System.out.println("회원 이름이 없습니다.");
            return;
        }
        for(int i = 0; i < users.size(); i++) {
            System.out.printf("### %d. 회원명: %s, 전화번호: %s, 포인트: %d\n",
                    i + 1,
                    users.get(i).getUserName(),
                    users.get(i).getPhoneNumber(),
                    users.get(i).getUserPoint());
        }
        int usernum = inputInteger("### 위에 조회된 회원 중 번호를 선택하세요 >> ");
        if (usernum < 0 || usernum >= users.size()) return;
        makeLine();
        PurchaseService.startPurchaseScreen(users.get(usernum));
    }
}
