package service;

import common.Condition;
import domain.Product;
import repository.ProductRepository;

import java.util.List;

import static ui.CommonUI.inputInteger;
import static ui.CommonUI.inputString;

public class ProductService {

    ProductRepository productRepository;

    private List<Product> searchProductDataForAdmin() throws Exception {
        System.out.println("\n============== 관리자 모드입니다. ===============");
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
    private List<Product> searchProductDataForCustomer() throws Exception {
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


    private void showSearchProductData() {

        try {
            List<Product> products = searchProductDataForCustomer();
            int count = products.size();
            if(count > 0) {
                System.out.printf("\n======================================= 검색 결과(총 %d건) =======================================\n", count);
                for (Product product : products) {
                    System.out.println(product);
                }
            } else {
                System.out.println("\n### 검색 결과가 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
