## FUNCTIONS AND OPERATOR REFERENCES

## Table of content

- [Your goals](#your-goals)
- [Functions](#functions)
    - [Type conversion](#type-conversion)
    - [Aggregate functions](#aggregation-functions)
- [Group by](#group-by)
- [Having clause](#having-clause)
- [Stored procedures](#stored-procedures)

## Your goals
- Understand main differences between function and stored procedure.
- Be able to name and briefly explain how aggregate function works.
- Understand when and how to use group by statement.
- Distinguish between having clause and where. 

## FUNCTIONS

Functions in SQL Server are the database objects that contains a set of SQL statements to perform a specific task.
Every kind of function can take base types, composite types, or combinations of these as arguments (parameters). In addition, every kind of function can return a base type or a composite type. Functions can
also be defined to return sets of base or composite values. Arguments of an SQL function can be referenced in the function body using names. To use a name, declare the function argument as having a name, and then just write that name in the
function body.

The following are the rules for creating SQL Server functions:

- A function must have a name, and the name cannot begin with a special character such as @, $, #, or other similar characters.
- SELECT statements are the only ones that operate with functions.
- We can use a function anywhere such as AVG, COUNT, SUM, MIN, DATE, and other functions with the SELECT query in SQL.
- Whenever a function is called, it compiles.
- Functions must return a value or result.
- Functions use only input parameters.
- We cannot use TRY and CATCH statements in functions.

SQL Server categorizes the functions into two types:
- System Functions
- User-Defined Functions

## System Functions

Functions that are defined by the system are known as system functions. In other words, all the built-in functions supported by 
the server are referred to as System functions. The built-in functions save us time while performing
the specific task. These types of functions usually work with the SQL SELECT statement to calculate values and manipulate data.

## Type Conversion

When you define SQL Server database tables, local variables, expressions or parameters, you should 
specify what kind of data will be stored in those objects, such as text data, numbers, money or dates.
This attribute is called the SQL Server Data Type. SQL Server provides us with a big library of system
data types that define all types of data that can be used with SQL Server, from which we can choose
the SQL Server data type that is suitable for the data we will store in that object.
SQL Server data types can be categorized into main categories:

- Exact Numeric types, such as INT, BIGINT and numeric.
- Approximate Numeric types, such as FLOAT and REAL.
- Date and Time types, such as DATE, TIME and DATETIME.
- Character Strings, such as CHAR and VARCHAR.
- Unicode Character Strings, such as NCHAR and NVARCHAR.
- Binary Strings, such as BINARY and VARBINARY.
- Other Data Types

When two expressions with different SQL Server data types are combined together by an operator, they
may not always play well together. They may be automatically, aka implicitly, converted into the
suitable data type, making the generated combination better, without raising any errors. But you may
face other cases, in which you need to manually, aka explicitly, perform a suitable data type
conversion, in order to avoid the combination error. 

### CAST

The **cast** function is an ANSI SQL-92 compliant function that is used to convert an expression from one 
SQL Server data type to another. You need to provide the **CAST** function with the expression that will 
be converted and the target data type that the provided value will be converted to. Being supported 
by ANSI SQL Standard, it is a preferable choice to convert between the different data types, especially 
when this conversion query will be executed on different DBMS. The syntax that is used for the **CAST**
function is shown below:

**CAST(expression AS data_type)**

When a **cast** is applied to a value expression of a known type, it represents a run-time type conversion. The
**cast** will succeed only if a suitable type conversion operation has been defined.

Example: SELECT **CAST**(42 AS float8);

converts the integer constant 42 to type float8 by invoking a previously specified function, in this case float8(int4).
(If no suitable **cast** has been defined, the conversion fails.)

Besides the type **CAST** syntax, you can use the following syntax to convert a value of one type into another:
expression::type
See the following example:

SELECT <br>
&emsp; '100'::INTEGER, <br>
&emsp; '30-AUG-2001'::DATE; <br>

In SQL, you can cast a value to a different data type using the CAST function. To cast a value to NULL, use the following syntax:
SELECT **CAST**(NULL AS data_type);

In the following example, CAST() function is used to convert a string expression to a datetime value.
SELECT **CAST**('11/23/2022' AS datetime) as result;

### Aggregation functions

An aggregate
function computes a single result from multiple input rows. For example, there are aggregates to compute
the [count](#count), [sum](#sum), [avg](#avg)(average), [max](#minmax) (maximum) and [min](#minmax) (minimum) over a set of rows.

### SUM

SUM is a SQL aggregate function. that totals the values in a given column. Unlike COUNT, you can
only use **SUM** on columns containing numerical values.

Let's take a look on the table:

| product_id(PK) |      name      | price | units_sold |
|----------------|:--------------:|------:|:----------:|
| 1              |   Spaghetti    | 12.99 |    204     | 
| 2              |    Chicken     | 10.75 |     44     | 
| 3              | Grilled Cheese |  9.50 |     23     | 
| 4              |     Steak      | 18.00 |    Null    | 
| 5              |     Sushi      | 14.99 |     86     | 
| 6              |    Lasagna     | 11.00 |    132     | 

The query below selects the sum of the units_sold column from the products' dataset:

SELECT **SUM**(units_sold) <br>
&emsp; FROM products

An important thing to remember: aggregators only **aggregate vertically**. If you want to 
perform a calculation across rows, you would do this with simple 
arithmetic.

You don't need to worry as much about the presence of nulls with SUM as you 
would with [COUNT](#count), as SUM treats nulls as 0.

### COUNT

COUNT is a SQL aggregate function for counting the number of rows in a particular column.
The result of the COUNT function depends on the argument that you pass to it.
The following example uses the COUNT(*) function to get the number of rows from the products table:

SELECT **COUNT**(*) <br>
&emsp; FROM products

The result of the select statement will be 6.

**NULL values will not be counted with count function**

SELECT COUNT(units_sold) <br>
&emsp; FROM products

The result of the select statement will be 5.

One nice thing about COUNT is that you can use it on non-numerical columns.

### MIN/MAX

MIN and MAX are SQL aggregation functions that return the lowest and highest values in a particular column.

They're similar to COUNT in that they can be used on non-numerical columns.
Depending on the column type, MIN will return the lowest number, earliest
date, or non-numerical value as close alphabetically to "A" as possible. 
As you might suspect, MAX does the opposite—it returns the highest number,
the latest date, or the non-numerical value closest alphabetically to "Z."

For example, the following query selects the MIN and the MAX from the numerical
volume column in the products' dataset.

SELECT **MIN**(price) AS min_price, <br>
&emsp; **MAX**(price) AS max_price <br>
&emsp;FROM products

MAX and MIN ignore any null values.
MAX and MIN returns NULL when there is no row to select.

### AVG

AVG is a SQL aggregate function that calculates the average of a selected
group of values. It's very useful, but has some limitations. First, it 
can only be used on numerical columns. Second, it ignores nulls completely.

For example, the following query selects average units_sold from the numerical
volume column in the products' dataset.

SELECT **AVG**(units_sold) <br>
&emsp; FROM products

Note: Sum of units_sold is 639 and avg will return 106.5. 639 is divided by 6, because
null value will be ignored.

## Group by

In SQL, the GROUP BY clause is used to group rows by one or more columns.

- In the query, GROUP BY clause is placed after the WHERE clause.
- In the query, GROUP BY clause is placed before ORDER BY clause if used any.
- In the query , Group BY clause is placed before Having clause .
- Place condition in the having clause

**Note**: The GROUP BY clause is used in conjunction with aggregate functions
such as MIN(), MAX(), SUM(), AVG() and COUNT(), etc.

Sample Table is as follows:

| driver_id(PK) |   name    | constructor | title | pole |
|---------------|:---------:|------------:|:-----:|:----:|
| 1             |   Mike    |     Ferrari |   1   |  4   |
| 2             | Sebastian |    Red Bull |   4   |  57  |
| 3             |  Michael  |     Ferrari |   7   |  68  |
| 4             | Fernando  |     Renault |   2   |  22  |
| 5             |   Lewis   |     McLaren |   1   | 103  |
| 6             |  Alberto  |     Ferrari |   2   |  14  |
| 7             |   Juan    |    Mercedes |   2   |  29  |

Following query will return how many titles each constructor won.

SELECT constructor SUM(title) <br>
&emsp; FROM drivers <br>
&emsp; **GROUP BY** constructor

The result will look like:

| constructor |   sum   |
|-------------|:-------:|
| McLaren     |    1    | 
| Ferrari     |   10    |   
| Red Bull    |    4    |     
| Mercedes    |    2    |     

## Distinct

The SELECT DISTINCT statement is used to return only distinct (different) values.

Inside a table, a column often contains many duplicate values; and sometimes you
only want to list the different (distinct) values.

Query: SELECT **DISTINCT** constructor FROM drivers; 
lists the number of different(distinct) constructors

In certain situations, the DISTINCT and GROUP BY clauses can be used to generate identical 
results–but one is consistently faster than the other.
The GROUP BY clause is faster than the SELECT DISTINCT clause (most of the cases) because
it does not require writing to a temporary file on disk. So if you need to use aggregate functions and
better performance **Group by** is the best solution for that.

## Having clause

A HAVING clause is like a WHERE clause, but applies only to groups as a whole
(that is, to the rows in the result set representing groups), whereas the WHERE
clause applies to individual rows. A query can contain both a WHERE clause and
a HAVING clause. In that case:

- The WHERE clause is applied first to the individual rows in the tables or table-valued objects
in the Diagram pane. Only the rows that meet the conditions in the WHERE clause are grouped.
- The HAVING clause is then applied to the rows in the result set. Only the groups that meet the
HAVING conditions appear in the query output. You can apply a **HAVING** clause only to columns
that also appear in the [GROUP BY](#group-by) clause or in an aggregate function. 

Imagine you want to get poles of each constructor, which has more than 50 poles and the driver 
must have more than 2 titles to be included.

For getting the result you should query:

SELECT constructor, SUM(pole) AS pole <br>
&emsp; FROM drivers <br>
&emsp; **WHERE** title > 1 <br>
&emsp; GROUP BY constructor <br>
&emsp; **HAVING SUM**(pole) > 50 <br>

At first, drivers with less than 2 titles will be filtered and will get:

| driver_id(PK) |   name    | constructor | title | pole |
|---------------|:---------:|------------:|:-----:|:----:|
| 2             | Sebastian |    Red Bull |   4   |  57  |
| 3             |  Michael  |     Ferrari |   7   |  68  |
| 4             | Fernando  |     Renault |   2   |  22  |
| 6             |  Alberto  |     Ferrari |   2   |  14  |
| 7             |   Juan    |    Mercedes |   2   |  29  |

After that they will be grouped by constructor and will get:

| constructor  | pole |
|--------------|:----:|
| Red Bull     |  57  | 
| Ferrari      |  82  |   
| Renault      |  22  |     
| Mercedes     |  29  |   

And finally only constructors with more than 50 poles(**HAVING** SUM(pole) > 50) will be returned:

| constructor  | pole |
|--------------|:----:|
| Red Bull     |  57  | 
| Ferrari      |  82  |

Note: 
- The WHERE clause cannot contain aggregate functions, while HAVING can.
- WHERE clause filters rows before grouping, HAVING clause - after grouping.
- HAVING clause can not be used without the group by clause.
- Where can be used with all DML commands, while HAVING can only be used while querying with SELECT. 

## Stored procedures

A procedure is a database object similar to a function. The key differences are:

- Procedures are defined with the CREATE PROCEDURE command, not CREATE FUNCTION.
- Procedures do not return a function value; hence CREATE PROCEDURE lacks a RETURNS clause.
- However, procedures can instead return data to their callers via output parameters.
- While a function is called as part of a query or DML command, a procedure is called in isolation using
the CALL command.
- A procedure can commit or roll back transactions during its execution (then automatically beginning a
new transaction), so long as the invoking CALL command is not part of an explicit transaction block. function cannot do that.
- Certain function attributes, such as strictness, don't apply to procedures. Those attributes control how
the function is used in a query, which isn't relevant to procedures.

Benefits of using stored procedures:
- It can be easily modified: We can easily modify the code inside the 
stored procedure without the need to restart or deploying the application.
SQL Server Stored procedures eliminate such challenges
by storing the code in the database. so, when we want to change the logic
inside the procedure we can just do it by simple ALTER PROCEDURE statement.
- Reduced network traffic: When we use stored procedures instead of writing
T-SQL queries at the application level, only the procedure name is passed
over the network instead of the whole T-SQL code. 
- Reusable: Stored procedures can be executed by multiple users or multiple
client applications without the need of writing the code again. 
- Security: Stored procedures reduce the threat by eliminating direct access
to the tables. we can also encrypt the stored procedures while creating
them so that source code inside the stored procedure is not visible.
Use third-party tools like ApexSQL Decrypt to decrypt the encrypted
stored procedures. 
- Performance: The SQL Server stored procedure when executed for the first
time creates a plan and stores it in the buffer pool so that the plan
can be reused when it executes next time.

Differences between Stored Procedure and Function in SQL Server:

1. The function must return a value but in Stored Procedure it is optional. Even a procedure can return zero or n values.
2. Functions can have only input parameters for it whereas Procedures can have input or output parameters.
3. Functions can be called from Procedure whereas Procedures cannot be called from a Function.
4. The procedure allows SELECT as well as DML(INSERT/UPDATE/DELETE) statement in it whereas Function allows only SELECT statement in it.
5. Procedures cannot be utilized in a SELECT statement whereas Function can be embedded in a SELECT statement.
6. Stored Procedures cannot be used in the SQL statements anywhere in the WHERE/HAVING/SELECT section whereas Function can be.
7. An exception can be handled by try-catch block in a Procedure whereas try-catch block cannot be used in a Function.
8. We can use Transactions in Procedure whereas we can't use Transactions in Function.