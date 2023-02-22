### FUNCTIONS_AND_OPERATOR_REFERENCES - Exercises

Suppose you have two tables:

EMPLOYEES:

| EMPLOYEE_ID | FIRST_NAME  | LAST_NAME | SALARY | COMMISSION_PCT   | DEPARTMENT_ID |
|-------------|-------------|-----------|--------|------------------|--------------|
| 100         | Steven      | Ernst     | 2400   | null             | 30           |
| 101         | Kevin       | Lorentz   | 1700   | 0.2              | 20           |
| 102         | Peter       | Grant     | 4500   | 0.15             | 40           |
| 103         | Curtis      | Abel      | 1900   | 0.1              | 10           |
| 104         | Ellen       | Taylor    | 5600   | null             | 20           |
| 105         | Pat         | Higgins   | 3200   | null             | 20           |
| 106         | William     | King      | 5300   | 0.4              | 30           |
| 107         | Shelley     | Fay       | 3300   | null             | 40           |

DEPARTMENTS:

| DEPARTMENT_ID(PK) | DEPARTMENT_NAME | MANAGER_ID | LOCATION_ID |
|-------------------|:---------------:|-----------:|:-----------:|
| 10                | ADMINISTRATION  | 200        |    1900     | 
| 20                |       IT        | 120        |    2300     | 
| 30                |   ACCOUNTING    | 30         |    3420     | 
| 40                |    MARKETING    | 55         |    5340     | 

1. Find the highest, lowest, sum and average salary of all employees.
2. Find the highest, lowest, sum and average salary for each department.
3. Write the query to display the number of people with the same department.
4. Find the average commission for all employees.
5. Create a query to display the manager and the salary of the lowest-paid employee for
   that manager.
6. For each department show the department and number of employees 
with salary of at least 3000.
7. List the departments that have at least 2 employee with not null commission.
