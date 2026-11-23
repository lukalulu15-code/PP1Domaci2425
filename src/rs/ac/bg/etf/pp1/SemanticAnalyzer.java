package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;


public class SemanticAnalyzer extends VisitorAdaptor {
	int printCallCount = 0;
	int varDeclCountGlobal = 0;
	int varDeclCountLocal = 0;
	Obj currentMethod = null;
	Obj currentCallFunc = null;
	boolean errorFuncCall = false;
	private Struct currType;
	private Struct currExprType;
	private ArrayList<Obj> arguments = null;
	private int argumentsIndex=0;
	private int localSymbolsIndex=0;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	int cntActPars=0;
	private String currentArrayName=null;
	private boolean factorNewBoolean = false;
	private boolean arrayAccess = false;
	private boolean newArrayError = false;
	private boolean minusError = false;
	private int numValue = 0;
	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	@Override
	public void visit(OneConstDeclNumber oneConstDeclNumber) {
		if(factorNewBoolean==true) {
			factorNewBoolean=false;
		}
		if(Tab.find(oneConstDeclNumber.getI1())!= Tab.noObj) {
			report_error("Vec postoji const ili promenljiva za datim nazivom " + oneConstDeclNumber.getI1(), oneConstDeclNumber);
		}else {
			if(Tab.intType.assignableTo(currType)){
				report_info("Deklarisana numericka konstanta " + oneConstDeclNumber.getI1(),oneConstDeclNumber);
				Obj constNode = Tab.insert(Obj.Con, oneConstDeclNumber.getI1(), currType);
				constNode.setAdr(oneConstDeclNumber.getN2());
			} else {
				report_error("Neodgovarajuci tip za konstantu " + oneConstDeclNumber.getI1(), oneConstDeclNumber);
			}
		}
	}
	
	@Override
	public void visit(OneConstDeclBool oneConstDeclBool) {
		if(factorNewBoolean==true) {
			factorNewBoolean=false;
		}
		if(Tab.find(oneConstDeclBool.getI1())!= Tab.noObj) {
			report_error("Vec postoji const ili promenljiva za datim nazivom " + oneConstDeclBool.getI1(), oneConstDeclBool);
		} else {
			if(Tab.intType.assignableTo(currType) || Tab.charType.assignableTo(currType)) {
				report_error("Neodgovarajuci tip za konstantu " + oneConstDeclBool.getI1(), oneConstDeclBool);
			}
			else {
				report_info("Deklarisana bool konstanta " + oneConstDeclBool.getI1(),oneConstDeclBool);
				Obj constNode = Tab.insert(Obj.Con, oneConstDeclBool.getI1(), currType);
				if(oneConstDeclBool.getB2()==true) {
					constNode.setAdr(1);
					
				} else {
					constNode.setAdr(0);
				}
			}
		}
	}
	
	@Override
	public void visit(OneConstDeclChar oneConstDeclChar) {
		if(factorNewBoolean==true) {
			factorNewBoolean=false;
		}
		if(Tab.find(oneConstDeclChar.getI1())!= Tab.noObj) {
			report_error("Vec postoji const ili promenljiva za datim nazivom " + oneConstDeclChar.getI1(), oneConstDeclChar);
		} else {
			if(Tab.charType.assignableTo(currType)){
				report_info("Deklarisana char konstanta " + oneConstDeclChar.getI1(),oneConstDeclChar);
				Obj constNode = Tab.insert(Obj.Con, oneConstDeclChar.getI1(), currType);
				constNode.setAdr(oneConstDeclChar.getC2());
			} else {
				report_error("Neodgovarajuci tip  za konstantu " + oneConstDeclChar.getI1(), oneConstDeclChar);
			}
		}
	}
	
