// generated with ast extension for cup
// version 0.8
// 19/7/2025 17:29:42


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclListAdd extends ConstDeclList {

    private OneConstDecl OneConstDecl;
    private ConstDeclList ConstDeclList;

    public ConstDeclListAdd (OneConstDecl OneConstDecl, ConstDeclList ConstDeclList) {
        this.OneConstDecl=OneConstDecl;
        if(OneConstDecl!=null) OneConstDecl.setParent(this);
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
    }

    public OneConstDecl getOneConstDecl() {
        return OneConstDecl;
    }

    public void setOneConstDecl(OneConstDecl OneConstDecl) {
        this.OneConstDecl=OneConstDecl;
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OneConstDecl!=null) OneConstDecl.accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OneConstDecl!=null) OneConstDecl.traverseTopDown(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OneConstDecl!=null) OneConstDecl.traverseBottomUp(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListAdd(\n");

        if(OneConstDecl!=null)
            buffer.append(OneConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListAdd]");
        return buffer.toString();
    }
}
