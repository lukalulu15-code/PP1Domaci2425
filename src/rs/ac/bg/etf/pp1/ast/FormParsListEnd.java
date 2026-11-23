// generated with ast extension for cup
// version 0.8
// 19/7/2025 17:29:42


package rs.ac.bg.etf.pp1.ast;

public class FormParsListEnd extends FormParsList {

    public FormParsListEnd () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsListEnd(\n");

        buffer.append(tab);
        buffer.append(") [FormParsListEnd]");
        return buffer.toString();
    }
}
