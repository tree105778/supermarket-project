package service;

import domain.Customer;
import domain.CustomerDTO;
import domain.Product;
import repository.CustomerRepository;

import java.util.List;

import static ui.CommonUI.*;


public class CustomerService {

    private final CustomerRepository customerRepository = new CustomerRepository();
    private final ProductService productService = new ProductService();
    private final AdminService adminService = new AdminService();
    private final PurchaseService purchaseService = new PurchaseService();

    private final String pwAdmin = "0000";

    public void start() {
        while (true) {
            startCustomerScreen();
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1:
                    purchase();
                    break;
                case 2:
                    searchProduct();
                    break;
                case 3:
                    findUser();
                    break;
                case 4:
                    register();
                    break;
                case 5 :
                    deleteUU();
                    break;
                case 6:
                    privacyAdmin();
                    break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");
            }
        }
    }

    private void deleteUU() {
        findUser();
        String name = inputString("### 탈퇴할 회원 이름: ");
        String phNum = inputString("### 전화번호(예: xxx-xxxx-xxxx): ");

        String pw = inputString("### 비밀번호 입력: ");
        customerRepository.deleteUser(name, pw);
        System.out.println("탈퇴에 성공했습니다.");
    }

    private void privacyAdmin(){
        while(true){
            System.out.println("====== 관리자 모드 접근 ======");
            System.out.println("### 1. 비밀번호 입력");
            System.out.println("### 2. 돌아가기");
            int option = inputInteger("### 옵션 입력: ");
            switch (option){
                case 1:
                    String pw = inputString("### 비밀번호 입력 : ");
                    if(pw.equals(pwAdmin)){
                        adminService.startAdminUI();
                    }
                    else{
                        System.out.println("비밀번호가 틀렸습니다.");
                        break;
                    }
                case 2:
                    start();
                    break;
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");
            }
        }
    }

    private void connectToAdmin() {
        adminService.startAdminUI();
    }

    private void register() {

        System.out.println("\n====== 회원 가입을 진행합니다. ======");

        String name = inputString("# 회원명: ");
        String phone;
        while (true) {
            phone = inputString("# 전화번호(예: xxx-xxxx-xxxx): ").trim();

            // 하이픈 포함 여부 + 숫자-숫자-숫자 형식 확인 (정규표현식 사용)
            if (phone.matches("\\d{2,3}-\\d{3,4}-\\d{4}")) {
                break;
            }
            System.out.println("잘못된 형식입니다. 예: xxx-xxxx-xxxx");
        }

        String pw = inputString("# 비밀번호: ");

        customerRepository.addUser(name, phone, pw);
        System.out.println("# 회원가입이 완료되었습니다.");
    }


    private void findUser() {
        String userName = inputString("### 조회할 회원 이름을 입력하세요 >> ");
        List<CustomerDTO> users = customerRepository.searchByUserName(userName);
        if (users.isEmpty()) {
            System.out.println("### 등록된 회원이 없습니다.");
            return;
        }

        System.out.printf("===================== 회원 목록 =====================\n");
        for (CustomerDTO user : users) {
            String lastDigits = user.getPhoneNumber().trim();
            if (lastDigits.contains("-")) {
                String[] parts = lastDigits.split("-");
                lastDigits = parts[parts.length - 1];  // 마지막 4자리만
            }
            System.out.printf("### 회원명: %s, 전화번호 뒷자리: %s, 포인트: %d\n",
                    user.getUserName(), lastDigits, user.getUserPoint());

        }
        makeLine();
    }

    private void searchProduct() {
        List<Product> products = productService.searchProductDataForCustomer();
        if (products.isEmpty()) {
            System.out.println("현재 조회된 제품이 없습니다.");
            return;
        }
        System.out.println("===================== 제품 목록 =====================");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void purchase() {
        String username = inputString("### 구매하기 전 회원 이름을 입력하세요 >> ");
        List<CustomerDTO> users = customerRepository.searchByUserName(username);
        if (users.isEmpty()) {
            System.out.println("회원 이름이 없습니다.");
            return;
        }
        for(int i = 0; i < users.size(); i++) {
            String rawPhone = users.get(i).getPhoneNumber().trim();
            String lastDigits = rawPhone;
            if (rawPhone.contains("-")) {
                String[] parts = rawPhone.split("-");
                lastDigits = parts[parts.length - 1];
            }
            System.out.printf("### %d. 회원명: %s, 전화번호 뒷자리: %s, 포인트: %d\n",
                    i + 1,
                    users.get(i).getUserName(),
                    lastDigits,
                    users.get(i).getUserPoint());
        }
        int usernum = inputInteger("### 위에 조회된 회원 중 번호를 선택하세요 >> ") - 1;
        if (usernum < 0 || usernum >= users.size()) return;
        makeLine();
        purchaseService.startPurchaseScreen(users.get(usernum));
    }
}