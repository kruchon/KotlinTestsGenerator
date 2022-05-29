import org.junit.Test;

public class TestRelationExtraction {

    TestGenerator testGenerator = new TestGenerator();

    @Test
    public void test() {
        System.out.println(testGenerator.generate("User paid enterprice tariff of price 500. User got bill 500"));
    }

}
