### Normalisation - Exercises

Table provides hospital  information about patient appointments with specific doctors

| Doctor Id | Doctor Name      | Patient age  | Patient Name       | Appointment Date and Time | Diagnosis code |
|-----------|------------------|--------------|--------------------|---------------------------|----------------|
| 010309    | Jane Smith       | 34           | Tom Meyer          | 02.12.22  10:00           | C23DSR         |
| 010309    | Jane Smith       | 22           | Katherine Bulatova | 05.12.22  15:00           | C23DSR         |
| 020143    | Mark Stenly      | 24           | Anna Komarnizka    | 05.12.22  10:30           | C23DOR         |
| 020143    | Mark Stenly      | 24           | Anna Komarnizka    | 17.01.23  10:00           | C23DOR         |
| 098818    | Katya Orlova     | 67           | Michael Baqradze   | 02.11.22  13:10           | CIUOP0         |
| 012321    | George Zimmerman | 67           | Michael Baqradze   | 17.12.22  15:40           | C09ASD         |
| 098818    | Katya Orlova     | 24           | Ella Fanning       | 15.01.23  12:20           | CIUOP0         |
| 010122    | Maria Schmidt    | 62           | Jason Davidson     | 11.12.22  11:10           | C0U999         |
| 010122    | Maria Schmidt    | 38           | Jeffry Garner      | 20.02.23  09:00           | C0U999         |


1. Table shown above has Data anomalies, please show the examples of insert, update and delete anomalies;
2. please illustrate the process of normalising the table from 1st normal form to 3rd normal form:
- which column(columns) contain the atomic values(non-atomic values)
- which columns contain(if any) repeated groups of data 
- which functional dependencies can you identify 
- which candidate keys can you identify in the given table
- which dependencies violate 2NF
- which dependencies violate 3NF
-----

Freelance platform provides software developers for various projects throughout the world. The table shown lists the 
time spent by freelance developers working on various projects.

| Id number | Contract Number | weekly spent hours | fullName           | Project Number | Project Country |
|-----------|-----------------|--------------------|--------------------|----------------|-----------------|
| 234511    | C203            | 20                 | Tom Mayer          | P5551          | USA             |
| 792318    | C109            | 25                 | Alex Rodriges      | P0012          | Dubai           |
| 121134    | C203            | 40                 | Nicole Turner      | P5551          | USA             |
| 176788    | C203            | 10                 | James Franco       | P5551          | USA             |
| 655433    | C132            | 14                 | Nick Jones         | P6721          | Belgium         |
| 234511    | C222            | 20                 | Tom Mayer          | P1021          | Hong Kong       |
| 982134    | C222            | 30                 | Michael Spencer    | P1021          | Hong Kong       |
| 213298    | C132            | 32                 | Michaela Stevenson | P1120          | Norwey          |

1. describe the data anomalies that can be spotted in the table
2. describe the steps needed for normalising the data