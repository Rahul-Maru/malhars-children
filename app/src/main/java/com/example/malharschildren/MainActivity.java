package com.example.malharschildren;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.malharschildren.adapter.ProductAdapter;
import com.example.malharschildren.databinding.ActivityMainBinding;
import com.example.malharschildren.viewmodel.CartViewModel;

/**
 * Main Activity using modern Android architecture:
 * - ViewBinding for efficient view access
 * - ViewModel for business logic separation
 * - RecyclerView for efficient list rendering
 * - Material Design 3 components
 * - LiveData for reactive UI updates
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CartViewModel viewModel;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Setup UI
        setupPaymentDropdown();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
    }

    private void setupPaymentDropdown() {
        String[] paymentMethods = getResources().getStringArray(R.array.payDropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                paymentMethods
        );
        binding.paymentMethod.setAdapter(adapter);
        binding.paymentMethod.setText(paymentMethods[0], false);
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(viewModel.getAllProducts(), viewModel);
        binding.productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.productsRecyclerView.setAdapter(adapter);
    }

    private void setupObservers() {
        // Observe total amount changes
        viewModel.getTotalAmount().observe(this, total -> {
            binding.totalAmount.setText(String.valueOf(total));
        });

        // Observe cart item count changes
        viewModel.getTotalItems().observe(this, count -> {
            binding.cartCount.setText(String.valueOf(count));
        });
    }

    private void setupClickListeners() {
        // Reset button
        binding.resetButton.setOnClickListener(v -> {
            viewModel.resetCart();
            binding.customerName.setText("");
            binding.customerEmail.setText("");
            binding.customerPhone.setText("");
            binding.paymentMethod.setText(getResources().getStringArray(R.array.payDropdown)[0], false);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Cart reset", Toast.LENGTH_SHORT).show();
        });

        // Submit button
        binding.submitButton.setOnClickListener(v -> submitOrder());
    }

    private void submitOrder() {
        // Get customer information
        String name = binding.customerName.getText() != null ?
                binding.customerName.getText().toString().trim() : "";
        String email = binding.customerEmail.getText() != null ?
                binding.customerEmail.getText().toString().trim() : "";
        String phone = binding.customerPhone.getText() != null ?
                binding.customerPhone.getText().toString().trim() : "";
        String payment = binding.paymentMethod.getText() != null ?
                binding.paymentMethod.getText().toString() : "Cash";

        // Validate customer information
        if (name.isEmpty()) {
            binding.customerName.setError("Name is required");
            binding.customerName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            binding.customerEmail.setError("Email is required");
            binding.customerEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            binding.customerPhone.setError("Phone is required");
            binding.customerPhone.requestFocus();
            return;
        }

        // Check if cart has items
        Integer totalItems = viewModel.getTotalItems().getValue();
        if (totalItems == null || totalItems == 0) {
            Toast.makeText(this, "Cart is empty. Please add items before submitting.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Update customer in ViewModel
        viewModel.updateCustomer(name, email, phone, payment);

        // Generate invoice
        String invoice = viewModel.generateInvoice();

        // Send email intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_TEXT, invoice);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_CC, new String[]{"malharschildren@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Invoice from Malhar's Children");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
