import edu.kruchon.natural.lang.parser.codegeneration.KotlinSource;
import edu.kruchon.natural.lang.parser.codegeneration.KotlinSourceGenerator;
import edu.kruchon.natural.lang.parser.syntax.TestParagraphSyntaxTreeParser;
import edu.kruchon.natural.lang.parser.syntax.Triplet;

import java.util.ArrayList;
import java.util.List;

public class TestGenerator {

    private final TestParagraphSyntaxTreeParser testParagraphSyntaxTreeParser = TestParagraphSyntaxTreeParser.INSTANCE;
    private final KotlinSourceGenerator kotlinSourceGenerator = KotlinSourceGenerator.INSTANCE;

    public List<KotlinSource> generate(String testScenario) {
        String[] paragraphs = testScenario.split("\\.");
        List<Triplet> triplets = new ArrayList<>(paragraphs.length);
        for (String paragraph : paragraphs) {
            triplets.add(testParagraphSyntaxTreeParser.parse(paragraph));
        }
        return kotlinSourceGenerator.generate(triplets);
    }
}
