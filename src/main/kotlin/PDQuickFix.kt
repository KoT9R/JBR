import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyElementGenerator
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull


class PDQuickFix : SuppressQuickFix {
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    override fun getFamilyName(): String {
        return "Replace to int"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        var anchor: PsiElement? = descriptor.psiElement
        while (anchor != null) {
            val next = PsiTreeUtil.nextLeaf(anchor)
            if (next != null && next.text.contains('=')) {
                break
            }
            anchor = next
        }
        val factory = PyElementGenerator.getInstance(project)
        val equalsCall = factory.createExpressionFromText(LanguageLevel.PYTHON37, ": int")

        anchor?.parent?.addAfter(equalsCall, anchor)
    }

    override fun isAvailable(p0: Project, p1: PsiElement): Boolean {
        return true
    }

    override fun isSuppressAll(): Boolean {
        return false
    }
}