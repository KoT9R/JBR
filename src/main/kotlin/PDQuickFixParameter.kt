import com.intellij.codeInsight.CodeInsightUtilCore
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyNamedParameter
import com.jetbrains.python.psi.PyTypeDeclarationStatement
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

class PDQuickFixParameter : SuppressQuickFix {
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    override fun getFamilyName(): String {
        return "Replace to int"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        var anchor = descriptor.psiElement
        var expression = descriptor.psiElement
        var assignmentStr = ""

        while (anchor != null) {
            assignmentStr += anchor.text
            val next = PsiTreeUtil.nextLeaf(anchor)
            if (next != null && next.textContains(':')) {
                return
            }
            if (next != null && (next.textContains(')') || next.textContains(',')) ) {
                assignmentStr += ": int "
                break
            }
            anchor = next
        }

        val elementGenerator = PyElementGenerator.getInstance(project)

        val assignment: PyNamedParameter = elementGenerator.createParameter(assignmentStr)
        expression = expression.replace(assignment)

        if (expression == null) return

        CodeInsightUtilCore.forcePsiPostprocessAndRestoreElement(expression)
    }

    override fun isAvailable(p0: Project, p1: PsiElement): Boolean {
        return true
    }

    override fun isSuppressAll(): Boolean {
        return false
    }
}