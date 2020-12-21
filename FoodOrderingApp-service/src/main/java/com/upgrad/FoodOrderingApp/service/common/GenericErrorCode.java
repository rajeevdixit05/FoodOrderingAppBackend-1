package com.upgrad.FoodOrderingApp.service.common;

import java.util.HashMap;
import java.util.Map;

public enum GenericErrorCode implements ErrorCode {

    /**
     * Error message: <b>An unexpected error occurred. Please contact System Administrator</b><br>
     * <b>Cause:</b> This error could have occurred due to undetermined runtime errors.<br>
     * <b>Action: None</b><br>
     */
    GEN_001("GEN-001", "An unexpected error occurred. Please contact System Administrator"),
    SGR_001("SGR-001", "This contact number is already registered! Try other contact number."),
    SGR_002("SGR-002", "Invalid email-id format!"),
    SGR_003("SGR-003", "Invalid contact number!"),
    SGR_004("SGR-004", "Weak password!"),
    SGR_005("SGR-005", "Except last name all fields should be filled"),
    ATH_001("ATH-001", "This contact number has not been registered!"),
    ATH_002("ATH-002", "Invalid Credentials"),
    ATH_003("ATH-003", "Incorrect format of decoded customer name and password"),
    ATH_004("ATH-003", "Prefix 'Basic ' missing on Authentication Token"),
    ATHR_001("ATHR-001", "Customer is not Logged in."),
    ATHR_002("ATHR-002", "Customer is logged out. Log in again to access this endpoint."),
    ATHR_003("ATHR-003", "Your session is expired. Log in again to access this endpoint."),
    ATHR_004("ATHR-004", "You are not authorized to view/update/delete any one else's address"),
    ATHR_005("ATHR-005", "Prefix 'Bearer ' missing on Access/Authorization token"),
    UCR_001("UCR-001", "Weak password!"),
    UCR_002("UCR-002", "First name field should not be empty"),
    UCR_003("UCR-003", "No field should be empty"),
    UCR_004("UCR-004", "Incorrect old password!"),
    SAR_001("SAR-001", "No field can be empty"),
    SAR_002("SAR-002", "Invalid pincode"),
    ANF_003("ANF-003", "No address by this id"),
    ANF_002("ANF-002", "No state by this id"),
    ANF_005("ANF-005", "Address id can not be empty"),
    CNF_001("CNF-001", "Category id field should not be empty"),
    CNF_002("CNF-002", "No category by this id"),
    CPF_001("CPF-001", "No coupon by this name"),
    CPF_002("CPF-002", "Coupon name field should not be empty"),
    RNF_003("RNF-003", "Restaurant name field should not be empty"),
    RNF_002("RNF-002", "Restaurant id field should not be empty"),
    RNF_001("RNF-001", "No restaurant by this id"),
    IRE_001("IRE-001", "Restaurant should be in the range of 1 to 5"),
    PNF_002("PNF-002", "No payment method found by this id"),
    INF_001("INF-001", "No item by this id exist");

    private static final Map<String, GenericErrorCode> LOOKUP = new HashMap<String, GenericErrorCode>();

    static {
        for (final GenericErrorCode enumeration : GenericErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;

    private final String defaultMessage;

    private GenericErrorCode(final String code, final String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }

}
