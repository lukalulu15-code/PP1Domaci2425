// generated with ast extension for cup
// version 0.8
// 19/7/2025 17:29:42


package rs.ac.bg.etf.pp1.ast;

public class ActParsListEnd extends ActParsList {

    public ActParsListEnd () {
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
        buffer.append("ActParsListEnd(\n");

        buffer.append(tab);
        buffer.append(") [ActParsListEnd]");
        return buffer.toString();
    }
}
