// generated with ast extension for cup
// version 0.8
// 20/7/2025 2:50:32


package rs.ac.bg.etf.pp1.ast;

public class StatementReturn extends Statement {

    private ReturnWithExpr ReturnWithExpr;

    public StatementReturn (ReturnWithExpr ReturnWithExpr) {
        this.ReturnWithExpr=ReturnWithExpr;
        if(ReturnWithExpr!=null) ReturnWithExpr.setParent(this);
    }

    public ReturnWithExpr getReturnWithExpr() {
        return ReturnWithExpr;
    }

    public void setReturnWithExpr(ReturnWithExpr ReturnWithExpr) {
        this.ReturnWithExpr=ReturnWithExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ReturnWithExpr!=null) ReturnWithExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ReturnWithExpr!=null) ReturnWithExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ReturnWithExpr!=null) ReturnWithExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementReturn(\n");

        if(ReturnWithExpr!=null)
            buffer.append(ReturnWithExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementReturn]");
        return buffer.toString();
    }
}
