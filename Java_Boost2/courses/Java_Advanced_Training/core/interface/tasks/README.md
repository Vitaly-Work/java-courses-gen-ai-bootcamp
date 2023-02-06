### Create 2 interfaces 
with the same names for default, the same names for static and the same names for private methods and try to use it.


### Will the code be compiled and running?
```java
public interface A{
  void doSome();
}

public class B implements A{
  @Override
  void doSome() {
	//....
  }
}
```