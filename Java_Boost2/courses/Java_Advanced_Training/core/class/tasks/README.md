### Consider the following code 
What is the result and why?
```java
public class Car {
   private String brand;
   public Car(String brand) {
       this.brand = brand;
   }
   public String getBrand() {
       return brand;
   }

   public static void changeBrand(Car car) {
       car = new Car("Audi");
   }
   public static void main(String[] args) {
       Car mers = new Car("Mercedes");
       changeBrand(mers);
       System.out.println(mers.getBrand());
   }
}
```

### Consider the following two classes:
```java
public class ClassA {
    public void methodOne(int i) {
    }
    public void methodTwo(int i) {
    }
    public static void methodThree(int i) {
    }
    public static void methodFour(int i) {
    }
}

public class ClassB extends ClassA {
    public static void methodOne(int i) {
    }
    public void methodTwo(int i) {
    }
    public void methodThree(int i) {
    }
    public static void methodFour(int i) {
    }
}
```
* Which method overrides a method in the superclass?
* Which method hides a method in the superclass?
* What do the other methods do?
* 
### Create nested classes example.
you should show in your code why selected type is used and access to variables from nested and outer classes

### Implement hierarchy of classes with sealed classes.