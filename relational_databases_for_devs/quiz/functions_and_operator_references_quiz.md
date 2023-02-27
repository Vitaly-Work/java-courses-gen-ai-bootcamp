## Questions

## 1. Identify the guidelines for group functions and the GROUP BY
clause.

1.  You cannot use a column alias in the GROUP BY clause. 
2.  The GROUP BY column must be in the SELECT clause. 
3.  By using a WHERE clause, you can exclude rows before
   dividing them into groups. 
4.  The GROUP BY clause groups rows and ensures order of
   the result set. 
5.  If you include a group function in a SELECT clause, you
   cannot select individual results as well. 

## 2. Group functions work across many rows to produce one result per group.

1.  True 
2.  False

## 3. The following is a valid statement:

SELECT MAX(AVG(salary)) 
FROM employees 
GROUP BY department_id;

1.  True 
2.  False

## 4. Which of the following SQL statements could display the number of people with the same last name?## 

1.  SELECT first_name, last_name, COUNT(employee_i4.  
FROM EMPLOYEES 
GROUP BY last_name; 

2.  SELECT employee_id, COUNT(last_nam5.  
FROM EMPLOYEES 
GROUP BY last_name; 

3.  SELECT last_name, COUNT(last_nam5.  
FROM EMPLOYEES 
GROUP BY last_name; <br >

SELECT employee_id, DISTINCT(last_nam5.  
FROM EMPLOYEES 
GROUP BY last_name; 

## 5. The GROUP BY statement is used in conjunction with the ........ 
to group the result-set by one or more columns. 

1.  Wildcards 
2.  Aggregate functions 
3.  Date functions 
4.  Joins

## 6. orders table: 

| order_id(PK) |    date    | price | Customer |
|--------------|:----------:|------:|:--------:|
| 1            | 2019/12/12 | 12.99 |  Harry   | 
| 2            | 2018/03/23 | 10.75 |  Nancy   | 
| 3            | 2018/09/29 |  9.50 |  Harry   | 
| 4            | 2020/05/04 | 18.00 |   Tom    | 
| 5            | 2021/07/09 | 14.99 |  Jensen  | 
| 6            | 2020/02/04 | 11.00 |  Nancy   | 

## We want to find the total sum (total order) of each customer.
Which of the below statement should we use: 

1.  SELECT customer,SUM(order_id)  FROM orders 
GROUP BY customer

2.  SELECT customer,SUM(price)  FROM orders
GROUP BY orders

3.  SELECT customer,SUM(price)  FROM orders
GROUP BY customer

4.  SELECT customer,SUM(price)  FROM orders
GROUP BY price


## 7. Group functions include nulls in calculations.

   1.  True 
   2.  False

## 8. Which SQL statement is used to return only different values?## 

   1.  SELECT DISTINCT 
   2.  SELECT UNIQUE 
   3.  SELECT DIFFERENT

## 9. Can you make the function which does not return value?

   1.  Yes 
   2.  No 

## 10. Which of the following IS NOT the true about stored procedures?## 

   1.  Procedures can have input or output parameters 
   2.  Procedure can not return multiple values 
   3.  Transactions can be used in stored procedures 
   4.  Procedures can be called from a Function 
   5.  None of them 
   6.  All of them 