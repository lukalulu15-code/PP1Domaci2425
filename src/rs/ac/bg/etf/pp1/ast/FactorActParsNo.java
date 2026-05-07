// generated with ast extension for cup
// version 0.8
// 20/7/2025 2:50:32


package rs.ac.bg.etf.pp1.ast;

public class FactorActParsNo extends FactorActPars {

    public FactorActParsNo () {
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
        buffer.append("FactorActParsNo(\n");

        buffer.append(tab);
        buffer.append(") [FactorActParsNo]");
        return buffer.toString();
    }
}
