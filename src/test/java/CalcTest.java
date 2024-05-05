import org.example.Calc;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


public class CalcTest {

    @BeforeAll
    public static void initAll() {
        System.out.println("Before All Method Get Called");
    }

    @BeforeEach
    public void init() {
        System.out.println("Before Each Method Get Called");
    }

    @Test
    @DisplayName("Should return true value")
    public void test() throws InterruptedException {
        Calc c = new Calc();
        Thread.sleep(10000);
        assertEquals(1, c.divide(12,12), "Regular multiplication should work");
    }

    @Test
    @DisplayName("Exception Handle")
    void expectException() {
        Throwable throwable = assertThrows(NullPointerException.class, () -> {throw new  NullPointerException("A null pointer exception");});
        assertEquals("A null pointer exception", throwable.getMessage());

    }


    @TestFactory
    Stream<DynamicTest> testDifferentMultiplyOperations() {
        MyClass tester = new MyClass();
        int[][] data = new int[][] { { 1, 2, 2 }, { 5, 3, 15 }, { 121, 4, 484 } };
        return Arrays.stream(data).map(entry -> {
            int m1 = entry[0];
            int m2 = entry[1];
            int expected = entry[2];
            return dynamicTest(m1 + " * " + m2 + " = " + expected, () -> {
                assertEquals(expected, tester.multiply(m1, m2));
            });
        });
    }

    // class to be tested
    class MyClass {
        public int multiply(int i, int j) {
            return i * j;
        }
    }



}
