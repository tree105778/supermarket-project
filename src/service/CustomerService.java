package service;

import repository.CustomerRepository;
import ui.CommonUI;
import domain.Customer;  // Customer 객체 사용 시 필요
import java.util.List;   // List<Customer> 사용 시 필요

import static ui.CommonUI.*;


public class CustomerService {

    private final CustomerRepository customerRepository = new CustomerRepository();

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
        while (true) {
            System.out.println("\n===== [ 관리자 모드 ] =====");
            System.out.println("1. 전체 회원 조회");
            System.out.println("2. 뒤로 가기");
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1:
                    List<Customer> customers = customerRepository.showAllUsers();
                    if (customers.isEmpty()) {
                        System.out.println("등록된 회원이 없습니다.");
                    } else {
                        System.out.println("\n[ 전체 회원 목록 ]");
                        for (Customer c : customers) {
                            System.out.println("---------------------------");
                            System.out.println("ID: " + c.getUserId());
                            System.out.println("이름: " + c.getUserName());
                            System.out.println("전화번호: " + c.getPhoneNumber());
                            System.out.println("총 결제금액: " + c.getTotalPay());
                            System.out.println("포인트: " + c.getUserPoint());
                            System.out.println("활성화 상태: " + (c.isActive() ? "활성" : "비활성"));
                        }
                    }
                    break;
                case 2:
                    return;
                default:
                    System.out.println("# 메뉴를 다시 선택해주세요.");
            }
        }
    }

    private void register() {

        System.out.println("\n====== 회원 가입을 진행합니다. ======");

        String name = inputString("# 회원명: ");
        String phone = inputString("# 전화번호: ");
        String pw = inputString("# 비밀번호: ");


        customerRepository.addUser(name, phone, pw);
        System.out.println("# 회원가입이 완료되었습니다.");
    }


    private void findUser() {


    }

    private void searchProduct() {
        // 구현 예정
    }

    private void purchase() {
        // 구현 예정
    }
}
