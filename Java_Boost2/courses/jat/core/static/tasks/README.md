

### Will the code be compiled and what is the result and why?
```java
public class Test {
    static {
        a = 6;
    }

    static int a = 5;
}
```

### Will the code be compiled and what is the result and why?
```java
public class A {
    public static final String a = "abc";
    public static int i = 0;

    static {
        i = Integer.parseInt(a);

    }
}
public class B  extends A{
    public static final String b = "class B" ;
    public static  int i = 0 ;

    static{
        i = Integer.parseInt("23");
    }
}

public class Test {
    public static void main(String[] args) {

        try {
            A ab = new B();
        } catch (Throwable e) {

        }
        System.out.println(A.a);
        System.out.println(B.b);

        B b = new B();
    }
}
```