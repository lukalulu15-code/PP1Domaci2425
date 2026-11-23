package rs.ac.bg.etf.pp1;

import java.util.Iterator;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.ActParsYes;
import rs.ac.bg.etf.pp1.ast.AddopPlus;
import rs.ac.bg.etf.pp1.ast.DesignatorArrayName;
import rs.ac.bg.etf.pp1.ast.DesignatorCasual;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementAssign;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementCallFunc;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementDec;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementInc;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementSetOpAssign;
import rs.ac.bg.etf.pp1.ast.FactorBool;
import rs.ac.bg.etf.pp1.ast.FactorChar;
import rs.ac.bg.etf.pp1.ast.FactorDesignator;
import rs.ac.bg.etf.pp1.ast.FactorListAdd;
import rs.ac.bg.etf.pp1.ast.FactorNew;
import rs.ac.bg.etf.pp1.ast.FactorNum;
import rs.ac.bg.etf.pp1.ast.FactorSignMinus;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodSignature;
import rs.ac.bg.etf.pp1.ast.MulopDiv;
import rs.ac.bg.etf.pp1.ast.MulopMul;
import rs.ac.bg.etf.pp1.ast.OneActPars;
import rs.ac.bg.etf.pp1.ast.StatementPrintNumNo;
import rs.ac.bg.etf.pp1.ast.StatementPrintNumYes;
import rs.ac.bg.etf.pp1.ast.StatementRead;
import rs.ac.bg.etf.pp1.ast.StatementReturn;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.TermListAdd;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPc;
	private int indexA=0;
	private int setOpPc=0;
	private int printNoNumSetPc=0;
	private int printYesNumSetPc=0;
	
	public int getMainPc() {
		return mainPc; 
	}
	
	private void initMeth() {
		Obj ordObj = Tab.find("ord");
		Obj chrObj = Tab.find("chr");
		ordObj.setAdr(Code.pc);
		chrObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Obj lenObj = Tab.find("len");
		lenObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		//Add
		
		Obj addObj = Tab.find("add");
		addObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(2);
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		
		Obj valuePorvera = Tab.insert(Obj.Var,"valueProvera",new Struct(Struct.Int));
		Obj indexPorvera = Tab.insert(Obj.Var,"indexProvera",new Struct(Struct.Int));
		Obj sizeProvera = Tab.insert(Obj.Var,"sizeProvera",new Struct(Struct.Int));
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup2);
		Code.put(Code.aload);
		//Da li je skup skroz pun => skok na tri popa
		
		Code.putFalseJump(Code.ne, 0);
		int addFirstJmp = Code.pc-2;
		
		Code.put(Code.pop);
		
		Code.put(Code.dup);
		Code.store(valuePorvera);
		Code.loadConst(0);
		Code.store(indexPorvera);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizeProvera);
		int pcProvera = Code.pc;
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.load(indexPorvera);
		Code.put(Code.aload);
		Code.load(valuePorvera);
		
		//Int vrednost vec postoji u skupu => skacemo na dva popa
		
		Code.putFalseJump(Code.ne, 0); //3
		int addSecondJmp = Code.pc-2;
		
		Code.load(indexPorvera);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexPorvera);
		Code.load(indexPorvera);
		Code.load(sizeProvera);
		Code.putFalseJump(Code.ge, pcProvera);
		
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.dup_x1);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.astore);
		
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup2);
		Code.put(Code.aload);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.astore);
		Code.putJump(0);
		int addThirdJmp = Code.pc-2;
		
		
		Code.fixup(addFirstJmp);
		Code.put(Code.pop);
		
		
		Code.fixup(addSecondJmp);
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		Code.fixup(addThirdJmp);
		
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
		//AddAll
		Obj addAllObj = Tab.find("addAll");
		addAllObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(2);
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		
		
		Obj indexIteratorAddAll = Tab.insert(Obj.Var,"indexAddAll",new Struct(Struct.Int));
		Code.loadConst(0);
		Code.store(indexIteratorAddAll);
		Obj sizeAddAll = Tab.insert(Obj.Var,"sizeAddAll",new Struct(Struct.Int));
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.store(sizeAddAll);
		
		Obj indexPorveraAddAll = Tab.insert(Obj.Var,"indexProveraAddAll",new Struct(Struct.Int));
		Obj sizeProveraAddAll = Tab.insert(Obj.Var,"sizeProveraAddAll",new Struct(Struct.Int));
		Code.loadConst(0);
		Code.store(indexPorveraAddAll);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizeProveraAddAll);
		int pcProveraAddAll = Code.pc;
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.load(indexPorveraAddAll);
		Code.put(Code.aload);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.load(indexIteratorAddAll);
		Code.put(Code.aload);
		
		//Isti element niza kao i skupa, prelazimo na sledeci element skupa, a vracamo se na pocetak niza
		Code.putFalseJump(Code.ne, 0);//53
		int addAllFirstJmp = Code.pc-2;
		
		Code.load(indexPorveraAddAll);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexPorveraAddAll);
		Code.load(indexPorveraAddAll);
		Code.load(sizeProveraAddAll);
		Code.putFalseJump(Code.ge, pcProveraAddAll);
		
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.load(sizeProveraAddAll);
		//Da li je skup popunjen
		Code.putFalseJump(Code.gt,0); //43
		int addAllSecondJmp = Code.pc-2;
		
		Code.put(Code.dup2);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.load(indexIteratorAddAll);
		Code.put(Code.aload);
		Code.put(Code.astore);
		
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup2);
		Code.put(Code.aload);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup);
		Code.store(sizeProveraAddAll);
		Code.put(Code.astore);
		
		
		Code.fixup(addAllFirstJmp);
		
		Code.load(indexIteratorAddAll);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexIteratorAddAll);
		Code.loadConst(0);
		Code.store(indexPorveraAddAll);
		Code.load(indexIteratorAddAll);
		Code.load(sizeAddAll);
		Code.putFalseJump(Code.ge, pcProveraAddAll);
		
		Code.fixup(addAllSecondJmp);
		
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		
		Code.put(Code.exit);
		Code.put(Code.return_);

		
		
		// Print set without Num
		printNoNumSetPc = Code.pc;
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);		
		Code.put(Code.load_n);
		
		Obj indexPrintNoNum = Tab.insert(Obj.Var,"index",new Struct(Struct.Int));
		Obj sizePrintNoNum = Tab.insert(Obj.Var,"size",new Struct(Struct.Int));
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizePrintNoNum);
		Code.loadConst(0);
		Code.store(indexPrintNoNum);
		
		int pcPrintNoNum = Code.pc;
		
		Code.put(Code.dup);
		Code.load(indexPrintNoNum);
		Code.put(Code.aload);
		Code.loadConst(5);
		Code.put(Code.print);
	/*	Code.loadConst(32);
		Code.loadConst(1);
		Code.put(Code.bprint); 
	*/
	
		Code.load(indexPrintNoNum);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexPrintNoNum);
		Code.load(indexPrintNoNum);
		Code.load(sizePrintNoNum);
		Code.putFalseJump(Code.ge, pcPrintNoNum);
		
		Code.put(Code.pop);
		
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
		//Print set with Num
		printYesNumSetPc=Code.pc;
		Code.put(Code.enter);
		Code.put(2);
		Code.put(2);
		Code.put(Code.load_n);
		Code.put(Code.load_1);
		Obj width = Tab.insert(Obj.Var, "width", new Struct(Struct.Int));
		Code.store(width);
		Obj indexPrintYesNum = Tab.insert(Obj.Var, "indexPrintYesNum", new Struct(Struct.Int));
		Obj sizePrintYesNum = Tab.insert(Obj.Var, "sizePrintYesNum", new Struct(Struct.Int));
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizePrintYesNum);
		Code.loadConst(0);
		Code.store(indexPrintYesNum);

		int pcPrintYesNum = Code.pc;

		Code.put(Code.dup);
		Code.load(indexPrintYesNum);
		Code.put(Code.aload);
		Code.load(width);
		;
		Code.put(Code.print);
		/*
		 * Code.loadConst(32); Code.loadConst(1); Code.put(Code.bprint);
		 */

		Code.load(indexPrintYesNum);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexPrintYesNum);
		Code.load(indexPrintYesNum);
		Code.load(sizePrintYesNum);
		Code.putFalseJump(Code.ge, pcPrintYesNum);

		Code.put(Code.pop);
		
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
		
		// SetOp union
		setOpPc = Code.pc;
		
		Code.put(Code.enter);
		Code.put(3);
		Code.put(3);
		
		Code.put(Code.load_n);
		Obj designator= Tab.insert(Obj.Var,"designator", new Struct(Struct.Int));
		Code.store(designator);
		
		Code.put(Code.load_1);
		Obj designator1= Tab.insert(Obj.Var,"designator1", new Struct(Struct.Int));
		Code.store(designator1);
		
		Code.put(Code.load_2);
		Obj designator2= Tab.insert(Obj.Var,"designator2", new Struct(Struct.Int));
		Code.store(designator2);
		
		// da li prvi operand ima vise elemanata nego skup u koji smestamo uniju
		Code.load(designator1);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.load(designator);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.putFalseJump(Code.le, 0);
		int unionSecondJmp = Code.pc - 2;

		// union uvek krece popunjavanje od nule
		Code.load(designator);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.loadConst(0);
		Code.put(Code.astore);

		Code.load(designator);
		Code.load(designator1);

		// Dodavanje prvog skupa
		Obj indexIteratorSetOp1 = Tab.insert(Obj.Var, "index", new Struct(Struct.Int));
		Code.loadConst(0);
		Code.store(indexIteratorSetOp1);
		Obj sizeSetOp1 = Tab.insert(Obj.Var, "size", new Struct(Struct.Int));
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizeSetOp1);

		int pcSetop = Code.pc;
		Code.put(Code.dup2);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.load(indexIteratorSetOp1);
		Code.put(Code.aload);
		Code.put(Code.astore);

		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup2);
		Code.put(Code.aload);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.astore);

		Code.load(indexIteratorSetOp1);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexIteratorSetOp1);
		Code.load(indexIteratorSetOp1);
		Code.load(sizeSetOp1);
		Code.putFalseJump(Code.ge, pcSetop);

		// Dodavanje drugog skupa
		Code.put(Code.pop);
		Code.load(designator2);

		Code.loadConst(0);
		Code.store(indexIteratorSetOp1);
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizeSetOp1);

		Obj indexPorveraSetOp2 = Tab.insert(Obj.Var, "indexProvera", new Struct(Struct.Int));
		Obj sizeProveraSetOp2 = Tab.insert(Obj.Var, "sizeProvera", new Struct(Struct.Int));
		Code.loadConst(0);
		Code.store(indexPorveraSetOp2);
		Code.load(designator);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizeProveraSetOp2);
		int pcProveraSetOp2 = Code.pc;
		/*
		 * //Da li moze da stane jos elemenata u skup namenjen za uniju
		 * Code.load(designatorStatementSetOpAssign.getDesignator().obj);
		 * Code.put(Code.dup); Code.put(Code.arraylength); Code.loadConst(1);
		 * Code.put(Code.sub); Code.put(Code.aload);
		 * Code.load(designatorStatementSetOpAssign.getDesignator().obj);
		 * Code.put(Code.arraylength); Code.loadConst(1); Code.put(Code.sub); //Ako ne
		 * moze odradimo dva popa pa brisanje Code.putFalseJump(Code.lt, 0); int
		 * unionFourthJmp = Code.pc-2;
		 */

		Code.load(designator);
		Code.load(indexPorveraSetOp2);
		Code.put(Code.aload);
		Code.load(designator2);
		Code.load(indexIteratorSetOp1);
		Code.put(Code.aload);

		// Element vec postoji u skupu ne dodajemo ga, prelazimo na sledeci
		Code.putFalseJump(Code.ne, 0);
		int unionFirstJmp = Code.pc - 2;

		Code.load(indexPorveraSetOp2);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexPorveraSetOp2);
		Code.load(indexPorveraSetOp2);
		Code.load(sizeProveraSetOp2);
		Code.putFalseJump(Code.ge, pcProveraSetOp2);

		// Da li moze da stane jos elemenata u skup namenjen za uniju
		Code.load(designator);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.load(designator);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		// Ako ne moze odradimo dva popa pa brisanje
		Code.putFalseJump(Code.lt, 0);
		int unionFourthJmp = Code.pc - 2;

		Code.put(Code.dup2);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.load(indexIteratorSetOp1);
		Code.put(Code.aload);
		Code.put(Code.astore);

		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.dup2);
		Code.put(Code.aload);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup);
		Code.store(sizeProveraSetOp2);
		Code.put(Code.astore);

		Code.fixup(unionFirstJmp);

		Code.load(indexIteratorSetOp1);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexIteratorSetOp1);
		Code.loadConst(0);
		Code.store(indexPorveraSetOp2);
		Code.load(indexIteratorSetOp1);
		Code.load(sizeSetOp1);
		Code.putFalseJump(Code.ge, pcProveraSetOp2);

		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.putJump(0);
		int unionThirdJmp = Code.pc - 2;

		Code.fixup(unionFourthJmp);
		Code.put(Code.pop);
		Code.put(Code.pop);

		Code.fixup(unionSecondJmp);
		Code.load(designator);
		Code.loadConst(0);
		Code.store(indexIteratorSetOp1);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.aload);
		Code.store(sizeProveraSetOp2);

		int pcBrisanjeSetOp = Code.pc;

		Code.load(designator);
		Code.load(indexIteratorSetOp1);
		Code.loadConst(0);
		Code.put(Code.astore);

		Code.load(indexIteratorSetOp1);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(indexIteratorSetOp1);

		Code.load(indexIteratorSetOp1);
		Code.load(sizeProveraSetOp2);
		Code.putFalseJump(Code.ge, pcBrisanjeSetOp);

		Code.load(designator);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.loadConst(0);
		Code.put(Code.astore);

		Code.fixup(unionThirdJmp);

		Code.put(Code.exit);
		Code.put(Code.return_);
		
	}	
	
	public CodeGenerator() {
		initMeth();
	}
	
	@Override
	public void visit(MethodSignature methodSignature) {
		methodSignature.obj.setAdr(Code.pc);
		if("main".contentEquals(methodSignature.getI1())) {
			mainPc = Code.pc;
		}
		
		SyntaxNode methNode = methodSignature.getParent();
		FormParamCounter formParamCnt = new FormParamCounter();
		methNode.traverseBottomUp(formParamCnt);
		VarCounter varCnt = new VarCounter();
		methNode.traverseBottomUp(varCnt);
		
		
		Code.put(Code.enter);
		
		Code.put(formParamCnt.getCount());
		Code.put(formParamCnt.getCount() + varCnt.getCount());
	}
	@Override 
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	@Override
	public void visit (StatementPrintNumNo statementPrintNumNo) {
		if(statementPrintNumNo.getExpr().struct.getKind() == Struct.Enum) {
			int offset = printNoNumSetPc - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
		}
		else if(statementPrintNumNo.getExpr().struct == Tab.intType || statementPrintNumNo.getExpr().struct.getKind() == Struct.Bool) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
		
	}
	@Override
	public void visit (StatementPrintNumYes statementPrintNumYes) {
		
		if(statementPrintNumYes.getExpr().struct.getKind() == Struct.Enum) {
			int offset = printYesNumSetPc - Code.pc;
			Code.loadConst(statementPrintNumYes.getN2());
			Code.put(Code.call);
			Code.put2(offset);
			
			
			
		}
		else if(statementPrintNumYes.getExpr().struct == Tab.intType || statementPrintNumYes.getExpr().struct.getKind() == Struct.Bool) {
			Code.loadConst(statementPrintNumYes.getN2());
			Code.put(Code.print);
		} else {
			Code.loadConst(statementPrintNumYes.getN2());
			Code.put(Code.bprint);
		}
		
	}
	@Override
	public void visit (StatementReturn statementReturn) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	@Override
	public void visit(StatementRead statementRead) {
		if(statementRead.getDesignator().obj.getType().getKind()==Struct.Char) {
			Code.put(Code.bread);
		} else {
			Code.put(Code.read);
		}
		Code.store(statementRead.getDesignator().obj);
	}
	
	@Override
	public void visit (FactorNum factorNum){
		Code.loadConst(factorNum.getN1());
	}
	@Override
	public void visit (FactorChar factorChar){
		Code.loadConst(factorChar.getC1());
	}
	@Override
	public void visit (FactorBool factorBool){
		if(factorBool.getB1().toString().contentEquals("true")) {
			Code.loadConst(1);
		} else {
			Code.loadConst(0);
		}
	}

	@Override
	public void visit(FactorDesignator factorDesignator) {
		if(Tab.find(factorDesignator.getDesignator().obj.getName()).getKind() != Obj.Meth) {
			Code.load(factorDesignator.getDesignator().obj);
		}
		else {
			int offset = factorDesignator.getDesignator().obj.getAdr() - Code.pc ;
			Code.put(Code.call);
			Code.put2(offset);
		}
	}

