<!-- TOC -->
# Table of content

- [Transactional and Locking Statements](#transactional-and-locking-statements)
- [Transaction and ACID properties](#transaction-and-acid-properties)
  - [Isolation levels](#isolation-levels)
- [Database features assist in ACID capabilities](#database-features-assist-in-acid-capabilities)
- [START TRANSACTION, COMMIT, and ROLLBACK Statements](#start-transaction-commit-and-rollback-statements)
- [Select for update](#select-for-update)
- [Pros and cons for topics](#pros-and-cons-for-topics)

<!-- TOC -->

# Your goals

- To understand the purpose and use of transactional statements in a database.
- To understand the differences between transactional and locking statements.
- To learn how transactional statements ensure data consistency and integrity.
- To understand the concept of a transaction in a database.
- To learn about the ACID (Atomicity, Consistency, Isolation, Durability) properties of transactions.
- To understand how the ACID properties ensure data consistency and reliability in a database.
- To understand the various database features that support the ACID properties of transactions.
- To learn how these features help ensure data consistency, reliability, and availability.
- To understand the purpose and use of the START TRANSACTION, COMMIT, and ROLLBACK statements.
- To learn how these statements are used to control transactions in a database.
- To understand the purpose and use of the SELECT FOR UPDATE statement.
- To learn how the SELECT FOR UPDATE statement is used to lock rows for exclusive access.

Overall, the goal of these topics is to provide a comprehensive understanding of how transactions and isolation levels are used to ensure data consistency and reliability in a database. By understanding these concepts, the reader will be able to use transactional and locking statements, control transactions, and make informed decisions about the trade-off between data consistency and performance.

# Transactional and Locking Statements
In a database management system, a transaction is a series of one or more operations that are executed as a single logical unit of work. Transactional statements are used to control and manage the operations within a transaction, such as starting, committing, or rolling back a transaction. Locking statements are used to control access to data within a transaction by other concurrent transactions, to ensure consistency and integrity of the data.

# Why do we need Transactional and Locking Statements

- To ensure consistency and integrity of the data in a database.
- To ensure that multiple users can access and modify the data simultaneously without conflicting with each other.
- To provide a way to undo or rollback changes in case of an error.

---
A practical example of transactional and locking statements can be found in a banking application where a user wants to transfer money from one account to another. The transaction would begin with a START TRANSACTION statement, then the money would be transferred from the first account to the second account using a series of UPDATE statements. To ensure consistency and integrity of the data, the rows of the account table would be locked using a LOCK statement. Once the transfer is complete, the transaction would be committed using a COMMIT statement.

```
START TRANSACTION;

UPDATE account SET balance = balance - 100 WHERE account_number = '12345';
UPDATE account SET balance = balance + 100 WHERE account_number = '67890';

COMMIT;

```





# Transaction and ACID properties
A transaction is a unit of work that is atomic, consistent, isolated, and durable. These properties, known as the ACID properties, ensure that a transaction is executed in a reliable and consistent manner.

- Atomic: A transaction is an atomic unit of work, meaning that it is indivisible and cannot be broken down into smaller parts. If a transaction is interrupted or fails, the entire transaction is rolled back to its initial state.
- Consistent: A transaction brings the database from one consistent state to another consistent state. A consistent state is one in which all integrity constraints are satisfied.
- Isolated: A transaction is isolated from other transactions, meaning that the changes made within a transaction are not visible to other transactions until the transaction is committed.
- Durable: A transaction is durable, meaning that the changes made within a transaction are permanent and will survive a system failure.

# Why do we need Transaction and ACID properties
- To ensure that a database transaction is atomic, consistent, isolated, and durable.
- To maintain data integrity and prevent data corruption in case of a system failure.
- To ensure that a transaction either completes successfully or fails and rolls back to its original state, leaving the database unchanged.

---
A practical example of a transaction and ACID properties can be found in a stock trading application where a user wants to buy shares of a particular stock. The transaction would begin with a START TRANSACTION statement, then the shares would be purchased using an INSERT statement and the user's account balance would be updated using an UPDATE statement. To ensure consistency and integrity of the data, the rows of the stock and account tables would be locked using a LOCK statement. Once the purchase is complete, the transaction would be committed using a COMMIT statement. If an error occurs during the transaction, the changes would be rolled back using a ROLLBACK statement.

```
START TRANSACTION;

INSERT INTO stock_purchases (user_id, stock_symbol, shares, purchase_price)
VALUES ('1234', 'AAPL', 100, 500.00);

UPDATE account SET balance = balance - (100 * 500.00) WHERE user_id = '1234';

COMMIT;

```

# Isolation levels
Isolation levels determine how much isolation a transaction has from other concurrent transactions. There are several isolation levels, such as:

- Read Uncommitted: This is the lowest isolation level, where a transaction can read data that is not yet committed by other transactions.
- Read Committed: This is a higher isolation level, where a transaction can only read data that is already committed by other transactions.
- Repeatable Read: This is an even higher isolation level, where a transaction can read the same data multiple times and get the same results, even if other transactions make changes to the data.
- Serializable: This is the highest isolation level, where a transaction is executed as if it were the only transaction in the system, with no other transactions able to access the data.

Unfortunately, Serializable is generally considered to be impractical, even for a non-distributed database. It is not a coincidence that all the existing popular databases like Postgres and MySQL recommend against it.

Why is this setting so impractical? Let us take the two use cases:

In the Bank use case, Serializable is perfect. After we have read a user’s balance, the database guarantees that the user’s balance will not change. So, it is safe for us to apply business logic such as ensuring that the user has sufficient balance, and then finally writing the new balance based on the value we have read.

In the Retail use case, Serializable will also work correctly. However, the process that updates the exchange rates will not be allowed to perform its action until the transaction that creates the order succeeds.

This may sound like a great feature at first glance, because of the clear sequencing of events. However, what if the transaction that created orders was slow and complex? Maybe it has to call out into warehouses to check inventory. Maybe it has to perform credit checks on the user placing the order. During all this time, it is going to hold the lock on that row, preventing the exchange rate process from updating it. This possibly unintended dependency may prevent the system from scaling.

A Serializable setting is also subject to frequent deadlocks. For example, if two transactions read a user’s balance, they will both place a shared read lock on the row. If the transactions later try to modify that row, they will each try to upgrade the read lock to a write lock. This will result in a deadlock because each transaction will be blocked by the read lock held by the other transaction.

# Why do we need Isolation Levels

Isolation levels are used to control the consistency and integrity of data in a database. They are important because they determine the level of visibility and accessibility of data between concurrent transactions. Without isolation levels, multiple transactions could interfere with each other and cause inconsistencies in the data.

Isolation levels determine the degree to which one transaction is isolated from the effects of other concurrent transactions. The higher the isolation level, the more isolated a transaction is, but also the higher the cost in terms of performance.

For example, if a high isolation level is required to ensure the integrity of sensitive data, the cost of this isolation in terms of performance must be weighed against the importance of the data. If a lower isolation level is acceptable, the performance of the database can be improved, but the risk of inconsistencies in the data may increase.

In general, isolation levels are used to balance the trade-off between data consistency and performance.

---
# Practical examples

| ID  | Name  | Salary |
|-----|-------|--------|
| 1   | David | 1000   |           
| 2   | Steve | 2000   |           
| 3   | Chris | 3000   |

**Read committed example**

Session 1
```
begin tran
update emp set Salary=999 where ID=1
waitfor delay '00:00:15'
commit
```
Session 2

        set transaction isolation level read committed
        select Salary from Emp where ID=1

Run both sessions side by side.

*Output*

`999`

In second session, it returns the result only after execution of complete transaction in first session because of the lock on Emp table. We have used wait command to delay 15 seconds after updating the Emp table in transaction.

**Read uncommitted example**

Session 1

        begin tran
        update emp set Salary=999 where ID=1
        waitfor delay '00:00:15'
        rollback
Session 2

        set transaction isolation level read uncommitted
        select Salary from Emp where ID=1

Run both sessions at a time one by one.

*Output*

`999`

Select query in Session2 executes after update Emp table in transaction and before transaction rolled back. Hence 999 is returned instead of 1000.
If you want to maintain Isolation level "Read Committed" but you want dirty read values for specific tables then use with(nolock) in select query for same tables as shown below.

    set transaction isolation level read committed
    select * from Emp with(nolock)

**Repeatable Read Example**

Session 1
```
set transaction isolation level repeatable read
begin tran
select * from emp where ID in(1,2)
waitfor delay '00:00:15'
select * from Emp where ID in (1,2)
rollback
```

Session 2

    update emp set Salary=999 where ID=1
Run both sessions side by side.

*Output*

Update command in session 2 will wait till session 1 transaction is completed because emp table row with ID=1 has locked in session1 transaction.

**Serializable**

Session 1

    set transaction isolation level serializable
    begin tran
    select * from emp
    waitfor delay '00:00:15'
    select * from Emp
    rollback
Session 2

    insert into Emp(ID,Name,Salary)
    values( 11,'Stewart',11000)
Run both sessions side by side.

*Output*

Complete Emp table will be locked during the transaction in Session 1. Unlike "Repeatable Read", insert query in Session 2 will wait till session 1 execution is completed. Hence Phantom read is prevented and both queries in session 1 will display same number of rows.

# Database features assist in ACID capabilities
Many database management systems have built-in features that assist in providing ACID capabilities. Some examples include:

- Logging: Logging is used to record changes made within a transaction, so that they can be rolled back if necessary.
- Locking: Locking is used to control access to data within a transaction, to ensure consistency and integrity of the data.
- Multi-version concurrency control (MVCC): MVCC is used to provide isolation for concurrent transactions by creating multiple versions of data.

A practical example of database features assist in ACID capabilities can be found in a retail e-commerce website where multiple users are adding items to their shopping carts and checking out simultaneously. The website uses a relational database management system (RDBMS) that has built-in support for logging, locking, and multi-version concurrency control (MVCC) to ensure consistency and integrity of the data.

When a user adds an item to their shopping cart, the RDBMS logs the change and creates a new version of the data. This allows other users to continue adding items to their own shopping carts without being blocked by the first user's transaction. When a user checks out, the RDBMS locks the relevant rows of the inventory and user account tables to ensure consistency and integrity of the data. If a user attempts to check out with an insufficient account balance or if the inventory levels have changed, the transaction is rolled back and the user is notified.

```
-- Using logging
INSERT INTO shopping_cart (user_id, item_id, quantity)
VALUES ('1234', '5678', 1);

-- Using locking
START TRANSACTION;
SELECT * FROM inventory WHERE item_id = '5678' FOR UPDATE;
SELECT * FROM user_account WHERE user_id = '1234' FOR UPDATE;
UPDATE inventory SET quantity = quantity - 1 WHERE item_id = '5678';
UPDATE user_account SET balance = balance - (SELECT price FROM items WHERE item_id = '5678') WHERE user_id = '1234';
COMMIT;

```
# START TRANSACTION, COMMIT, and ROLLBACK Statements
- START TRANSACTION: This statement begins a new transaction.
- COMMIT: This statement makes the changes made within a transaction permanent and makes them visible to other transactions.

- ROLLBACK: This statement undoes the changes made within a transaction and returns the database to its state before the transaction began.

# Why do we need START TRANSACTION, COMMIT, and ROLLBACK Statements

- To provide a way to start, commit, and rollback transactions in a database.
- To ensure that a transaction either completes successfully or fails and rolls back to its original state.
- To provide a way to undo changes in case of an error.

---
A practical example of the START TRANSACTION, COMMIT, and ROLLBACK statements can be found in a payroll system where an employee's salary is being updated. The transaction would begin with a START TRANSACTION statement, then the employee's salary would be updated using an UPDATE statement. To ensure consistency and integrity of the data, the rows of the employee table would be locked using a LOCK statement. Once the update is complete, the transaction would be committed using a COMMIT statement. If an error occurs during the transaction, the changes would be rolled back using a ROLLBACK statement.

```
START TRANSACTION;

UPDATE employee SET salary = salary + 1000 WHERE employee_id = '1234';

COMMIT;

```
# Select for update

- SELECT FOR UPDATE is a type of locking statement that is used to lock the rows of a table that are returned by a SELECT statement. This is used to ensure that the data read by a transaction is consistent and that other transactions are unable to modify the data until the current transaction is committed or rolled back.

# Why do we need Select for update

- To provide a way to lock rows in a table to prevent other users from modifying them.
- To ensure that data integrity is maintained when multiple users are trying to update the same data simultaneously.
- To ensure that data is consistent and up-to-date for all users.




---
A practical example of SELECT FOR UPDATE can be found in a inventory management system where multiple users are trying to update the stock levels of a product simultaneously. A user would begin a transaction and run a SELECT statement with the FOR UPDATE clause to lock the rows of the product table. This prevents other users from updating the stock levels of the same product until the current transaction is committed or rolled back.

```
START TRANSACTION;

SELECT * FROM product WHERE product_id = '1234' FOR UPDATE;

UPDATE product SET stock_level = stock_level - 10 WHERE product_id = '1234';

COMMIT;

```

# Pros and cons for topics

------------------

# Transactional and Locking Statements

# Pros

- Provides a way to control and manage the operations within a transaction, such as starting, committing, or rolling back a transaction.
- Allows for the control of access to data within a transaction by other concurrent transactions, to ensure consistency and integrity of the data.
- Enables the ability to perform multiple operations as a single unit of work.

# Cons

- Can lead to performance issues if not used correctly, such as lock contention and deadlocks.
- Can be complex to implement and manage, particularly in large and complex systems.

# Transaction and ACID properties

# Pros

- Ensures that a transaction is executed in a reliable and consistent manner.
- Provides a way to ensure data integrity and consistency.
- Enables the ability to rollback changes if a failure occurs during the transaction.

# Cons

- Can lead to performance issues if not used correctly, such as lock contention and deadlocks.
- Can be complex to implement and manage, particularly in large and complex systems.
- Can result in increased complexity and overhead in the system.

# Isolation levels

# Pros

- Provide a way to control the level of isolation between concurrent transactions.
- Allow for the selection of the appropriate isolation level for a given use case.

# Cons

- Can lead to performance issues if not used correctly, such as lock contention and deadlocks.
- Can be complex to implement and manage, particularly in large and complex systems.
- Can result in increased complexity and overhead in the system.

# Select for update

# Pros

- Allows for the locking of rows in a table to ensure consistency and integrity of the data.
- Provides a way to read consistent data in a transaction.

# Cons

- Can lead to performance issues if not used correctly, such as lock contention and deadlocks.
- Can be complex to implement and manage, particularly in large and complex systems.



