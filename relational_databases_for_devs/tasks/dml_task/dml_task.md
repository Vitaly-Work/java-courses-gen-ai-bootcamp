### DATA MANIPULATION STATEMENTS - Exercises


1. combine locations and countries tables, to return country_name,
country_id, and city, based on country_id column in such
a way to return all countries whether their country_id column has
the corresponding city values in the locations table or not.
(Note: Same country can be returned in several records, with different city value)
2. Select two columns from employees table. One column must be
called employee_name and another manager_name, so that this result
will show which employee reports to which manager. 
HINT: Use self join for employees table.
3. Perform query for countries table to remove the country with country_id of AU.
4. Perform INSERT query for countries to insert new country named Georgia with
region id of 2, and country id of GE.
5. Perform UPDATE query for countries to update the record with country_id
of 'GE' to set the region_id value to 3.
6. Perform CROSS JOIN for countries and locations tables. How many records 
does it return?
7. View the scripts for the tasks 7 to 9 on this link: [scripts](https://git.epam.com/epm-cdp/global-java-foundation-program/java-courses/-/blob/main/relational_databases_for_devs/tasks/dml_task/scripts/task_scripts.md) 
Select movie title and release_year columns from two tables: 
top_rated_movies and most_popular_films. Use UNION in such a way to
return only distinct movie names (without duplicates).
8. Perform the same operation as in task 7, but now include the movies, which
are common in both tables.
9. Perform UNION ALL on the two tables indicated in task 7, but now
order the titles by descending order. (meaning from Z to A).
