package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.ActParsYes;
import rs.ac.bg.etf.pp1.ast.OneActPars;
import rs.ac.bg.etf.pp1.ast.OneFormPar;
import rs.ac.bg.etf.pp1.ast.OneFormParArray;
import rs.ac.bg.etf.pp1.ast.OneFormParOneD;
import rs.ac.bg.etf.pp1.ast.OneVarDecl;
import rs.ac.bg.etf.pp1.ast.OneVarDeclArray;
import rs.ac.bg.etf.pp1.ast.OneVarDeclOneD;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	protected int count;
	
	public int getCount() {
		return count;
	}
	
	public static class ActParamCounter extends CounterVisitor{
		
		@Override
		public void visit(OneActPars oneActPars) {
			count++;
		}
		
	}
	
	public static class FormParamCounter extends CounterVisitor{
		
		@Override
		public void visit(OneFormParOneD oneFormParOneD) {
			count++;
		}
		@Override
		public void visit(OneFormParArray oneFormParArray) {
			count++;
		}
		
	}
	
	public static class VarCounter extends CounterVisitor{
		
		@Override
		public void visit(OneVarDeclOneD oneVarDeclOneD) {
			count++;
		}
		@Override
		public void visit(OneVarDeclArray oneVarDeclArray) {
			count++;
		}
		
	}
	
}
