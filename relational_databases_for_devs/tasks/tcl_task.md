Suppose we have a Carsharing application where drivers publish information about available rides which passengers can
book. for each ride only certain number of seats are available. let's imagine we have the following tables:

1. passenger table with information about id(pk), name, surname , email and password
2. driver table with information about name, surname, email, password, driver license number(pk).
3. ride with information id(pk), driver(fk),number of available seats for passengers, description of ride, start
   address, destination address, datetime of ride.
4. passenger_on_ride which is junction table between ride and passenger tables with information about passenger id, ride
   id;

based on tables described above please imagine following situation:

David who is a driver published an information about upcoming ride from Tbilisi to Batumi on 9th of March 2023, he
specified that he had only one available seat.
Natalia and Sasha who were planning to go on this route decide to book this ride simultaneously

**question 1.**
please describe the steps needed to book a ride.
**Answer : we have to read specific row in a ride table to make sure there is enough places for booking, then
update ride by subtracting from the number of available seats, and we have to insert row in the
passenger_on_ride table**

**question 2.**
would you surround these steps with transaction statement? why?

**question 3.**
if you used transactional statement which isolation level would you use?
