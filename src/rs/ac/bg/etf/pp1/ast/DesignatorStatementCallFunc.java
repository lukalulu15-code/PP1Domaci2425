// generated with ast extension for cup
// version 0.8
// 20/7/2025 2:50:32


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementCallFunc extends DesignatorStatement {

    private Designator Designator;
    private ActParsYesOrNo ActParsYesOrNo;

    public DesignatorStatementCallFunc (Designator Designator, ActParsYesOrNo ActParsYesOrNo) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ActParsYesOrNo=ActParsYesOrNo;
        if(ActParsYesOrNo!=null) ActParsYesOrNo.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
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
        if(Designator!=null) Designator.accept(visitor);
        if(ActParsYesOrNo!=null) ActParsYesOrNo.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ActParsYesOrNo!=null) ActParsYesOrNo.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ActParsYesOrNo!=null) ActParsYesOrNo.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementCallFunc(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsYesOrNo!=null)
            buffer.append(ActParsYesOrNo.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementCallFunc]");
        return buffer.toString();
    }
}
