###  Analyze the code and explain if there anything wrong with it
```java
public class Product {
  int id;
  String name;

  public Product(int i, String n) {
	id = i; name = n;
  }

  @Override
  public boolean equals(Product p) {
	return (p != null)
    	&& (p.id==id)
    	&& (p.name.equals(name));
  }
}

public class SetDemo {

  public static void main(String[] args) {
	Set<Product> set = Set.of(
    	new Product(0, "P1"),
    	new Product(0, "P1"));
	System.out.print(set.stream().count());
  }
}


```
 ###  Find the employeeId of "Charlie" from a map using Java Streams Api.
```java
public class Demo {
    Map<Long, String> employees = Map.of(
            123L, "Alice",
            124L, "Bob",
            125L, "Charlie"
    );
}
```
### Count employees in HR department
```java
class Employee {
    private String name;
    private String department;
    //getters, setters
}
public class Demo {
    List<Employee> employees = Arrays.asList(
            new Employee("Alice", "HR"),
            new Employee("Bob", "HR"),
            new Employee("Charlie", "Sales"),
            new Employee("Eve", "Sales")
    );
}
``` 



### Calculate the sum of numbers from 1 to 100 using streams in a different ways ( at least 6 variants)
