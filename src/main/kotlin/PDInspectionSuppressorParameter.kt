import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyAnnotation
import com.jetbrains.python.psi.PyAssignmentStatement
import com.jetbrains.python.psi.PyParameterList

class PDInspectionSuppressorParameter: InspectionSuppressor {
    override fun isSuppressedFor(p0: PsiElement, p1: String): Boolean {
        if (!p1.startsWith("Py"))
            return false
        return checkLine(p0)
    }

    private fun checkLine(p0: PsiElement): Boolean {
        val file: PsiFile = p0.containingFile
        return isContainingFlakeLineMarker(file)
    }

    private fun isContainingFlakeLineMarker(element: PsiElement): Boolean {
        run {
            var child = element.firstChild
            while (child != null) {
                if (element is PyAnnotation) {
                    return false
                }
                if (element is PyParameterList && element.text != "()") {
                    return true
                }
                child = child!!.nextSibling
            }
        }
        var child = element.firstChild
        while (child != null) {
            if (isContainingFlakeLineMarker(child!!)) {
                return true
            }
            child = child!!.nextSibling
        }
        return false
    }

    override fun getSuppressActions(p0: PsiElement?, p1: String): Array<SuppressQuickFix> {
        return arrayOf(
                PDQuickFixParameter()
        )
    }
}