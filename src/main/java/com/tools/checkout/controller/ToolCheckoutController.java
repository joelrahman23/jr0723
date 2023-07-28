package com.tools.checkout.controller;

import com.tools.checkout.model.RentalAgreement;
import com.tools.checkout.model.ToolCheckoutRequest;
import com.tools.checkout.service.RentalAgreementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ToolCheckoutController {

    private final RentalAgreementService rentalAgreementService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody ToolCheckoutRequest request) {
        try {
            validateRequest(request);

            RentalAgreement rentalAgreement = calculateRentalAgreement(request);

            rentalAgreementService.printRentalAgreement(rentalAgreement);

            return ResponseEntity.ok("Checkout successful. Rental Agreement details printed to console.");
        } catch (IllegalArgumentException e) {
           log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validateRequest(ToolCheckoutRequest request) {

        if(request.getRentalDays() < 1) {
            throw new IllegalArgumentException("Error: Rental day count is not 1 or greater");
        }

        if(request.getDiscountPercent() < 0 || request.getDiscountPercent()>100) {
            throw new IllegalArgumentException("Error: Discount percent is not in the range 0-10");
        }

    }

    private RentalAgreement calculateRentalAgreement(ToolCheckoutRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");

        String toolCode = request.getToolCode();
        LocalDate checkoutDate = LocalDate.parse(request.getCheckoutDate(), formatter);
        int rentalDays = request.getRentalDays();
        int discountPercent = request.getDiscountPercent();
        return rentalAgreementService.calculateCharges(toolCode, checkoutDate, rentalDays, discountPercent);
    }
}

