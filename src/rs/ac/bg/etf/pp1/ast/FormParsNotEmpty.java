// generated with ast extension for cup
// version 0.8
// 19/7/2025 17:29:42


package rs.ac.bg.etf.pp1.ast;

public class FormParsNotEmpty extends FormPars {

    private OneFormPar OneFormPar;
    private FormParsList FormParsList;

    public FormParsNotEmpty (OneFormPar OneFormPar, FormParsList FormParsList) {
        this.OneFormPar=OneFormPar;
        if(OneFormPar!=null) OneFormPar.setParent(this);
        this.FormParsList=FormParsList;
        if(FormParsList!=null) FormParsList.setParent(this);
    }

    public OneFormPar getOneFormPar() {
        return OneFormPar;
    }

    public void setOneFormPar(OneFormPar OneFormPar) {
        this.OneFormPar=OneFormPar;
    }

    public FormParsList getFormParsList() {
        return FormParsList;
    }

    public void setFormParsList(FormParsList FormParsList) {
        this.FormParsList=FormParsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OneFormPar!=null) OneFormPar.accept(visitor);
        if(FormParsList!=null) FormParsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OneFormPar!=null) OneFormPar.traverseTopDown(visitor);
        if(FormParsList!=null) FormParsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OneFormPar!=null) OneFormPar.traverseBottomUp(visitor);
        if(FormParsList!=null) FormParsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsNotEmpty(\n");

        if(OneFormPar!=null)
            buffer.append(OneFormPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsList!=null)
            buffer.append(FormParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsNotEmpty]");
        return buffer.toString();
    }
}
