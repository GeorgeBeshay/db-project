# Adopter APIs &amp; Services

This subtopic demonstrates the APIs provided for the Adopter users, in addition to discussing the services implemented
behind each API that is being used by the frontend application.

## Adopter APIs

Listed below the set of provided APIs, that are being utilized by the angular frontend application:

- **_ResponseEntity\<Adopter> signUp(@RequestBody Adopter adopter)_**
  Utilized by the frontend application to allow the adopter users to register an account,
  where the passed Adopter object contains the adopter's required information: email, password, first name, last name,
  phone number, birthdate, gender and address. It returns the adopter object
  (in case of being successfully registered) restored from the system database.


- **_ResponseEntity\<Adopter> signIn(@RequestBody Adopter adopter)_**
  Utilized by the frontend application to allow the adopter users to sign in to their account,
  where the passed Adopter object contains the adopter's email, and the adopter's password. It returns the adopter object
  (in case of being successfully authenticated) restored from the system database.

- **_ResponseEntity\<Integer> submitAdoptionApplication(@RequestBody AdoptionApplication adoptionApplication)_**
  This API method is responsible for submitting an adoption application. It takes an AdoptionApplication object as the 
  request body and creates a new adoption application using the provided data. The method logs the submission request 
  and returns an appropriate HTTP response along with the ID of the newly created adoption application if successful.

- **_ResponseEntity\<List\<AdoptionApplication>> fetchAdoptionApplications(@PathVariable("id") int adopterId)_**
  This API method is responsible for retrieving adoption applications associated with a specific adopter. 
  It takes the adopter's ID as an argument and returns a list of adoption applications corresponding to that adopter.
  The method logs the request for adoption applications and returns an appropriate HTTP response along with the list of 
  adoption applications if available.

- **_ResponseEntity\<Adopter> findById(@PathVariable("id") int adopterId)_**
  This API method is responsible for retrieving an adopter by their unique identifier (ID). It takes the adopter's ID as
  a path variable and returns the adopter details if found. The method logs the request for an adopter with a specific ID
  and returns an appropriate HTTP response along with the adopter details if available or null object otherwise.

- **_ResponseEntity\<List\<ApplicationNotification>> fetchAppNotifications(@PathVariable("id") int adopterId)_**
  This API method is responsible for retrieving application notifications associated with a specific adopter. It takes 
  the adopter's ID as a path variable and returns a list of application notifications corresponding to that adopter. 
  The method logs the request for application notifications and returns an appropriate HTTP response along with the list
  of application notifications if available.

- **_ResponseEntity\<List\<PetAvailabilityNotification>> fetchPetNotifications(@PathVariable("id") int adopterId)_**
  This API method retrieves pet availability notifications associated with a specific adopter. It takes the adopter's ID
  as a path variable and returns a list of pet availability notifications corresponding to that adopter. 
  The method logs the request for pet notifications and returns an appropriate HTTP response along with the list of pet 
  notifications if available.

## Services Behind The APIs

This section provides a comprehensive explanation of the implemented services related to the adopter user, that are being
used by the Adopter APIs. It is important to note that the adopter services
are implemented in the `AdopterService` class that is considered as a part of the Business Logic Layer
(refer to the System Architecture topic for more information about the system layers), and is based on a set of functionalities that
have been made available by the layer below it, DAOs layer. Finally, it equips the layer on top of it (APIs layer) by the needed tools
to respond to the frontend application queries and requests.

Listed below the implemented services, ordered by their usage by the corresponding API listed above and their explanation.

### 1. Adopter signUpLogic(Adopter newAdopter)
   #### Method Description: {id="method-description_1"}
   This method handles the logic for signing up a new adopter. It takes an instance of the Adopter class representing the new adopter to be signed up. The method performs necessary validation, hashes the password, creates a new adopter record, and retrieves the newly created adopter if the operation is successful.
   #### Parameters: {id="parameters_1"}
   **newAdopter(Adopter):** An instance of the Adopter class representing the new adopter to be signed up.
   #### Return Type: {id="return-type_1"}
   **Adopter:** An instance of the Adopter class representing the newly signed-up adopter if successful, otherwise null.
   #### Method Logic: {id="method-logic_1"}
   1. Check if the `newAdopter` object is null. If null, log an error message and return null.
   2. Hash the password of the new adopter using a hashing algorithm.
   3. Create a new adopter record in the data access object (adopterDAO) and retrieve the auto-generated adopter ID.
   4. If the adopter ID is greater than or equal to 1, log a success message, and return the adopter details fetched by ID from the adopterDAO.
   5. If the adopter ID is less than 1, log an error message indicating that something went wrong and return null.


### 2. Adopter signInLogic(Adopter actualAdopter)
   #### Method Description:
   This method handles the logic for signing in an existing adopter. It takes an instance of the Adopter class representing the adopter attempting to sign in. The method validates the provided adopter credentials, checks if the adopter exists, and verifies the password hash for authentication.
   #### Parameters:
   **actualAdopter (Adopter):** An instance of the Adopter class representing the adopter attempting to sign in.
   #### Return Type:
   **Adopter:** An instance of the Adopter class representing the signed-in adopter if successful, otherwise null.
   #### Method Logic:
   1. Check if the actualAdopter object is null. If null, log an error message and return null.
   2. Retrieve the adopter record associated with the provided email from the database (relying on the DAOs layer).
   3. If the adopter record doesn't exist, log an error message and return null.
   4. Compare the hashed password provided by the `actualAdopter` with the hashed password stored in the `expectedAdopter`.
      - If they do not match, log an authentication failure message and return null.
      - If they match, log a successful authentication message and return the `expectedAdopter`.
   5. If there is an unexpected condition, log an error message indicating that something went wrong and return null.

