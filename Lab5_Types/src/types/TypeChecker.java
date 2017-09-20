package types;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;

import ast.*;

public class TypeChecker implements CommandVisitor {
    
    private HashMap<Command, Type> typeMap;
    private StringBuffer errorBuffer;
    private Return b_return = null;
    private Return i_return = null;
    private Return e_return = null;

    /* Useful error strings:
     *
     * "Function " + func.name() + " has a void argument in position " + pos + "."
     * "Function " + func.name() + " has an error in argument in position " + pos + ": " + error.getMessage()
     *
     * "Function main has invalid signature."
     *
     * "Not all paths in function " + currentFunctionName + " have a return."
     *
     * "IfElseBranch requires bool condition not " + condType + "."
     * "WhileLoop requires bool condition not " + condType + "."
     *
     * "Function " + currentFunctionName + " returns " + currentReturnType + " not " + retType + "."
     *
     * "Variable " + varName + " has invalid type " + varType + "."
     * "Array " + arrayName + " has invalid base type " + baseType + "."
     */

    public TypeChecker()
    {
        typeMap = new HashMap<Command, Type>();
        errorBuffer = new StringBuffer();
    }

    private void reportError(int lineNum, int charPos, String message)
    {
        errorBuffer.append("TypeError(" + lineNum + "," + charPos + ")");
        errorBuffer.append("[" + message + "]" + "\n");
    }

    private void put(Command node, Type type)
    {
        if (type instanceof ErrorType) {
            reportError(node.lineNumber(), node.charPosition(), ((ErrorType)type).getMessage());
        }
        typeMap.put(node, type);
    }
    
    public Type getType(Command node)
    {
        return typeMap.get(node);
    }
    
    public boolean check(Command ast)
    {
        ast.accept(this);
        return !hasError();
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }

    @Override
    public void visit(ExpressionList node) {
		TypeList tl = new TypeList();
    	for (Expression e : node)
    	{
    		e.accept(this);
    		tl.append(getType((Command) e));
    	}
    	put(node, tl);
    }

    @Override
    public void visit(DeclarationList node) {
		TypeList tl = new TypeList();
        for (Declaration d : node)
        {
        	d.accept(this);
        	tl.append(getType((Command) d));
        }
        put(node, tl);
    }

    @Override
    public void visit(StatementList node) {
		TypeList tl = new TypeList();
    	for (Statement s : node)
    	{
    		s.accept(this);
    		tl.append(getType((Command) s));
    	}
    	put(node, tl);
    }

    @Override
    public void visit(AddressOf node) {
    	put(node, new AddressType(node.symbol().type()));
    }

    @Override
    public void visit(LiteralBool node) {
    	put(node, new BoolType());
    }

    @Override
    public void visit(LiteralFloat node) {
        put(node, new FloatType());
    }

    @Override
    public void visit(LiteralInt node) {
        put(node, new IntType());
    }

    @Override
    public void visit(VariableDeclaration node) {
    	String varName = node.symbol().name();
    	if(new VoidType().equivalent(node.symbol().type()))
    		put(node, new ErrorType("Variable " + varName + " has invalid type " + node.symbol().type() + "."));
    	else if(node.symbol().type() instanceof ErrorType)
		{
			put(node, new ErrorType("Variable " + varName + " has invalid type " + node.symbol().type()));// + error.getMessage()));
		}
    	else
    	{
    		put(node, node.symbol().type());
    	}
    }

    @Override
    public void visit(ArrayDeclaration node) {
    	ArrayType array_type = (ArrayType)node.symbol().type();
    	
    	System.out.println(array_type.base());
    	
    	while (array_type.base() instanceof ArrayType)//array_type.base() instanceof ArrayType)
    	{
    		realBaseType((ArrayType)array_type.base());
//    		t = ((ArrayType)t).base();
//    		array_type = (ArrayType)array_type.base();
    	}
    	if ((array_type.base() instanceof IntType) || (array_type.base() instanceof FloatType) || (array_type.base() instanceof BoolType))
    	{
    		put(node, array_type);
    	}
    	else
    	{
    		put(node, new ErrorType("Array " + node.symbol().name() + " has invalid base type " + node.symbol().type() + "."));
    	}
    }

