

### Find the sentence containing the largest number of words in some given text.
The text is specified as a string S consisting of N characters: letters, spaces, dots, question marks and exclamation marks.
The text can be divided into sentences by splitting it at dots, question marks and exclamation marks. A sentence can be divided into words by splitting it at spaces. A sentence without words is valid, but a valid word must contain at least one letter.

For example, given S = “We are the best test coders. Give us a try? !“, there are three sentences: “We are the best test coders“, “ Give us a try” and “ “.

The first sentence contains six words: “We“,“are”, “the”, “best”, “test” and “coders“.
The second sentence contains four words: “Give“, “us“, “a” and “try“.
The third sentence is space.

Write a function that returns the maximum number of words in a sentence.

For example, given S = “We are the best test coders. Give us a try? !“, the function should return 6, as explained above.
Implement 2 solutions with array and split.


### Create benchmark tests for stringbuffer and stringbuilder.
Tests should be for append, insert, reverse and replace


### Create a class with the name “String” and use your String & java.lang.String  classes.

### Analyze the printed results  -  which of strings where created in heap or in String pool
```java
public class Test {

    public static void main(String[] args) {
        String s1 = "cat";
        String s2 = "cat";
        String s3 = new String("cat");
        String part = "ca";

        String s4 = part + "t";
        String s5 = part + "t";
        StringBuilder stringBuilder = new StringBuilder(s1);
        String s6 = stringBuilder.toString();
        String s7 = stringBuilder.toString();
        String s8 = "1cat4".substring(1, 4);
        String s9 = "1cat4".substring(1, 4);

        System.out.println(s1 == s2);
        System.out.println(s2 == s3);
        System.out.println(s1 == s4);
        System.out.println(s4 == s5);
        System.out.println(s1 == s6);
        System.out.println(s6.intern() == s1);
        System.out.println(s7 == s6);
        System.out.println(s8 == s1);
        System.out.println(s8.intern() == s1);
        System.out.println(s8 == s9);
    }
}
```