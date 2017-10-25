package wso2.org;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaDocCommentVisitor extends VoidVisitorAdapter<Void> {

    private String docComment;

    @Override
    public void visit(JavadocComment n, Void arg) {
        super.visit(n, arg);
        n.getComment();
        this.docComment = n.getContent().toString();
    }

    public String getDocContent() {
        return docComment;
    }

}
