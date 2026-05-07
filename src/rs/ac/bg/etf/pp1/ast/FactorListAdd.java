// generated with ast extension for cup
// version 0.8
// 20/7/2025 2:50:32


package rs.ac.bg.etf.pp1.ast;

public class FactorListAdd extends FactorList {

    private FactorList FactorList;
    private Mulop Mulop;
    private FactorSign FactorSign;

    public FactorListAdd (FactorList FactorList, Mulop Mulop, FactorSign FactorSign) {
        this.FactorList=FactorList;
        if(FactorList!=null) FactorList.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.FactorSign=FactorSign;
        if(FactorSign!=null) FactorSign.setParent(this);
    }

    public FactorList getFactorList() {
        return FactorList;
    }

    public void setFactorList(FactorList FactorList) {
        this.FactorList=FactorList;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public FactorSign getFactorSign() {
        return FactorSign;
    }

    public void setFactorSign(FactorSign FactorSign) {
        this.FactorSign=FactorSign;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorList!=null) FactorList.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(FactorSign!=null) FactorSign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorList!=null) FactorList.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(FactorSign!=null) FactorSign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorList!=null) FactorList.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(FactorSign!=null) FactorSign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorListAdd(\n");

        if(FactorList!=null)
            buffer.append(FactorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorSign!=null)
            buffer.append(FactorSign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorListAdd]");
        return buffer.toString();
    }
}