	@Override
	public void visit(OneVarDeclOneD oneVarDeclOneD){
		if(factorNewBoolean==true) {
			factorNewBoolean=false;
		}
		if(currentMethod == null) {
			if(Tab.find(oneVarDeclOneD.getI1())!= Tab.noObj) {
				report_error("Vec postoji promenljiva ili const za datim nazivom " + oneVarDeclOneD.getI1(), oneVarDeclOneD);
			}else {
				varDeclCountGlobal++;
				report_info("Deklarisana globalna promenljiva " + oneVarDeclOneD.getI1() + " ",oneVarDeclOneD);
				Obj varNode = Tab.insert(Obj.Var, oneVarDeclOneD.getI1(), currType);
			} 
		}
		else {
			if(Tab.currentScope().findSymbol(oneVarDeclOneD.getI1())!= null) {
				report_error("Vec postoji promenljiva ili const za datim nazivom "+  oneVarDeclOneD.getI1() + " unutar opsega " , oneVarDeclOneD);
			}else {
				varDeclCountLocal++;
				report_info("Deklarisana lokalna promenljiva " + oneVarDeclOneD.getI1() + " ",oneVarDeclOneD);
				Obj varNode = Tab.insert(Obj.Var, oneVarDeclOneD.getI1(), currType);
			} 
			
		}
		
	}
	public void visit(OneVarDeclArray oneVarDeclArray){
		if(factorNewBoolean==true) {
			factorNewBoolean=false;
		}
		if(currentMethod == null) {
			if(Tab.find(oneVarDeclArray.getI1())!= Tab.noObj) {
				report_error("Vec postoji promenljiva ili const za datim nazivom " + oneVarDeclArray.getI1(), oneVarDeclArray);
			}else {
				varDeclCountGlobal++;
				report_info("Deklarisana globalna promenljiva " + oneVarDeclArray.getI1() + " ",oneVarDeclArray);
				Obj varNode = Tab.insert(Obj.Var, oneVarDeclArray.getI1(), new Struct(Struct.Array, currType));
			} 
		}
		else {
			if(Tab.currentScope().findSymbol(oneVarDeclArray.getI1())!= null) {
				report_error("Vec postoji promenljiva ili const za datim nazivom unutar metode " + oneVarDeclArray.getI1(), oneVarDeclArray);
			}else {
				varDeclCountLocal++;
				report_info("Deklarisana nizovska lokalna promenljiva " + oneVarDeclArray.getI1() + " ",oneVarDeclArray);
				Obj varNode = Tab.insert(Obj.Var, oneVarDeclArray.getI1(), new Struct(Struct.Array, currType));
			} 
			
		}
	}
	
    
    
    @Override
    public void visit(ProgramName programName) {
    	programName.obj = Tab.insert(Obj.Prog, programName.getI1(), Tab.noType);
    	Tab.openScope();
    }
    @Override
    public void visit(Program program) {
    	nVars = Tab.currentScope.getnVars();
    	Tab.chainLocalSymbols(program.getProgramName().obj);
    	Tab.closeScope();
    }
    
    @Override
    public void visit(MethodSignature methodSignature) {
    	if(!methodSignature.getI1().contentEquals("main")) {
    		report_error("Naziv metode mora biti 'main' ", methodSignature);
    	} else {
    		SyntaxNode methNode = methodSignature.getParent();
    		FormParamCounter formParamCnt = new FormParamCounter();
    		methNode.traverseBottomUp(formParamCnt);
    		if(formParamCnt.getCount()>0) {
    			report_error("Main metoda nema parametre", methodSignature);
    		} else {
	    		currentMethod = Tab.insert(Obj.Meth, methodSignature.getI1(), Tab.noType);
	    		methodSignature.obj=currentMethod;
	    		Tab.openScope();
	    		report_info("Usli smo u funkciju " + methodSignature.getI1(), methodSignature);
    		}
    	}
    }
    
    @Override 
    public void visit(MethodDecl methodDecl) {
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	currentMethod=null;
    	
    }
    
    @Override 
    public void visit (Type type) {
    	factorNewBoolean=true;
    	Obj currTypeObj = Tab.find(type.getI1());
    	if(currTypeObj == Tab.noObj) {
    		report_error("Nepostojeci tip podataka " + type.getI1(), null);
    		currType=Tab.noType;
    	}else {
    		if(Obj.Type == currTypeObj.getKind()) {
    			currType=currTypeObj.getType();
    		}else{
    			report_error("Greska: Ime " + type.getI1() + " ne predstavlja tip!", type);
    			currType=Tab.noType;
    		} 
    	}
    	type.struct=currType;
    }
    
