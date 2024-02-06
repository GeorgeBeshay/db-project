# Pet APIs &amp; Services

This subtopic demonstrates the APIs provided for the Pets management, in addition to discussing the services implemented
behind each API that is being used by the frontend application.

## Pets API

Listed below the set of provided APIs, that are being utilized by the angular frontend application:

- **_ResponseEntity\<Integer> createPet(@RequestBody Pet pet)_**
  Exploited by the frontend application to allow the staff users to create a new pet, where the API
  accepts the new pet object as an argument, and returns the ID of the created pet.

- **_ResponseEntity\<Boolean> updatePet(@RequestBody Pet pet)_**
  Exploited by the frontend application to allow the staff users to update an existing pet, where the API
  accepts the existing pet object as an argument, and returns a boolean value, indicating either the success
  or the failure of the updating process.

- **_ResponseEntity\<Boolean> deletePet(@RequestBody Pet pet)_**
  Leveraged by the frontend application to allow the staff users to delete an existing pet from the system,
  passing the pet object to be deleted as an argument. The API returns a boolean value, indicating either the success
  or the failure of the deletion process.

- **_ResponseEntity\<List\<Pet>> getUnAdoptedPets()_**
  Used by the frontend application to get all unAdopted pets (pets that are available for adoption),
  to allow the staff member edit or delete them.

- **_ResponseEntity\<Pet> findPetById(@RequestBody Integer petId)_**
  Used by the frontend application to find a pet, where the API accepts the petId as argument. The API returns the pet found
  or null in case of no pet found.

## Services Behind The APIs

This section provides a comprehensive explanation of the implemented services related to the pet management, that are being
used by the pet APIs. It is important to note that the pet services
are implemented in the `PetService` class that is considered as a part of the Business Logic Layer
(refer to the System Architecture topic for more information about the system layers), and is based on a set of functionalities that
have been made available by the layer below it, DAOs layer. Finally, it equips the layer on top of it (APIs layer) by the needed tools
to respond to the frontend application queries and requests.

Listed below the implemented services, ordered by their usage by the corresponding API listed above and their explanation:

### 1. int createPet(Pet pet)
#### Method Description: {id="method-description_1"}
This method is responsible for creating a new pet entry in the system. It interacts with the `petDAO` to persist the pet in the database and notifies adopters upon successful creation.

#### Parameters: {id="parameters_1"}
pet: An instance of the Pet class representing the pet to be created.

#### Return Type: {id="return-type_1"}
int: The ID of the newly created pet if the creation operation was successful; otherwise, returns 0.

#### Method Logic: {id="method-logic_1"}
1. Calls the `create` method in the petDAO layer to persist the provided pet in the database.
2. If the `petCreatedId` is greater than 0 (indicating successful creation):
   - Logs a success message indicating that a pet has been created. 
   - Notifies adopters about the newly created pet.
3. If the `petCreatedId` is 0 or less (indicating failure):
   - Logs a failure message indicating that the pet creation has failed.
4. Returns the `petCreatedId`, which represents the ID of the newly created pet or 0 if the creation operation failed.

### 2. boolean updatePet(Pet pet)
#### Method Description: {id="method-description_2"}
This method serves to update an existing pet's information within the system. It interacts with the `petDAO` to perform the update operation and persists it in the database.
#### Parameters: {id="parameters_2"}
**`pet`:** An instance of the `Pet` class representing the updated information of the pet.
#### Return Type: {id="return-type_2"}
**`boolean`:** Indicates whether the update operation was successful (`true`) or not (`false`).
#### Method Logic: {id="method-logic_2"}
1. Calls the `update` method in the `petDAO` layer to update the provided `pet` in the database.
2. If the update operation is successful:
   - Logs a success message indicating that a pet has been updated successfully.
3. If the update operation fails:
   - Logs a failure message indicating that the pet update has failed.
4. Returns `true` if the update operation is successful; otherwise, returns `false`.

### 3. boolean deletePet(Pet pet)
#### Method Description: {id="method-description_3"}
This method serves to delete an existing pet from the system. It interacts with the `petDAO` to execute the deletion operation and remove the pet from the database.
#### Parameters: {id="parameters_3"}
**`pet`:** An instance of the `Pet` class representing the pet to be deleted.
#### Return Type: {id="return-type_3"}
**`boolean`:** Indicates whether the deletion operation was successful (`true`) or not (`false`).
#### Method Logic: {id="method-logic_3"}
1. Calls the `delete` method in the `petDAO` layer to remove the provided `pet` from the database.
2. If the deletion operation is successful:
   - Logs a success message indicating that a pet has been deleted successfully.
3. If the deletion operation fails:
   - Logs a failure message indicating that the pet deletion has failed.
4. Returns `true` if the deletion operation is successful; otherwise, returns `false`.

### 4. List<Pet> getUnAdoptedPets()
#### Method Description: {id="method-description_4"}
This method retrieves a list of unAdopted pets from the system. It interacts with the `petDAO` to fetch the unAdopted pets' information.
#### Return Type: {id="return-type_4"}
**`List<Pet>`:** A list containing instances of the `Pet` class representing unAdopted pets.
#### Method Logic: {id="method-logic_4"}
1. Calls the `getUnAdoptedPets` method in the `petDAO` layer to retrieve unAdopted pets from the database.
2. If `unAdoptedPets` is not `null`:
   - Logs a success message indicating that unAdopted pets have been retrieved successfully.
3. If `unAdoptedPets` is `null`:
   - Logs a failure message indicating that the attempt to retrieve unAdopted pets has failed.
4. Returns `unAdoptedPets`, which represents the list of unAdopted pets if available, or `null` if the retrieval operation failed.

### 5. Pet findById(int petId)
#### Method Description: {id="method-description_5"}
This method retrieves a pet by its ID from the system. It interacts with the `petDAO` to fetch the pet's information based on the provided ID.
#### Parameters: {id="parameters_5"}
**`petId`:** An integer representing the ID of the pet to be retrieved.
#### Return Type: {id="return-type_5"}
**`Pet`:** An instance of the `Pet` class representing the found pet, or `null` if the pet with the given ID is not found.
#### Method Logic: {id="method-logic_5"}
1. Calls the `findById` method in the `petDAO` layer to retrieve the pet with the provided `petId` from the database.
2. If `pet` is not `null`:
   - Logs a success message indicating that the pet has been found successfully.
3. If `pet` is `null`:
   - Logs a failure message indicating that the pet search has failed.
4. Returns `pet`, representing the found pet if available, or `null` if the pet with the given ID is not found.

### 6. void notifyAdopters(int petId)
#### Method Description: {id="method-description_6"}
This method notifies adopters about the availability of a pet for adoption. It retrieves adopters' IDs and creates pet availability notifications for each adopter.
#### Parameters: {id="parameters_6"}
**`petId`:** An integer representing the ID of the pet for which availability notifications are being sent.
#### Method Logic: {id="method-logic_6"}
1. Retrieves a list of adopters' IDs using the `getAdoptersIDs` method from the `adopterDAO`.
2. Iterates over each adopter ID in the `adoptersIds` list.
3. Creates a new instance of `PetAvailabilityNotification` with the `petId`, `adopterId`, `false` indicating notification not seen, and the current date.
4. Calls the `create` method in the `petAvailabilityNotificationDAO` to create the pet availability notification.
5. If the notification creation is successful:
    - Logs a success message indicating that the pet availability notification has been created successfully.
6. If the notification creation fails:
    - Logs a failure message indicating that the attempt to create the pet availability notification has failed.