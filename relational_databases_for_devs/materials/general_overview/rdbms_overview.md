# RDBMS Overview

### Table of Contents

<!-- TOC -->

* [Your Goals](#your-goals)
* [RDBMS](#rdbms)

<!-- TOC -->

### Your Goals

1. Understand the basic concepts of a relational database and a relational database management system (RDBMS).
2. Learn about the properties of relational tables and how they are used in a RDBMS.
3. Gain knowledge of common database objects such as schemas, tables, views, indexes, constraints, triggers, and stored
   procedures.

---

### RDBMS

In this chapter, we will briefly overview RDBMS and explain what views, indexes and triggers are.

<b> A relational database management system (RDBMS) </b> is a type of database management system (DBMS) that uses a relational model to organize data. In a relational database, data is organized into one or more __tables__, each of which consists of a __set of rows and columns__.

Here is a brief summary of the properties of a relational table:

1. __Atomic values__ (Each column's values cannot be broken down into smaller parts)
2. __Same data type__ (All values in a column are of the same type)
3. __Unique rows__ (Each row has a unique identifier)
4. __Column order does not matter__
5. __Row order does not matter__
6. __Unique column names__ (Each column has a distinct name)

These properties are collectively known as the Relational Model, which is the foundation of a relational database
management system (RDBMS). Relational tables are the core data storage units of a relational database and designed to
store data in a way that is easy to maintain, query and understand.

In SQL Server, there are many different types of objects that make up a database, such as tables, views, stored
procedures, functions, constraints, triggers, etc. As a developer or database administrator, it can be challenging to
keep track of all the objects within a database or across multiple databases. One way to find specific objects is to use
system catalog views or information schema views.

Another way to find specific objects is to use the Object Explorer in SQL Server Management Studio (SSMS) to search for
objects within a specific database. There are also third-party tools such as ApexSQL Search that can be used to search
for SQL Server objects across multiple databases.

1. __Use `sys.objects` system catalog view__:

```sql
SELECT name FROM sys.objects WHERE type = 'U' AND name LIKE '%mytable%';
```

This query uses the `sys.objects` system catalog view to find all tables in a specific database whose name contains the
string 'mytable'. The `sys.objects` view contains a row for each object in a database, and the `type` column can be used
to filter for specific types of objects, such as 'U' for user tables. The `name` column is used to filter for tables
whose name contains the string 'mytable' using the `LIKE` operator.

2. __Use system information schema views__

```sql
SELECT column_name FROM information_schema.columns WHERE table_name = 'mytable';
```

This query uses the `information_schema.columns` view to find all columns in a specific table. The `information_schema`
views provide a consistent way to access metadata across different types of SQL databases. The `table_name` column is
used to filter for the specific table, in this case 'mytable'.

3. __Search object using SSMS object explorer details__:
   You can use object explorer in SQL Server management studio and expand the databases, schemas, and objects to find
   the required object. You can also filter and search for specific object using the search bar in object explorer.

4. __Use ApexSQL Search in SSMS to search for SQL database objects__:
   ApexSQL Search is a third-party tool that can be integrated into SSMS. It allows you to search for SQL Server objects
   such as tables, views, stored procedures, and functions across multiple databases. It also allows you to filter and
   search for specific objects based on their name, type, or schema.

As we have already said, in RDBMS we often meet tables. Here is an example of a simple SQL table:

| Column Name | Data Type | Constraints |
| --- | --- | --- |
| id | INT | PRIMARY KEY |
| name | VARCHAR(255) | NOT NULL |##
| age | INT | NOT NULL || address | VARCHAR(255) | NOT NULL |
| phone_number | VARCHAR(15) | NOT NULL |

This table represents a simple table with 5 columns: "id", "name", "age", "address", "phone_number".

- "id" column is of INT type and has a PRIMARY KEY constraint
- "name" column is of VARCHAR(255) type and has a NOT NULL constraint
- "age" column is of INT type and has a NOT NULL constraint
- "address" column is of VARCHAR(255) type and has a NOT NULL constraint
- "phone_number" column is of VARCHAR(15) type and has a NOT NULL constraint

Please note that this is a simple example, and actual tables will usually have much more complex structure and a lot
more columns, each with its own unique set of constraints, Also each RDBMS has its own implementation of data types and
constraints and this table syntax is not standard syntax it is just to represent the table structure in a simple format.

Here is an example of the table I described earlier, with 4 rows of data:

| id | name | age | address         | phone_number |
|----|------|-----|-----------------|--------------|
| 1  | John | 25  | New York City, NY | 555-555-1234 |
| 2  | Jane | 32  | Los Angeles, CA  | 555-555-5678 |
| 3  | Bob  | 42  | Chicago, IL      | 555-555-9876 |
| 4  | Emily| 29  | Seattle, WA      | 555-555-4321 |

Please note that the data is just an example and a placeholder, this table is not connected to any database and you can
use any data that you want. Also, primary key values usually are incremental and are generated by the database and not
entered by the user.

The __rows__ in a table represent individual records, while the __columns__ represent the fields or attributes of each
record. The data in a relational database is typically organized into one or more tables, each of which is made up of a
set of rows and columns.

The main benefit of using an RDBMS is that it allows for data to be related across multiple tables through the use of __
keys__, which are used to link rows in one table to rows in other tables. This allows for the creation of powerful,
flexible, and efficient databases that can be easily queried and updated.

Some popular examples of RDBMS software include <b>MySQL, Oracle, and Microsoft SQL Server</b>.

Most of RDBMSs support SQL (__Structured Query Language__) as main interface for manipulate the data

In a relational database, tables have several properties that define how the data is organized and stored. Some of the
main properties of relational tables are:

- **Rows and columns**: Each table is made up of rows and columns, where each row represents a single record in the
  table, and each column represents a field or attribute of that record.

- **Primary key**: Each table must have a primary key, which is a field or set of fields that uniquely identify each row
  in the table. Primary keys are used to link tables together and to enforce the integrity of the data.

- **Foreign key**: A foreign key is a field or set of fields in a table that is used to link that table to another
  table. A foreign key refers to the primary key of another table, and can be used to enforce referential integrity.

- **Normalization**: Normalization is the process of organizing data into separate tables to minimize data redundancy
  and improve data integrity. The most common forms of normalization are first normal form (1NF), second normal form (
  2NF), and third normal form (3NF).

- **Index** : Some RDBMSs allow indexes on specific column or group of columns for faster searching and ordering on
  them.

- **Data Types** : Each column in a table has a specific data type that determines what kind of data it can store.
  Common data types include text, number, date, and Boolean.

Together, these properties help ensure that data is stored in a way that is consistent, accurate, and efficient.

There are two types of SQL commands that are used to interact with a database:

1. **Data Definition Language (DDL)** commands are used to define the structure of the database, such as creating
   tables, altering tables, and deleting tables.

2. **Data Manipulation Language (DML)** commands are used to manage the data within the database, such as selecting
   data, inserting data, updating data, and deleting data.

__DDL__ commands are used to set up the structure of the database and usually run quickly. __DML__ commands are used to
manage the data in the database and might take longer to run, especially if the table is large.

In a relational database, there are several types of objects that are used to organize and manipulate data. Each RDBMS
may have different implementation of these objects and additional objects, but the basic principles remain the same.
Here's a brief overview of some common database objects using syntax specific to __PostgreSQL__, don't be afraid, its
syntax, as I said before, is not much different from others:

1. **Schemas**: A schema is a container for database objects such as tables, views, indexes, procedures, and so on. A
   schema can be thought of as a namespace, or a way to group related objects together.

The default schema in a database can vary depending on the RDBMS being used. Here are a few examples of the default
schema in popular databases:

- In PostgreSQL, the default schema is called "public".
- In Oracle, the default schema is the name of the user that owns the database objects.
- In MySQL, the default schema is the name of the database.
- In SQL Server, the default schema is "dbo".
- In SQLite, every table is created in a single, implicit schema called "main".
- In DB2, the default schema is called "NULLID".

It's worth noting that in some cases it's possible to configure a different default schema, but it depends on the RDBMS
capabilities.

You can create a new schema with the following command:

```sql
CREATE SCHEMA IF NOT EXISTS myschema;
```

You can also specify owner of the schema:

```sql
CREATE SCHEMA IF NOT EXISTS myschema AUTHORIZATION myuser;
```

The "IF NOT EXISTS" clause will check if a schema with the specified name already exists in the database. If it does,
the query will not create a new schema, and will return a notice instead. If it does not, the query will create a new
schema with the specified name and the specified owner.

2. **Tables**: A table is the basic structure for storing data in a relational database. Tables are made up of rows and
   columns, and each row represents a single record in the table. Tables are used to store data in a structured way, and
   can be queried, updated, and deleted using SQL commands.

You can create a new table with the following command:

```sql
CREATE TABLE IF NOT EXISTS myschema.mytable (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    age int NOT NULL
);
```

##### 2.1 Database table relationships

After you have created a table for each subject in your database, you have to give Access a way to bring that
information back together again when needed. You do this by placing common fields in tables that are related, and by
defining relationships between your tables. You can then create queries, forms, and reports that display information
from several tables at once.

###### **one to one relationship**

In One-to-One relationship, one record of the first table will be linked to zero or one record of another table.This
relationship is not common because, most often, the information related in this way is stored in the same table. You
might use a one-to-one relationship to divide a table with many fields, to isolate part of a table for security reasons,
or to store information that applies only to a subset of the main table. When you do identify such a relationship, both
tables must share a common field.

###### **one to many relationship**

One-to-Many is the most commonly used relationship among tables. A single record from one table can be linked to zero or
more rows in another table.To represent a one-to-many relationship in your database design, take the primary key on
the "one" side of the relationship and add it as an additional field or fields to the table on the "many" side of the
relationship.

###### **many to many relationship**

Many-to-Many relationship lets you relate each row in one table to many rows in another table and vice versa.To
represent a many-to-many relationship, you must create a third table, often called a junction table, that breaks down
the many-to-many relationship into two one-to-many relationships. You insert the primary key from each of the two tables
into the third table. As a result, the third table records each occurrence, or instance, of the relationship.

3. **Views**: A view is a virtual table that is based on the result of a SELECT statement. Views do not store data
   themselves, but rather provide a way to access data from one or more tables in a specific way. A view can be thought
   of as a pre-defined query that can be used to retrieve data from the underlying tables.

You can create a new view with the following command:

```sql
CREATE VIEW IF NOT EXISTS myschema.myview AS
SELECT name, age FROM myschema.mytable
WHERE age > 21;
```

4. **Indexes**: An index is a database object that is used to improve the performance of queries. Indexes are typically
   created on one or more columns in a table and are used to speed up the process of searching for specific data. An
   index allows the database to quickly locate the requested data without having to scan the entire table.

You can create an index on one or more columns in a table with the following command:

```sql
CREATE INDEX IF NOT EXISTS myindex ON myschema.mytable (name, age);
```

5. **Constraints**: Constraints are used to enforce rules on the data in a table. They are used to ensure that data is
   entered correctly and consistently, and can detect and prevent errors. Some examples of constraints include primary
   key, foreign key, and unique constraints.

You can add a primary key constraint to a table with the following command:

```sql
ALTER TABLE myschema.mytable ADD CONSTRAINT 
IF NOT EXISTS mypk PRIMARY KEY (id);
```

You can also add a foreign key constraint on table as follow:

```sql
ALTER TABLE myschema.mytable ADD CONSTRAINT 
IF NOT EXISTS myfk FOREIGN KEY (other_table_id) 
REFERENCES other_schema.other_table(id);
```

6. **Triggers**: A trigger is a special kind of stored procedure that is automatically executed in response to certain
   events. Triggers can be used to enforce business rules, maintain data integrity, or perform other tasks
   automatically.

You can create a new trigger that automatically updates a timestamp field in a table when a record is inserted or
updated with the following command:

```sql
CREATE TRIGGER IF NOT EXISTS update_timestamp
	BEFORE INSERT OR UPDATE ON myschema.table
	FOR EACH ROW
	EXECUTE FUNCTION update_timestamp_func();
```

7. **Stored Procedures**: Stored Procedures are pre-compiled and executed in the database, they are like a set of
   instructions to perform specific task, they can contain a number of DML and DDL statements, they can also accept
   parameters and return result set. They can be used to encapsulate complex logic and business rules and to improve the
   performance of the system.

You can create a new stored procedure with the following command:

```sql
CREATE OR REPLACE FUNCTION myschema.my_procedure(var1 INTEGER, var2 INTEGER)
RETURNS TABLE(col1 INTEGER, col2 INTEGER) AS $$
BEGIN
    RETURN QUERY SELECT col1, col2 FROM myschema.mytable WHERE age > var1 AND age < var2;
END;
```





