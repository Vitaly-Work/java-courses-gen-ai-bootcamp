# Triggers - Practical Task


### Table of Contents
<!-- TOC -->
* [Your Goals](#your-goals)
* [Practical Task](#practical-task)
<!-- TOC -->

### Your Goals
1.  This task will help you to understand how to design and implement a trigger in SQL that enforces a business rule. 
---

## Practical Task
Create a trigger to enforce the following rule for assigning employees to departments:

-   An employee cannot be assigned to a department with a lower rank than their current department.
-   If an employee is not currently assigned to any department, they can be assigned to any department with any rank.

###  __For achieving this, here are the steps you should follow:__
1. Create a table to store department information, including a department ID and a rank for each department.
	```sql
	CREATE TABLE IF NOT EXISTS department (
	    department_id INT PRIMARY KEY,
	    rank INT NOT NULL
	    -- other columnts if necessary...
	);
	```
2. Create a table to store employee information, including an employee ID, a department ID, and other relevant information.
	```sql
	CREATE TABLE IF NOT EXISTS employee (
	    employee_id INT PRIMARY KEY,
	    department_id INT,
	    -- other columns if necessary...
	    FOREIGN KEY (department_id) REFERENCES department (department_id)
	);
	```
3. Create a trigger named "check_employee_department_rank" that is executed before any update operations on the "employee" table. The trigger should retrieve the rank of the employee's current department and the rank of the new department they are being assigned to, and compare them. If the new department has a lower rank than the current department and the employee is currently assigned to a department, the trigger should raise an error and cancel the update operation. Otherwise, the update should be allowed to proceed.
Note that if the employee is not currently assigned to any department, the trigger should not perform any comparison and allow the update to proceed, regardless of the rank of the new department.


