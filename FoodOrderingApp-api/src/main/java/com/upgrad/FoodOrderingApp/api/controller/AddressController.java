package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.AddressService;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.common.AppUtils;
import com.upgrad.FoodOrderingApp.service.common.UnexpectedException;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
public class AddressController {

    @Autowired private CustomerService customerService;

    @Autowired private AddressService addressService;

    /**
     * This Method takes Customer's SaveAddress request, stores customer address
     *
     * @param saveAddressRequest
     * @return ResponseEntity with Address Id
     * @throws SaveAddressException on invalid saveAddress request
     * @throws AddressNotFoundException on invalid state id
     * @throws AuthorizationFailedException on invalid customer access-token
     * @throws UnexpectedException on any other errors
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(
            @RequestHeader("authorization") final String authorization,
            @RequestBody(required = false) final SaveAddressRequest saveAddressRequest)
            throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {

        // Get Bearer Authorization Token
        final String accessToken = AppUtils.getBearerAuthToken(authorization);

        // Get customer details on successful authentication of accessToken
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // Generate Address entity for insert
        AddressEntity address = new AddressEntity();
        address.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        address.setLocality(saveAddressRequest.getLocality());
        address.setCity(saveAddressRequest.getCity());
        address.setPincode(saveAddressRequest.getPincode());
        address.setUuid(UUID.randomUUID().toString());
        address.setActive(1);
        StateEntity state = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        address.setCustomers(customerEntity);

        // Store Address entity in the database
        AddressEntity savedAddress = addressService.saveAddress(address, state);

        // Map persisted Address Entity to Response Object
        SaveAddressResponse addressResponse =
                new SaveAddressResponse()
                        .id(savedAddress.getUuid())
                        .status("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SaveAddressResponse>(addressResponse, HttpStatus.CREATED);
    }

    /**
     * This method takes authorization from customer, returns all address of profile
     *
     * @param authorization  access token as request header
     * @return ResponseEntity with list of Addresses
     * @throws AuthorizationFailedException on invalid customer access-token
     * @throws UnexpectedException on any other errors
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/address/customer",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddresses(
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        // Get Bearer Authorization Token
        final String accessToken = AppUtils.getBearerAuthToken(authorization);

        // Get customer details on successful authentication of accessToken
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // Retrieve addresses from database
        List<AddressEntity> sortedAddress = addressService.getAllAddress(customerEntity);

        List<AddressList> addressesList = new ArrayList<>();

        // Map retrieved Address Entity to Response Object List
        sortedAddress.forEach(
                address -> {
                    AddressListState addressListState = new AddressListState();
                    addressListState.setId(UUID.fromString(address.getState().getUuid()));
                    addressListState.setStateName(address.getState().getStateName());

                    AddressList addressList =
                            new AddressList()
                                    .id(UUID.fromString(address.getUuid()))
                                    .flatBuildingName(address.getFlatBuilNo())
                                    .city(address.getCity())
                                    .locality(address.getLocality())
                                    .pincode(address.getPincode())
                                    .state(addressListState);
                    addressesList.add(addressList);
                });

        AddressListResponse addressListResponse = new AddressListResponse().addresses(addressesList);
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    /**
     * This Method takes address id from customer, delete address in profile
     *
     * @param authorization Customer access token in header
     * @param addressId Address id to be deleted
     * @return ResponseEntity with id of address deleted
     * @throws AddressNotFoundException if invalid address id
     * @throws AuthorizationFailedException if invalid customer access-token
     * @throws UnexpectedException if any other errors
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/address/{address_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(
            @RequestHeader("authorization") final String authorization,
            @PathVariable(value = "address_id") final String addressId)
            throws AuthorizationFailedException, AddressNotFoundException {
        // Get Bearer Authorization Token
        final String accessToken = AppUtils.getBearerAuthToken(authorization);

        // Get customer details on successful authentication of accessToken
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        // Get address entity of address to be deleted
        AddressEntity address = addressService.getAddressByUUID(addressId, customerEntity);

        AddressEntity deletedAddress;

        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse();

        if (address
                .getOrders()
                .isEmpty()) { // If address have no history of orders, delete the address from database
            deletedAddress = addressService.deleteAddress(address);
        } else { // If address have history of orders, soft delete (deactivate) the address from
            // database
            address.setActive(0);
            deletedAddress = addressService.deactivateAddress(address);
        }

        // Map deleted Address status & id to Response Object
        deleteAddressResponse.status("ADDRESS DELETED SUCCESSFULLY");
        deleteAddressResponse.id(UUID.fromString(deletedAddress.getUuid()));
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    /**
     * This method  returns all states
     * @return ResponseEntity with list of States
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/states",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {

        // Retrieve all states
        List<StateEntity> states = addressService.getAllStates();

        // Map retrieved State Entity with Response Object List
        if (!states.isEmpty()) {
            List<StatesList> statesList = new LinkedList<>();
            states.forEach(
                    state -> {
                        StatesList stateList = new StatesList();
                        stateList.setId(UUID.fromString(state.getUuid()));
                        stateList.setStateName(state.getStateName());

                        statesList.add(stateList);
                    });
            StatesListResponse statesListResponse = new StatesListResponse().states(statesList);
            return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
        } else return new ResponseEntity<StatesListResponse>(new StatesListResponse(), HttpStatus.OK);
    }
}
