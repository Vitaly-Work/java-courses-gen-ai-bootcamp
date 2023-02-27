## DDL - Data Definition Language

---
<!-- TOC -->

* [Goals](#goals)
* [Data definition language](#data-definition-language)
    * [Create](#create)
    * [Drop](#drop)
    * [Alter](#alter)
    * [truncate](#truncate)
* [Atomic Data Definition Statement Support](#atomic-data-definition-statement-support)
  * [atomic ddl(mysql support) vs transactional ddl(postgres support)](#atomic-ddl--mysql-support--vs-transactional-ddl--postgres-support-)
    * [atomic ddl](#atomic-ddl)
    * [transactional ddl](#transactional-ddl)

<!-- TOC -->

## Goals

-----

- understand the basic ddl in sql
- be able to use ddl to design and update database schema
- understand the limitations of ddl
- understand what is atomic ddl and its usefulness

## Data definition language

---
DDL or Data Definition Language actually consists of the SQL commands that can be used to define the database schema. It
simply deals with descriptions of the database schema and is used to create and modify the structure of database objects
in the database. DDL is a set of SQL commands used to create, modify, and delete database structures but not data. These
commands are normally not used by a general user, who should be accessing the database via an application.

List of DDL commands:

- CREATE: This command is used to create the database or its objects (like table, index, function, views, store
  procedure,
  and triggers).
- DROP: This command is used to delete objects from the database.
- ALTER: This is used to alter the structure of the database.
- TRUNCATE: This is used to remove all records from a table, including all spaces allocated for the records are removed.

A table in a relational database is much like a table on paper: It consists of rows and columns. The number and order of
the columns is fixed, and each column has a name. The number of rows is variable — it reflects how much data is stored
at a given moment. SQL does not make any guarantees about the order of the rows in a table. When a table is read, the
rows will appear in an unspecified order, unless sorting is explicitly requested.
Furthermore, SQL does not assign unique identifiers to rows, so it is possible to have several completely identical rows
in a table. This is a consequence of the mathematical model that underlies SQL but is usually not desirable. Later in
this chapter we will see how to deal with this issue.

Each column has a data type. The data type constrains the set of possible values that can be assigned to a column and
assigns semantics to the data stored in the column so that it can be used for computations. For instance, a column
declared to be of a numerical type will not accept arbitrary text strings, and the data stored in such a column can be
used for mathematical computations. By contrast, a column declared to be of a character string type will accept almost
any kind of data but it does not lend itself to mathematical calculations, although other operations such as string
concatenation are available.

PostgreSQL includes a sizable set of built-in data types that fit many applications. Users can also define their own
data types. Most built-in data types have obvious names and semantics.
Some of the frequently used data types are integer for whole numbers, numeric for possibly fractional numbers, text for
character strings, date for dates, time for time-of-day values, and timestamp for values containing both date and time.

### Create

---

To create a table, you use the aptly named CREATE TABLE command. In this command you specify at least a name for the new
table, the names of the columns and the data type of each column. For example:

````SQL
CREATE TABLE IF NOT EXISTS my_first_table (
    first_column text,
    second_column integer
);
````

This creates a table named my_first_table with two columns. The first column is named first_column and has a data type
of text; the second column has the name second_column and the type integer. Note that the column list is comma-separated
and surrounded by parentheses.

Another important point is that we use syntax IF NOT EXISTS here which is generally a
good practice when creating a table, because if table with declared table name already exists it avoids returning an
error.

Of course, the previous example was heavily contrived. Normally, you would give names to your tables and columns that
convey what kind of data they store. So let's look at a more realistic example:

````SQL
CREATE TABLE IF NOT EXISTS products (
    product_no integer,
    name text,
    price numeric
);
````

(The numeric type can store fractional components, as would be typical of monetary amounts.)

When you create many interrelated tables it is wise to choose a consistent naming pattern for the tables and columns.
For instance, there is a choice of using singular or plural nouns for table names, both of which are favored by some
theorist or other.

There is a limit on how many columns a table can contain. Depending on the column types, it is between 250 and 1600.
However, defining a table with anywhere near this many columns is highly unusual and often a questionable design.

### Drop

-------

If you no longer need a table, you can remove it using the DROP TABLE command. For example:

````SQL
DROP TABLE my_first_table;
DROP TABLE products;
````

Attempting to drop a table that does not exist is an error. Nevertheless, it is common in SQL script files to
unconditionally try to drop each table before creating it, ignoring any error messages, so that the script works whether
or not the table exists.

If you like, you can use the **DROP TABLE IF EXISTS** variant to avoid the error messages, but
this is not standard SQL.

````SQL
DROP TABLE IF EXISTS products;
````

When you create complex database structures involving many tables with foreign key constraints, views, triggers,
functions, etc. you implicitly create a net of dependencies between the objects. For instance, a table with a foreign
key constraint depends on the table it references.

To ensure the integrity of the entire database structure, PostgreSQL makes sure that you cannot drop objects that other
objects still depend on. For example, attempting to drop the products table with the
orders table depending on it, would result in an error message like this:

````SQL
DROP TABLE IF EXISTS products;

ERROR:  cannot drop table products because other objects depend on it
DETAIL:  constraint orders_product_no_fkey on table orders depends on table products
HINT:  Use DROP ... CASCADE to drop the dependent objects too.

````

The error message contains a useful hint: if you do not want to bother deleting all the dependent objects individually,
you can run:

````SQL
DROP TABLE IF EXISTS products CASCADE;
````

and all the dependent objects will be removed, as will any objects that depend on them, recursively. In this case, it
doesn't remove the orders table, it only removes the foreign key constraint. It stops there because nothing depends on
the foreign key constraint.

Almost all DROP commands in PostgreSQL support specifying CASCADE. Of course, the nature of the possible dependencies
varies with the type of the object. You can also write RESTRICT instead of CASCADE to get the default behavior, which is
to prevent dropping objects that any other objects depend on.

**According to the SQL standard, specifying either RESTRICT or CASCADE is required in a DROP command. No database system
actually enforces that rule, but whether the default behavior is RESTRICT or CASCADE varies across systems.**

### Alter

--------
When you create a table and you realize that you made a mistake, or the requirements of the application change, you can
drop the table and create it again. But this is not a convenient option if the table is already filled with data, or if
the table is referenced by other database objects (for instance a foreign key constraint). Therefore, PostgreSQL
provides
a family of commands to make modifications to existing tables. Note that this is conceptually distinct from altering the
data contained in the table: here we are interested in altering the definition, or structure, of the table.

You can:

- Add columns

- Remove columns

- Add constraints

- Remove constraints

- Change default values

- Change column data types

- Rename columns

- Rename tables

All these actions are performed using the ALTER TABLE command, whose reference page contains details beyond those given
here.

To add a column, use a command like:

````SQL
ALTER TABLE products ADD COLUMN IF NOT EXISTS description text;
````

The new column is initially filled with whatever default value is given (null if you don't specify a DEFAULT clause).
You can also define constraints on the column at the same time, using the usual syntax:

````SQL
ALTER TABLE products ADD COLUMN IF NOT EXISTS description text CHECK (description <> '');
````

In fact all the options that can be applied to a column description in CREATE TABLE can be used here. Keep in mind
however that the default value must satisfy the given constraints, or the ADD will fail. Alternatively, you can add
constraints later (see below) after you've filled in the new column correctly.

To remove a column, use a command like:

````SQL
ALTER TABLE products DROP COLUMN IF EXISTS description;
````

Whatever data was in the column disappears. Table constraints involving the column are dropped, too. However, if the
column is referenced by a foreign key constraint of another table, PostgreSQL will not silently drop that constraint.
You can authorize dropping everything that depends on the column by adding CASCADE:

````SQL
ALTER TABLE products DROP COLUMN IF EXISTS description CASCADE;
````

To add a constraint, the table constraint syntax is used. For example:

````SQL
ALTER TABLE products ADD CHECK (name <> '');
ALTER TABLE products ADD CONSTRAINT some_name UNIQUE (product_no);
ALTER TABLE products ADD FOREIGN KEY (product_group_id) REFERENCES product_groups;
````

To add a not-null constraint, which cannot be written as a table constraint, use this syntax:

````SQL
ALTER TABLE products ALTER COLUMN product_no SET NOT NULL;
````

The constraint will be checked immediately, so the table data must satisfy the constraint before it can be added.

To remove a constraint you need to know its name. If you gave it a name then that's easy. Otherwise the system assigned
a generated name, which you need to find out. The psql command \d **tablename** can be helpful here; other interfaces
might
also provide a way to inspect table details. Then the command is:

````SQL
ALTER TABLE products DROP CONSTRAINT some_name;
````

(If you are dealing with a generated constraint name like $2, don't forget that you'll need to double-quote it to make
it a valid identifier.)

As with dropping a column, you need to add CASCADE if you want to drop a constraint that something else depends on. An
example is that a foreign key constraint depends on a unique or primary key constraint on the referenced column(s).

This works the same for all constraint types except not-null constraints. To drop a not null constraint use:

````SQL
ALTER TABLE products ALTER COLUMN product_no DROP NOT NULL;
````

(Recall that not-null constraints do not have names.)

To set a new default for a column, use a command like:

````SQL
ALTER TABLE products ALTER COLUMN price SET DEFAULT 7.77;
````

Note that this doesn't affect any existing rows in the table, it just changes the default for future INSERT commands.

To remove any default value, use:

````SQL
ALTER TABLE products ALTER COLUMN price DROP DEFAULT;
````

This is effectively the same as setting the default to null. As a consequence, it is not an error to drop a default
where one hadn't been defined, because the default is implicitly the null value.

To convert a column to a different data type, use a command like:

````SQL
ALTER TABLE products ALTER COLUMN price TYPE numeric(10,2);
````

This will succeed only if each existing entry in the column can be converted to the new type by an implicit cast. If a
more complex conversion is needed, you can add a USING clause that specifies how to compute the new values from the old.

PostgreSQL will attempt to convert the column's default value (if any) to the new type, as well as any constraints that
involve the column. But these conversions might fail, or might produce surprising results. It's often best to drop any
constraints on the column before altering its type, and then add back suitably modified constraints afterwards.

To rename a column:

````SQL
ALTER TABLE products RENAME COLUMN product_no TO product_number;
````

To rename a table:

````SQL
ALTER TABLE products RENAME TO items;
````

### truncate

---------

TRUNCATE quickly removes all rows from a set of tables. It has the same effect as an unqualified DELETE on each table,
but since it does not actually scan the tables it is faster. Furthermore, it reclaims disk space immediately, rather
than requiring a subsequent VACUUM operation. This is most useful on large tables.

````SQL
TRUNCATE [ TABLE ] [ ONLY ] name [ * ] [, ... ]
    [ RESTART IDENTITY | CONTINUE IDENTITY ] [ CASCADE | RESTRICT ]
````

**Parameters**

**name**
The name (optionally schema-qualified) of a table to truncate. If ONLY is specified before the table name, only that
table is truncated. If ONLY is not specified, the table and all its descendant tables (if any) are truncated.
Optionally, * can be specified after the table name to explicitly indicate that descendant tables are included.

**RESTART IDENTITY**
Automatically restart sequences owned by columns of the truncated table(s).

**CONTINUE IDENTITY**
Do not change the values of sequences. This is the default.

**CASCADE**
Automatically truncate all tables that have foreign-key references to any of the named tables, or to any tables added to
the group due to CASCADE.

**RESTRICT**
Refuse to truncate if any of the tables have foreign-key references from tables that are not listed in the command. This
is the default.

TRUNCATE acquires an ACCESS EXCLUSIVE lock on each table it operates on, which blocks all other concurrent operations on
the table. When RESTART IDENTITY is specified, any sequences that are to be restarted are likewise locked exclusively.
If concurrent access to a table is required, then the DELETE command should be used instead.

TRUNCATE cannot be used on a table that has foreign-key references from other tables, unless all such tables are also
truncated in the same command. Checking validity in such cases would require table scans, and the whole point is not to
do one. The CASCADE option can be used to automatically include all dependent tables — but be very careful when using
this option, or else you might lose data you did not intend to! Note in particular that when the table to be truncated
is a partition, siblings partitions are left untouched, but cascading occurs to all referencing tables and all their
partitions with no distinction.

TRUNCATE will not fire any ON DELETE triggers that might exist for the tables. But it will fire ON TRUNCATE triggers. If
ON TRUNCATE triggers are defined for any of the tables, then all BEFORE TRUNCATE triggers are fired before any
truncation happens, and all AFTER TRUNCATE triggers are fired after the last truncation is performed and any sequences
are reset. The triggers will fire in the order that the tables are to be processed (first those listed in the command,
and then any that were added due to cascading).

TRUNCATE is not MVCC-safe. After truncation, the table will appear empty to
concurrent transactions, if they are using a
snapshot taken before the truncation occurred.This will only be an issue for a transaction that did not access the table
in question before the DDL command started — any transaction that has done so would hold at least an ACCESS SHARE table
lock, which would block the DDL command until that transaction completes. So these commands will not cause any apparent
inconsistency in the table contents for successive queries on the target table, but they could cause visible
inconsistency between the contents of the target table and other tables in the database.

TRUNCATE is transaction-safe with respect to the data in the tables: the truncation will be safely rolled back if the
surrounding transaction does not commit.

When RESTART IDENTITY is specified, the implied ALTER SEQUENCE RESTART operations are also done transactionally; that
is, they will be rolled back if the surrounding transaction does not commit. Be aware that if any additional sequence
operations are done on the restarted sequences before the transaction rolls back, the effects of these operations on the
sequences will be rolled back, but not their effects on currval(); that is, after the transaction currval() will
continue to reflect the last sequence value obtained inside the failed transaction, even though the sequence itself may
no longer be consistent with that. This is similar to the usual behavior of currval() after a failed transaction.

TRUNCATE can be used for foreign tables if supported by the foreign data wrapper, for instance,
see https://www.postgresql.org/docs/current/postgres-fdw.html.

**Examples**
Truncate the tables bigtable and fattable:

````SQL
TRUNCATE bigtable, fattable;
````

The same, and also reset any associated sequence generators:

````SQL
TRUNCATE bigtable, fattable RESTART IDENTITY;
````

Truncate the table othertable, and cascade to any tables that reference othertable via foreign-key constraints:

````SQL
TRUNCATE othertable CASCADE;
````

In Oracle, TRUNCATE TABLE is a DDL statement that cannot be used in a transaction (or, more accurately, cannot be rolled
back). AFAIK, if there is a transaction in progress when the statement is executed, the transaction is committed and
then the TRUNCATE is executed and cannot be undone.In Informix, the behaviour of TRUNCATE is slightly different; you can
use TRUNCATE in a transaction, but the only statements permissible after that are COMMIT and ROLLBACK.

## Atomic Data Definition Statement Support

-----------------

###### atomic ddl(mysql support) vs transactional ddl(postgres support)

### atomic ddl

An atomic DDL statement combines the data dictionary updates, storage engine operations, and binary log writes
associated with a DDL operation into a single, atomic operation. The operation is either committed, with applicable
changes persisted to the data dictionary, storage engine, and binary log, or is rolled back, even if the server halts
during the operation.

Atomic DDL is not transactional DDL. DDL statements, atomic or otherwise, implicitly end any transaction that is active
in the current session, as if you had done a COMMIT before executing the statement. This means that DDL statements
cannot be performed within another transaction, within transaction control statements such as START TRANSACTION ...
COMMIT, or combined with other statements within the same transaction.

Let's take an example

DROP TABLE operations are fully atomic if all named tables use an atomic DDL-supported storage engine. The statement
either drops all tables successfully or is rolled back.

Currently, only the InnoDB storage engine supports atomic DDL. Storage engines that do not support atomic DDL are
exempted from DDL atomicity. DDL operations involving exempted storage engines remain capable of introducing
inconsistencies that can occur when operations are interrupted or only partially completed.

DROP TABLE fails with an error if a named table does not exist, and no changes are made, regardless of the storage
engine. This change in behavior is demonstrated in the following example, where the DROP TABLE statement fails because a
named table does not exist:

````
mysql> CREATE TABLE t1 (c1 INT);
mysql> DROP TABLE t1, t2;
ERROR 1051 (42S02): Unknown table 'test.t2'
mysql> SHOW TABLES;
+----------------+
| Tables_in_test |
+----------------+
| t1             |
+----------------+
````

Prior to the introduction of atomic DDL, DROP TABLE reports an error for the named table that does not exist but
succeeds for the named table that does exist:

````
mysql> CREATE TABLE t1 (c1 INT);
mysql> DROP TABLE t1, t2;
ERROR 1051 (42S02): Unknown table 'test.t2'
mysql> SHOW TABLES;
Empty set (0.00 sec)
````

CREATE TABLE, ALTER TABLE, RENAME TABLE, TRUNCATE TABLE, CREATE TABLESPACE, and DROP TABLESPACE operations for tables
that use an atomic DDL-supported storage engine are either fully committed or rolled back if the server halts during
their operation.

In earlier MySQL releases, interruption of these operations could cause discrepancies between the storage engine, data
dictionary, and binary log, or leave behind orphan files. RENAME TABLE operations are only atomic if all named tables
use an atomic DDL-supported storage engine.

Partial execution of account management statements is no longer permitted. Account management statements either succeed
for all named users or roll back and have no effect if an error occurs. In earlier MySQL versions, account management
statements that name multiple users could succeed for some users and fail for others.

The change in behavior is demonstrated in this example, where the second CREATE USER statement returns an error but
fails because it cannot succeed for all named users.

````
mysql> CREATE USER userA;
mysql> CREATE USER userA, userB;
ERROR 1396 (HY000): Operation CREATE USER failed for 'userA'@'%'
mysql> SELECT User FROM mysql.user WHERE User LIKE 'user%';
+-------+
| User  |
+-------+
| userA |
+-------+
````

Prior to the introduction of atomic DDL, the second CREATE USER statement returns an error for the named user that does
not exist but succeeds for the named user that does exist:

````
mysql> CREATE USER userA;
mysql> CREATE USER userA, userB;
ERROR 1396 (HY000): Operation CREATE USER failed for 'userA'@'%'
mysql> SELECT User FROM mysql.user WHERE User LIKE 'user%';
+-------+
| User  |
+-------+
| userA |
| userB |
+-------+
````

### transactional ddl

Like several of its commercial competitors, one of the more advanced features of PostgreSQL is its ability to perform
transactional DDL via its Write-Ahead Log design. This design supports backing out even large changes to DDL, such as
table creation. You can't recover from an add/drop on a database or tablespace, but all other catalog operations are
reversible.

````SQL
$ psql mydb
mydb=# DROP TABLE IF EXISTS foo;
NOTICE: table "foo" does not exist
DROP TABLE
mydb=# BEGIN;
BEGIN
mydb=# CREATE TABLE foo (bar int);
CREATE TABLE
mydb=# INSERT INTO foo VALUES (1);
INSERT 0 1
mydb=# ROLLBACK;
ROLLBACK
mydb=# SELECT * FROM foo;
ERROR: relation "foo" does not exist
mydb=# SELECT version();
version
----------------------------------------------------------------------
PostgreSQL 8.3.7 on i386-redhat-linux-gnu, compiled by GCC gcc (GCC) 4.3.2 20081105 (Red Hat 4.3.2-7)
(1 row)

````

Experienced PostgreSQL DBA's know to take advantage of this feature to protect themselves when doing complicated work
like schema upgrades. If you put all such changes into a transaction block, you can make sure they all apply atomically
or not at all. This drastically lowers the possibility that the database will be corrupted by a typo or other such error
in the schema change, which is particularly important when you're modifying multiple related tables where a mistake
might destroy the relational key.