import editor.TextEditor;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;

import static org.hyperskill.hstest.testcase.CheckResult.correct;

public class EditorTest extends SwingTest {
    public EditorTest() {
        super(new TextEditor());
    }

    @SwingComponent
    private JTextComponentFixture textArea;

    @DynamicTest
    CheckResult test1() {
        requireEditable(textArea);
        requireEmpty(textArea);
        return correct();
    }

    @DynamicTest(feedback = "Can't enter multiline text in TextArea.")
    CheckResult test2() {
        String text = "Basic text editor\nType here too\nHere also";
        textArea.setText(text);
        textArea.requireText(text);
        return correct();
    }
}
