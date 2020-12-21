# FoodOrderingApp-Backend
## REST API Endpoints

In this project, we will be developing from scratch REST API endpoints of various functionalities required for the web app FoodOrderingApp. In order to observe the functionalities of the endpoints, we will create the FoodOrderingApp UI using React to interact with and store the data in the PostgreSQL database. In order to observe the functionality of the endpoints, we will use the Swagger user interface and store the data in the PostgreSQL database. Also, the project has to be implemented using Java Persistence API (JPA).

## New Features!
The following API endpoints are implemented in respective classes:
### Rest API Endpoints-I
1. **signup -"/customer/signup"** This endpoint is used to register a new user in the web Application.
2. **login- "/customer/login"** This endpoint is used for user authentication.Customer authenticates in the application and after successful authentication, JWT token is given to a customer.
3. **logout - "/customer/logout"** This endpoint is used to sign out from the web Application.
4. **Update - “/customer”** This endpoint requests for all the attributes in “UpdateCustomerRequest” about the customer.
5. **Change Password - “/customer/password”** This endpoint changes the user password.
6. **Save Address - “/address”** Returns uuid of the address saved and message “ADDRESS SUCCESSFULLY REGISTERED” in the JSON response with the corresponding HTTP status.
7. **Get All Saved Addresses -“/address/customer”**  Returns the list of saved address in descending order of their saved time in the Json response with the corresponding HTTP status.
8. **Delete Saved Address - “/address/{address_id}”** Returns uuid of the address deleted and message “ADDRESS DELETED SUCCESSFULLY” in the JSON response with the corresponding HTTP status.
9. **Get All States - “/states”** When any customer tries to access this endpoint, it should retrieve all the states present in the database and display the response in a JSON format with the corresponding HTTP status.
### Rest API Endpoints-II
10. **Get All Restaurants - "/restaurant"** When any customer tries to access this endpoint, it should retrieve all the restaurants in order of their ratings and display the response in a JSON format with the corresponding HTTP status.
11. **Get Restaurant/s by Name - “/restaurant/name/{reastaurant_name}”** This endpoint must request the following value from the customer as a path variable: Restaurant name - String
12. **Get Restaurants by Category Id “/restaurant/category/{category_id}”** Returns Category UUID - String Within each restaurant, the list of categories should be displayed in a categories string, in alphabetical order and the items shouldn’t be displayed.
13. **Get Restaurant by Restaurant ID - “/api/restaurant/{restaurant_id}”** The restaurant detail should have all the items it contains grouped by their categories in alphabetical order.
14. **Update Restaurant Details- “/api/restaurant/{restaurant_id}”** If the restaurant id entered by the customer matches any restaurant in the database, it should update that restaurant’s rating in the database along with the number of customers who have rated it. Then return the uuid of the restaurant updated and message “RESTAURANT RATING UPDATED SUCCESSFULLY” in the JSON response with the corresponding HTTP status.
15. **Get Top 5 Items by Popularity - “/item/restaurant/{restaurant_id}** If the restaurant id entered by the customer matches any restaurant in the database, it should retrieve the top five items of that restaurant based on the number of times that item was ordered and then display the response in a JSON format with the corresponding HTTP status.
16. **Get All Categories - “/category”** it should retrieve all the categories present in the database, ordered by their name and display the response in a JSON format with the corresponding HTTP status.
17. **Get Category by Id - “/category/{category_id}”** If the category id entered by the customer matches any category in the database, it should retrieve that category with all items within that category and then display the response in a JSON format with the corresponding HTTP status. Also, the name searched should not be case sensitive.
### Rest API Endpoints-III
18. **Get Coupon by Coupon Name - “/order/coupon/{coupon_name}”** If the coupon name entered by the customer matches any coupon in the database, retrieve the coupon details and display the response in a JSON format with the corresponding HTTP status.
19. **Get Past Orders of User - “/order”** retrieve all the past orders from the customer sorted by their order date, with the newest order first, and return them in a JSON format with the corresponding HTTP status.
20. **Save Order - “/order”** This endpoint rests for all the attributes in “SaveOrderRequest” from the customer.
21. **Get Payment Methods - “/payment”** Retrieve all the payment methods and return them in the JSON format with the corresponding HTTP status.

> The overall goal of this project is to provide a backend for Food Ordering Web Application