    @Override
    public void visit(DesignatorCasual designatorCasual) {
    	Obj objDesignatorCasual = Tab.find(designatorCasual.getI1());
    	if(objDesignatorCasual == Tab.noObj && !designatorCasual.getI1().contentEquals("null")) {
   		 report_error("Designator var nije prethodno definisan " + designatorCasual.getI1() + " ", designatorCasual);
    	} else {
	    	if(objDesignatorCasual.getKind()==Obj.Meth) {
	    		currentCallFunc=objDesignatorCasual;
	    		arguments = new ArrayList<>();
	    		report_info("Poziv funkcije " + designatorCasual.getI1(), designatorCasual);
	    	} else if( designatorCasual.getI1().contentEquals("null")) {
	    		objDesignatorCasual = Tab.find("null");
	    		if(currentCallFunc!=null && currentArrayName==null) {
	        		arguments.add(objDesignatorCasual);
	        	}
	    		
	    	} else if(objDesignatorCasual.getKind() == Obj.Var || objDesignatorCasual.getType().getKind()==Struct.Array || objDesignatorCasual.getType().getKind() == Struct.Enum) {
	    		if(objDesignatorCasual.getLevel()==0) {
	    			report_info("Detektovan simbol globalne promenljive " + designatorCasual.getI1() + " " + objDesignatorCasual, designatorCasual);
	    		}
	    		if(objDesignatorCasual.getLevel()==1) {
	    			report_info("Detektovan simbol lokalne promenljive " + designatorCasual.getI1() + " " + objDesignatorCasual, designatorCasual);
	    		}
	    		currExprType = new Struct(objDesignatorCasual.getType().getKind());
	        	Obj factorDesignatorObj = Tab.find(objDesignatorCasual.getName());
	        	if(currentCallFunc!=null && currentArrayName==null) {
	        		arguments.add(factorDesignatorObj);
	        	}
	    	} else if(objDesignatorCasual.getKind() == Obj.Con) {
	    		report_info("Detektovana simbolicka konstanta " + designatorCasual.getI1() + " " + objDesignatorCasual, designatorCasual);
	    		if(currentCallFunc!=null && currentArrayName==null ) {
	    			arguments.add(objDesignatorCasual);
	    		}
	    	}
	    	designatorCasual.obj= objDesignatorCasual;
	 //   	report_error(" +dddd " + designatorCasual.obj.getName() + " " + designatorCasual.obj.getKind() , designatorCasual);
    	}
    }
    @Override
    public void visit(DesignatorArray designatorArray) {
    	Obj designatorArrayObj = designatorArray.getDesignatorArrayName().obj;
    	Struct elemType = designatorArrayObj.getType().getElemType();
    	report_info("Detektovan simbol " + designatorArrayObj.getName() + " " + designatorArrayObj, designatorArray);
    	if(elemType == null) {
    		report_error("elemType = null " + designatorArrayObj.getName(), designatorArray);
    	}
    	if(designatorArray.getExpr().struct.getKind() != Struct.Int) {
    		report_error("Za pristup elementu niza Expr mora biti int tipa ", designatorArray);
    	}
    	designatorArray.obj = new Obj(Obj.Elem,currentArrayName + "elem",elemType);
    //	designatorArray.obj= designatorArrayObj;
    	arrayAccess=true;
    	if(currentCallFunc!=null) {
    		Obj designatorElemObj = new Obj(Obj.Elem, currentArrayName+"elem", designatorArrayObj.getType().getElemType());
    		arguments.add(designatorElemObj);
    	} 
    	currentArrayName=null;
    } 
    
