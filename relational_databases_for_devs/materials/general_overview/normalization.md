<!-- TOC -->

* [What is Normalisation and why do we need it](#what-is-normalisation-and-why-do-we-need-it)
* [Data Anomalies](#data-anomalies)
* [Normal Forms](#normal-forms)
    * [First Normal Form](#first-normal-form)
    * [Second Normal Form](#second-normal-form)
    * [Third Normal Form](#third-normal-form)
* [What is Denormalization](#what-is-denormalization)
    * [Why do we denormalize Data](#why-do-we-denormalize-data)
* [Pros and Cons of Denormalisation](#pros-and-cons-of-denormalisation)

<!-- TOC -->

## What is Normalisation and why do we need it

**Normalization is part of successful database design. Without normalization, database systems can be inaccurate, slow,
and inefficient and they might not produce the data you expect.**

Illogically or inconsistently stored data can cause a number of problems. In a relational database, a logical and
efficient design is just as critical. A poorly designed database may provide erroneous information, may be difficult to
use, or may even fail to work properly.

Most of these problems are the result of two bad design features called: redundant data and anomalies. Redundant data is
unnecessary reoccurring data (repeating groups of data). Anomalies are any occurrence that weakens the integrity of your
data due to irregular or inconsistent storage (delete, insert and update irregularity, that generates the inconsistent
data).

The process of designing a relational database includes making sure that a table contains only data directly related to
the primary key, that each data field contains only one item of data, and that redundant (duplicated and unnecessary)
data is eliminated. The task of a database designer is to structure the data in a way that eliminates unnecessary
duplication(s) and provides a rapid search path to all necessary information. This process of specifying and defining
tables, keys, columns, and relationships in order to create an efficient database is called normalization.
There are a few rules for database normalization. Each rule is called a "normal form". If the first rule is observed,
the database is said to be "in first normal form". If the first three rules are observed, the database is considered to
be "in third normal form". Although other levels of normalization are possible, third normal form is considered the
highest level necessary for most applications.

As with many formal rules and specifications, real world scenarios do not always allow for perfect compliance. In
general, normalization requires additional tables and some designers find this first difficult and then cumbersome. If
you decide to violate one of the first three rules of normalization, make sure that your application anticipates any
problems that could occur.

## Data Anomalies

There are three common types of errors that we can consider to better understand the need for normalization. Each of
these modification anomalies arise from one of the data modification operations available to us: insert, update, and
delete. We can illustrate each of these with the relation pictured below, which lists information about employed people
and trainings at a company. The relation gives the id number, fullName, training number, training name, required
expertise
level, date of completion, finished task. Right away, we should be concerned that there is information in the
relation about both employees and trainings.

| id number | fullName            | training number | training Name               | required expertise Level | date of completion | finished_task                                                             |
|-----------|---------------------|-----------------|-----------------------------|--------------------------|--------------------|---------------------------------------------------------------------------|
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Stream api task, Optionals task,Datetime Api task                         |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.12.22           | transaction task, caching task, Spring data task                          |
| 012213    | Sofia Vasileva      | N890            | Training in Java 8          | junior                   | 30.12.22           | Optionals task, Datetime Api task                                         |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | DDL task, Transactions task, triggers task                                |
| 080211    | Gabriela Garsia     | N111            | Training in Spring boot     | middle                   | 25.11.22           | caching task                                                              |
| 020133    | Anna Michael Geller | N899            | Training in design patterns | senior                   | 10.10.22           | Proxy pattern task, Dacorator pattern task, Abstract factory pattern task |

This relation also exhibits redundancy. We are given the facts that Mariam Khorava took two trainings, for example.
Note that Mariam Khorava appearing multiple times is not itself redundancy, as
each appearance is a new fact: she took training in java 8 and training in spring boot.

**Update anomaly:** − If data items are scattered and are not linked to each other properly, then it
could lead to strange situations. For example, when we try to update one data item having its
copies scattered over several places, a few instances get updated properly while a few others are
left with old values. Such instances leave the database in an inconsistent state

Update anomalies are a direct consequence of the redundancy in our database. Consider what happens when required
expertise level for java 8 training changes.
If we are incautious, we will update the tuple listing Mariam khorava taking java training 8, but
forget to update the tuple for Sofia Vasileva, leaving our data internally inconsistent. Training in java 8 will be
listed as requiring two different competence level
without any indication which is correct. To avoid trouble, we must remember to always update all
required expertise level for training in java 8.

**Insert anomaly:** Consider what happens when a new Training is created in the company. Training in clean code is not
taken by any people yet
so How should we add it to the database? There are a few options, but none of them are good - whatever we do, we must
provide values for
id number, fullname etc. We might think that NULL is the best choice for each of these
attributes, as otherwise we must create fake id number, fullname etc. information. Either way, we are making trouble for
ourselves later on
because our database now contains a tuple that must be handled in a special fashion, different from the other tuples in
the
relation.

**Delete anomaly:**  Now, consider what happens when Training in sql is cancelled . If we are incautious, we
will delete the tuple Michael Rodriges - removing employee information from the database. Similarly, we cannot
remove Michael Rodriges without removing all information about sql training. The only way to
avoid these issues with our current database design is to replace the existing values with NULLs. As with the insert
anomaly example, this leaves us with tuples that require special handling.

Normalizing of this table will prevent each of the situations above. In effect, normalization requires us to
structure relations such that the data is expressed in a very simple and consistent form. We typically achieve
normalization by decomposing a relation into multiple smaller relations.

## Normal Forms

The concept of normalization originates with the relational model itself. Additional refinements have been added over
time, leading to a series of normal forms, which mostly build on earlier normal forms. We will not study every normal
form that has been proposed, but focus on the forms which are most useful and most likely to be of value in most
applications.

When a database meets the requirement for a normal form, we say that the database is in the form. As commonly defined,
most normal forms include a requirement that earlier normal forms are also met. Therefore, any database that is in 3NF
is necessarily also in 1NF, 2NF and 3NF;

However, it is also true that higher forms address less frequently occurring situations, so, for example, a database
that has been restructured to be in 3NF is very likely to also be in BCNF or even 4NF. 3NF is generally considered the
minimum requirement a database must meet to be considered “normalized”.

### First Normal Form

A database is in first normal form if it satisfies the following conditions:

- Contains only atomic values

- There are no repeating groups

Atomic here simply means that we cannot usefully break the value down into smaller parts. Non-atomic elements include
compound values, arrays of values, and relations. For example, a character string containing an author’s name may be
atomic , but a string identifying a book by author and title is probably compound; a list of authors would be an array;
and a table of values giving a book’s publication history (including publisher, year, ISBN, etc. for each publication)
would be a relation. To meet the 1NF requirements, compound values should be broken into separate attributes, while
arrays and relations should be broken out into their own relations (with a foreign key referencing the original
relation).

A repeating group means that a table contains two or more columns that are closely related. For example, a table that
records data on a book and its author(s) with the following columns: [Book ID], [Author 1], [Author 2], [Author 3] is
not in 1NF because [Author 1], [Author 2], and [Author 3] are all repeating the same attribute.

How do we bring an unnormalized table into first normal form? Consider our example again:

| id number | fullName            | training number | training name               | required expertise level | date of completion | finished task                                                             |
|-----------|---------------------|-----------------|-----------------------------|--------------------------|--------------------|---------------------------------------------------------------------------|
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Stream api task, Optionals task,Datetime Api task                         |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.12.22           | transaction task, caching task, Spring data task                          |
| 012213    | Sofia Vasileva      | N890            | Training in Java 8          | junior                   | 30.12.22           | Optionals task, Datetime Api task                                         |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | DDL task, Transactions task, triggers task                                |
| 080211    | Gabriela Garsia     | N111            | Training in Spring boot     | middle                   | 25.11.22           | caching task                                                              |
| 020133    | Anna Michael Geller | N899            | Training in design patterns | senior                   | 10.10.22           | Proxy pattern task, Dacorator pattern task, Abstract factory pattern task |

This table is not in first normal form because the finished tasks column can contain multiple values(i.e. it is not
atomic). For example, the first row includes values "stream api task" , "optionals task" and "datetime api task"

To bring this table to first normal form, we need to take the repeated values in each row and put them in separate rows

| id number | fullName            | training number | training name               | required expertise level | date of completion | finished task          |
|-----------|---------------------|-----------------|-----------------------------|--------------------------|--------------------|------------------------|
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Stream api task        |
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Optionals task         |
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Datetime Api task      |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.12.22           | transaction task       |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.11.22           | caching task           |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.11.22           | spring data task       |
| 012213    | Sofia Vasileva      | N890            | Training in Java 8          | junior                   | 30.12.22           | Optionals task         |
| 012213    | Sofia Vasileva      | N890            | Training in Java 8          | junior                   | 30.12.22           | Datetime Api task      |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | DDL task               |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | Transactions task      |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | triggers task          |
| 080211    | Gabriela Garsia     | N111            | Training in Spring boot     | middle                   | 25.11.22           | caching task           |
| 020133    | Anna Michael Geller | N899            | Training in design patterns | senior                   | 10.10.22           | Proxy pattern task     |
| 020133    | Anna Michael Geller | N899            | Training in design patterns | senior                   | 10.10.22           | Decorator pattern task |

as we can see, putting table in first normal form has resulted in many duplications of data

### Second Normal Form

A table is said to be in 2NF if it meets the following criteria:

- it’s already in 1NF

- All non-key attributes are fully functional dependent on the primary key.A non-key attribute is an attribute which is
  not part of any key.

In a table, if attribute B is functionally dependent on A, but is not functionally dependent on a proper subset of A,
then B is considered fully functional dependent on A. Hence, in a 2NF table, all non-key attributes cannot be dependent
on a subset of the primary key. Note that if the primary key is not a composite key, all non-key attributes are always
fully functional dependent on the primary key. A table that is in 1st normal form and contains only a single key as the
primary key is automatically in 2nd normal form.

Let us check our example table which we already put in first normal form

| id number | fullName            | training number | training Name               | required expertise level | date of completion | finished task          |
|-----------|---------------------|-----------------|-----------------------------|--------------------------|--------------------|------------------------|
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Stream api task        |
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Optionals task         |
| 020180    | Mariam Khorava      | N890            | Training in Java 8          | junior                   | 09.11.22           | Datetime Api task      |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.12.22           | transaction task       |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.11.22           | caching task           |
| 020180    | Mariam Khorava      | N111            | Training in Spring boot     | middle                   | 20.11.22           | spring data task       |
| 012213    | Sofia Vasileva      | N890            | Training in Java 8          | junior                   | 30.12.22           | Optionals task         |
| 012213    | Sofia Vasileva      | N890            | Training in Java 8          | junior                   | 30.12.22           | Datetime Api task      |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | DDL task               |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | Transactions task      |
| 090821    | Michael Rodriges    | N878            | Training in sql             | junior                   | 12.09.22           | triggers task          |
| 080211    | Gabriela Garsia     | N111            | Training in Spring boot     | middle                   | 25.11.22           | caching task           |
| 020133    | Anna Michael Geller | N899            | Training in design patterns | senior                   | 10.10.22           | Proxy pattern task     |
| 020133    | Anna Michael Geller | N899            | Training in design patterns | senior                   | 10.10.22           | Decorator pattern task |

First of all we should find the unique identifier for this table which can be a single column or combination of columns.

This table has primary key [id Number,training Number, finished task], these three columns together can uniquely
identify the given table and
represent composite key. After this step we have to check the functional
dependencies towards the key to make sure that 2NF is not violated.

In relation to 2NF following can be observed:

1. In the table we can see that fullName is only dependent on the id number(partial key).
2. Training name and expertise level are functionally dependent on training number which is again
   only partial key.
3. date of completion is dependent only on training number and id number(partial keys).

all these facts represent the violation of 2ND normal form.

Let's refactor the table and see what we have

| id number | fullName            |
|-----------|---------------------|
| 020180    | Mariam Khorava      |
| 012213    | Sofia Vasileva      |
| 090821    | Michael Rodriges    |
| 080211    | Gabriela Garsia     |
| 020133    | Anna Michael Geller |

| training number | training name               | required expertise level |
|-----------------|-----------------------------|--------------------------|
| N890            | Training in Java 8          | junior                   |
| N111            | Training in Spring boot     | middle                   |
| N878            | Training in sql             | junior                   |
| N899            | Training in design patterns | senior                   |

| id number | training number | date of completion |
|-----------|-----------------|--------------------|
| 020180    | N890            | 09.11.22           |
| 020180    | N111            | 20.11.22           |
| 012213    | N890            | 30.12.22           |
| 090821    | N878            | 12.09.22           |
| 080211    | N111            | 25.11.22           |
| 020133    | N899            | 10.10.22           |

| id number | training number | finished task          |
|-----------|-----------------|------------------------|
| 020180    | N890            | Stream api task        |
| 020180    | N890            | Optionals task         |
| 020180    | N890            | Datetime Api task      |
| 020180    | N111            | transaction task       |
| 020180    | N111            | caching task           |
| 020180    | N111            | spring data task       |
| 012213    | N890            | Optionals task         |
| 012213    | N890            | Datetime Api task      |
| 090821    | N878            | DDL task               |
| 090821    | N878            | Transactions task      |
| 090821    | N878            | triggers task          |
| 080211    | N111            | caching task           |
| 020133    | N899            | Proxy pattern task     |
| 020133    | N899            | Decorator pattern task |

What we have done is to remove the partial functional dependency that we initially had. Now in all tables, non-key
columns are fully dependent on the primary key of that table.

**A table for which there are no partial functional dependencies on the primary key might or might not be in 2NF. In
addition to the primary key, the table may contain other candidate keys; it is necessary to establish that no non-prime
attributes have part-key dependencies on any of these candidate keys.**

### Third Normal Form

A table is said to be in 3NF if it meets the following criteria:

- it is in 2NF

- Every non-prime attribute is non-transitively dependent on every key .

  A non-prime attribute is an attribute that does not belong to any candidate key. A transitive dependency is a
  functional dependency in which X → Z (X determines Z) indirectly, by virtue of X → Y and Y → Z (where it is not the
  case that Y → X).

Let's examine our tables that we put into 2NF. The table which contains training information is not in 3NF because it
has functionally dependent non-key columns.

| training number | training name               | required expertise level |
|-----------------|-----------------------------|--------------------------|
| N890            | Training in Java 8          | junior                   |
| N111            | Training in Spring boot     | middle                   |
| N878            | Training in sql             | junior                   |
| N111            | Training in Spring boot     | middle                   |
| N899            | Training in design patterns | senior                   |

The breach of 3NF occurs because the non-prime attribute, required expertise level
is transitively dependent on the primary key which is training number. The fact that required
expertise level column and training names column are functionally dependent makes the table
vulnerable to logical inconsistencies, as change in training name might require change in required expertise level.

In order to express the same facts without violating 3NF, it is necessary to split the table further:

| training number | training name               | required expertise level |
|-----------------|-----------------------------|--------------------------|
| N890            | Training in Java 8          | 1                        |
| N111            | Training in Spring boot     | 2                        |
| N878            | Training in sql             | 1                        |
| N111            | Training in Spring boot     | 2                        |
| N899            | Training in design patterns | 3                        |

| Id  | required expertise level |
|-----|--------------------------|
| 1   | junior                   |
| 2   | middle                   |
| 3   | senior                   |

## What is Denormalization

Denormalization is the process of adding precomputed redundant data to an otherwise normalized relational database to
improve read performance of the database. Normalizing a database involves removing redundancy so only a single copy
exists of each piece of information. Denormalizing a database requires data has first been normalized.

**With denormalization, the database administrator selectively adds back specific instances of redundant data after the
data structure has been normalized. A denormalized database should not be confused with a database that has never been
normalized.**

### Why do we denormalize Data

Denormalization addresses a fundamental fact in databases: Read and join operations are slow.

In a fully normalized database, each piece of data is stored only once, generally in separate tables, with a relation to
one another. For this information to become useable it must be read out from the individual tables, as a query, and then
joined together. If this process involves large amounts of data or needs to be done many times a second, it can quickly
overwhelm the hardware of the database and slow performance -- or even crash the database.

An important consideration for normalizing data is if the data will be read heavy or write heavy. Because data is
duplicated in a denormalized database, when data needs to be added or modified, several tables will need to be changed.
This results in slower write operations.

**Therefore, the fundamental tradeoff becomes fast writes and slow reads in normalized databases versus slow writes and
fast reads in denormalized.**

For example, imagine a database of customer orders from a website. If customers place many orders every second but each
order is only read out a few times during order processing, prioritizing write performance may be more important (a
normalized database). On the other hand, if each order is read out hundreds of times per second to provide a 'based on
your order recommendations' list or is read by big data trending systems, then faster read performance will become
important (a denormalized database).

Another important consideration in a denormalized system is data consistency. In a normalized database, each piece of
data is stored in one place; therefore, the data will always be consistent and will never produce contradictory results.
Since data may be duplicated in a denormalized database, it is possible that one piece of data is updated while another
duplicated location is not, which will result in a data inconsistency called an update anomaly. This places extra
responsibility on the application or database system to maintain the data and handle these errors.

Denormalization has become commonplace in database design. Advancing technology is addressing many of the issues
presented by denormalization, while the decrease in cost of both disk and RAM storage has reduced the impact of storing
redundant data for denormalized databases. Additionally, increased emphasis on read performance and making data quickly
available has necessitated the use of denormalization in many databases

## Pros and Cons of Denormalisation

Performing denormalization on databases has its pros and cons, including the following:

Denormalization pros

- Faster reads for denormalized data

- Simpler queries for application developers

- Less compute on read operations

Denormalization cons

- Slower write operations

- Additional database complexity

- Potential for data inconsistency

- Additional storage required for redundant tables 