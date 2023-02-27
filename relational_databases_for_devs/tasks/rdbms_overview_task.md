# RDBMS Overview - Practical Task


### Table of Contents
<!-- TOC -->
* [Your Goals](#your-goals)
* [Practical Task](#practical-task)
<!-- TOC -->

### Your Goals
1.  The purpose of this practical task is to help you create and manipulate tables in a relational database using SQL. Specifically, we'll be focusing on creating the `employees` and `employee_skills` tables, and performing some basic data insertion and retrieval operations.
---

## Practical Task  
1.  Create the `employees` table, specifying the appropriate column names and data types, and any constraints: 
	 - employee_id INT PRIMARY KEY
	 - first_name VARCHAR(50) NOT NULL
	 - last_name VARCHAR(50) NOT NULL 
	 - email VARCHAR(100)
	 - hire_date DATE
	 - salary DECIMAL(10,2);
    
2.  Insert data into the employees table, specifying the appropriate values for each column.
    - (1, 'John', 'Doe', '[johndoe@email.com](mailto:johndoe@email.com)', '2023-01-01', 50000.00), 
    - (2, 'Jane', 'Smith', '[janesmith@email.com](mailto:janesmith@email.com)', '2023-02-01', 60000.00), 
    - (3, 'Bob', 'Johnson', '[bobjohnson@email.com](mailto:bobjohnson@email.com)', '2023-03-01', 70000.00);
    
3.  Verify that the employees table and data were created successfully by running SQL query to select and display the data from the employees table.

4.  Create a table named `employee_skills` with columns: `employee_id` of INT data type, `skill` of VARCHAR(50) data type, and `level` of INT data type.

5.  Add a primary key constraint to the `employee_id` column on  `employee_skills` table.

6.  Add a foreign key constraint to the `employee_id` column that references the `employees` table and its `employee_id` column.

7.  Insert sample data to `employee_skills` table to reflect the following: employee with `employee_id` 1 has skill "Programming" with level 5, employee with `employee_id` 2 has skill "Design" with level 4, and employee with `employee_id` 3 has skill "Marketing" with level 3.
 
8.  Query the `employee_skills` table to retrieve the `skill` and `level` of employees who have a `level` greater than or equal to 4. Order the result by `level` in descending order.

9.  in the next questions please establish possible database relationships between following hypothetical tables :
    ###### 9.1. country and city
    ###### 9.2. book and author
    ###### 9.3. department and teacher
    ###### 9.4. student and teacher