    @Override
    public void visit (DesignatorArrayName designatorArrayName) {
    	Obj objDesignatorArrayName = Tab.find(designatorArrayName.getI1());
    	currentArrayName = designatorArrayName.getI1();
    	if(objDesignatorArrayName == Tab.noObj) {
    		report_error("Designator array nije prethodno definisan " + designatorArrayName.getI1() + " ", designatorArrayName);
    	} else if(objDesignatorArrayName.getType().getKind() != Struct.Array) {
    		report_error("Designator mora biti tipa Array " + designatorArrayName.getI1(),designatorArrayName);
    	}
    	designatorArrayName.obj = objDesignatorArrayName;
    }
    @Override
    public void visit (DesignatorStatementAssign designatorStatementAssign ) {
    	if(newArrayError == false) {
	    	designatorStatementAssign.obj = Tab.find(designatorStatementAssign.getDesignator().obj.getName());
	    	Struct exprType = designatorStatementAssign.getExpr().struct;
	    //	report_info("designatorStatementAssign.getDesignator().obj.getType().getKind() =" + designatorStatementAssign.getDesignator().obj.getType().getKind() , designatorStatementAssign);
	    //	report_info("designatorStatementAssign.getExpr().struct.getKind() =" + designatorStatementAssign.getExpr().struct.getKind(),designatorStatementAssign);
	    	if(designatorStatementAssign.obj.getKind() == Obj.Con) {
	    		report_error("Konstanti se ne moze dodeliti nova vrednost", designatorStatementAssign);
	    	} else if(designatorStatementAssign.obj.getKind() != Obj.Var && designatorStatementAssign.obj.getKind() != Obj.Elem ) {
	    		report_error("Sa leve strane jednakosti moze biti ili promenljiva ili elemnt niza",designatorStatementAssign);
	    	} else if (designatorStatementAssign.getDesignator().obj.getType().getKind() != exprType.getKind()
	    			&& !(designatorStatementAssign.getDesignator().obj.getType().getKind() == exprType.getKind() && exprType.getKind()==Struct.Array && designatorStatementAssign.getDesignator().obj.getType().getElemType().getKind()== exprType.getElemType().getKind())
	    			&& (!designatorStatementAssign.getDesignator().obj.getType().isRefType() || exprType != Tab.nullType )
	    			&& (designatorStatementAssign.getDesignator().obj.getType().getKind() != Struct.Enum || exprType != Tab.nullType ))  {
	    		report_error("Nisu kompatiblini tipovi za dodelu", designatorStatementAssign);
	    	} else if(designatorStatementAssign.getDesignator().obj.getType().getKind() == exprType.getKind() && exprType.getKind()==Struct.Array && designatorStatementAssign.getDesignator().obj.getType().getElemType().getKind()!= exprType.getElemType().getKind()) {
	    		report_error("Nisu kompatiblini tipovi za dodelu", designatorStatementAssign);
	    	}
	    	else {
	    		designatorStatementAssign.getDesignator().obj.setFpPos(numValue);
	    	}
    	}
    	newArrayError = false;
    	numValue=0;
    }
    
    @Override
    public void visit (DesignatorStatementCallFunc designatorStatementCallFunc) {
    	Obj objDesignatorStatementCallFunc = designatorStatementCallFunc.getDesignator().obj;
    	designatorStatementCallFunc.obj = designatorStatementCallFunc.getDesignator().obj;
    	//Collection<Obj> localSymbols = currentCallFunc.getLocalSymbols();
    	if(Obj.Meth!=objDesignatorStatementCallFunc.getKind()) {
    		report_error("Nepostojeca funkcija " + designatorStatementCallFunc.getDesignator(), designatorStatementCallFunc);
    	} else if(errorFuncCall) {
    		report_error("Nepravilan poziv funkcije " + objDesignatorStatementCallFunc.getName(), designatorStatementCallFunc);
    		errorFuncCall=false;
    	} 
    	/* Ovde je bilo dodavanje i provera parametara prilokom poziva funkcije*/
    	currentCallFunc=null; 
    }
    
