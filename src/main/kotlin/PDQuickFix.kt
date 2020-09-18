import com.intellij.codeInsight.CodeInsightUtilCore
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyTypeDeclarationStatement
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull


class PDQuickFix : LocalQuickFix {
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    override fun getFamilyName(): String {
        return "Add type int"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        var anchor = descriptor.psiElement.children[0]

        val elementGenerator = PyElementGenerator.getInstance(project)

        val assignment: PyTypeDeclarationStatement = elementGenerator.createFromText(LanguageLevel.PYTHON38, PyTypeDeclarationStatement::class.java,
                anchor.text + ": int")

        anchor = anchor.replace(assignment)
        if (anchor == null) return

        CodeInsightUtilCore.forcePsiPostprocessAndRestoreElement(anchor)
    }
}