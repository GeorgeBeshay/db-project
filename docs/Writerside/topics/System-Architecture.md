# System Architecture

## Introduction

This section of the documentation provides detailed information considering the overall system architecture adopted, and the architecture patterns used. 
Also it mentions the rationale behind the decisions / choices made.

## Layered Architecture

### Why Layered Architecture ?

The layered architecture pattern has been adopted in this system, because it is one of the patters that achieves 
separation and independence between the system components, where the system functionalities are organized into separate layers
and each layer depends **only** on the layer below it, and to be more precise, each layer depends on the interface of the components
of the layer below it, that is, even if an implementation change has been made to a layer, without changing the component interface, the
layer on top of it will not be affected.

The layered architecture supports the incremental development of software, where the software engineers can work on different 
components in different layers independently of the other components in other layers status.

### Description

The system is essentially divided into 4 layers, and they are (ordered from the top most layer to the lowest layer): 

- **Frontend Interface Layer** which is the layer that implements the user-friendly interface that gives the user access to all of the implemented
features in the layers below it. The frontend interface for this  layer has been chosen to be a web-based interface, and this choice is kind of being realistic, because
most of the systems that provide that kind of functionality are web-based.

- **Endpoints / APIs Layer** that provides a means of communication between the frontend application and the system backend, where it contains a set of defined
endpoints, that is being utilized by the frontend application to communicate all the actions / requests made by the user.

- **Business Logic Layer** that contains the core implementation of the system logic, such as the actual services requested 
by the frontend through the system APIs, in addition to implementing a set of utility classes (e.g. `Hasher` and `Logger` classes). 
This layer directly depends on the DAOs layer below it, where it communicates with the database through it.   

- **DAOs Layer**, that is considered as the communication link between the system business logic and the system database.
The DAOs layer contains a dedicated DAO class for each relation defined in the DB relational schema, where each DAO mainly contains
the set of the CRUD operations related to this relation, plus providing a group of methods that is dependent on the logic of the DAO's relation, 
to make the last statement clear, this means that there are some relations that may need a certain search methods based on some attributes, so those 
functionalities are also implemented in this specific DAO only (e.g. the `ApplicationNotificationDAO` & `PetAvailabilityNotificationDAO` contain a `findByStatus()` method, that is not present in any other DAO).

- **Core System Database Layer** which is the layer that contains the core database that follows the defined relational schema. 
It contains the secondary means of access to the relations (indexes) that have been made on specific relations to increase 
the database throughput by decreasing the access time. In addition to that, it contains implemented triggers and procedures that are 
related to the system business logic, however, the software authors have decided to move a specific functionalities 
down to this layer, to rely on the built-in functionalities that are provided by the database server.  

The previous layers are distributed over 3 major applications that are defined in their suitable technological tool, and they are, 
* **Angular Web Application Framework** which includes the frontend interface layer.
* **Java Spring Boot Application** which defines the APIs layer, business logic layer and the DAOs layer.
* **Microsoft SQL Server Database** which is essentially the core system database that contains the relational schema defined, 
the indexes, triggers, and the procedures implemented.

---
