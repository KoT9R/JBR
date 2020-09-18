import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.psi.PyAssertStatement
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyParameterList
import com.jetbrains.python.psi.PyTupleExpression
import org.jetbrains.annotations.Nls

class StatementInspection : PyInspection() {
    @Nls
    override fun getDisplayName(): String {
        return "Reform to ... : int"
    }

    override fun buildVisitor(holder: ProblemsHolder,
                              isOnTheFly: Boolean,
                              session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return Visitor(holder, session)
    }

    companion object {
        class Visitor(holder: ProblemsHolder?, session: LocalInspectionToolSession) :
            BaseInspectionVisitor(holder, session) {
            override fun visitPyStatement(node: PyAssignmentStatement) {
                registerProblem(
                        node,
                        "Replace ${node.children[0].text} to ${node.children[0].text}: int",
                        PDQuickFix())
            }
        }
    }
}