# Indexes Overview

### Table of Contents
<!-- TOC -->
* [Your Goals](#your-goals)
* [Indexes](#indexes)
<!-- TOC -->

### Your Goals
1.  Gain knowledge of common database objects such as indexes.
2.  Understand the concept of indexes, when and how to create them, and the advantages and disadvantages of using them.
3.  Learn about different types of indexes and best practices for creating and using them.
---

## Indexes

__An index__ is a data structure that improves the speed of data retrieval operations on a database table. It allows the database management system to quickly locate and retrieve specific rows in a table, rather than having to scan the entire table.
    
To create an index in PostgreSQL, use the "CREATE INDEX" command. The basic syntax is as follows:
```sql
CREATE INDEX IF NOT EXISTS index_name ON table_name (column_name);
```

### Advantages of using indexes:

1.  __Faster data retrieval__: Indexes allow the database management system to quickly locate and retrieve specific rows in a table, rather than having to scan the entire table, which can greatly improve query performance.
    
2.  __Improved query performance__: Indexes can improve the performance of various types of queries such as SELECT, UPDATE, and DELETE statements.
    
3.  __Reduced disk I/O__: By reducing the number of disk I/O operations required to retrieve data, indexes can also help to reduce disk I/O contention and improve the overall performance of the database.
    
4.  __Improved concurrency__: Indexes can also improve the concurrency of a database by allowing multiple queries to access the data simultaneously, and also by reducing contention for shared resources.
    

### Disadvantages of using indexes:

1.  __Increased storage space__: Indexes require additional storage space, as they are a separate data structure that needs to be maintained in addition to the table itself.
    
2.  __Slower update and insert operations__: As indexes need to be updated whenever the underlying data is modified, update and insert operations can be slower when indexes are present.
    
3.  __Increased maintenance__: Indexes require regular maintenance such as monitoring, analyzing and rebuilding.
    
4.  __Not always necessary__: If a table is small, or the queries executed on it are not very complex, indexes may not be necessary and can even hurt the performance.
    
5.  __Not suitable for low-cardinality columns__: Indexes are not suitable for low-cardinality columns which have a high number of distinct values relative to the number of rows in the table.
    

It's worth noting that the use of indexes is a trade-off between the performance and storage space. Therefore, it is important to use indexes judiciously and only create them on columns that will be frequently searched or sorted on.

### Types of indexes

Indexes can be classified as __clustered__ and __non-clustered__ indexes.

In databases, a __clustered index__ determines the physical order of data in a table. In other words, it determines how the rows of a table are stored on disk. In a clustered index, the leaf nodes of the index contain the actual data rows of the table. Because the data is physically stored in the same order as the index, searching for data using the index can be more efficient.

Consider a table named "orders" that contains a primary key column "order_id" and several other columns such as "customer_id", "order_date", and "total_amount". A clustered index on the "order_id" column would physically order the data in the table based on the "order_id" values. This would make it more efficient to search for orders by their order ID, since the data would be stored in the same order as the index.
 
A __non-clustered index__, on the other hand, provides a fast way to look up rows in a table, but does not determine the physical order of the data. Instead, the leaf nodes of the index contain the non-clustered index's key values and a reference, or pointer, to the actual data row that contains the key value.
   
Consider a table named "employees" that contains a primary key column "employee_id" and several other columns such as "first_name", "last_name", "department", and "salary". A non-clustered index on the "department" column would improve performance when searching for employees by their department, but it would not affect the physical order of the data in the table. The leaf nodes of this index would contain the department names and a reference to the actual data row that contains the department name.

It's worth to mention that different databases management systems may handle clustered and non-clustered indexes differently and in some systems, the term "clustered index" may not be used, and instead the physical ordering of data is determined by the primary key or other unique constraint.

__A B-Tree index__ is a type of index that is stored on disk and is used to speed up the search for specific data in a table. It works by organizing the data in a balanced tree structure, where each node in the tree contains a set of indexed values and a set of pointers to other nodes in the tree. The indexed values are used to determine the order of the data in the tree, and the pointers are used to navigate the tree to find the desired data

Here's an example of creating a table and a __B-Tree index__:
```sql
CREATE TABLE IF NOT EXISTS employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary NUMBER(10, 2)
);

CREATE INDEX IF NOT EXISTS emp_idx ON employees (employee_id);
```
This will create a table called "employees" with three columns: "employee_id", "name", and "salary". The primary key of the table is the "employee_id" column. And then we create an index on "employee_id" column, it will be called "emp_idx"

Here's an example of a query that would use the B-Tree index:
```sql
SELECT  *  FROM employees WHERE employee_id =  123;
```
This query would use the "emp_idx" index to quickly locate the row with employee_id = 123 in the "employees" table.

Here is also an visual representation of a __B-Tree index__:

```
                            Root (level 0)
                      /                 |                  \
                   Node1 (level 1)   Node2 (level 1)    Node3 (level 1)
                  /       |          /       |         /       |
                Leaf1   Leaf2     Leaf3    Leaf4    Leaf5    Leaf6

Leaf1: (10, "John Smith", "Los Angeles", "rowid:123")
      (15, "Jane Doe", "New York", "rowid:456")
Leaf2: (20, "Bob Johnson", "Chicago", "rowid:789")
      (25, "Samantha Williams", "Houston", "rowid:012")
Leaf3: (30, "Michael Brown", "Philadelphia", "rowid:345")
      (35, "Emily Davis", "Phoenix", "rowid:678")
Leaf4: (40, "Jacob Garcia", "San Antonio", "rowid:901")
      (45, "Nicholas Rodriguez", "San Diego", "rowid:234")
Leaf5: (50, "Alexander Martinez", "Dallas", "rowid:567")
      (55, "Elizabeth Taylor", "San Jose", "rowid:890")
Leaf6: (60, "Matthew Hernandez", "Jacksonville", "rowid:123")
      (65, "Ashley Anderson", "Indianapolis", "rowid:456")
```
I suggest you to watch this 5 minute video to understand better: https://www.youtube.com/watch?v=7h6x1SDWNnI

__A Bitmap index__ is a type of index that uses a bitmap to represent the index data. Each bit in the bitmap corresponds to a row in the table, and the value of the bit indicates whether the indexed value for that row is present in the index or not.

Bitmap indexes are most useful in situations where the indexed column has a low cardinality, meaning that it has a limited number of distinct values. This is because the size of a bitmap index is proportional to the number of rows in the table and the number of distinct indexed values.

Here is an example of creating a bitmap index on a table named "sales" on the column "product_id":
```sql
CREATE BITMAP INDEX IF NOT EXISTS product_id_idx ON sales (product_id);
```
Here is an example of using the bitmap index in a query:
```sql
SELECT * FROM sales WHERE product_id = 123;
```
This query will use the `product_id_idx` bitmap index to quickly locate all rows in the "sales" table where the `product_id` is equal to 123.

Example of table:
```sql
| id | name | product_id |
|----|------|------------|
| 1  | ABC  | 123        |
| 2  | XYZ  | 456        |
| 3  | LMN  | 123        |
```
In the above table, we can create a bitmap index on `product_id`. The index will have two bitmap one for product_id `123` and other for `456`. It will have 1 for the tuple having that product_id and 0 for the tuple not having that product_id.

Please note that, Bitmap index are not recommended to use in the case of high cardinality column and it also not suitable for columns frequently updated or inserted.

I suggest you to watch this 5 minute video to understand better: https://www.youtube.com/watch?v=5imhr4ye3Tw

In general, B-tree indexes can be either clustered or non-clustered. Bitmap indexes are non-clustered.

It is worth noting that different database management systems may handle clustered and non-clustered indexes differently. In some systems, the term "clustered index" may not be used, and instead the physical ordering of data is determined by the primary key or other unique constraint. Additionally, some database management systems may not support bitmap indexes.

### Best practices of creating and using indexes:

-   Create indexes on columns that are frequently searched or sorted on
-   Use a small number of indexes on a table, as too many indexes can slow down updates and inserts
-   Regularly analyze and rebuild indexes to maintain their efficiency
-   Remember about cardinality in columns as it requires different types of indexes
-   Consider the use of partial indexes for queries that only need to search a subset of the data.


