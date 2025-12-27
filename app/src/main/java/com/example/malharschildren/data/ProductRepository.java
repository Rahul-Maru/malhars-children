package com.example.malharschildren.data;

import com.example.malharschildren.R;
import com.example.malharschildren.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repository class that provides product data
 * In a real app, this might fetch from a database or API
 */
public class ProductRepository {
    private static ProductRepository instance;
    private final List<Product> products;

    // Standard sizes for most products
    private static final List<String> STANDARD_SIZES = Arrays.asList(
            "8 - 9", "9 - 10", "10 - 11", "11 - 12", "12 - 14"
    );
    private static final List<String> KAFTAN_SIZES = Arrays.asList("S/M", "M/L");
    private static final List<String> PALAZZO_SIZES = Arrays.asList("S", "M", "L");
    private static final List<String> FREE_SIZE = Arrays.asList("Free size");

    private ProductRepository() {
        products = new ArrayList<>();
        initializeProducts();
    }

    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    private void initializeProducts() {
        // 0. Airie
        products.add(new Product(
                0, "Airie", R.drawable.airie,
                STANDARD_SIZES,
                Arrays.asList("None"),
                new int[][]{{875}}
        ));

        // 1. Kaftan
        products.add(new Product(
                1, "Kaftan", R.drawable.kaftan,
                KAFTAN_SIZES,
                Arrays.asList("Indigo", "Kora", "Pomo"),
                new int[][]{{950}, {950}, {950}}
        ));

        // 2. Vest
        products.add(new Product(
                2, "Vest", R.drawable.vest_flamingo,
                STANDARD_SIZES,
                Arrays.asList("Unicorn", "White", "Heart"),
                new int[][]{{650}, {650}, {650}}
        ));

        // 3. Boxy
        products.add(new Product(
                3, "Boxy", R.drawable.boxy,
                STANDARD_SIZES,
                Arrays.asList("None"),
                new int[][]{{650}}
        ));

        // 4. Olly
        products.add(new Product(
                4, "Olly", R.drawable.olly_octopus,
                STANDARD_SIZES,
                Arrays.asList("Stripe", "Octopus"),
                new int[][]{{775}, {680}}
        ));

        // 5. Farsi
        products.add(new Product(
                5, "Farsi", R.drawable.farsi,
                STANDARD_SIZES,
                Arrays.asList("None"),
                new int[][]{{950}}
        ));

        // 6. Shirt
        products.add(new Product(
                6, "Shirt", R.drawable.shirt_ziggy,
                STANDARD_SIZES,
                Arrays.asList("Ziggy", "Almond", "Crab"),
                new int[][]{{850}, {850}, {800}}
        ));

        // 7. Kurta
        products.add(new Product(
                7, "Kurta", R.drawable.kurta_cloud,
                STANDARD_SIZES,
                Arrays.asList("Cloud", "Peach", "Fox", "BLU"),
                new int[][]{{960}, {960}, {960}, {960}}
        ));

        // 8. Tunic
        products.add(new Product(
                8, "Tunic", R.drawable.tunic_green,
                STANDARD_SIZES,
                Arrays.asList("Yellow", "Green"),
                new int[][]{{1060}, {1060}}
        ));

        // 9. Shift
        products.add(new Product(
                9, "Shift", R.drawable.shift_indigo,
                STANDARD_SIZES,
                Arrays.asList("Pomo", "Indigo", "Orange/Green"),
                new int[][]{{1450}, {1450}, {1450}}
        ));

        // 10. Calla
        products.add(new Product(
                10, "Calla", R.drawable.calla_green,
                STANDARD_SIZES,
                Arrays.asList("Green", "Blue"),
                new int[][]{{1550}, {1550}}
        ));

        // 11. Palazzo
        products.add(new Product(
                11, "Palazzo", R.drawable.palazzo_indigo,
                PALAZZO_SIZES,
                Arrays.asList("Indigo", "Green"),
                new int[][]{{975}, {975}}
        ));

        // 12. Everybody Pants
        products.add(new Product(
                12, "Everybody Pants", R.drawable.everybody_pomo,
                STANDARD_SIZES,
                Arrays.asList("Indigo", "Pomo"),
                new int[][]{{1200}, {1200}}
        ));

        // 13. BLU Pants
        products.add(new Product(
                13, "BLU Pants", R.drawable.blu_pants,
                STANDARD_SIZES,
                Arrays.asList("None"),
                new int[][]{{850}}
        ));

        // 14. Hair Band
        products.add(new Product(
                14, "Hair Band", R.drawable.bands,
                FREE_SIZE,
                Arrays.asList("None"),
                new int[][]{{200}}
        ));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
}
