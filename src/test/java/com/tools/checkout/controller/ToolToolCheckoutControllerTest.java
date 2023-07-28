package com.tools.checkout.controller;

import com.tools.checkout.model.ToolCheckoutRequest;
import com.tools.checkout.service.RentalAgreementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class ToolToolCheckoutControllerTest {

   @InjectMocks
   private ToolCheckoutController toolCheckoutController;

   @Mock
   private RentalAgreementService rentalAgreementService;

    @Test
    public void testCheckoutWithInvalidDiscount() {
        ToolCheckoutRequest request = new ToolCheckoutRequest("JAKR", "9/3/15", 5, 101);
        ResponseEntity<String> response = toolCheckoutController.checkout(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Discount percent is not in the range 0-10", response.getBody());
    }

    @Test
    public void testCheckoutForLadder() {
        ToolCheckoutRequest request = new ToolCheckoutRequest("LADW", "7/2/20", 3, 10);
        ResponseEntity<String> response = toolCheckoutController.checkout(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Checkout successful. Rental Agreement details printed to console.", response.getBody());
    }

    @Test
    public void testCheckoutForChainsaw() {
        ToolCheckoutRequest request = new ToolCheckoutRequest("CHNS", "7/2/15", 5, 25);
        ResponseEntity<String> response = toolCheckoutController.checkout(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Checkout successful. Rental Agreement details printed to console.", response.getBody());
    }

    @Test
    public void testCheckoutForJackhammerWithZeroDiscount() {
        ToolCheckoutRequest request = new ToolCheckoutRequest("JAKD", "9/3/15", 6, 0);
        ResponseEntity<String> response = toolCheckoutController.checkout(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Checkout successful. Rental Agreement details printed to console.", response.getBody());
    }

    @Test
    public void testCheckoutForJackhammerWithMaximumRentalDays() {
        ToolCheckoutRequest request = new ToolCheckoutRequest("JAKR", "7/2/15", 9, 0);
        ResponseEntity<String> response = toolCheckoutController.checkout(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Checkout successful. Rental Agreement details printed to console.", response.getBody());
    }

    @Test
    public void testCheckoutForJackhammerWithDiscount() {
        ToolCheckoutRequest request = new ToolCheckoutRequest("JAKR", "7/2/20", 4, 50);
        ResponseEntity<String> response = toolCheckoutController.checkout(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Checkout successful. Rental Agreement details printed to console.", response.getBody());
    }

}