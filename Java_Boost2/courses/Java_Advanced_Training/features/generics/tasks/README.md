### Will the following class compile? If not, why?
```java
public class Singleton<T> {
  public static T getInstance() {
	if (instance == null)
  	instance = new Singleton<T>();
	return instance;
  }
  private static T instance = null;
}
 ```

### Will the following class compile? If not, why?
```java
public final class Algorithm {
  public static <T> T max(T x, T y) {
	return x > y ? x : y;
  }
}
 ```