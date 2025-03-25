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

    }
}
