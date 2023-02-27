# Indexes - Practical Task


### Table of Contents
<!-- TOC -->
* [Your Goals](#your-goals)
* [Practical Task](#practical-task)
<!-- TOC -->

### Your Goals
1.  Purpose of this task is to create three indexes on the "employees" table, analyze the query execution plan and cost for two different queries with and without the indexes, and finally remove the created indexes from the "employees" table.
---

## Practical Task
This is an example of creating the `employees` table with sample data:
```sql
CREATE TABLE employees (
  id SERIAL PRIMARY KEY,
  first_name TEXT,
  last_name TEXT,
  department TEXT,
  salary NUMERIC(10, 2)
);

INSERT INTO employees (first_name, last_name, department, salary) VALUES
('John', 'Smith', 'Sales', 50000),
('Jane', 'Doe', 'Marketing', 60000),
('Bob', 'Johnson', 'Sales', 55000),
('Alice', 'Williams', 'HR', 70000),
('Charlie', 'Brown', 'IT', 65000),
('David', 'Davis', 'IT', 75000),
('Emily', 'Wilson', 'Marketing', 80000),
('Frank', 'Jones', 'HR', 45000),
('Gina', 'Garcia', 'Sales', 90000),
('Harry', 'Harris', 'Marketing', 55000);

```

__Here is a task to create and use indexes on the `employees` table:__ 

1.  Create an index named "employee_dept_idx" on the "department" column of the "employees" table.

2.  Create an index named "employee_salary_idx" on the "salary" column of the "employees" table.

3.  Create a non-clustered index named "employee_name_idx" on the "last_name" and "first_name" columns of the "employees" table. 

4.  Find out the execution plan and cost of the following query with and without indexes created:
	```sql
	`EXPLAIN ANALYZE SELECT * FROM employees WHERE department = 'Sales';
	```
5.  Find out the execution plan and cost of the following query with and without indexes created:
	```sql
	`EXPLAIN ANALYZE SELECT * FROM employees WHERE last_name = 'Smith' AND first_name = 'John';
	```
6.  Remove the "employee_dept_idx" index from the "employees" table.

7.  Remove the "employee_salary_idx" index from the "employees" table.

8.  Remove the "employee_name_idx" index from the "employees" table.

