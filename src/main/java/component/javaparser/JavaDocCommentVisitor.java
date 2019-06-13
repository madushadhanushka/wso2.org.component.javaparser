package component.javaparser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaDocCommentVisitor extends VoidVisitorAdapter<Void> {

    private String docComment;

    @Override
    public void visit(JavadocComment comment, Void arg) {
        super.visit(comment, arg);
        if (comment.getCommentedNode().isPresent()) {
            Node node = comment.getCommentedNode().get();
            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration)node;
                if (classOrInterfaceDeclaration.getComment().isPresent()) {
                    this.docComment = classOrInterfaceDeclaration.getComment().get().getContent();
                }
            }
        }
    }

    public String getDocContent() {
        return docComment;
    }

}
