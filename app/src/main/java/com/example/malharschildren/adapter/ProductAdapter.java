package com.example.malharschildren.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.malharschildren.databinding.ItemProductBinding;
import com.example.malharschildren.model.Product;
import com.example.malharschildren.viewmodel.CartViewModel;

import java.util.List;

/**
 * RecyclerView adapter for displaying products in a list
 * Uses ViewBinding for efficient view access
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> products;
    private final CartViewModel viewModel;

    public ProductAdapter(List<Product> products, CartViewModel viewModel) {
        this.products = products;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;
        private Product product;
        private int selectedSizeIndex = 0;
        private int selectedVariantIndex = 0;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            this.product = product;

            // Set product name and image
            binding.productName.setText(product.getName());
            binding.productImage.setImageResource(product.getImageRes());

            // Setup size spinner
            ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(
                    binding.getRoot().getContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    product.getSizes()
            );
            binding.sizeSpinner.setAdapter(sizeAdapter);
            binding.sizeSpinner.setText(product.getSizes().get(0), false);

            // Setup variant spinner
            ArrayAdapter<String> variantAdapter = new ArrayAdapter<>(
                    binding.getRoot().getContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    product.getVariants()
            );
            binding.variantSpinner.setAdapter(variantAdapter);
            binding.variantSpinner.setText(product.getVariants().get(0), false);

            // Set initial price
            updatePrice();

            // Size selection listener
            binding.sizeSpinner.setOnItemClickListener((parent, view, position, id) -> {
                selectedSizeIndex = position;
                updateFromCart();
                updatePrice();
            });

            // Variant selection listener
            binding.variantSpinner.setOnItemClickListener((parent, view, position, id) -> {
                selectedVariantIndex = position;
                updateFromCart();
                updatePrice();
            });

            // Increment button
            binding.incrementButton.setOnClickListener(v -> {
                viewModel.incrementTempQuantity(product.getId(), selectedSizeIndex, selectedVariantIndex);
                updateQuantityDisplay();
            });

            // Decrement button
            binding.decrementButton.setOnClickListener(v -> {
                viewModel.decrementTempQuantity(product.getId(), selectedSizeIndex, selectedVariantIndex);
                updateQuantityDisplay();
            });

            // Update cart button
            binding.updateButton.setOnClickListener(v -> {
                int quantity = viewModel.getTempQuantity(product.getId(), selectedSizeIndex, selectedVariantIndex);
                String priceText = binding.priceEdit.getText() != null ?
                        binding.priceEdit.getText().toString() : "";

                if (quantity == 0) {
                    Toast.makeText(v.getContext(), "Nothing to update", Toast.LENGTH_SHORT).show();
                    return;
                }

                int price;
                try {
                    price = Integer.parseInt(priceText);
                } catch (NumberFormatException e) {
                    price = product.getDefaultPrice(selectedVariantIndex, selectedSizeIndex);
                }

                viewModel.updateCart(product.getId(), selectedSizeIndex, selectedVariantIndex, quantity, price);

                String variantName = product.getVariants().get(selectedVariantIndex);
                String sizeName = product.getSizes().get(selectedSizeIndex);
                String variant = variantName.equalsIgnoreCase("none") ? "" : variantName + " ";
                String size = sizeName.equalsIgnoreCase("free size") ? "" : " (" + sizeName + ")";

                Toast.makeText(v.getContext(),
                        "Updated " + variant + product.getName() + size + " qty to " + quantity,
                        Toast.LENGTH_SHORT).show();
            });

            // Initialize from cart if exists
            updateFromCart();
        }

        private void updateFromCart() {
            int quantity = viewModel.getTempQuantity(product.getId(), selectedSizeIndex, selectedVariantIndex);
            binding.quantityText.setText(String.valueOf(quantity));
        }

        private void updatePrice() {
            int defaultPrice = product.getDefaultPrice(selectedVariantIndex, selectedSizeIndex);
            binding.priceEdit.setText(String.valueOf(defaultPrice));
        }

        private void updateQuantityDisplay() {
            int quantity = viewModel.getTempQuantity(product.getId(), selectedSizeIndex, selectedVariantIndex);
            binding.quantityText.setText(String.valueOf(quantity));
        }
    }
}
