package service;

import common.Condition;
import domain.ItemCart;
import domain.Product;
import repository.ProductRepository;
import repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static ui.CommonUI.inputInteger;
import static ui.CommonUI.inputString;

public class ProductService {

    ProductRepository productRepository = new ProductRepository();
    CategoryRepository categoryRepository = new CategoryRepository();

    public void insertProductData() {
        System.out.println("\n ====== 새로운 제품을 추가합니다. ======");
        String productName = inputString("# 제품명: ");
        String category_name = inputString("# 카테고리명: ");

        // 해당 제품의 존재 여부
        boolean isExist = productRepository.isDeletedProduct(productName, category_name);
        int stockNum = inputInteger("# 재고수: ");
        int productPrice = 0;
        // 없는 제품인 경우 추가로 제품 가격을 입력받음.
        if(!isExist){
            productPrice = inputInteger("# 제품 가격: ");
        }
        productRepository.addProductData(productName, category_name, stockNum, productPrice, isExist);


        System.out.printf("\n### [%s] 정보가 정상적으로 추가되었습니다.\n", productName);
    }

    public void deleteProductData() {
        try {
            System.out.println("\n### 삭제를 위한 제품 검색을 시작합니다.");
            List<Product> products = searchProductDataForAdmin();

            if (products.size() > 0) {
                List<Integer> productsNums = new ArrayList<>();
                for (Product product : products) {
                    System.out.println(product);
                    productsNums.add(product.getProductId());
                }
                System.out.println("\n### 삭제할 제품의 번호를 입력하세요.");
                int delProductNum = inputInteger(">>> ");

                if (productsNums.contains(delProductNum)) {
                    Product delProduct = productRepository.deleteProductData(delProductNum);
                    System.out.printf("\n### 제품번호: %d -> %s 제품의 정보를 정상 삭제하였습니다.\n"
                            , delProduct.getProductId(), delProduct.getProductName());
                } else {
                    System.out.println("\n### 검색된 제품 번호로만 삭제가 가능합니다.");
                }

            } else {
                System.out.println("\n### 조회 결과가 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> searchProductDataForAdmin(){
        System.out.println("\n============== 관리자 모드입니다. ===============");
        System.out.println("\n============== 제품 검색 조건을 선택하세요. ===============");
        System.out.println("[ 1. 이름 검색 | 2. 카테고리검색 | 3. 전체검색 ]");
        int selection = inputInteger(">>> ");

        Condition condition = Condition.ALL;

        switch (selection) {
            case 1:
                System.out.println("\n## 제품명으로 검색합니다.");
                condition = Condition.NAME;
                break;
            case 2:
                System.out.println("\n## 카테고리로 검색합니다.");
                condition = Condition.CATEGORY;
                // 카테고리 목록 출력
                List<String> categories = categoryRepository.findAllCategoryNames();
                if (categories.isEmpty()) {
                    System.out.println("\n※ 현재 등록된 카테고리가 없습니다.");
                } else {
                    System.out.println("-- 현재 사용 가능한 카테고리 목록 --");
                    for (String category : categories) {
                        System.out.println("- " + category);
                    }
                }
                break;
            case 3:
                System.out.println("\n## 전체로 검색합니다.");
                break;
            default:
                System.out.println("\n### 해당 메뉴가 존재하지 않습니다. 전체 정보로 검색합니다.");
        }

        String keyword = "";
        if (condition != Condition.ALL) {
            keyword = inputString("# 검색어: ");
        }

        return productRepository.searchProductList(condition, keyword, true);
    }



    // 제품 검색 로직 -> 고객용
    public List<Product> searchProductDataForCustomer()  {
        System.out.println("\n============== 제품 검색 조건을 선택하세요. ===============");
        System.out.println("[ 1. 제품명검색 | 2. 카테고리검색 | 3. 전체검색 ]");
        int selection = inputInteger(">>> ");

        Condition condition = Condition.ALL;

        switch (selection) {
            case 1:
                System.out.println("\n## 제품명으로 검색합니다.");
                condition = Condition.NAME;
                break;
            case 2:
                System.out.println("\n## 카테고리로 검색합니다.");
                condition = Condition.CATEGORY;
                // 카테고리 목록 출력
                List<String> categories = categoryRepository.findAllCategoryNames();
                if (categories.isEmpty()) {
                    System.out.println("\n※ 현재 등록된 카테고리가 없습니다.");
                } else {
                    System.out.println("-- 현재 사용 가능한 카테고리 목록 --");
                    for (String category : categories) {
                        System.out.println("- " + category);
                    }
                }
                break;
            case 3:
                System.out.println("\n## 전체로 검색합니다.");
                break;
            default:
                System.out.println("\n### 해당 메뉴가 존재하지 않습니다. 전체 정보로 검색합니다.");
        }

        String keyword = "";
        if (condition != Condition.ALL) {
            keyword = inputString("# 검색어: ");
        }

        return productRepository.searchProductList(condition, keyword, false);
    }

    // 관리자면 true, 고객이면 false
    public List<Product> showSearchProductData(boolean admin) {

        List<Product> products = new ArrayList<>();
        try {
            if (admin) {
                products = searchProductDataForAdmin();
            }
            else{
                products = searchProductDataForCustomer();
            }
            int count = products.size();
            if(count > 0) {
                System.out.printf("\n======================================= 검색 결과(총 %d건) =======================================\n", count);
                for (Product product : products) {
                    if(admin){
                        System.out.println(product.toString2());
                    }
                    else{
                        System.out.println(product);
                    }
                }
            } else {
                System.out.println("\n### 검색 결과가 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    // 장바구니 메소드
    public List<ItemCart> shopProductCart(){
        List<ItemCart> itemList = new ArrayList<>();
        List<Product> productList = showSearchProductData(false);
        List<Integer> productID = new ArrayList<>();
        for (Product product : productList) {
            productID.add(product.getProductId());
        }
        System.out.println("### 모두 장바구니에 담으셨다면 -1을 입력하세요! ");
        while(true){
            int option = inputInteger("### 제품 번호: ");
            if(option == -1){
                break;
            }
            else{
                if(!productID.contains(option)){
                    System.out.println("========= 잘못 입력하셨습니다 =======");
                }
                else{
                    for (Product product : productList) {
                        if(product.getProductId() == option){
                            Product pr = product;
                            int count = inputInteger("### 제품 수량: ");
                            ItemCart temp = new ItemCart(option, count, pr.getPrice());
                            itemList.add(temp);
                        }
                    }

                }
            }
        }
        return itemList;
    }


}