### 3. int createAdoptionApplication(AdoptionApplication adoptionApplication)
   #### Method Description: {id="method-description_2"}
   This method is responsible for creating a new adoption application using the provided adoption application object. It calls the appropriate method in the DAO layer to persist the adoption application in the database. The method logs the outcome of the operation and returns the ID of the newly created adoption application.   
   #### Parameters: {id="parameters_2"}
   **adoptionApplication (AdoptionApplication):** An instance of the AdoptionApplication class representing the adoption application to be created.
   #### Return Type: {id="return-type_2"}
   **int:** An integer representing the ID of the newly created adoption application.   
   #### Method Logic: {id="method-logic_2"}
   1. Call the create method of the DAO layer object to create a new adoption application in the database using the provided `adoptionApplication`.
   2. If the retrieved id is greater than 0, log a success message indicating that the adoption application was created successfully.
   3. If the retrieved id is not greater than 0, log an error message indicating that the adoption application failed to create.
   4. Return the retrieved id representing the ID of the newly created adoption application.

### 4. List\<AdoptionApplication> fetchAdoptionApplications(int adopterId)
   #### Method Description: {id="method-description_3"}
   This method retrieves a list of adoption applications associated with a specific adopter. It calls the appropriate method in the DAO layer to fetch the adoption applications from the database. The method logs the outcome of the operation and returns the list of adoption applications.
   #### Parameters: {id="parameters_3"}
   **adopterId (int):** An integer representing the unique identifier of the adopter whose adoption applications are to be fetched.   
   #### Return Type: {id="return-type_3"}
   **List\<AdoptionApplication>:** A list containing instances of the AdoptionApplication class representing the adoption applications associated with the specified adopter.
   #### Method Logic: {id="method-logic_3"}
   1. Call the appropriate method in the DAO layer to fetch the adoption applications from the database associated with the specified adopter ID.
   2. If the retrieved list is not null, log a success message indicating that the adoption applications were fetched successfully.
   3. If the retrieved list is null, log an error message indicating that the adoption applications failed to fetch.
   4. Return the retrieved list containing the fetched adoption applications.

### 5. Adopter findById(int adopterId)
   #### Method Description: {id="method-description_4"}
   This method retrieves an adopter by their unique identifier (ID). It calls the appropriate method in the DAO layer to fetch the adopter from the database using the provided adopter ID. The method logs the outcome of the operation and returns the adopter if found.
   #### Parameters: {id="parameters_4"}
   **adopterId (int):** An integer representing the unique identifier of the adopter to be retrieved.
   #### Return Type: {id="return-type_4"}
   **Adopter:** An instance of the Adopter class representing the adopter details if found, otherwise null.
   #### Method Logic: {id="method-logic_4"}
   1. Call the appropriate method in the DAO layer to retrieve the adopter details by the specified adopter ID.
   2. If the retrieved adopter object is not null, log a success message indicating that the adopter was fetched successfully.
   3. If the adopter object is null, log an error message indicating that the adopter failed to fetch.
   4. Return the adopter object containing the fetched adopter details.

### 6. List\<ApplicationNotification> fetchAppNotifications(int adopterId)
   #### Method Description: {id="method-description_5"}
   This method retrieves application notifications associated with a specific adopter. It calls the corresponding method in the DAO layer to fetch the application notifications from the database. The method logs the outcome of the operation and returns the list of application notifications.
   #### Parameters: {id="parameters_5"}
   **adopterId (int):** An integer representing the unique identifier of the adopter whose application notifications are to be fetched.
   #### Return Type: {id="return-type_5"}
   **List\<ApplicationNotification>:** A list containing instances of the `ApplicationNotification` class representing the application notifications associated with the specified adopter.
   #### Method Logic: {id="method-logic_5"}
   1. Call the appropriate method in the DAO layer to fetch the application notifications associated with the specified adopter ID from the database.
   2. If the retrieved list is not null, log a success message indicating that the application notifications were fetched successfully.
   3. If the retrieved list is null, log an error message indicating that the application notifications failed to fetch.
   4. Return the list containing the fetched application notifications.

### 7. List\<PetAvailabilityNotification> fetchPetNotifications(int adopterId)
   #### Method Description: {id="method-description_6"}
   This method retrieves pet availability notifications associated with a specific adopter. It calls the corresponding method in the DAO layer to fetch the pet availability notifications from the database. The method logs the outcome of the operation and returns the list of pet availability notifications.
   #### Parameters: {id="parameters_6"}
   **adopterId (int):** An integer representing the unique identifier of the adopter whose pet notifications are to be fetched.
   #### Return Type: {id="return-type_6"}
   **List\<PetAvailabilityNotification>:** A list containing instances of the `PetAvailabilityNotification` class representing the pet availability notifications associated with the specified adopter.
   #### Method Logic: {id="method-logic_6"}
   1. Call the appropriate method in the DAO layer to fetch the pet availability notifications associated with the specified adopter ID.
   2. If the retrieved list is not null, log a success message indicating that the pet notifications were fetched successfully.
   3. If the retrieved list is null, log an error message indicating that the pet notifications failed to fetch.
   4. Return the list containing the fetched pet availability notifications.