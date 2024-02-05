# Notifications APIs &amp; Services

This subtopic demonstrates the APIs provided for the Notifications management, in addition to discussing the services implemented
behind each API that is being used by the frontend application.

## Notifications APIs

Listed below the set of provided APIs, that are being utilized by the angular frontend application:

- **_ResponseEntity\<Boolean> updateAppNotification(@RequestBody ApplicationNotification applicationNotification)_**
This API method is responsible for updating an application notification. It takes an instance of the `ApplicationNotification` 
class as the request body, performs the update operation via the notification service, and returns an appropriate HTTP response
based on the success or failure of the update operation. If the update operation is successful, return an HTTP response 
with status code 200 (OK) and a Boolean value of true. If the update operation fails, return an HTTP response with status 
code 400 (Bad Request) and a Boolean value of false.

- **_ResponseEntity\<Boolean> updatePetNotification(@RequestBody PetAvailabilityNotification petAvailabilityNotification)_**
This API method is responsible for updating a pet availability notification. It takes an instance of the `PetAvailabilityNotification` 
class as the request body, performs the update operation via the notification service, and returns an appropriate HTTP response 
based on the success or failure of the update operation. If the update operation is successful, return an HTTP response with status 
code 200 (OK) and a Boolean value of true. If the update operation fails, return an HTTP response with status code 400 (Bad Request)
and a Boolean value of false.


## Services Behind The APIs

This section provides a comprehensive explanation of the implemented services related to the notifications handling, that are being
used by the Notifications APIs. It is important to note that the notification services
are implemented in the `NotificationService` class that is considered as a part of the Business Logic Layer
(refer to the System Architecture topic for more information about the system layers), and is based on a set of functionalities that
have been made available by the layer below it, DAOs layer. Finally, it equips the layer on top of it (APIs layer) by the needed tools
to respond to the frontend application queries and requests.

Listed below the implemented services, ordered by their usage by the corresponding API listed above and their explanation.

### 1. boolean updateAppNotification(ApplicationNotification applicationNotification)
#### Method Description: {id="method-description_1"}
This method serves to update application notifications. It interacts with the respective DAOs to perform the update operation
and persist it in the database.
#### Parameters: {id="parameters_1"}
**`applicationNotification`:** An instance of the `ApplicationNotification` class representing the application notification to be updated.
#### Return Type: {id="return-type_1"}
**`boolean`:** Indicates whether the update operation was successful (`true`) or not (`false`).
#### Method Logic: {id="method-logic_1"}
1. Calls the respective method in the DAOs layer to update the provided `applicationNotification` in the database.
2. If the update operation is successful, logs a success message; otherwise, logs a failure message.
3. Returns `true` if the update operation is successful; otherwise, returns `false`.

### 2. boolean updatePetNotification(PetAvailabilityNotification petAvailabilityNotification)
#### Method Description:
This method serves to update pet availability notifications. It interacts with the respective DAOs to perform the update operation
and persist it in the database.
#### Parameters:
**`petAvailabilityNotification`:** An instance of the `PetAvailabilityNotification` class representing the pet availability notification to be updated.
#### Return Type:
**`boolean`:** Indicates whether the update operation was successful (`true`) or not (`false`).
#### Method Logic:
1. Calls the respective method in the DAOs layer to update the provided `petAvailabilityNotification` in the database.
2. If the update operation is successful, logs a success message; otherwise, logs a failure message.
3. Returns `true` if the update operation is successful; otherwise, returns `false`.


