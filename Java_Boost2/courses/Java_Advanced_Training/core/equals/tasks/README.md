### In the following program, explain why the value "6" is printed twice in a row:
```java
class PrePostDemo {
    public static void main(String[] args){
        int i = 3;
        i++;
        System.out.println(i);	// "4"
        ++i;                 	
        System.out.println(i);	// "5"
        System.out.println(++i);  // "6"
        System.out.println(i++);  // "6"
        System.out.println(i);	// "7"
    }
}
```

### What is the result and why?
```java
public class Test {

    public static void main(String[] args) {
        Integer int1 = 10;
        Integer int2 = 10;
        Integer int3 = 1000;
        Integer int4 = 1000;
        Integer int5 = 127;
        Integer int6 = 127;
        System.err.println(int1 == int2);
        System.err.println(int3 == int4);
        System.err.println(int5 == int6);
    }
}
```
