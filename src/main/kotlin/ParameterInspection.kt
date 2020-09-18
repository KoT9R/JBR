import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.PyParameter
import com.jetbrains.python.psi.PyParameterList
import com.jetbrains.python.psi.PyTupleExpression
import org.jetbrains.annotations.Nls

class ParameterInspection : PyInspection() {
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
            override fun visitPyParameterFunc(node: PyParameterList) {
                    registerProblem(node,
                            "Add type for parameter",
                            PDQuickFixParameter())
            }
        }
    }
}