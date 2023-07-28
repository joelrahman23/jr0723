package com.tools.checkout.model;

import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RentalAgreement {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    private ImmutableMap<String, Tool> toolMap = ImmutableMap.of(
        "CHNS", new Tool("CHNS", "Chainsaw", "Stihl"),
        "LADW", new Tool("LADW", "Ladder", "Werner"),
        "JAKD", new Tool("JAKD", "Jackhammer", "DeWalt"),
        "JAKR", new Tool("JAKR", "Jackhammer", "Ridgid"));


    private ImmutableMap<String, ToolRental> rentalMap = ImmutableMap.of(
        "Ladder", new ToolRental("Ladder", 1.99, true, true, false),
        "Chainsaw", new ToolRental("Chainsaw", 1.49, true, false, true),
        "Jackhammer", new ToolRental("Jackhammer", 2.99, true, false, false));
}
