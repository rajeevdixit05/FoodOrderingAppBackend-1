package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.common.AppConstants;
import com.upgrad.FoodOrderingApp.service.common.UnexpectedException;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.UUID;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.*;

@Service
public class CustomerService {

    @Autowired private CustomerDao customerDao;

    @Autowired private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method takes CustomerEntity and stores it on the database
     *
     * @param customerEntity New CustomerEntity
     * @return Saved Customer Entity
     * @throws SignUpRestrictedException on invalid email/contact/password on the input customer
     *     entity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity)
            throws SignUpRestrictedException {
        // Check if Email is Valid (right format)
        if (!isValidEmail(customerEntity.getEmail())) {
            throw new SignUpRestrictedException(SGR_002.getCode(), SGR_002.getDefaultMessage());
        }

        // Check if Contact Is Valid
        if (!isValidContactNumber(customerEntity.getContactNumber())) {
            throw new SignUpRestrictedException(SGR_003.getCode(), SGR_003.getDefaultMessage());
        }

        // Check is password is valid and meets minimum strength requirements
        if (!isStrongPassword(customerEntity.getPassword())) {
            throw new SignUpRestrictedException(SGR_004.getCode(), SGR_004.getDefaultMessage());
        }

        // Encrupt customer password
        final String[] encryptedText =
                passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        try {
            // Store customer on the database
            return customerDao.saveCustomer(customerEntity);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            if (dataIntegrityViolationException.getCause() instanceof ConstraintViolationException) {
                String constraintName =
                        ((ConstraintViolationException) dataIntegrityViolationException.getCause())
                                .getConstraintName();

                // A customer with the same contact details already exists (Duplicate Customer)
                if (StringUtils.containsIgnoreCase(constraintName, "customer_contact_number_key")) {
                    throw new SignUpRestrictedException(SGR_001.getCode(), SGR_001.getDefaultMessage());
                } else {
                    throw new UnexpectedException(GEN_001, dataIntegrityViolationException);
                }
            } else {
                throw new UnexpectedException(GEN_001, dataIntegrityViolationException);
            }
        } catch (Exception exception) {
            throw new UnexpectedException(GEN_001, exception);
        }
    }

    /**
     * Method takes customer's login information and generates & stores customer's authentication
     *
     * @param contactNumber Customer's contact number
     * @param password Customer's password
     * @return CustomerAuthEntity (with access token)
     * @throws AuthenticationFailedException on invalid/incorrect credentials
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(final String contactNumber, final String password)
            throws AuthenticationFailedException {

        // Get customer with input contact number from the database
        final CustomerEntity customerEntity = getCustomerByContactNumber(contactNumber);

        // No such customer with input contact number
        if (customerEntity == null) {
            throw new AuthenticationFailedException(ATH_001.getCode(), ATH_001.getDefaultMessage());
        }

        // Encrypt input password
        final String encryptedPassword =
                PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        // Check encrypted password with the password stored on the database (also encrypted)
        if (encryptedPassword != null && encryptedPassword.equals(customerEntity.getPassword())) {

            // Generate acccess token for customer (JWT)
            final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            final CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setCustomer(customerEntity);
            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            final ZonedDateTime loginAt = ZonedDateTime.now();
            final ZonedDateTime expiresAt = loginAt.plusHours(AppConstants.EIGHT_8);
            customerAuthEntity.setLoginAt(loginAt);
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setAccessToken(
                    jwtTokenProvider.generateToken(customerEntity.getUuid(), loginAt, expiresAt));
            return customerDao.saveCustomerAuthentication(customerAuthEntity);

        }
        // Supplied password and stored password don't match
        else {
            throw new AuthenticationFailedException(ATH_002.getCode(), ATH_002.getDefaultMessage());
        }
    }

    /**
     * method used by customer to log out from the application.
     *
     * @param accessToken accestoken through which used has logged in
     * @return logout customerEntiry object
     * @throws AuthorizationFailedException exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity logout(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerByAccessToken(accessToken);
        if (customerAuthEntity == null) {
            //if access token does not exist then throw ATHR-001
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt() != null) {
            //if customer with this accestoken has already logged out then throw ATHR-002
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            //if expiry date of this token is already past the current date then throw ATHR-003
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        } else {
            //update the logout date for this accesstoken
            final ZonedDateTime logoutAtDate = ZonedDateTime.now();
            customerAuthEntity.setLogoutAt(logoutAtDate);
            customerDao.updateLogOutDate(customerAuthEntity);
            return customerAuthEntity.getCustomer();
        }
    }

    /**
     * Method takes customer's access token and fetches his details
     *
     * @param accessToken Customer's accessToken
     * @return CustomerAuthEntity with customer's authentication information
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomer(final String accessToken) throws AuthorizationFailedException {
        final CustomerAuthEntity customerAuthEntity =
                getCustomerAuthEntity(accessToken);
        return customerAuthEntity.getCustomer();
    }

    /**
     * Method takes updated CustomerEntity and stores it to the database
     *
     * @param customerEntity Updated CustomerEntity
     * @return Updated CustomerEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity) {
        return customerDao.updateCustomer(customerEntity);
    }

    /**
     * Method takes customer's old & new passwords and updates it in the database
     *
     * @param oldPassword Customer's old password
     * @param newPassword Customer's new password
     * @param customerEntity CustomerEntity with old Password
     * @return Updated CustomerEntity with new password
     * @throws UpdateCustomerException on incorrect old password & invalid new password
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(
            final String oldPassword, final String newPassword, final CustomerEntity customerEntity)
            throws UpdateCustomerException {
        // Check password meets specified minimum requirements
        if (!isStrongPassword(newPassword)) {
            throw new UpdateCustomerException(UCR_001.getCode(), UCR_001.getDefaultMessage());
        } else {
            // Encrypt old password
            final String encryptedOldPassword =
                    PasswordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());

            // Check encrypted old password is correct/valid (authorize customer)
            if (encryptedOldPassword != null
                    && encryptedOldPassword.equals(customerEntity.getPassword())) {

                // Encrypt new password
                final String[] encryptedText = passwordCryptographyProvider.encrypt(newPassword);
                customerEntity.setSalt(encryptedText[0]);
                customerEntity.setPassword(encryptedText[1]);

                // Update customer with new password
                return customerDao.updateCustomer(customerEntity);
            } else {
                // Throw error on incorrect old password
                throw new UpdateCustomerException(UCR_004.getCode(), UCR_004.getDefaultMessage());
            }
        }
    }

    /**
     * helper method to check the authentication of user through accesstoken
     *
     * @param accessToken token of the customer
     * @return CustomerAuthentity object
     * @throws AuthorizationFailedException exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity getCustomerAuthEntity(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerByAccessToken(accessToken);
        if (customerAuthEntity == null) {
            //if access token does not exist then throw ATHR-001
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt() != null) {
            //if customer with this accestoken has already logged out then throw ATHR-002
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            //if expiry date of this token is already past the current date then throw ATHR-003
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return customerAuthEntity;
    }

    /**
     * Method takes customer's contact number as input and returns CustomerEntity
     *
     * @param contactNumber Customer's contact number
     * @return CustomerEntity with customer information
     */
    private CustomerEntity getCustomerByContactNumber(final String contactNumber) {
        return customerDao.getCustomerByContactNumber(contactNumber);
    }

    // This method users regular expressions to guage the strength of a user's
    // password returns password score

    /**
     * Check if password strength meets minimum specifications
     *
     * @param password Customer's password
     * @return true if password meets minimum requirements else false
     */
    private boolean isStrongPassword(final String password) {
        return password.matches(AppConstants.REG_EXP_PASSWD_UPPER_CASE_CHAR)
                && password.matches(AppConstants.REG_EXP_PASSWD_SPECIAL_CHAR)
                && password.matches(AppConstants.REG_EXP_PASSWD_DIGIT)
                && (password.length() > AppConstants.SEVEN_7);
    }

    /**
     * Check's if customer's contact number is valid
     *
     * @param contactNumber Customer's contact number
     * @return true if contact number is numeric and or length 10
     */
    private boolean isValidContactNumber(final String contactNumber) {
        return StringUtils.isNumeric(contactNumber)
                && (contactNumber.length() == AppConstants.NUMBER_10);
    }

    /**
     * Check's if customer's email is valid
     *
     * @param email Customer's email
     * @return true is email format is correct else false
     */
    private boolean isValidEmail(final String email) {
        return email.matches(AppConstants.REG_EXP_VALID_EMAIL);
    }

    /**
     * method used for updating customer details into database.
     *
     * @param customerEntity entity object
     * @throws AuthorizationFailedException exception for authorization
     * @throws UpdateCustomerException      exception for updating customer details.
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerDetails(CustomerEntity customerEntity) throws AuthorizationFailedException, UpdateCustomerException {
        //CustomerAuthEntity customerAuthEntity = getCustomerAuthEntity(accessToken);

        if (customerEntity.getFirstName().trim().isEmpty()) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }

        customerEntity.setFirstName(customerEntity.getFirstName());
        customerEntity.setLastName(customerEntity.getLastName());
        customerEntity = customerDao.createUpdateUser(customerEntity);
        return customerEntity;
    }
}