    @Override
    public void visit (DesignatorStatementInc designatorStatementInc) {
    	if(designatorStatementInc.getDesignator().obj.getKind() != Obj.Elem && designatorStatementInc.getDesignator().obj.getKind() != Obj.Var ){
    		report_error("Samo element niza i promenljive mogu da se inkrementiraju", designatorStatementInc);
    	} else if(designatorStatementInc.getDesignator().obj.getType() != Tab.intType) {
    		report_error("Samo promenljive tipa int type mogu da se inkrementiraju", designatorStatementInc);
    	} else {
    		report_info("Inkrementirana je vrednost promenljive " + designatorStatementInc.getDesignator().obj.getName(), designatorStatementInc);
    	} 
    }
    @Override
    public void visit (DesignatorStatementDec designatorStatementDec) {
    	if(designatorStatementDec.getDesignator().obj.getKind() != Obj.Elem && designatorStatementDec.getDesignator().obj.getKind() != Obj.Var ){
    		report_error("Samo element niza i promenljive mogu da se inkrementiraju", designatorStatementDec);
    	} else if(designatorStatementDec.getDesignator().obj.getType() != Tab.intType) {
    		report_error("Samo promenljive tipa int type mogu da se inkrementiraju", designatorStatementDec);
    	} else {
    		report_info("Dekrementirana je vrednost promenljive" + designatorStatementDec.getDesignator().obj.getName(), designatorStatementDec);
    	}
    }
    @Override
    public void visit (DesignatorStatementSetOpAssign designatorStatementSetOpAssign) {
    	if(designatorStatementSetOpAssign.getDesignator().obj.getType().getKind() != Struct.Enum) {
    		report_error("Promenljiva sa leve strane znaka jednakosti mora biti tipa set", designatorStatementSetOpAssign);
    	} else if(designatorStatementSetOpAssign.getDesignator1().obj.getType().getKind() != Struct.Enum) {
    		report_error("Prva promenljiva sa desne strane znaka jednakosti mora biti tipa set", designatorStatementSetOpAssign);
    	} else if(designatorStatementSetOpAssign.getDesignator2().obj.getType().getKind() != Struct.Enum) {
    		report_error("Druga promenljiva sa desne strane znaka jednakosti mora biti tipa set", designatorStatementSetOpAssign);
    	} else {
    		report_info("Izvrsena je setop", designatorStatementSetOpAssign);
    	}
    }
    @Override
    public void visit (StatementRead statementRead) {
    	if(statementRead.getDesignator().obj.getKind()!=Obj.Elem && statementRead.getDesignator().obj.getKind()!=Obj.Var) {
    		report_error("Designator " + statementRead.getDesignator().obj.getName() + " mora biti element niza ili promenljiva ", statementRead);
    	} else if(statementRead.getDesignator().obj.getType().getKind() != Struct.Int && statementRead.getDesignator().obj.getType().getKind() != Struct.Char && statementRead.getDesignator().obj.getType().getKind() != Struct.Bool) {
    		report_error("Designator " + statementRead.getDesignator().obj.getName() + " mora biti tipa int, char ili bool" , statementRead);
    	} else {
    		report_info("Read " + statementRead.getDesignator().obj.getName() , statementRead);
    	}
    }
    @Override
    public void visit(StatementPrintNumNo statementPrint) {
    	if(statementPrint.getExpr().struct.getKind() != Struct.Bool && statementPrint.getExpr().struct.getKind() != Struct.Char 
    		&&	statementPrint.getExpr().struct.getKind() != Struct.Int && statementPrint.getExpr().struct.getKind() != Struct.Enum) {
    		report_error("Expr u print statement u mora biti tipa int, char, bool ili set ", statementPrint);
    	} else {
    		report_info("Statement print ", statementPrint);
		printCallCount++;
    	}
    }
    @Override
    public void visit(StatementPrintNumYes statementPrint) {
    	if(statementPrint.getExpr().struct.getKind() != Struct.Bool && statementPrint.getExpr().struct.getKind() != Struct.Char 
    		&&	statementPrint.getExpr().struct.getKind() != Struct.Int && statementPrint.getExpr().struct.getKind() != Struct.Enum) {
    		report_error("Expr u print statement u mora biti tipa int, char, bool ili set ", statementPrint);
    	} else {
    		report_info("Statement print ", statementPrint);
		printCallCount++;
    	}
    }
    @Override
    public void visit (StatementReturn statementReturn) {
    	report_error("Funkcija tipa void nema povratnu vrednost", statementReturn);
    }
    