    @Override
    public void visit(FunctionDefinition node) {
    	FuncType ft = (FuncType) node.function().type();
//    	System.out.println(ft);
//    	System.out.println(ft.returnType());
    	String funcName = node.function().name();
//    	int pos = node.charPosition();
    	VoidType v = new VoidType();
    	if (funcName.equals("main"))
    	{
//    		System.out.println("in if equal main");
    		if (!v.equivalent(ft.returnType()))
    			put(node, new ErrorType("Function main has invalid signature."));
    		else
    		{
    			put(node, ft);
    			node.body().accept(this);
    		}
    	}
    	else
    	{
//    		TypeList tl = new TypeList();
    		int i = 0;
    		for (Type t : ft.arguments())
    		{
    			if (v.equivalent(t))
    			{
    				put(node, new ErrorType("Function " + funcName + " has a void argument in position " + i + "."));
    			}
    			else if(t instanceof ErrorType)
    			{
    				put(node, new ErrorType("Function " + funcName + " has an error in argument in position " + i + ": Unknown type: error"));// + error.getMessage()));
    			}
//    			tl.append(s.type());
    		}
//    		put(node, ft);
    		StatementList sl = node.body();
    		sl.accept(this);
    		Type func_returntype = ft.returnType();
    		Type ret = checkpaths(sl, funcName, func_returntype);
//    		if (!(ret.equivalent(func_returntype)) && (ret instanceof ErrorType) && ((ErrorType) ret).getMessage().equals(""))
//    		{
//    			
//    		}
    		if (!(ret.equivalent(func_returntype)) && !(ret instanceof ErrorType))
    		{
    			put(b_return, new ErrorType("Function " + funcName + " returns " + func_returntype + " not " + ret + "."));
//    			put(node, ret);
    		}
    		else if (!(ret.equivalent(func_returntype)) && (ret instanceof ErrorType))
    		{
    			if (((ErrorType) ret).getMessage().equals("."))
    			{
    				
    			}
    			else
    			{
    				put(b_return, new ErrorType("Function " + funcName + " returns " + func_returntype + " not " + ret + "."));
    			}
    		}
    		else
    		{
    			put(node, ret);
    		}
//    		put(node, ret);
//    		for (Statement s : sl)
//    		{
//    			if(s instanceof Return)
//    			{
//    				Return r = (Return) s;
//    				Type t = getType((Command) r);
//    				Type func_returntype = ft.returnType();
//    				if(func_returntype.equivalent(t))
//    				{
//    					put(node, ft);
//    				}
//    				else
//    				{
////    					typeMap.replace(r, new ErrorType("Function " + funcName + " returns " + func_returntype + " not " + t + "."));
//    					put(r, new ErrorType("Function " + funcName + " returns " + func_returntype + " not " + t + "."));
//    				}
//    			}
//    		}
    		//still need to check the function body if all paths(i.e ifs and elses) have return statements
    		//check if return statement returns the type associated with the function symbol
    	}
    }

    @Override
    public void visit(Comparison node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	Type t1 = getType((Command)node.leftSide());
    	Type t2 = getType((Command)node.rightSide());
    	put(node, t1.compare(t2));
    }
    
    @Override
    public void visit(Addition node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	Type t1 = getType((Command)node.leftSide());
    	Type t2 = getType((Command)node.rightSide());
    	put(node, t1.add(t2));
    }
    
    @Override
    public void visit(Subtraction node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	Type t1 = getType((Command)node.leftSide());
    	Type t2 = getType((Command)node.rightSide());
    	put(node, t1.sub(t2));
    }
    
    @Override
    public void visit(Multiplication node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	Type t1 = getType((Command)node.leftSide());
    	Type t2 = getType((Command)node.rightSide());
    	put(node, t1.mul(t2));
    }
    
    @Override
    public void visit(Division node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	Type t1 = getType((Command)node.leftSide());
    	Type t2 = getType((Command)node.rightSide());
    	put(node, t1.div(t2));
    }
    
    @Override
    public void visit(LogicalAnd node) {
        node.leftSide().accept(this);
        node.rightSide().accept(this);
        Type t1 = getType((Command)node.leftSide());
        Type t2 = getType((Command)node.rightSide());
        put(node, t1.and(t2));
    }

    @Override
    public void visit(LogicalOr node) {
    	 node.leftSide().accept(this);
         node.rightSide().accept(this);
         Type t1 = getType((Command)node.leftSide());
         Type t2 = getType((Command)node.rightSide());
         put(node, t1.or(t2));
    }

    @Override
    public void visit(LogicalNot node) {
    	 node.expression().accept(this);
         Type t = getType((Command)node.expression());
         put(node, t.not());
    }
    
    @Override
    public void visit(Dereference node) {
    	node.expression().accept(this);
    	Type addr = getType((Command)node.expression());
    	put(node, addr.deref());
    }

    @Override
    public void visit(Index node) {
//    	System.out.println("base is "+node.base());
//    	System.out.println("amount is "+node.amount());
    	node.base().accept(this);
    	node.amount().accept(this);
    	Type array_base = getType((Command) node.base());
    	Type array_index = getType((Command) node.amount());
    	Type array_value = array_base.index(array_index);
//    	if (array_value instanceof ErrorType)
//    	{
    		put(node, array_value);
//    	}
//    	else
//    	{
//    		put(node, new AddressType(array_value));
//    	}
//    	put(node, getType((Command)node.base()).index(getType((Command) node.amount())));
    }

