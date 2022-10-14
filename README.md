# Distributed Database System

### About The Project
This project provides an application that serves as a Distributed Database Management System for two databases, allowing it to manage multi-user queries. It has a command-line interface that accepts user input and performs several DDBMS functions. The logged-in user will be able to execute and access the tables in both databases that are based on the Global Data Dictionary (GDD). The following operations have been performed by the team: <br/>
* User Authentication<br/>
* Query Parser<br/>
* Transaction<br/>
* Log Management<br/>
* ERD Generation<br/>
* SQL Dump<br/>
* Analytics<br/>

### Tech Stack
Core Java JDK 17 <br/>

### Linear Data Structure used for Query Processing
For this project, we have used Array List, HashMap, and Maps data structures. We chose this data structure because of its ease of use and various advantages.<br/>

*  **Hash Map:** HashMap stores items in key/value pairs, and you can access them by an index of another string. HashMap is non-synchronized. HashMap cannot be shared between multiple threads without proper synchronization. HashMap is a fail-fast iterator and provides faster access to elements due to hashing technology.<br/>
<br/>
HashMap<Integer, String> hm = new HashMap<Integer, String>(); <br/>

* **Array List:** We chose ArrayList over the array as you can define ArrayList as a re-sizable array.     Elements can be inserted at or deleted from a particular position. ArrayList class has many methods to manipulate the stored objects. If generics are not used, ArrayList can hold any type of objects. <br/>
ArrayList<String> cars = new ArrayList<String>(); <br/>

* **Map:** A Map doesn't allow duplicate keys, but you can have duplicate values. We used map as the Map interface includes methods for basic operations (such as put, get, remove, containsKey, containsValue, size, and empty), bulk operations (such as putAll and clear), and collection views (such as keySet, entrySet, and values).
