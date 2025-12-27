package com.example.malharschildren.model;

/**
 * Represents customer information for invoice generation
 */
public class Customer {
    private String name;
    private String email;
    private String phone;
    private String paymentMethod;

    public Customer() {
        this.name = "";
        this.email = "";
        this.phone = "";
        this.paymentMethod = "Cash";
    }

    public Customer(String name, String email, String phone, String paymentMethod) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty();
    }
}
