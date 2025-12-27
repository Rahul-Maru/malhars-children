package com.example.malharschildren.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.malharschildren.data.ProductRepository;
import com.example.malharschildren.model.CartItem;
import com.example.malharschildren.model.Customer;
import com.example.malharschildren.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel for managing cart state and business logic
 * Separates business logic from UI, making code more testable and maintainable
 */
public class CartViewModel extends ViewModel {
    private final ProductRepository productRepository;
    private final MutableLiveData<List<CartItem>> cartItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> totalAmount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalItems = new MutableLiveData<>(0);
    private final MutableLiveData<Customer> customer = new MutableLiveData<>(new Customer());

    // Map to store temporary quantities: productId -> {sizeIndex -> {variantIndex -> quantity}}
    private final Map<Integer, Map<Integer, Map<Integer, Integer>>> tempQuantities = new HashMap<>();

    public CartViewModel() {
        this.productRepository = ProductRepository.getInstance();
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    public LiveData<Integer> getTotalAmount() {
        return totalAmount;
    }

    public LiveData<Integer> getTotalItems() {
        return totalItems;
    }

    public LiveData<Customer> getCustomer() {
        return customer;
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public int getTempQuantity(int productId, int sizeIndex, int variantIndex) {
        return tempQuantities.getOrDefault(productId, new HashMap<>())
                .getOrDefault(sizeIndex, new HashMap<>())
                .getOrDefault(variantIndex, 0);
    }

    public void setTempQuantity(int productId, int sizeIndex, int variantIndex, int quantity) {
        tempQuantities.computeIfAbsent(productId, k -> new HashMap<>())
                .computeIfAbsent(sizeIndex, k -> new HashMap<>())
                .put(variantIndex, Math.max(0, quantity));
    }

    public void incrementTempQuantity(int productId, int sizeIndex, int variantIndex) {
        int current = getTempQuantity(productId, sizeIndex, variantIndex);
        setTempQuantity(productId, sizeIndex, variantIndex, current + 1);
    }

    public void decrementTempQuantity(int productId, int sizeIndex, int variantIndex) {
        int current = getTempQuantity(productId, sizeIndex, variantIndex);
        if (current > 0) {
            setTempQuantity(productId, sizeIndex, variantIndex, current - 1);
        }
    }

    public void updateCart(int productId, int sizeIndex, int variantIndex, int quantity, int price) {
        if (quantity <= 0) {
            return;
        }

        Product product = productRepository.getProductById(productId);
        if (product == null) return;

        String sizeName = product.getSizes().get(sizeIndex);
        String variantName = product.getVariants().get(variantIndex);

        List<CartItem> currentCart = new ArrayList<>(cartItems.getValue());
        CartItem existingItem = findCartItem(currentCart, productId, sizeIndex, variantIndex);

        if (existingItem != null) {
            existingItem.setQuantity(quantity);
            existingItem.setPrice(price);
        } else {
            currentCart.add(new CartItem(
                    productId, product.getName(),
                    sizeIndex, sizeName,
                    variantIndex, variantName,
                    quantity, price
            ));
        }

        cartItems.setValue(currentCart);
        calculateTotals(currentCart);
    }

    private CartItem findCartItem(List<CartItem> items, int productId, int sizeIndex, int variantIndex) {
        for (CartItem item : items) {
            if (item.getProductId() == productId &&
                    item.getSizeIndex() == sizeIndex &&
                    item.getVariantIndex() == variantIndex) {
                return item;
            }
        }
        return null;
    }

    private void calculateTotals(List<CartItem> items) {
        int total = 0;
        int count = 0;

        for (CartItem item : items) {
            total += item.getTotalAmount();
            count += item.getQuantity();
        }

        totalAmount.setValue(total);
        totalItems.setValue(count);
    }

    public void resetCart() {
        cartItems.setValue(new ArrayList<>());
        totalAmount.setValue(0);
        totalItems.setValue(0);
        tempQuantities.clear();
        customer.setValue(new Customer());
    }

    public void updateCustomer(String name, String email, String phone, String paymentMethod) {
        Customer updatedCustomer = new Customer(name, email, phone, paymentMethod);
        customer.setValue(updatedCustomer);
    }

    public String generateInvoice() {
        Customer cust = customer.getValue();
        if (cust == null || !cust.isValid()) {
            return "";
        }

        List<CartItem> items = cartItems.getValue();
        if (items == null || items.isEmpty()) {
            return "";
        }

        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(cust.getName()).append(",\n\n");
        message.append("Thank you so much for shopping at Malhar's Children!\n\n");
        message.append("This is your invoice:\n\n");
        message.append("Name: ").append(cust.getName()).append("\n");
        message.append("Phone: ").append(cust.getPhone()).append("\n");
        message.append("Payment: ").append(cust.getPaymentMethod()).append("\n\n");

        int itemNumber = 0;
        for (CartItem item : items) {
            if (item.getQuantity() > 0) {
                itemNumber++;
                String variant = item.getVariantName().equalsIgnoreCase("none") ? "" : item.getVariantName() + " ";
                message.append("    ").append(itemNumber).append(". ")
                        .append(variant).append(item.getProductName())
                        .append(" X ").append(item.getQuantity()).append("\n");
                message.append("        Size: ").append(item.getSizeName()).append("\n");
                message.append("        Price: ₹").append(item.getPrice()).append("\n");
                message.append("        Amount: ₹").append(item.getTotalAmount()).append("\n\n");
            }
        }

        message.append("Grand Total: ₹").append(totalAmount.getValue()).append("\n\n\n");
        message.append("Warmly,");

        return message.toString();
    }
}
