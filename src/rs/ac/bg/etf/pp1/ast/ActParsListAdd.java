// generated with ast extension for cup
// version 0.8
// 20/7/2025 2:50:32


package rs.ac.bg.etf.pp1.ast;

public class ActParsListAdd extends ActParsList {

    private OneActPars OneActPars;
    private ActParsList ActParsList;

    public ActParsListAdd (OneActPars OneActPars, ActParsList ActParsList) {
        this.OneActPars=OneActPars;
        if(OneActPars!=null) OneActPars.setParent(this);
        this.ActParsList=ActParsList;
        if(ActParsList!=null) ActParsList.setParent(this);
    }

    public OneActPars getOneActPars() {
        return OneActPars;
    }

    public void setOneActPars(OneActPars OneActPars) {
        this.OneActPars=OneActPars;
    }

    public ActParsList getActParsList() {
        return ActParsList;
    }

    public void setActParsList(ActParsList ActParsList) {
        this.ActParsList=ActParsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OneActPars!=null) OneActPars.accept(visitor);
        if(ActParsList!=null) ActParsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OneActPars!=null) OneActPars.traverseTopDown(visitor);
        if(ActParsList!=null) ActParsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OneActPars!=null) OneActPars.traverseBottomUp(visitor);
        if(ActParsList!=null) ActParsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsListAdd(\n");

        if(OneActPars!=null)
            buffer.append(OneActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsList!=null)
            buffer.append(ActParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsListAdd]");
        return buffer.toString();
    }
}
