package com.example.malharschildren.model;

import androidx.annotation.DrawableRes;

import java.util.List;
import java.util.Objects;

/**
 * Represents a product in the catalog with its variants, sizes, and pricing
 */
public class Product {
    private final int id;
    private final String name;
    private final @DrawableRes int imageRes;
    private final List<String> sizes;
    private final List<String> variants;
    private final int[][] defaultPrices; // [variant][size] if applicable, or single price

    public Product(int id, String name, int imageRes, List<String> sizes,
                   List<String> variants, int[][] defaultPrices) {
        this.id = id;
        this.name = name;
        this.imageRes = imageRes;
        this.sizes = sizes;
        this.variants = variants;
        this.defaultPrices = defaultPrices;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public List<String> getVariants() {
        return variants;
    }

    public int getDefaultPrice(int variantIndex, int sizeIndex) {
        if (defaultPrices.length == 1) {
            return defaultPrices[0][0]; // Single price for all
        }
        if (defaultPrices[0].length == 1) {
            return defaultPrices[variantIndex][0]; // Price varies by variant only
        }
        return defaultPrices[variantIndex][sizeIndex]; // Price varies by both
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
