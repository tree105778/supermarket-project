package domain;

import java.util.List;

public class Category {
    private int categoryId;
    private String name;
    private List<Product> cate_pro; // 카테고리별 제품리스트

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getCate_pro() {
        return cate_pro;
    }

    public void setCate_pro(List<Product> cate_pro) {
        this.cate_pro = cate_pro;
    }


}
