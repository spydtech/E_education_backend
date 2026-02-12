package com.Eonline.Education.modals;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull(message = "Amount is required")
    private Long amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Receipt is required")
    private String receipt;
    private String paymentMethod;

    // Add other necessary fields as needed

    // Constructor for initializing fields
    public OrderRequest(Long amount, String currency, String receipt, String paymentMethod) {
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
        this.paymentMethod = paymentMethod;
    }

    // Add getters and setters if needed

    @Override
    public String toString() {
        return "OrderRequest{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                ", receipt='" + receipt + '\'' +
                ". paymentMethod='" + paymentMethod + '\'' +
                // Add other fields here for toString() representation
                '}';
    }
}




































































































//package com.Eonline.Education.modals;
//
//import lombok.Data;
//
//@Data
//public class OrderRequest {
//    private Long amount;
//    private String currency;
//    private String receipt;
//    // Add other necessary fields as needed
//
//    public Long getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Long amount) {
//        this.amount = amount;
//    }
//
//    public String getCurrency() {
//        return currency;
//    }
//
//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }
//
//    public String getReceipt() {
//        return receipt;
//    }
//
//    public void setReceipt(String receipt) {
//        this.receipt = receipt;
//    }
//
//    // Add getters and setters for other fields as needed
//
//    @Override
//    public String toString() {
//        return "OrderRequest{" +
//                "amount=" + amount +
//                ", currency='" + currency + '\'' +
//                ", receipt='" + receipt + '\'' +
//                // Add other fields here for toString() representation
//                '}';
//    }
//}