/*	@Override
	public void visit (DesignatorCasual designatorCasual) {
		Code.load(designatorCasual.obj);
	} 
*/
	@Override
	public void visit(FactorNew factorNew) {
		if(factorNew.getType().getI1().contentEquals("set")) {
			Code.loadConst(1);
			Code.put(Code.add);
			Code.put(Code.newarray);
		} else {
			Code.put(Code.newarray);
		}
		if(factorNew.getType().struct.equals(Tab.charType)) {
			Code.put(0);
		} else {
			Code.put(1);
		}
		
	}
	@Override
	public void visit(DesignatorArrayName designatorArrayName) {
		Code.load(designatorArrayName.obj);
	}
	@Override
	public void visit(DesignatorStatementAssign designatorStatementAssign) {
		Code.store(designatorStatementAssign.getDesignator().obj);
	}
/*	@Override
	public void visit(ExprMinus exprMinus) {
		Code.put(Code.neg);
	}
*/
	@Override
	public void visit(FactorSignMinus factorSignMinus) {
		Code.put(Code.neg);
	}
	
	@Override
	public void visit(TermListAdd termListAdd) {
		if(termListAdd.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		}
		else {
			Code.put(Code.sub);
		}
	}
	@Override
	public void visit(FactorListAdd factorListAdd) {
		if(factorListAdd.getMulop() instanceof MulopMul) {
			Code.put(Code.mul);
		} else if(factorListAdd.getMulop() instanceof MulopDiv) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}
	}
	@Override
	public void visit(DesignatorStatementInc designatorStatementInc) {
		if(designatorStatementInc.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designatorStatementInc.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designatorStatementInc.getDesignator().obj);
	}
	@Override
	public void visit(DesignatorStatementDec designatorStatementDec) {
		if(designatorStatementDec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designatorStatementDec.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designatorStatementDec.getDesignator().obj);
	}
		
	@Override
	public void visit(DesignatorStatementSetOpAssign designatorStatementSetOpAssign) {
		Code.load(designatorStatementSetOpAssign.getDesignator().obj);
		Code.load(designatorStatementSetOpAssign.getDesignator1().obj);
		Code.load(designatorStatementSetOpAssign.getDesignator2().obj);
		int offset = setOpPc-Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	@Override
	public void visit(ActParsYes actPars) {
	
	}
	@Override
	public void visit (OneActPars oneActPars) {
		
	}
	
	@Override
	public void visit(DesignatorStatementCallFunc designatorStatementCallFunc) {
		Obj callFuncObj = designatorStatementCallFunc.getDesignator().obj;
		int offset = callFuncObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(designatorStatementCallFunc.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
		
	}

	
	
	
}




