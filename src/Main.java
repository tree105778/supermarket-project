import service.CustomerService;


public class Main {

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        customerService.start();
    }

}
