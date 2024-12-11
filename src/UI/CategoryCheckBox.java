package UI;

import javafx.scene.control.CheckBox;

public class CategoryCheckBox extends CheckBox {
    private int categoryId;

    CategoryCheckBox(int categoryId, String categoryValue) {
        super(categoryValue);
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return this.categoryId;
    }
}