    @Override
    public void visit(Assignment node) {
    	node.destination().accept(this);
    	node.source().accept(this);
    	Type d = getType((Command) node.destination());
    	Type s = getType((Command) node.source());
    	put(node, d.assign(s));
    }

    @Override
    public void visit(Call node) {
    	FuncType ft = (FuncType) node.function().type();
//    	System.out.println(typeMap.keySet());
//    	System.out.println(typeMap.values());
    	node.arguments().accept(this);
//    	System.out.println(getType(node.arguments()));
    	put(node, ft.call(getType(node.arguments())));
//    	for(Map.Entry<Command, Type> e : new HashSet<Map.Entry<Command, Type>>(typeMap.entrySet()))
//    	{
////    		System.out.println(e);
////    		System.out.println(((FuncType)e.getValue()).arguments());
//    		if (e.getKey() instanceof FunctionDefinition)//e.getValue() instanceof FuncType)
//    		{
//    			FunctionDefinition fd = (FunctionDefinition)e.getKey();
//    			if (fd.function().equals(node.function()))
//    			{
//    				ExpressionList el = node.arguments();
//    				el.accept(this);
//    				Type tl = getType((Command) el);
////    				System.out.println("FD symbol = call symbol: "+tl);
////    				System.out.println(((FuncType)e.getValue()).arguments());
////    				if (((FuncType)e.getValue()).arguments().equals((TypeList)tl))
////    				{
//    					put(node, e.getValue().call(tl));
////    				}
////    				else
////    				{
////    					put(node, new ErrorType("The given parameters do not match the function's parameters."));
////    				}
//    			}
//    		}
//    	}
//    	put(node, ft.call(ft.arguments()));
    }

    @Override
    public void visit(IfElseBranch node) {
    	node.condition().accept(this);
    	Type cond = getType((Command) node.condition());
    	Type b = new BoolType();
    	if (b.equivalent(cond))
    	{
    		put(node, b);
    		node.thenBlock().accept(this);
    		node.elseBlock().accept(this);
    	}
    	else
    	{
    		put(node, new ErrorType("IfElseBranch requires bool condition not " + cond + "."));
    	}
    }

    @Override
    public void visit(WhileLoop node) {
    	node.condition().accept(this);
    	Type cond = getType((Command) node.condition());
    	Type b = new BoolType();
    	if(b.equivalent(cond))
    	{
    		put(node, b);
    		node.body().accept(this);
    		Type state_body = getType(node.body());
    	}
    	else
    	{
    		put(node, new ErrorType("WhileLoop requires bool condition not " + cond + "."));
    	}
    }

    @Override
    public void visit(Return node) {
//    	node.argument().accept(this);
//    	System.out.println("in return visit: "+node);
//    	System.out.println(getType((Command) node.argument()));
    	if(check((Command)node.argument()))
    	{
    		put(node, getType((Command) node.argument()));
    	}
    	else
    	{
    		typeMap.put(node, getType((Command) node.argument()));
    	}
    }

    @Override
    public void visit(ast.Error node) {
        put(node, new ErrorType(node.message()));
    }
    
    private Type realBaseType(ArrayType at)//, int i, Type t)
    {
    	if (!(at.base() instanceof ArrayType))
    	{
    		return at.base();
    	}
    	else
    	{
    		return realBaseType((ArrayType)at.base());//, i, t);
    	}
    }
    
