# Questions DDL

## 1. which commands belong to ddl:

1. create, alter, delete, drop 
2. create, insert, truncate, delete 
3. create, alter,
4. truncate, drop 
5. create, update, truncate, drop

## 2. which command can we execute multiple times without getting an error
1. CREATE TABLE order (
   id integer,
   full_name text,
   item_name text
   );
2. CREATE TABLE if exists order (
   id integer,
   full_name text,
   item_name text
   );
3.  a) CREATE TABLE if not exists order (
   id integer,
   full_name text,
   item_name text
   );
## 3. how to drop table on which other tables depend 
1. DROP TABLE table-name 
2. DROP TABLE table-name CASCADE 
3. DROP TABLE  table-name RESTRICT

## 4. what from the listed can not be changed with the alter command 
1. add default value 
2. remove column 
3. add index 
4. change column data type

## 5. what is the default value for the column that you add with the alter statement
1. depends on the data type 
2. null 
3. 0

## 6. what effect has altering default constraint on the past values 
1. all the existing values are reset 
2. has no effect
## 7. which statement is correct with regard to ONLY parameter if it is specified 
1. only truncates current table and does not truncate tables that depend on it 
2. truncates current table and its descendant tables as well 
3. truncates current table only without its descendant tables

## 8. which triggers are fired when truncating table 
1. truncate triggers 
2. delete and truncate triggers 
3. no triggers

## 9. which statement we should use to delete all rows from table ORDER 
1.  DELETE FROM ORDER WHERE 
2.  DROP TABLE ORDER 
3.  TRUNCATE TABLE ORDER

