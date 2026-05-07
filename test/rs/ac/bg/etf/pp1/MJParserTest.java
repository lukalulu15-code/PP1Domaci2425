package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/program.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        
	        // ispis sintaksnog stabla
	        log.info(prog.toString(""));
	     	log.info("===================================");

	        
	        Tab.init();
	        Struct boolType = new Struct(Struct.Bool);
	        Obj boolObj = Tab.insert(Obj.Type, "bool", boolType);
	        boolObj.setAdr(-1);
	        boolObj.setLevel(-1);
	        
	        Struct setType = new Struct(Struct.Enum, new Struct(Struct.Int));
	        Obj setObj = Tab.insert(Obj.Type, "set", setType);
	        setObj.setAdr(-1);
	        setObj.setLevel(-1);
	        
	        Struct intType = new Struct (Struct.Int);
	        Struct voidType = new Struct(Struct.None);
	        Struct intArrayType = new Struct(Struct.Array, intType);
	        
			Obj addObj = Tab.insert(Obj.Meth, "add", Tab.noType);
			addObj.setAdr(-1);
			addObj.setLevel(2);
			addObj.setFpPos(2);
			{
				Tab.openScope();
				Tab.currentScope().addToLocals(new Obj(Obj.Var,"addA",setType,0,1));
				Tab.currentScope().addToLocals(new Obj(Obj.Var,"addB",intType,0,1));
				addObj.setLocals(Tab.currentScope().getLocals());
				Tab.closeScope();
			}
			
			
			
			
			Obj addAllObj = Tab.insert(Obj.Meth, "addAll", Tab.noType);
			addAllObj.setAdr(-1);
			addAllObj.setLevel(2);
			addAllObj.setFpPos(2);
			{
				Tab.openScope();
				Tab.currentScope().addToLocals(new Obj(Obj.Var,"addAllA",setType,0,1));
				Tab.currentScope().addToLocals(new Obj(Obj.Var,"addAllB",intArrayType,0,1));
				addAllObj.setLocals(Tab.currentScope().getLocals());
				Tab.closeScope();
			}
	        
			Obj ordObj = Tab.find("ord");
			ordObj.setFpPos(1);
			
			Obj chrObj = Tab.find("chr");
			chrObj.setFpPos(1);
			
			Obj lenObj = Tab.find("len");
			lenObj.setFpPos(1);
			
			
			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v); 
	      
			log.info(" Print count calls = " + v.printCallCount);
			
			log.info(" Deklarisanih globalnih promenljivih ima = " + v.varDeclCountGlobal);
			log.info(" Deklarisanih lokalnih promenljivih ima = " + v.varDeclCountLocal);
			
			// Ispis Tabele simbola
			Tab.dump();
			log.info("===================================");

			
			if(!p.errorDetected && v.passed()){
				
				File objFile = new File("test/program.obj");
				if(objFile.exists())
					objFile.delete();
				
				CodeGenerator codeGenerator = new CodeGenerator();
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = v.nVars;
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Parsiranje uspesno zavrseno!");
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
		
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
	
}