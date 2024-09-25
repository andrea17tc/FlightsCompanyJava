# Flights Company Java App

## Description
In this application you can view flights and book seats. Uses authentication for users defined in the database and has 
a logout mechanism implemented. An user can book seats for himself and other 5 people, stating his name, address, and
the companions' names. User can see only the flights with at least one seat left and gets a notification if five
or less seats are available. Flights can be filtered by date and destination.

## Structure
A client - server application designed with Java, using RPC for communication.

Wired to a SQLite database.

Used Protobuff for communication with C# server.

Has a REST server with front-end developed in Next.js, using React.

## TODOs
+ Sign Up - create an account
+ View Bookings - a page for seeing past and current bought seats
+ Admin functionalities - CRUD operations on flights for a special user
+ UI Improvement

