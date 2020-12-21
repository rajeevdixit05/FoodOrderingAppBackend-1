package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.business.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/payment")
public class PaymentController {

  @Autowired PaymentService paymentService;

  @CrossOrigin
  @RequestMapping(
      path = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaymentListResponse> getPaymentModes() {

    // Fetch all payment modes as a list of Payment Entities from the database
    List<PaymentEntity> paymentEntities = paymentService.getAllPaymentMethods();

    // Map list of payment entities to Payment List Response object
    PaymentListResponse response = new PaymentListResponse();
    paymentEntities.forEach(
        paymentEntity ->
            response.addPaymentMethodsItem(
                new PaymentResponse()
                    .id(UUID.fromString(paymentEntity.getUuid()))
                    .paymentName(paymentEntity.getPaymentName())));

    // Return response with right HttpStatus
    if (response.getPaymentMethods().isEmpty()) {
      return new ResponseEntity<PaymentListResponse>(response, HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<PaymentListResponse>(response, HttpStatus.OK);
    }
  }
}
