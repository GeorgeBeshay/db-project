# Visitor APIs &amp; Services

This subtopic demonstrates the APIs provided for the Visitor users, in addition to discussing the services implemented
behind each API that is being used by the frontend application.

## Visitor APIs

Listed below the set of provided APIs, that are being utilized by the angular frontend application:

- _**ResponseEntity<List\<Pet>> getSearchedAndSortedPets(@RequestBody Map<String, Object> requestedMap)**_
Used by the frontend application to retrieve the pets available for adoption to be displayed to the application visitor.
The API provides means of sorting and filtering based on criteria attributes of the pet record such as specie, gender, etc...
The API returns a list of pets matching the desired properties.
It accepts a map containing all the relevant information for the filtering and sorting process, that is, the attributes 
on which the pets are filtered, and the attributes which the pets are sorted by.
The map passed as an argument to the API is expected
to contain 3 <Key, Object> pairs, where the key is a string of characters, and they are:
  - **\<"criteria", Map<String, Object>>** The criteria map contains the values of the filtering attributes
  to filter on, e.g. gender 
  - **\<"shelterLocation", String>** The shelter location string specifies the desired location of shelters offering those pets for adoption,
  it is important to note that the shelter location is not unique, and so there might be multiple shelters in the same location.
  - **\<"orderedByColumns", List\<String>>** This list specifies the columns on which the filtered pets should be sorted on, 
  and they must belong to a set of defined attributes on which the sorting is possible, and they are the _neutering_, 
  _house_training_, and _vaccination_ attributes.

## Services Behind The APIs

This section provides a comprehensive explanation of the implemented services related to the visitor user, that are being
used by the Visitor APIs. It is important to note that the visitor services
are implemented in the `VisitorService` class that is considered as a part of the Business Logic Layer
(refer to the System Architecture topic for more information about the system layers), and is based on a set of functionalities that
have been made available by the layer below it, DAOs layer. Finally, it equips the layer on top of it (APIs layer) by the needed tools
to respond to the frontend application queries and requests.

Listed below the implemented services, ordered by their usage by the corresponding API listed above, their explanation,
and finally each method is followed by its flowchart illustrating the procedure throughout the well-known flowchart symbols:

1. **_List\<Pet> getSearchedAndSortedPets(Map\<String, Object> requestedMap)_**
This is the method where the frontend request is directly delegated to it through the API. The method mainly does one thing,
which is extracting the filtering and sorting information from the passed map, and then delegate the request to a more complex function
that is defined in the same class, named _getPetsForVisitor_.

    ![getSearchedAndSortedPets() Flowchart](Visitor_getSearchedAndSortedPets_Flowchart.png){ width=600 }{border-effect=line}

2. **_List\<Pet> getPetsForVisitor(Map\<String, Object> criteria, String location, List\<String> orderByColumns)_**
The method checks that the criteria and orderByColumns values are valid, then computes the shelter IDs of the shelters
located in the given _location_, finally calls the suitable method from the **petDAO** to find the pets matching the given properties
(pet belongs to one of the given shelters, pet matching the specified criteria, and finally retrieved pets are sorted based on the desired
columns).

    ![getPetsForVisitor() Flowchart](Visitor_getPetsForVisitor_Flowchart.png){ width=600 }{border-effect=line}

3. **_boolean checkForOrderByColumns(List\<String> orderByColumns)_**
This method is straight forward and used by the previous method to check that the _orderByColumns_ variable is valid, in other words, 
that the columns names exist and valid to prevent any unexpected behaviour that may result. The method simply checks that each
element in the _orderByColumns_ list belongs to the _allowedColumns_ list that contains all the valid columns names to order by them.
In case any element was found to be not belonging to the _allowedColumns_ list, the method returns false, else, it returns 
true indicating the validity of the list.

   ![checkForOrderByColumns() Flowchart](Visitor_checkForOrderByColumns_Flowchart.png){ width=600 }{border-effect=line}        
  
4. **_boolean checkCriteriaKeys(Map\<String, Object> criteria)_**
Similar to the previous method, this one is used by the _getPetsForVisitor_ method to check that the criteria map contains 
valid <Key, Object> pairs, and this is done through checking that each key in the map is one of the _allowedKeys_ list. In case
any element was found to be not belonging to the _allowedKeys_ list, the method returns false, else, it returns true indicating
the validity of the criteria map.

   ![checkCriteriaKeys() Flowchart](Visitor_checkCriteriaKeys_Flowchart.png){ width=600 }{border-effect=line}
