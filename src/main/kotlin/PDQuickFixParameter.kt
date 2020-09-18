import com.intellij.codeInsight.CodeInsightUtilCore
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.*
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

class PDQuickFixParameter : LocalQuickFix {
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    override fun getFamilyName(): String {
        return "Replace to int"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        var parameters = descriptor.psiElement.children

        val elementGenerator = PyElementGenerator.getInstance(project)

        for (param in parameters) {
            if (param.children.isNotEmpty() && param.children[0] is PyAnnotation) {
                continue
            } else {
                var expression = param
                val assignment: PyNamedParameter = elementGenerator.createParameter(param.text + ": int")
                expression = expression.replace(assignment)
                if (expression == null) return
                CodeInsightUtilCore.forcePsiPostprocessAndRestoreElement(expression)
            }

        }
    }
}