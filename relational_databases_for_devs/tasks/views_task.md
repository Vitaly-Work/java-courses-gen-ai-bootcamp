# Views - Practical Task


### Table of Contents
<!-- TOC -->
* [Your Goals](#your-goals)
* [Practical Task](#practical-task)
<!-- TOC -->

### Your Goals
1.  This task will help you to understand the practical usage of views and how they can be managed to simplify data retrieval and management in a database
---

## Practical Task

Create a view that displays a subset of data from an existing table, and then use the view to run a query.

   1. Choose a database such as PostgreSQL.
   2. Create a new database, or use an existing one.
   3. Create a new table with some sample data, using a CREATE TABLE statement.
   4. Insert some data into the table using an INSERT INTO statement.
   5. Write a SELECT statement that retrieves a subset of the data from the table, based on certain criteria, such as a specific value in a column.
   6. Use the CREATE VIEW statement to create a view based on the SELECT statement. Give the view a name and specify the columns that should be included in the view.
   7. Use the view to run a SELECT statement that retrieves the same data as the previous SELECT statement. The difference is that the data is now being retrieved from the view instead of the original table.
   8. Experiment with updating or deleting the data in the original table, and then re-running the query that uses the view. Notice how the data returned by the view is automatically updated, based on the data in the original table.
   9.  If you are using PostgreSQL, try creating a materialized view using the CREATE MATERIALIZED VIEW statement instead of a regular view, and observe the performance differences between the two types of views.

