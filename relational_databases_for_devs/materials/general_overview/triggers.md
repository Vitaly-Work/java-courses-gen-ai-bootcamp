# Triggers Overview

### Table of Contents
<!-- TOC -->
* [Your Goals](#your-goals)
* [Triggers](#triggers)
<!-- TOC -->

### Your Goals
1.  Gain knowledge of common database objects such as triggers.
2.  Understand the concept of database triggers, including different types of triggers and the differences between them.
3.  Learn best practices for using triggers in a database.
---
## Triggers

__A database trigger__ is a set of instructions or a program that is automatically executed in response to a specific event or changes to data in a table. Triggers are typically used to enforce business rules, maintain data integrity, or perform other actions in response to changes in the data.

There are three main types of triggers in SQL:

1.  <b>BEFORE triggers</b> are executed before the data is actually modified in the table.
```sql
CREATE TRIGGER IF NOT EXISTS update_timestamp
	BEFORE INSERT OR UPDATE ON myschema.table
	FOR EACH ROW
	EXECUTE FUNCTION update_timestamp_func();
```
This example creates a trigger named "update_timestamp" that is executed before any insert or update operations on the table "mytable" in the schema "myschema". The trigger calls a function "update_timestamp_func" which updates a timestamp field in the table.

2.  <b>AFTER triggers</b> are executed after the data is modified in the table.
```sql
CREATE TRIGGER IF NOT EXISTS audit_employee_updates
AFTER UPDATE ON employees
FOR EACH ROW
BEGIN
  INSERT INTO employee_audit (employee_id, action, old_salary, new_salary)
  VALUES (:old.employee_id, 'UPDATE', :old.salary, :new.salary);
END;
```
This example creates a trigger named "audit_employee_updates" that is executed after any update operations on the table "employees". The trigger inserts a new row into the "employee_audit" table with information about the update, including the employee ID, the type of action, and the old and new salary values.

3.  <b>INSTEAD OF triggers</b> are executed in place of the triggering action, such as insert, update, delete.
```sql
CREATE TRIGGER IF NOT EXISTS tr_mytable_instead_of_delete
ON mytable
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;
    -- some code here
END;
```

This example creates a trigger named "tr_mytable_instead_of_delete" that is executed in place of any delete operations on the table "mytable". It allows you to perform custom actions in place of the delete statement, such as logging or cascading deletes to other tables.

The main difference between the three types of triggers is when they are executed in relation to the changes made to the data. BEFORE triggers are executed before the data is modified, AFTER triggers are executed after the data is modified, and INSTEAD OF triggers are executed in place of the triggering action.

The main difference between the three types of triggers is when they are executed in relation to the changes made to the data. BEFORE triggers are executed before the data is modified, AFTER triggers are executed after the data is modified, and INSTEAD OF triggers are executed in place of the triggering action.

Let's take a deeper look at the differences between BEFORE, AFTER, and INSTEAD OF triggers:

1.  <b>BEFORE triggers</b> allow you to make changes to the data before it is actually modified in the table. This can be useful for data validation, setting default values, or performing calculations. With BEFORE triggers, you have the ability to cancel the operation if certain conditions are not met.
    
2.  <b>AFTER triggers</b> execute after the data is modified in the table. This can be useful for logging changes, enforcing referential integrity, or performing additional actions based on the changes made to the data.
    
3.  <b>INSTEAD OF triggers</b> allow you to perform custom actions in place of the triggering action, such as insert, update, delete. These triggers can be useful for handling views that do not allow certain types of modifications, such as views that are based on multiple tables or views that contain joins. With INSTEAD OF triggers, you have the ability to perform custom action on table that does not allow modification.
    

Another important difference between these three types of triggers is that BEFORE triggers are only available for DML events (insert, update, delete), whereas AFTER triggers are available for DDL and DML events, and INSTEAD OF triggers are available for DML events on views.

It's important to keep in mind that using triggers can introduce complexity and may cause performance issues if not used properly. It's best to use triggers sparingly, only when necessary, and for specific use cases such as auditing and logging, data validation and integrity, cascading updates or deletes, or handling views that do not allow certain types of modifications.

Here are some specific examples of when triggers can be useful. Best practices of using triggers:

1.  <b>Auditing and logging</b>: Triggers can be used to automatically log changes made to the data, such as the date and time of the change, the user who made the change, and the old and new values of the data. This can be useful for keeping track of changes made to the data and for auditing purposes.
    
2.  <b>Data validation and integrity</b>: Triggers can be used to validate data before it is inserted or updated in the table. For example, a trigger can be used to check that a value being inserted into a column is within a certain range, or that a foreign key value being inserted or updated exists in the referenced table. This can be useful for ensuring that the data in the table is consistent and accurate.
    
3.  <b>Cascading updates or deletes</b>: Triggers can be used to automatically update or delete related data when a row is updated or deleted. For example, a trigger can be used to automatically delete all rows in a child table when a row in the parent table is deleted.
    
4.  <b>Handling views that do not allow certain types of modifications</b>: INSTEAD OF triggers can be used to handle views that do not allow certain types of modifications, such as views that are based on multiple tables or views that contain joins. With INSTEAD OF triggers, you have the ability to perform custom actions on table that does not allow modification.
    
5.  <b>Business logic</b>: Triggers can be used to implement business logic. It can be used to update the value of a column based on the value of other columns, or to enforce complex business rules.


