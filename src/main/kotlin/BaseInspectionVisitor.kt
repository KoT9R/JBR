import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.*

open class BaseInspectionVisitor(holder: ProblemsHolder?, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

    open fun visitPyStatement(node: PyAssignmentStatement) {
        visitElement(node)
    }

    open fun visitPyParameterFunc(node: PyParameterList) {
        visitElement(node)
    }

    override fun visitPyParameterList(node: PyParameterList) {
        if (checkParameterList(node)) {
            visitPyParameterFunc(node)
        } else {
            visitElement(node)
        }
    }

    override fun visitPyAssignmentStatement(node: PyAssignmentStatement) {
        if (checkAssignmentStatement(node)) {
            visitPyStatement(node)
        } else {
            visitElement(node)
        }
    }

    private fun checkAssignmentStatement(node: PyAssignmentStatement) : Boolean {
        for (param in node.children) {
            if (param is PyTargetExpression)
                continue
            if (param is PyTypeDeclarationStatement || param is PyAnnotation)
                return false
        }
        return true
    }

    private fun checkParameterList(node: PyParameterList) : Boolean {
        var counterAnnotation = 0
        for (param in node.children) {
            for (elem in param.children) {
                if (elem is PyAnnotation) {
                    counterAnnotation += 1
                }
            }
        }
        return counterAnnotation != node.parameters.size
    }
}