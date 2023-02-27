# Views Overview

### Table of Contents
<!-- TOC -->
* [Your Goals](#your-goals)
* [Views](#views)
<!-- TOC -->

### Your Goals
1.  Gain knowledge of common database objects such as views.
2.  Understand the concept of views, including the advantages and disadvantages of using them.
3.  Learn about materialized views and their limitations.
---
## Views

A __view__ is a virtual table that is based on the result of a SELECT statement. The SELECT statement is stored in the database and is associated with a name, which can be used to reference the view like a table. Here's an example of how to create a view in PostgreSQL:
```sql
CREATE OR REPLACE VIEW view_name AS
SELECT column1, column2
FROM table_name
WHERE some_column = some_value;
```
In this example, `view_name` is the name of the view, and the `SELECT` statement defines the columns and rows that the view will consist of. The view can then be used in SELECT, UPDATE, and DELETE statements just like a regular table.

### Advantages of using views include

-   Simplifying the structure of the underlying tables by only exposing the columns and rows that are needed for a specific purpose
-   Creating a level of security by only allowing access to certain columns and rows of a table
-   Improving query performance by pre-calculating and storing the result of a SELECT statement
-   Providing a consistent interface to access the data in the underlying tables, even if the structure of the underlying tables change.

### Disadvantages of using views include:

-   Increased complexity of the database schema, as views add another level of abstraction
-   Additional overhead to maintain the view when the underlying tables are modified
-   Depending on the complexity of the view, the performance may be worse than running the SELECT statement directly on the underlying table

## A materialized view 

__A materialized view__ is a view whose result is stored in a physical table, which allows for faster query performance, since the result does not have to be recalculated every time the view is queried. In PostgreSQL, Materialized views can be created using the `CREATE MATERIALIZED VIEW` statement.
```sql
CREATE OR REPLACE MATERIALIZED VIEW mat_view_name AS
SELECT column1, column2
FROM table_name
WHERE some_column = some_value;
```