    @Override
    public void visit(ActParsNo actParsNo) {
    	if(currentCallFunc.getLevel()>0) {
    		report_error("Funkcija " + currentCallFunc.getName() + " treba da ima " +currentCallFunc.getLevel() + " argument/a", actParsNo);
    	//	errorFuncCall=true;
    	}
    }
    @Override
    public void visit (ActParsYes actParsYes) {
    	if(currentCallFunc.getLevel()!=cntActPars) {
    		report_error("Funkcija " + currentCallFunc.getName() + " treba da ima " +currentCallFunc.getLevel() + " argument/a", actParsYes);
    	//  errorFuncCall=true;	
    	} else {
		    	Collection<Obj> localSymbols = currentCallFunc.getLocalSymbols();
		    	Obj objDesignatorStatementCallFunc = Tab.find(currentCallFunc.getName());
				if(Obj.Meth!=objDesignatorStatementCallFunc.getKind()) {
		    		report_error("Nepostojeca funkcija " + objDesignatorStatementCallFunc.getName(), actParsYes);
		    	} else if(errorFuncCall) {
		    		report_error("Nepravilan poziv funkcije " + objDesignatorStatementCallFunc.getName(), actParsYes);
		    		errorFuncCall=false;
		    	} else {
		    	
		    	Struct [] localSymbolsArray = new Struct [localSymbols.size()];
		    	localSymbols.forEach(t->{
		    		localSymbolsArray[localSymbolsIndex++]=t.getType();
		  //  		report_info("localSymbolsArray "+t.getName(), actParsYes);
		    	});
		 //   	report_info("localSymbolsArray[0].getKind()= " + localSymbolsArray[0].getKind(), actParsYes);
		    	Struct [] argumetsArray = new Struct [arguments.size()];
		    	arguments.forEach(t ->{
		    		argumetsArray[argumentsIndex++]=t.getType();
		    	//	report_info("argumetsArray " + t.getName(), actParsYes);
		    	});
		//    	report_info("argumetsArray[0].getKind()="+ argumetsArray[0].getKind(), actParsYes);
		//    	report_info(" argumetsArray.length =" + argumetsArray.length, actParsYes);
		//    	report_info(" localSymbolsArray.length = " + localSymbolsArray.length, actParsYes);
		    	
		    	for(int i=0;i<localSymbolsArray.length;i++) {
		    	//	report_info("localSymbolsArray.length= ="+localSymbolsArray.length, actParsYes);
		    	//	report_info("argumetsArray.length = "+ argumetsArray.length , actParsYes);
		    	//	report_info("argumetsArray["+i+"].kind ="+argumetsArray[i].getKind(), actParsYes);
		    	//	report_info("localSymbolsArray["+i+"].kind ="+localSymbolsArray[i].getKind(), actParsYes);
		    		//&& (!designatorStatementAssign.getDesignator().obj.getType().isRefType() || exprType != Tab.nullType )
		    /*		if(argumetsArray[i].isRefType()) {
		    	//		report_error(argumetsArray[i].getKind() +  "je ref type", actParsYes);
		    		}
		    		if(argumetsArray[i] == Tab.nullType) {
		    	//		report_error(argumetsArray[i].getKind() +  "je null type", actParsYes);
		    		}
		    	//	if(localSymbolsArray[i].isRefType()) {
		    	//		report_info(localSymbolsArray[i].getKind() +  "je ref type", actParsYes);
		    	//	}
		    		if(localSymbolsArray[i] == Tab.nullType) {
		    	//		report_info(localSymbolsArray[i].getKind() +  "je null type", actParsYes);
		    		}
		    	*/
		    		if(argumetsArray[i].getKind() != (localSymbolsArray[i]).getKind()
		    				&& !(argumetsArray[i] == Tab.nullType && localSymbolsArray[i].isRefType())
		    				&& !(argumetsArray[i] == Tab.nullType && localSymbolsArray[i].getKind() == Struct.Enum )
		    				) {
		    			int j=i+1;
		    			report_error("Ne poklapa se tip " + j + ". argumenta u pozivu funkcije " , actParsYes); 
		    		} 
		    		if(argumetsArray[i].getKind() == (localSymbolsArray[i]).getKind() && argumetsArray[i].getKind()==Struct.Array && argumetsArray[i].getElemType().getKind()!= localSymbolsArray[i].getElemType().getKind() && !currentCallFunc.getName().contentEquals("len")
		    				&& !(argumetsArray[i] == Tab.nullType && localSymbolsArray[i].isRefType())) {
		    			int j=i+1;
		    			report_error("Ne poklapa se tip " + j + ". argumenta u pozivu funkcije " , actParsYes);
		    		}
		    	}
		    	//	report_error("Localsymbols size= "+localSymbols.size() + " argumets size= " +arguments.size() + "localSymbolsArray size " + localSymbolsArray.length + " argumentsArray size = " + argumetsArray.length, actParsYes);
		    	}	
    		}
    	argumentsIndex=0;
    	localSymbolsIndex=0;
    	arguments=null;
    	currentCallFunc=null;
    	cntActPars=0;
    
    }
    
