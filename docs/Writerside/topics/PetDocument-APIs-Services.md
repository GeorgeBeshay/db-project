# PetDocument APIs &amp; Services

This subtopic demonstrates the APIs provided for the Pet Documents management, in addition to discussing the services implemented
behind each API that is being used by the frontend application.

## Pet Documents API

Listed below the set of provided APIs, that are being utilized by the angular frontend application:

- **_ResponseEntity\<Integer> uploadFilesForPet(@PathVariable("pet_id") int petId, @RequestParam("files") MultipartFile\[] files)_**
  Exploited by the frontend application to enable users to upload files associated with a pet. This API accepts the pet's ID and an array of files to be uploaded, 
  returning the result of the upload operation, which represents the number of files successfully uploaded.

- **_ResponseEntity\<Resource> downloadDocument(@PathVariable("documentId") int documentId)_**
  Utilized by the frontend application to facilitate the downloading of a document associated with a pet. This endpoint accepts the ID of the document to be downloaded and 
  returns a ResponseEntity containing the resource representing the document to be downloaded.

- **_ResponseEntity\<List\<PetDocument>> findAllDocuments(@PathVariable("petId") int petId)_**
  Accessed by the frontend application to retrieve all documents associated with a specific pet. This endpoint accepts the ID of the pet and returns a ResponseEntity 
  containing a list of PetDocument objects representing the documents associated with the specified pet.


## Services Behind The APIs

This section provides a comprehensive explanation of the implemented services related to the pet documents management, that are being
used by the pet documents APIs. It is important to note that the pet documents services
are implemented in the `PetDocumentService` class that is considered as a part of the Business Logic Layer
(refer to the System Architecture topic for more information about the system layers), and is based on a set of functionalities that
have been made available by the layer below it, DAOs layer. Finally, it equips the layer on top of it (APIs layer) by the needed tools
to respond to the frontend application queries and requests.

Listed below the implemented services, ordered by their usage by the corresponding API listed above and their explanation:


### 1. int uploadFilesForPet(int petId, MultipartFile\[] files)
#### Method Description: {id="method-description_1"}
This method facilitates the uploading of files for a pet. It accepts the ID of the pet and an array of files to be uploaded. Upon successful upload, it returns the count of files that have been successfully uploaded.
#### Parameters: {id="parameters_1"}
**`petId`:** An integer representing the ID of the pet for which files are being uploaded.
**`files`:** An array of `MultipartFile` objects representing the files to be uploaded.
#### Method Logic: {id="method-logic_1"}
1. Initializes a counter `count` to keep track of the number of files successfully uploaded.
2. Iterates over each `MultipartFile` object in the `files` array.
3. For each file:
    - Creates a new instance of `PetDocument`.
    - Sets the `petId`, `documentType`, and `name` attributes of the `PetDocument` object.
    - Attempts to set the `document` attribute by retrieving the bytes from the file.
    - Calls the `create` method in the `petDocumentDAO` to persist the `PetDocument` object in the database.
    - If the creation operation returns a value greater than or equal to 1, increments the `count`.
    - If an exception occurs during the process, logs a failure message indicating that a document failed to be created and returns `-1`.
4. Returns the `count`, representing the number of files successfully uploaded.

### 2. PetDocument downloadFile(int documentId)
#### Method Description: {id="method-description_2"}
This method facilitates the downloading of a file associated with a pet. It accepts the ID of the document to be downloaded and returns the corresponding `PetDocument` object.
#### Parameters: {id="parameters_2"}
**`documentId`:** An integer representing the ID of the document to be downloaded.
#### Method Logic: {id="method-logic_2"}
1. Retrieves the `PetDocument` associated with the provided `documentId` using the `findById` method from the `petDocumentDAO`.
2. Checks if the retrieved `PetDocument` is `null` or if the document itself is `null` or empty.
3. If any of the conditions in step 2 are met:
    - Logs a failure message indicating that a document failed to be downloaded.
    - Returns `null`.
4. Otherwise, returns the retrieved `PetDocument` object.

### 3. List<PetDocument> findDocumentsByPetId(int petId)
#### Method Description: {id="method-description_3"}
This method retrieves a list of documents associated with a specific pet. It accepts the ID of the pet and returns a list of `PetDocument` objects representing the documents associated with that pet.
#### Parameters: {id="parameters_3"}
**`petId`:** An integer representing the ID of the pet for which documents are being retrieved.
#### Method Logic: {id="method-logic_3"}
1. Invokes the `findByPetId` method from the `petDocumentDAO` to retrieve documents associated with the provided `petId`.
2. Returns the list of `PetDocument` objects representing the documents associated with the specified pet.