    private Type checkpaths(StatementList sl, String funcname, Type funcreturn)
    {
    	Type t1 = new ErrorType("");
    	Type e1 = new ErrorType("");
    	Type t2 = new ErrorType("");
    	Type e2 = new ErrorType("");
    	Type r = new ErrorType("");
    	Return if_return = i_return;
    	Return else_return = e_return;
    	Return body_return = b_return;
    	Return first_return = null;
    	for (Statement s : sl)
    	{
    		if (s instanceof IfElseBranch)
    		{
    			t2 = checkpaths(((IfElseBranch) s).thenBlock(), funcname, funcreturn);
    			if_return = b_return;
    			e2 = checkpaths(((IfElseBranch) s).elseBlock(), funcname, funcreturn);
    			else_return = b_return;
    			if (t2.equivalent(e2))
    			{
    				t1 = t2;
    				e1 = e2;
    			}
    		}
    		else if((s instanceof Return) && (r instanceof ErrorType))
    		{
    			r = getType((Command) s);
    			b_return = (Return) s;
    			body_return = b_return;
    		}
    	}

    	if (body_return == null)
    	{
    		if (if_return == null)
    		{
    			b_return = null;
    		}
    		else
    		{
    			b_return = if_return;
    		}
    	}
    	else
    	{
    		b_return = body_return;
    	}
    	
//    	if (r instanceof ErrorType && ((ErrorType) r).getMessage().equals(""))
//    	{
//    		return new ErrorType("Not all paths in function " + funcname + " have a return.");
//    	}
    	if((r instanceof ErrorType && ((ErrorType) r).getMessage().equals("")) && !(t1.equivalent(e1)) && ((!(t1 instanceof ErrorType) && (e1 instanceof ErrorType)) || ((t1 instanceof ErrorType) && !(e1 instanceof ErrorType))))
    	{
    		return new ErrorType("Not all paths in function " + funcname + " have a return.");
    	}
    	else if ((r instanceof ErrorType && ((ErrorType) r).getMessage().equals("")) && (t1.equivalent(e1) && !(t1 instanceof ErrorType) && funcreturn.equivalent(t1)))
    	{
    		return t1;
    	}
    	else if((!(r instanceof ErrorType) && funcreturn.equivalent(r)))
    	{
    		return r;
    	}
    	else
    	{
    		if (!(funcreturn.equivalent(t1)) && (funcreturn.equivalent(e1)) && funcreturn.equivalent(r))
    		{
    			return t1;//new ErrorType("Function " + funcname + " returns " + funcreturn + " not " + t1 + ".");
    		}
    		else if(funcreturn.equivalent(t1) && !(funcreturn.equivalent(e1)) && funcreturn.equivalent(r))
    		{
    			return e1;//new ErrorType("Function " + funcname + " returns " + funcreturn + " not " + e1 + ".");
    		}
    		else if(funcreturn.equivalent(r) || !(funcreturn.equivalent(r) && !(r instanceof ErrorType)))// && !(r instanceof ))
    		{
    			System.out.println("hahahaha "+r);
    			return r;//new ErrorType("Function " + funcname + " returns " + funcreturn + " not " + r + ".");
    		}
    		else
    		{
//    			System.out.println("hahaha"+r);
    			if(r instanceof ErrorType && ((ErrorType) r).getMessage().equals(""))
    			{
    				return new ErrorType(".");
    			}
    			else
    			{
    				return r;//new ErrorType("");
    			}
    		}
    	}
    }
    
//    private Type checkReturn(StatementList sl, String funcname, Type funcreturn)
//    {
//    	Type t1 = new ErrorType("");
//    	Type e1 = new ErrorType("");
//    	Type t2 = new ErrorType("");
//    	Type e2 = new ErrorType("");
//    	Type r = new ErrorType("");
//    	for (Statement s : sl)
//    	{
//    		if (s instanceof IfElseBranch)
//    		{
//    			t2 = checkpaths(((IfElseBranch) s).thenBlock(), funcname, funcreturn);
//    			e2 = checkpaths(((IfElseBranch) s).elseBlock(), funcname, funcreturn);
//    			if (t2.equivalent(e2))
//    			{
//    				t1 = t2;
//    				e1 = e2;
//    			}
//    		}
//    		else if((s instanceof Return) && (r instanceof ErrorType))
//    		{
//    			r = getType((Command) s);
//    		}
//    	}
//
//    	if (r instanceof ErrorType)
//    	{
//    		return new ErrorType("Not all paths in function " + funcname + " have a return.");
//    	}
//    	else if(!(t1.equivalent(e1)) && ((!(t1 instanceof ErrorType) && (e1 instanceof ErrorType)) || ((t1 instanceof ErrorType) && !(e1 instanceof ErrorType))))
//    	{
//    		return new ErrorType("Not all paths in function " + funcname + " have a return.");
//    	}
//    	else if ((t1.equivalent(e1) && !(t1 instanceof ErrorType) && funcreturn.equivalent(t1)))
//    	{
//    		return t1;
//    	}
//    	else if((!(r instanceof ErrorType) && funcreturn.equivalent(r)))
//    	{
//    		return r;
//    	}
//    	else
//    	{
//    		if (!(funcreturn.equivalent(t1)) && funcreturn.equivalent(r))
//    		{
//    			return new ErrorType("Function " + funcname + " returns " + funcreturn + " not " + t1 + ".");
//    		}
//    		else if(funcreturn.equivalent(t1) && !(funcreturn.equivalent(e1)) && funcreturn.equivalent(r))
//    		{
//    			return new ErrorType("Function " + funcname + " returns " + funcreturn + " not " + e1 + ".");
//    		}
//    		else
//    		{
//    			return new ErrorType("Function " + funcname + " returns " + funcreturn + " not " + r + ".");
//    		}
//    	}
//    }
}
