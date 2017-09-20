package mips;

import java.util.regex.Pattern;

import ast.*;
//import jdk.nashorn.internal.ir.Expression;
//import jdk.nashorn.internal.ir.Statement;
import types.*;

public class CodeGen implements ast.CommandVisitor {

	private StringBuffer errorBuffer = new StringBuffer();
	private TypeChecker tc;
	private Program program;
	private ActivationRecord currentFunction;

	public CodeGen(TypeChecker tc) {
		this.tc = tc;
		this.program = new Program();
	}

	public boolean hasError() {
		return errorBuffer.length() != 0;
	}

	public String errorReport() {
		return errorBuffer.toString();
	}

	private class CodeGenException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public CodeGenException(String errorMessage) {
			super(errorMessage);
		}
	}

	public boolean generate(Command ast) {
		try {
			currentFunction = ActivationRecord.newGlobalFrame();
			ast.accept(this);
			return !hasError();
		} catch (CodeGenException e) {
			return false;
		}
	}

	public Program getProgram() {
		return program;
	}

	@Override
	public void visit(ExpressionList node) {
		for (Expression e : node) {
			// System.out.println("in expression list: "+e);
			e.accept(this);
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(DeclarationList node) {
		for (Declaration d : node) {
			d.accept(this);
		}
		program.appendExitSequence();
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(StatementList node) {
		for (Statement s : node) {
			s.accept(this);
			// System.out.println("in statement list: "+s);
			// System.out.println(tc.getType((Command) s));
			if (s instanceof Call && !(tc.getType((Command) s) instanceof VoidType)) {
				program.appendInstruction("addi    $sp, $sp, 4");
			}
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(AddressOf node) {
		currentFunction.getAddress(program, "$t0", node.symbol());
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LiteralBool node) {
		int i = ActivationRecord.numBytes(tc.getType(node));
		if (node.value().equals(LiteralBool.Value.TRUE)) {
			program.appendInstruction("addi    $t0, $zero, 1"); // check
		} else if (node.value().equals(LiteralBool.Value.FALSE)) {
			program.appendInstruction("add    $t0, $zero, $zero");
		}
		program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
		program.pushInt("$t0");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LiteralFloat node) {
		int i = ActivationRecord.numBytes(tc.getType(node));
		program.appendInstruction("li.s    $f0, " + Float.toString(node.value()));
		program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
		program.pushFloat("$f0");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LiteralInt node) {
		int i = ActivationRecord.numBytes(tc.getType(node));
		program.appendInstruction("addi    $t0, $zero, " + Integer.toString(node.value()));
		program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
		program.pushInt("$t0");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(VariableDeclaration node) {
		currentFunction.add(program, node);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(ArrayDeclaration node) {
		currentFunction.add(program, node);
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(FunctionDefinition node) {
		String func = node.function().name();
		int pos = 0;
		if (!func.equals("main")) {
			pos = program.appendInstruction("func." + func + ": \n");
		} else {
			pos = program.appendInstruction("main:");
		}
		currentFunction = new ActivationRecord(node, currentFunction);
		node.body().accept(this);
		program.insertPrologue(pos + 1, currentFunction.stackSize());

		// if (func.equals("main"))
		// {
		// program.appendExitSequence();
		// }
		// System.out.println(func + ": " + node.function().type());
		if (((FuncType) node.function().type()).returnType() instanceof VoidType) {
			program.appendEpilogue(currentFunction.stackSize());
		}
		currentFunction = currentFunction.parent();
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Addition node) {
		node.leftSide().accept(this);
		node.rightSide().accept(this);
		Type t = tc.getType((Command) node.leftSide());
		int i = ActivationRecord.numBytes(t);
		if (t instanceof IntType) {
			program.popInt("$t1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popInt("$t0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("add    $t2, $t0, $t1");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushInt("$t2");
		} else if (t instanceof FloatType) {
			program.popFloat("$f1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popFloat("$f0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("add.s    $f2, $f0, $f1");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushFloat("$f2");
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Subtraction node) {
		node.leftSide().accept(this);
		node.rightSide().accept(this);
		Type t = tc.getType((Command) node.leftSide());
		int i = ActivationRecord.numBytes(t);
		if (t instanceof IntType) {
			program.popInt("$t1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popInt("$t0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("sub    $t2, $t0, $t1");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushInt("$t2");
		} else if (t instanceof FloatType) {
			program.popFloat("$f1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popFloat("$f0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("sub.s    $f2, $f0, $f1");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushFloat("$f2");
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Multiplication node) {
		node.leftSide().accept(this);
		node.rightSide().accept(this);
		Type t = tc.getType((Command) node.leftSide());
		int i = ActivationRecord.numBytes(t);
		if (t instanceof IntType) {
			program.popInt("$t1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popInt("$t0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("mult    $t0, $t1");
			program.appendInstruction("mflo $t2");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushInt("$t2");
		} else if (t instanceof FloatType) {
			program.popFloat("$f1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popFloat("$f0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("mul.s    $f2, $f0, $f1");
//			program.appendInstruction("mflo $f2");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushFloat("$f2");
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Division node) {
		node.leftSide().accept(this);
		node.rightSide().accept(this);
		Type t = tc.getType((Command) node.leftSide());
		int i = ActivationRecord.numBytes(t);
		if (t instanceof IntType) {
			program.popInt("$t1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popInt("$t0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("div    $t2, $t0, $t1");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushInt("$t2");
		} else if (t instanceof FloatType) {
			program.popFloat("$f1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popFloat("$f0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("div.s    $f2, $f0, $f1");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushFloat("$f2");
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LogicalAnd node) {
		node.leftSide().accept(this);
		node.rightSide().accept(this);
		int i = ActivationRecord.numBytes(new BoolType());
		program.popInt("$t1");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.popInt("$t0");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.appendInstruction("and    $t2, $t0, $t1");
		program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
		program.pushInt("$t2");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LogicalOr node) {
		node.leftSide().accept(this);
		node.rightSide().accept(this);
		int i = ActivationRecord.numBytes(new BoolType());
		program.popInt("$t1");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.popInt("$t0");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.appendInstruction("or    $t2, $t0, $t1");
		program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
		program.pushInt("$t2");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(LogicalNot node) {
		node.expression().accept(this);
		int i = ActivationRecord.numBytes(new BoolType());
		program.popInt("$t0");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.appendInstruction("sle    $t2, $t0, $zero");
		program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
		program.pushInt("$t2");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Comparison node) {
		node.leftSide().accept(this);
		node.rightSide().accept(this);
		Type t = tc.getType((Command) node.leftSide());
		int i = ActivationRecord.numBytes(t);
		if (t instanceof IntType) {
			program.popInt("$t1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popInt("$t0");
			program.appendInstruction("addi    $sp, $sp, 4");
			if (node.operation().equals(Comparison.Operation.LT)) {
				program.appendInstruction("slt    $t2, $t0, $t1");
			} else if (node.operation().equals(Comparison.Operation.LE)) {
				program.appendInstruction("sle    $t2, $t0, $t1");
			} else if (node.operation().equals(Comparison.Operation.EQ)) {
				program.appendInstruction("seq    $t2, $t0, $t1");
			} else if (node.operation().equals(Comparison.Operation.NE)) {
				program.appendInstruction("sne    $t2, $t0, $t1");
			} else if (node.operation().equals(Comparison.Operation.GT)) {
				program.appendInstruction("sgt    $t2, $t0, $t1");
			} else if (node.operation().equals(Comparison.Operation.GE)) {
				program.appendInstruction("sge    $t2, $t0, $t1");
			}
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushInt("$t2");
		} 
		else if (t instanceof FloatType) {
			program.popFloat("$f1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popFloat("$f0");
			program.appendInstruction("addi    $sp, $sp, 4");
			String CompTrue = program.newLabel();
			String CompFalse = program.newLabel();
			String CompOut = program.newLabel();
			if (node.operation().equals(Comparison.Operation.LT)) {
				program.appendInstruction("c.lt.s    $f0, $f1");
				program.appendInstruction("bc1t    " + CompTrue);
				program.appendInstruction("bc1f    " + CompFalse);
			} else if (node.operation().equals(Comparison.Operation.LE)) {
				program.appendInstruction("c.le.s    $f0, $f1");
				program.appendInstruction("bc1t    " + CompTrue);
				program.appendInstruction("bc1f    " + CompFalse);
			} else if (node.operation().equals(Comparison.Operation.EQ)) {
				program.appendInstruction("c.eq.s    $f0, $f1");
				program.appendInstruction("bc1t    " + CompTrue);
				program.appendInstruction("bc1f    " + CompFalse);
			} else if (node.operation().equals(Comparison.Operation.NE)) {
				program.appendInstruction("c.eq.s    $f0, $f1");
				program.appendInstruction("bc1t    " + CompFalse);
				program.appendInstruction("bc1f    " + CompTrue);
			} else if (node.operation().equals(Comparison.Operation.GT)) {
				program.appendInstruction("c.le.s    $f0, $f1");
				program.appendInstruction("bc1t    " + CompFalse);
				program.appendInstruction("bc1f    " + CompTrue);
			} else if (node.operation().equals(Comparison.Operation.GE)) {
				program.appendInstruction("c.lt.s    $f0, $f1");
				program.appendInstruction("bc1t    " + CompFalse);
				program.appendInstruction("bc1f    " + CompTrue);
			}
			program.appendInstruction(CompTrue + ":");
			program.appendInstruction("addi    $t4, $zero, 1");
			program.appendInstruction("j    " + CompOut);
			program.appendInstruction(CompFalse + ":");
			program.appendInstruction("add    $t4, $zero, $zero");
			program.appendInstruction(CompOut + ":");
			program.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
			program.pushInt("$t4");
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Dereference node) {
		Expression addr = node.expression();
		addr.accept(this);
		Type t = tc.getType((Command) node);
		program.popInt("$t6");
		program.appendInstruction("addi   $sp, $sp, 4");
		if (t instanceof IntType || t instanceof BoolType) {
			program.appendInstruction("lw    $t5, 0($t6)");
			program.appendInstruction("subu    $sp, $sp, 4");
			program.pushInt("$t5");
		} else if (t instanceof FloatType) {
			program.appendInstruction("l.s    $f5, 0($t6)");
			program.appendInstruction("subu    $sp, $sp, 4");
			program.pushFloat("$f5");
		}

		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Index node) {
		node.base().accept(this);
		node.amount().accept(this);
		Type t = tc.getType((Command) node.base());
		if (t instanceof AddressType) 
		{
			t = ((AddressType) t).base();
			t = ((ArrayType) t).base();
//			System.out.println(t);
		}
		int i = ActivationRecord.numBytes(t);
		program.appendInstruction("addi    $t7, $zero, " + Integer.toString(i));
		program.popInt("$t8");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.appendInstruction("mult    $t7, $t8");
		program.appendInstruction("mflo    $t2");
		program.popInt("$t3");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.appendInstruction("add    $t4, $t3, $t2");
		program.appendInstruction("subu    $sp, $sp, 4");
		program.pushInt("$t4");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Assignment node) {
		node.destination().accept(this);
		node.source().accept(this);
		// System.out.println(node.destination());
		// System.out.println(node.source());
		// System.out.println(tc.getType((Command) node.destination()));
		// System.out.println(tc.getType((Command) node.source()));
		if (tc.getType((Command) node.source()) instanceof IntType
				|| tc.getType((Command) node.source()) instanceof BoolType) 
		{
			program.popInt("$t1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popInt("$t0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("sw    $t1, 0($t0)");
			// program.appendInstruction("subu $sp, $sp, 4");
			// program.pushInt("$t0");
		} 
		else if (tc.getType((Command) node.source()) instanceof FloatType) 
		{
			program.popFloat("$f1");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.popInt("$t0");
			program.appendInstruction("addi    $sp, $sp, 4");
			program.appendInstruction("s.s    $f1, 0($t0)");
			// program.appendInstruction("subu $sp, $sp, 4");
			// program.pushFloat("$f0");
		}
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Call node) {
		node.arguments().accept(this);
		Type t = node.function().type();
		program.appendInstruction("jal " + "func." + node.function().name());
		for (Expression e : node.arguments()) 
		{
			// System.out.println(e + " argument size: " +
			// node.arguments().size());
			program.appendInstruction("addi    $sp, $sp, " + Integer.toString(ActivationRecord.numBytes(tc.getType((Command) e))));
		}

		// if (t instanceof IntType || t instanceof BoolType)
		// {
		if (!(((FuncType) t).returnType() instanceof VoidType)) 
		{
			if (((FuncType) t).returnType() instanceof FloatType) {
				program.appendInstruction("subu    $sp, $sp, 4");
				program.pushFloat("$v0");
			} else {
				program.appendInstruction("subu    $sp, $sp, 4");
				program.pushInt("$v0");
			}
		}
		// }
		// else if(t instanceof FloatType)
		// {
		// program.appendInstruction("subu $sp, $sp, 4");
		// program.pushFloat("$v0");
		// }
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(IfElseBranch node) {
		node.condition().accept(this);
		program.popInt("$t0");
		program.appendInstruction("addi    $sp, $sp, 4");
		String iflabel = program.newLabel();
		String elselabel = program.newLabel();
		String outside = program.newLabel();
		program.appendInstruction("beq    $t0, $zero, " + elselabel);
		program.appendInstruction(iflabel + ": \n");
		node.thenBlock().accept(this);
		program.appendInstruction("j    " + outside);
		program.appendInstruction(elselabel + ": \n");
		node.elseBlock().accept(this);
		program.appendInstruction(outside + ": \n");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(WhileLoop node) {
		String whilelabel = program.newLabel();
		String outside = program.newLabel();
		program.appendInstruction(whilelabel + ": \n");
		node.condition().accept(this);
		program.popInt("$t0");
		program.appendInstruction("addi    $sp, $sp, 4");
		program.appendInstruction("beq    $t0, $zero, " + outside);
		node.body().accept(this);
		program.appendInstruction("j    " + whilelabel);
		program.appendInstruction(outside + ": \n");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(Return node) {
		node.argument().accept(this);
//		 System.out.println("hi in return");
//		 System.out.println(tc.getType(node));
//		 System.out.println(tc.getType((Command)node.argument()));
		Type rt =  tc.getType((Command)node.argument());
//		if (rt instanceof IntType || rt instanceof BoolType) 
//		{
			program.popInt("$v0");
//		} 
//		else if (rt instanceof FloatType) 
//		{
//			program.popFloat("$v0");
//		}
		program.appendInstruction("addi    $sp, $sp, 4");
		program.appendEpilogue(currentFunction.stackSize());
		// program.appendInstruction("addi $sp, $sp, " +
		// currentFunction.stackSize());
		// program.appendInstruction("lw $ra, 4($sp)");
		// program.appendInstruction("lw $fp, 0($sp)");
		// program.appendInstruction("addi $sp, $sp, 8");
		// program.appendInstruction("jr $ra");
		// throw new RuntimeException("Implement this");
	}

	@Override
	public void visit(ast.Error node) {
		String message = "CodeGen cannot compile a " + node;
		errorBuffer.append(message);
		throw new CodeGenException(message);
	}
}
