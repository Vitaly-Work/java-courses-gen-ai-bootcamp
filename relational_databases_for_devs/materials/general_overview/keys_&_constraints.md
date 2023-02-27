# Keys and Constraints


## Table of content

- [Your goals](#your-goals)
- [Constraints Overview](#constraints-overview)
  - [Not-Null Constraints](#not-null-constraints)
  - [Unique Constraints](#unique-constraints)
  - [Check Constraints](#check-constraints)
  - [Default Constraints](#default-constraints)
- [Primary Keys ](#primary-keys)
- [Foreign Keys ](#foreign-keys)

## Your goals
- Understand basic concepts of constraints.
- Be able to name and briefly explain each type of constraint.
- Understand when and how to create constraints.
- Know what [primary key](#primary-keys) is and how it differs from [unique](#unique-constraints) and [foreign keys](#foreign-keys).


## Constraints Overview

Constraints in SQL means we are applying certain conditions or restrictions on the database.
Constraints are used to limit the type of data that can go into a table. This ensures the accuracy and reliability of the data in the table.

Data types are a way to limit the kind of data that can be stored in a table. But there is no standard data type that accepts only positive numbers. Another
issue is that you might want to constrain column data with respect to other columns or rows. For example,
in a table containing product information, there should be only one row for each product number. To that end, SQL allows you to define constraints on columns and tables. If a user attempts to store data in a column that would
violate a constraint, an error is raised.


## Not-Null Constraints

A not-null constraint simply specifies that a column must not assume the null value. A syntax example:

CREATE TABLE products ( <br>
&emsp; product_id integer **NOT NULL**, <br>
&emsp; name text **NOT NULL**, <br>
&emsp; price numeric <br>
); <br>

A not-null constraint is always written as a column constraint. A not-null constraint is functionally equivalent to creating a check constraint CHECK (column_name IS NOT NULL), but in PostgreSQL
creating an explicit not-null constraint is more efficient. The drawback is that you cannot give explicit
names to not-null constraints created this way.

The NOT NULL constraint has an inverse: the NULL constraint. This does not mean that the column must
be null, which would surely be useless. Instead, this simply selects the default behavior that the column
might be null.

CREATE TABLE products ( <br>
&emsp; product_id integer **NULL**, <br>
&emsp; name text **NULL**, <br>
&emsp; price numeric <br>
); <br>

**In most database designs the majority of columns should be marked not null.**


## Unique Constraints

Unique constraints ensure that the data contained in a column, or a group of columns, is unique among
all the rows in the table. The syntax is:

CREATE TABLE products ( <br>
&emsp; product_id integer **UNIQUE**, <br>
&emsp; name text, <br>
&emsp; price numeric <br>
); <br>

when written as a column constraint, and:

CREATE TABLE products ( <br>
&emsp; product_id integer, <br>
&emsp; name text, <br>
&emsp; price numeric, <br>
&emsp; **UNIQUE** (product_id) <br>
); <br>

when written as a table constraint.

To define a unique constraint for a group of columns, write it as a table constraint with the column names
separated by commas:

CREATE TABLE example ( <br>
&emsp; a integer, <br>
&emsp; b integer, <br>
&emsp; c integer, <br>
&emsp; **UNIQUE** (a, c) <br>
);

This specifies that the combination of values in the indicated columns is unique across the whole table,
though any one of the columns need not be (and ordinarily isn't) unique.

You can assign your own name for a unique constraint, in the usual way:

CREATE TABLE products ( <br>
&emsp; product_id integer CONSTRAINT must_be_different **UNIQUE**, <br>
&emsp; name text, <br>
&emsp; price numeric <br>
);

In general, a unique constraint is violated if there is more than one row in the table where the values of all
of the columns included in the constraint are equal. By default, two null values are not considered equal
in this comparison. That means even in the presence of a unique constraint it is possible to store duplicate
rows that contain a null value in at least one of the constrained columns. **NULL NOT DISTINCT** and **NOT NULL** constraint
can solve this problem.

## Check Constraints

A check constraint is the most generic constraint type. It allows you to specify that the value in a certain
column must satisfy a Boolean (truth-value) expression. For instance, to require positive product prices,
you could use:

CREATE TABLE products ( <br>
&emsp; product_id integer, <br>
&emsp; name text, <br>
&emsp; price numeric **CHECK (price > 0)** <br>
);

A check constraint consists of the key word CHECK
followed by an expression in parentheses. You can also give the constraint a separate name. This clarifies error messages and allows you to refer to
the constraint when you need to change it. The syntax is:

CREATE TABLE products ( <br>
&emsp; product_id integer, <br>
&emsp; name text, <br>
&emsp; price numeric CHECK (price > 0), <br>
&emsp; discounted_price numeric CHECK (discounted_price > 0), <br>
&emsp; CHECK (price > discounted_price) <br>
);

It should be noted that a check constraint is satisfied if the check expression evaluates to **true** or the **null**
value. Since most expressions will evaluate to the null value if any operand is null, they will not prevent
null values in the constrained columns. To ensure that a column does not contain null values, the **NOT NULL**
constraint can be used.

## Default Constraints

A column can be assigned a default value. When a new row is created and no values are specified for
some of the columns, those columns will be filled with their default values. If no default value is 
declared explicitly, the default value is the **null** value. This usually makes sense
because a null value can be considered to represent unknown data.

In a table definition, default values are listed after the column data type. For example:

CREATE TABLE products ( <br>
&emsp; product_id integer, <br>
&emsp; name text, <br> 
&emsp; price numeric **DEFAULT** 9.99 <br>
);

The default value can be an expression, which will be evaluated whenever the default value is inserted
(not when the table is created).

A common example is for a timestamp column to have a default of
CURRENT_TIMESTAMP, so that it gets set to the time of row insertion. Another common example is
generating a “serial number” for each row. In PostgreSQL this is typically done by something like:

CREATE TABLE products ( <br>
&emsp;  product_id integer **DEFAULT** nextval('products_product_id_seq'), <br>
&emsp; name text, <br>
);

where the nextval() function supplies next values from a sequence object.

**Note** that, dropping the default constraint will not affect the current data in the table,
it will only apply to new rows.

## Primary Keys

A primary key constraint indicates that a column, or group of columns, can be used as a unique identifier
for rows in the table. Primary keys can span more than one column; the syntax is similar to [unique constraints](#unique-constraints):

CREATE TABLE example ( <br>
&emsp; a integer, <br>
&emsp; b integer, <br>
&emsp; c integer, <br>
&emsp; **PRIMARY KEY** (a, c) <br>
);

Adding a primary key will automatically create a unique B-tree index on the column or group of columns
listed in the primary key, and will force the column(s) to be marked NOT NULL.

A table can have at most **one** primary key. (There can be any number of [unique](#unique-constraints) and [not-null constraints](#not-null-constraints),
which are functionally almost the same thing, but only one can be identified as the primary key.) Relational
database theory dictates that every table must have a primary key. This rule is not enforced by PostgreSQL,
but it is usually best to follow it.

Primary keys are useful both for documentation purposes and for client applications. For example, a GUI
application that allows modifying row values probably needs to know the primary key of a table to be
able to identify rows uniquely. There are also various ways in which the database system makes use of a
primary key if one has been declared; for example, the primary key defines the default target column(s)
for [foreign keys](#foreign-keys) referencing its table.

### Best practices for primary keys

- Primary keys should never change. Updating a primary key should always be out of the question. This is
because it is most likely to be used in multiple indexes and used as a [foreign key](#foreign-keys). Updating a single primary key
could cause of ripple effect of changes.
- Uniqueness. This is the most important It implies that no other row in the table has the same value 
(or combination of values) in this column (or combination of columns).
- If you’re using a composite primary key, no one column or smaller combination of columns should uniquely identify 
each In other words, if you remove any column from the PK, the combination should stop being unique.

## Foreign Keys

A foreign key constraint specifies that the values in a column (or a group of columns) must match the
values appearing in some row of another table. We say this maintains the referential integrity between
two related tables.

Say you have the product table that we have used several times already:

CREATE TABLE products ( <br>
&emsp; product_id integer PRIMARY KEY, <br>
&emsp; name text, <br>
&emsp; price numeric <br>
);

Let's also assume you have a table storing orders of those products. We want to ensure that the orders table
only contains orders of products that actually exist. So we define a foreign key constraint in the orders
table that references the products table:

CREATE TABLE orders ( <br>
&emsp; order_id integer PRIMARY KEY, <br>
&emsp; product_id integer **REFERENCES** products (product_id), <br>
&emsp; quantity integer <br>
);

We will get tables:


Table of products.

| product_id(PK) | name  | price |
|----------------|:-----:|------:|
| 1              | Book  |    10 |
| 2              | Phone |   120 |
| 3              |  Tv   |   700 |

Table of orders.

| order_id(PK) | product_id(FK) | quantity |
|--------------|:--------------:|---------:|
| 1            |       3        |        2 |
| 2            |       1        |        6 |

Now it is impossible to create orders with non-NULL product_id entries that do not appear in the
products table. For example, we can not insert a row in orders table having product_id = 4.
We say that in this situation the orders table is the referencing table and the products table is the referenced
table. Similarly, there are referencing and referenced columns.You can assign your own name for a foreign key constraint, in the usual way:

CREATE TABLE employee ( <br>
&emsp; employee_id integer PRIMARY KEY, <br>
&emsp; first_name integer, <br>
&emsp; last_name integer, <br>
&emsp; dept_id	INT, <br>
&emsp; CONSTRAINT FK_Employee_Department **FOREIGN KEY**(dept_id) <br>
&emsp; &ensp; REFERENCES department(dept_id) <br>
);

A foreign key must reference columns that either are a [primary key](#primary-keys) or form a unique constraint. This
means that the referenced columns always have an index (the one underlying the [primary key](#primary-keys) or [unique
constraint](#unique-constraints)); so checks on whether a referencing row has a match will be efficient. 

CREATE TABLE order_items ( <br>
&emsp; product_id integer **REFERENCES** products **ON DELETE RESTRICT**, <br>
&emsp; order_id integer **REFERENCES** orders **ON DELETE CASCADE**, <br>
&emsp; quantity integer, <br>
&emsp; PRIMARY KEY (product_id, order_id) <br>
);

Restricting and cascading deletes are the two most common options with using foreign keys. **RESTRICT** prevents deletion of a
referenced row. **NO ACTION** means that if any referencing rows still exist when the constraint is checked,
an error is raised; this is the default behavior if you do not specify anything. (The essential difference
between these two choices is that NO ACTION allows the check to be deferred until later in the transaction, whereas RESTRICT does not.) 
**CASCADE** specifies that when a referenced row is deleted, row(s)
referencing it should be automatically deleted as well. There are two other options: **SET NULL** and **SET
DEFAULT**. These cause the referencing column(s) in the referencing row(s) to be set to nulls or their default values, respectively, when the referenced row is deleted. Note that these do not excuse you from
observing any constraints. For example, if an action specifies **SET DEFAULT** but the default value would
not satisfy the foreign key constraint, the operation will fail.

The appropriate choice of **ON DELETE** action depends on what kinds of objects the related tables represent. When the referencing table represents something that is a component of what is represented by the
referenced table and cannot exist independently, then **CASCADE** could be appropriate. If the two tables
represent independent objects, then **RESTRICT** or **NO ACTION** is more appropriate; an application that
actually wants to delete both objects would then have to be explicit about this and run two delete commands. In the above example, order items are part of an order, and it is convenient if they are deleted
automatically if an order is deleted. But products and orders are different things, and so making a deletion
of a product automatically cause the deletion of some order items could be considered problematic. The
actions **SET NULL** or **SET DEFAULT** can be appropriate if a foreign-key relationship represents optional
information. For example, if the products table contained a reference to a product manager, and the product
manager entry gets deleted, then setting the product's product manager to null or a default might be useful.