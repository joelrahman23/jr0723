package com.tools.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToolCheckoutRequest {
    private String toolCode;
    private String checkoutDate;
    private int rentalDays;
    private int discountPercent;
}
