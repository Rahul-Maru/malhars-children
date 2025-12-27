package com.example.malharschildren.model;

import java.util.Objects;

/**
 * Represents an item in the shopping cart
 */
public class CartItem {
    private final int productId;
    private final String productName;
    private final int sizeIndex;
    private final String sizeName;
    private final int variantIndex;
    private final String variantName;
    private int quantity;
    private int price;

    public CartItem(int productId, String productName, int sizeIndex, String sizeName,
                    int variantIndex, String variantName, int quantity, int price) {
        this.productId = productId;
        this.productName = productName;
        this.sizeIndex = sizeIndex;
        this.sizeName = sizeName;
        this.variantIndex = variantIndex;
        this.variantName = variantName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getSizeIndex() {
        return sizeIndex;
    }

    public String getSizeName() {
        return sizeName;
    }

    public int getVariantIndex() {
        return variantIndex;
    }

    public String getVariantName() {
        return variantName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalAmount() {
        return quantity * price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return productId == cartItem.productId &&
                sizeIndex == cartItem.sizeIndex &&
                variantIndex == cartItem.variantIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, sizeIndex, variantIndex);
    }
}
