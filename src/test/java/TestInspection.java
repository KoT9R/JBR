import com.google.common.collect.Sets;
import com.intellij.codeInspection.InspectionEP;
import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.codeInspection.LocalInspectionEP;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;

import java.util.Set;

public class TestInspection extends LightPlatformCodeInsightFixture4TestCase {
    private Set<String> excluded = Sets.newHashSet("PyInterpreterInspection", "PyMandatoryEncodingInspection", "PyMissingOrEmptyDocstringInspection");

    @Before
    public void setup() {
        InspectionProfileEntry[] inspections = LocalInspectionEP.LOCAL_INSPECTION.extensions()
                .map(InspectionEP::instantiateTool)
                .filter(e -> e.getShortName().startsWith("Py"))
                .filter(e -> !excluded.contains(e.getShortName()))
                .toArray(InspectionProfileEntry[]::new);
        myFixture.enableInspections(inspections);
    }
    @Test
    public void testErrorSuppression() {
        //language=Python
        assertNoErrors("def foo():\n" +
                "    x: str = \"1\"");

        //language=Python
        assertNoErrors("def foo(x: int):\n    x : float = 1.2   ");

        //language=Python
        assertNoErrors("def foo(x: int):\n    x = 1");
    }

    @Test
    public void testNoErrorSuppression() {
        //language=Python
        assertErrors("def foo():\n    x = 1");

        // noq instead of noqa must not suppress inspections
        //language=Python
        assertErrors("def foo(y: int):\n    x = 2");

        //language=Python
        assertErrors("def foo():\n    x = 1.2");
    }

    private void assertNoErrors(String code) {
        myFixture.configureByText("file.py", code);
        myFixture.checkHighlighting();
    }

    private void assertErrors(String code) {
        try {
            myFixture.configureByText("file.py", code);
            myFixture.checkHighlighting();
        } catch (ComparisonFailure ignored) {
            // this is expected because errors must be added by inspections
        }
    }
}
