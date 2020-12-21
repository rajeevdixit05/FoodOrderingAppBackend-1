package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.common.AppConstants;
import com.upgrad.FoodOrderingApp.service.common.UnexpectedException;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.*;

@Service
public class AddressService {

    @Autowired private AddressDao addressDao;

    @Autowired private StateDao stateDao;

    /**
     * Method takes AddressEntity/ StateEntity and stores it on the database
     *
     * @param address New AddressEntity
     * @param state is StateEntity of address
     * @return Saved Address Entity
     * @throws SaveAddressException on invalid flat/locality/city/pincode on the input address entity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity address, StateEntity state)
            throws SaveAddressException {
        // Check if flat/locality/city/pincode is empty
        if (addressFieldsEmpty(address))
            throw new SaveAddressException(SAR_001.getCode(), SAR_001.getDefaultMessage());
        // Check if pincode is invalid
        if (!validPincode(address.getPincode())) {
            throw new SaveAddressException(SAR_002.getCode(), SAR_002.getDefaultMessage());
        }
        // Add state to the input address
        address.setState(state);
        try { // Store address on the database
            return addressDao.saveAddress(address);
        } catch (Exception dataIntegrityViolationException) {
            throw new UnexpectedException(GEN_001, dataIntegrityViolationException);
        }
    }

    /**
     * Method takes CustomerEntity and returns AddressEntity List
     *
     * @param customerEntity Customer entity
     * @return AddressEntity List of the customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) {

        // Retrieve list of customer addresses from database
        List<AddressEntity> addresses =
                customerEntity.getAddresses().stream()
                        .filter(address -> address.getActive() == AppConstants.ONE_1)
                        .sorted(Comparator.comparing(AddressEntity::getId, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

        return addresses;
    }

    /**
     * Method takes no input and returns StateEntity List
     *
     * @return AddressEntity List of the States
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getAllStates() {
        // Retrieve list of States from database
        return stateDao.getAllStates();
    }

    /**
     * Method takes AddressId/ CustomerEntity and return AddressEntity from the database
     *
     * @param addressId Address id to be deleted
     * @param customerEntity is customer details
     * @return AddressEntity of addressId
     * @throws AddressNotFoundException on invalid address id
     * @throws AuthorizationFailedException on invalid customer access-token
     * @throws UnexpectedException on any other errors
     */
    public AddressEntity getAddressByUUID(String addressId, CustomerEntity customerEntity)
            throws AddressNotFoundException, AuthorizationFailedException, UnexpectedException {
        try { // Throw error if addressId is null
            if (addressId == null) {
                throw new AddressNotFoundException(ANF_005.getCode(), ANF_005.getDefaultMessage());
            }
            AddressEntity address = addressDao.getAddressByAddressId(addressId);
            if (address == null) { // Throw error if address not found matching addressId
                throw new AddressNotFoundException(ANF_003.getCode(), ANF_003.getDefaultMessage());
            }
            AddressEntity customerAddress =
                    customerEntity.getAddresses().stream()
                            .filter(addressEntity -> addressEntity.getUuid().equals(address.getUuid()))
                            .findFirst()
                            .orElse(null);
            if (customerAddress == null) { // Throw error if address doesn't belong to logged in customer
                throw new AuthorizationFailedException(ATHR_004.getCode(), ATHR_004.getDefaultMessage());
            }
            return address;
        } catch (NullPointerException npe) { // Throw error if unexpected error
            throw new UnexpectedException(GEN_001, npe);
        }
    }

    /**
     * Method takes AddressEntity and delete AddressEntity from the database
     *
     * @param address is AddressEntity to be deleted
     * @return AddressEntity of the deleted address
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(final AddressEntity address) {
        return addressDao.deleteAddress(address);
    }

    /**
     * Method takes AddressEntity and soft delete AddressEntity from the database
     *
     * @param address is AddressEntity to be soft deleted
     * @return AddressEntity of the soft deleted address
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deactivateAddress(final AddressEntity address) {
        return addressDao.deactivateAddress(address);
    }

    /**
     * Method takes stateUUID and return StateEntity from the database
     *
     * @param stateUUID State id to retrieved
     * @return StateEntity of stateUUID
     * @throws AddressNotFoundException on invalid stateUUID
     */
    public StateEntity getStateByUUID(final String stateUUID) throws AddressNotFoundException {
        // Retrieve StateEntity from database
        StateEntity state = stateDao.findStateByUUID(stateUUID);
        if (state == null) { // Throw error if State not found
            throw new AddressNotFoundException(ANF_002.getCode(), ANF_002.getDefaultMessage());
        }
        return state;
    }

    /**
     * Method takes addressEntity and validates for mandatory fields
     *
     * @param address AddressEntity to be validated
     * @return validation true/false
     */
    private boolean addressFieldsEmpty(AddressEntity address) {
        return (address.getFlatBuilNo() == null
                || address.getLocality() == null
                || address.getCity() == null
                || address.getPincode() == null
                || address.getFlatBuilNo().isEmpty()
                || address.getLocality().isEmpty()
                || address.getCity().isEmpty()
                || address.getPincode().isEmpty());
    }

    /**
     * Method takes pincode and validates using regex
     *
     * @param pincode regex to be validated
     * @return validation true/false
     */
    private boolean validPincode(String pincode) {
        Pattern p = Pattern.compile("\\d{6}\\b");
        Matcher m = p.matcher(pincode);
        return (m.find() && m.group().equals(pincode));
    }
}
