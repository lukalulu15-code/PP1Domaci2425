// generated with ast extension for cup
// version 0.8
// 20/7/2025 2:50:32


package rs.ac.bg.etf.pp1.ast;

public class FactorActParsYes extends FactorActPars {

    private ActParsYesOrNo ActParsYesOrNo;

    public FactorActParsYes (ActParsYesOrNo ActParsYesOrNo) {
        this.ActParsYesOrNo=ActParsYesOrNo;
        if(ActParsYesOrNo!=null) ActParsYesOrNo.setParent(this);
    }

    public ActParsYesOrNo getActParsYesOrNo() {
        return ActParsYesOrNo;
    }

    public void setActParsYesOrNo(ActParsYesOrNo ActParsYesOrNo) {
        this.ActParsYesOrNo=ActParsYesOrNo;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParsYesOrNo!=null) ActParsYesOrNo.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParsYesOrNo!=null) ActParsYesOrNo.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParsYesOrNo!=null) ActParsYesOrNo.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorActParsYes(\n");

        if(ActParsYesOrNo!=null)
            buffer.append(ActParsYesOrNo.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorActParsYes]");
        return buffer.toString();
    }
}