    @Override 
    public void visit (OneActPars oneActPars) {
    	Obj callFunc = Tab.find(currentCallFunc.getName());
    	cntActPars++;
    //	report_info("arg "+arguments.size(), oneActPars);
    	
    }
    @Override
    public void visit (FactorNum factorNum) {
    	currExprType = Tab.intType;
    	factorNum.struct=currExprType;
    	Obj factorNumObj = new Obj(Obj.Con, factorNum.getN1().toString(), new Struct(Struct.Int));
    	if(factorNewBoolean==true){
    		numValue = factorNum.getN1();
    		factorNewBoolean =false;
    	} 
    	if(currentCallFunc!=null && currentArrayName==null && factorNewBoolean==false) {
    		arguments.add(factorNumObj);
    	}
    	
    }
    @Override
    public void visit (FactorChar factorChar) {
    	currExprType = Tab.charType;
    	factorChar.struct=currExprType;
    	report_info("factorChar.struct.getKind() " + factorChar.struct.getKind(), factorChar); 
    	Obj factorCharObj = new Obj(Obj.Con, factorChar.getC1().toString(), new Struct(Struct.Char));
    	if(currentCallFunc!=null && currentArrayName==null) {
    		arguments.add(factorCharObj);
    	}
    }
    @Override
    public void visit (FactorBool factorBool) {
    	currExprType = Tab.find("bool").getType();
    	factorBool.struct=currExprType;
    	Obj factorBoolObj = new Obj(Obj.Con, factorBool.getB1().toString(), new Struct(Struct.Bool));
    	if(currentCallFunc!=null && currentArrayName==null) {
    		arguments.add(factorBoolObj);
    	}
    }
    @Override
    public void visit (FactorExpr factorExpr) {
    	factorExpr.struct = factorExpr.getExpr().struct;
    }
    @Override
    public void visit (FactorDesignator factorDesignator) {
    	currExprType = factorDesignator.getDesignator().obj.getType();
    //	report_info("factorDesignator.getDesignator().obj = "+factorDesignator.getDesignator().obj, factorDesignator);
    	if(factorDesignator.getDesignator().obj.getType().getKind()==Struct.Array) {
	    		currExprType.setElementType(factorDesignator.getDesignator().obj.getType().getElemType());
	    }
    	
    	Obj factorDesignatorObj = Tab.find(factorDesignator.getDesignator().obj.getName());
    	factorDesignator.struct=currExprType;
    /*	if(currentCallFunc!=null && currentArrayName==null) {
    		if(factorDesignatorObj.getType().getKind()==Struct.Array) {
    			arguments.add(new Obj(Obj.Var,factorDesignator.toString(),new Struct(factorDesignatorObj.getType().getElemType().getKind())));
    		}else {
    		arguments.add(factorDesignatorObj);
    		}
    	}
    	*/
    }
    @Override
    public void visit (FactorNew factorNew) {
    	String type =factorNew.getType().getI1();
    	currExprType = Tab.find(type).getType();
    	if(factorNew.getExpr().struct != Tab.intType) {
    		report_error("Za velicinu niza mora se koristiti int type", factorNew);
    		newArrayError = true;
    	}else {
	    	if(type.contentEquals("set")) {
	    		factorNew.struct=currExprType;
	    		if(currentCallFunc!=null && currentArrayName==null) {
	    			Struct setFactor = new Struct(Struct.Enum, new Struct(Struct.Int));
	        		Obj factorNewObj = new Obj(Obj.Var,"factorSetObj",setFactor);
	        		arguments.add(factorNewObj);
	        	}
	    	} else if(type.contentEquals("int")) {
	    		factorNew.struct = new Struct(Struct.Array, currExprType);
	    		if(currentCallFunc!=null && currentArrayName==null) {
	    			Struct intArrayFactor = new Struct(Struct.Array,new Struct(Struct.Int));
	        		Obj factorNewObj = new Obj(Obj.Var,"factorIntArrayObj",intArrayFactor);
	        		arguments.add(factorNewObj);
	    		}
	    		
	    	} else if(type.contentEquals("char")) {
	    		factorNew.struct = new Struct(Struct.Array, currExprType);
	    		if(currentCallFunc!=null && currentArrayName==null) {
	    			Struct charArrayFactor = new Struct(Struct.Array,new Struct(Struct.Char));
	        		Obj factorNewObj = new Obj(Obj.Var,"factorCharArrayObj",charArrayFactor);
	        		arguments.add(factorNewObj);
	    		}
	    	} else if(type.contentEquals("bool")) {
	    		factorNew.struct=new Struct(Struct.Array, currExprType);
	    		if(currentCallFunc!=null && currentArrayName==null) {
	    			Struct boolArrayFactor = new Struct(Struct.Array,new Struct(Struct.Bool));
	        		Obj factorNewObj = new Obj(Obj.Var,"factorBoolArrayObj",boolArrayFactor);
	        		arguments.add(factorNewObj);	
	    		}
	    	}
    	}
    	
    	
    }
    
