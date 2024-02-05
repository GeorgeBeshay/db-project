# Admin APIs &amp; Services

This subtopic demonstrates the APIs provided for the Admin users, in addition to discussing the services implemented
behind each API that is being used by the frontend application.

## Admin APIs

Listed below the set of provided APIs, that are being utilized by the angular frontend application:

- **_ResponseEntity\<Admin> adminSignIn(Admin admin)_**
Utilized by the frontend application to allow the admin users to sign in to their account, 
where the passed Admin object contains the admin ID, and the admin password. It returns the admin object 
(in case of being successfully authenticated) restored from the system database.

- **_ResponseEntity\<Shelter> createShelter(Shelter shelterToBeCreated)_**
Exploited by the frontend application to allow the admin users to create a new shelter, where the API
accepts the new shelter object as an argument, and returns the created shelter object after being created and 
stored in the system database. 

- **_ResponseEntity\<Staff> createStaff(Staff staffToBeCreated)_**
Employed by the frontend application to allow the admin users to create a new staff user account, specifying the required
details in the staff object passed as an argument to the API. In case of successful creation, the API returns the created
staff record, after being recorded in the database.

- **_ResponseEntity\<Boolean> deleteShelter(Shelter shelterToBeDeleted)_**
Leveraged by the frontend application to allow the admin users to delete an existing shelter from the system, 
passing the shelter object to be deleted as an argument. The API returns a boolean value, indicating either the success 
or the failure (e.g. in case of the passed shelter didn't exist in the system) of the deletion process.    

- **_ResponseEntity<List\<Shelter>> findAllShelters()_**
Used by the frontend application to allow the admin users to retrieve all the system shelters, to be displayed.
The API accepts no argument, and returns a list of the existing shelter found in the system database.

## Services Behind The APIs

This section provides a comprehensive explanation of the implemented services related to the admin user, that are being
used by the Admin APIs. It is important to note that the admin services
are implemented in the `AdminService` class that is considered as a part of the Business Logic Layer 
(refer to the System Architecture topic for more information about the system layers), and is based on a set of functionalities that 
have been made available by the layer below it, DAOs layer. Finally, it equips the layer on top of it (APIs layer) by the needed tools
to respond to the frontend application queries and requests.

Listed below the implemented services, ordered by their usage by the corresponding API listed above, their explanation, 
and finally each method is followed by its flowchart illustrating the procedure throughout the well-known flowchart symbols:

1. _**Admin signInLogic(Admin actualAdmin)**_
This method implements the logic behind the admin sign in and the authentication process made to check for the admin 
credentials. The method accepts the admin object requesting to sign in, where the object specifies the ID and password typed by the user.
The method first checks that the passed object is not null, to prevent a null pointer exception, then finds the admin 
record from the DB (relying on the DAOs layer) given the same ID of the passed actualAdmin object, then checks if the retrieved admin
was null, this indicates that there is no admin with this ID, and so the method will return null in this case. Finally, if the retrieved
admin object was not null, this means that there exists an admin with this ID, and the only remaining step is to check for the
password similarity. However, we must **note that the passwords themselves are not stored in the DB, but instead their hash string is.** 
(The rationale behind this decision is to achieve higher level of security and privacy to the users, where even the DBA can't know a user password given its hash string).
In case of the similarity of the hash string, the method returns the retrieved admin object containing all of its profile information, if not, the method returns null.
    
    ![signInLogic() Flowchart](Admin_signInLogic_Flowchart.png){ width=600 }{border-effect=line}
   


2. _**Shelter createShelter(Shelter shelter)**_
This method implements the logic behind the shelter creation API. The method first checks that the given shelter object is not null, 
then proceeds with the remaining steps to create the shelter. The next step in the procedure is to create the shelter (by utilizing the methods implemented in the DAOs layer) and check for the retrieved ID.
If the ID is valid, the method fetches the created shelter record (again, using the DAOs layer methods), and finally, it ensures that the
retrieved shelter record (that has just been created) is not null, and returns it.

   ![createShelter() Flowchart](Admin_createShelter_Flowchart.png){ width=600 }{border-effect=line}

3. _**Staff createStaff(Staff staff)**_
This method implements the logic behind the staff user creation API. The procedure is similar to a great extent
to the shelter creation procedure, with a little variation related to the password attribute only. We will discuss this variation and revisit the normal
procedure mentioned in the previous method.

   ![createStaff() Flowchart](Admin_createStaff_Flowchart.png){ width=600 }{border-effect=line}

4. _**boolean deleteShelter(Shelter shelter)**_
This method implements the logic behind the shelter deletion process. The procedure is trivial and essentially depends on 2 steps.
First, is to check that the passed shelter object as an argument is not null, and second is to delete the shelter (by utilizing the DAOs layer methods) 
and return the result of the deletion process made by the DAO to the layer on top of it (APIs layer).

   ![deleteShelter() Flowchart](Admin_deleteShelter_Flowchart.png){ width=600 }{border-effect=line}

5. _**List\<Shelter> findAllShelters()**_
This method implements the logic behind 'finding all the shelters' process. This method is very simple, 
and contributes with no additional logic, it only delegates the request to the suitable DAO and return the result from it.  

   ![findAllShelters() Flowchart](Admin_findAllShelters_Flowchart.png){ width=600 }{border-effect=line}
