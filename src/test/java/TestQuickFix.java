import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.MockProblemDescriptor;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.intellij.lang.annotations.Language;
import org.junit.Assert;
import org.junit.Test;

public class TestQuickFix extends LightPlatformCodeInsightFixture4TestCase {
    @Test
    public void testQuickFix() {
        assertFixResult(
                "x<caret> = 1",
                "x: int = 1");

        assertFixResult(
                "x<caret>: int = 1",
                "x: int = 1");

        assertFixResult(
                "def foo():\n    x<caret> = 1",
                "def foo():\n    x: int = 1");

        assertFixResult(
                "def foo():\n    x<caret> = 1 \ndef bar():\n    x = 1",
                "def foo():\n    x: int = 1 \ndef bar():\n    x = 1");


    }

    @Test
    public void testQuickFixParameter() {
        assertFixResultParam(
                "def foo(x<caret>):\n    x = 1",
                "def foo(x: int):\n    x = 1");

        assertFixResultParam(
                "def foo(x<caret>: int):\n    x = 1",
                "def foo(x: int):\n    x = 1");
    }

    private void assertFixResult(@Language("Python") String code, @Language("Python") String expectedCode) {
        myFixture.configureByText("test.py", code);

        PsiElement current = myFixture.getElementAtCaret();
        Assert.assertNotNull(current);

        SuppressQuickFix fix = new PDQuickFix();
        Assert.assertTrue(fix.isAvailable(getProject(), current));

        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            ProblemDescriptor descriptor = new MockProblemDescriptor(current, "", ProblemHighlightType.LIKE_UNUSED_SYMBOL);
            fix.applyFix(getProject(), descriptor);
        });

        Assert.assertEquals(expectedCode, myFixture.getFile().getText());
    }

    private void assertFixResultParam(@Language("Python") String code, @Language("Python") String expectedCode) {
        myFixture.configureByText("test.py", code);

        PsiElement current = myFixture.getElementAtCaret();
        Assert.assertNotNull(current);

        SuppressQuickFix fix = new PDQuickFixParameter();
        Assert.assertTrue(fix.isAvailable(getProject(), current));

        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            ProblemDescriptor descriptor = new MockProblemDescriptor(current, "", ProblemHighlightType.LIKE_UNUSED_SYMBOL);
            fix.applyFix(getProject(), descriptor);
        });

        Assert.assertEquals(expectedCode, myFixture.getFile().getText());
    }

}