    @Override 
    public void visit (FactorSignPlus factorSignPlus) {
    	factorSignPlus.struct = factorSignPlus.getFactor().struct;
    }
    
    @Override 
    public void visit (FactorSignMinus factorSignMinus) {
    	if(factorSignMinus.getFactor().struct.getKind()!= Struct.Int && factorSignMinus.getFactor().struct != null) {
    		report_error("Minus u Expr moze samo ispred int vrednosti ", factorSignMinus);
    	}
    	factorSignMinus.struct = factorSignMinus.getFactor().struct;
    }
    
    @Override
    public void visit(FactorListEnd factorListEnd) {
    	factorListEnd.struct = factorListEnd.getFactorSign().struct;
    }
    @Override
    public void visit (FactorListAdd factorListAdd) {
    	Struct factorListStruct = factorListAdd.getFactorList().struct;
    	Struct factor = factorListAdd.getFactorSign().struct;
    	if(factorListStruct!=null)
	    	if(factorListStruct.getKind()!=Struct.Int) {
	    		report_error("Izraz sa leve strane operatora mulOp mora biti int tipa", factorListAdd);
	    	}
    	if(factor!=null)
	    	if(factor.getKind()!=Struct.Int) {
	    		report_error("Izraz sa desne strane operatora mulOp mora biti int tipa", factorListAdd);
	    	}
    	factorListAdd.struct= Tab.intType;
    }
    
    
    @Override
    public void visit (Term term) {
    	term.struct= term.getFactorList().struct;
    }
    
    @Override
    public void visit (TermListEnd termListEnd) {
    	termListEnd.struct = termListEnd.getTerm().struct;
    }
    @Override 
    public void visit (TermListAdd termListAdd) {
    	Struct termListStruct = termListAdd.getTermList().struct;
    	Struct term = termListAdd.getTerm().struct;
    	if(termListStruct!=null)
	    	if(termListStruct.getKind()!=Struct.Int) {
	    		report_error("Izraz sa leve strane operatora addOp mora biti int tipa", termListAdd);
	    	}
    	if(term!=null)
	    	if(term.getKind()!=Struct.Int) {
	    		report_error("Izraz sa desne strane operatora addOp mora biti int tipa", termListAdd);
	    	}
    	termListAdd.struct = Tab.intType;
    }
    
    @Override
    public void visit (Expr expr) {
    	expr.struct = expr.getTermList().struct;
    }
 /*   @Override
    public void visit (ExprMinus exprMinus) {
    	if(exprMinus.getTermList().struct.getKind()!= Struct.Int) {
    		report_error("Minus u Expr moze samo ispred int vrednosti ", exprMinus);
    	} 
    	exprMinus.struct = exprMinus.getTermList().struct;
    	
    }
   */
    
    
    public boolean passed(){
    	return !errorDetected;
    }
    
}
