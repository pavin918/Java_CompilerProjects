package mips;

import java.util.HashMap;

import crux.Symbol;
import types.*;

public class ActivationRecord
{
    private static int fixedFrameSize = 2*4;
    private ast.FunctionDefinition func;
    private ActivationRecord parent;
    private int stackSize;
    private HashMap<Symbol, Integer> locals;
    private HashMap<Symbol, Integer> arguments;
    
    public static ActivationRecord newGlobalFrame()
    {
        return new GlobalFrame();
    }
    
    protected static int numBytes(Type type)
    {
    	if (type instanceof BoolType)
    		return 4;
        if (type instanceof IntType)
            return 4;
        if (type instanceof FloatType)
            return 4;
        if (type instanceof ArrayType) {
            ArrayType aType = (ArrayType)type;
            return aType.extent() * numBytes(aType.base());
        }
        throw new RuntimeException("No size known for " + type);
    }
    
    protected ActivationRecord()
    {
        this.func = null;
        this.parent = null;
        this.stackSize = 0;
        this.locals = null;
        this.arguments = null;
    }
    
    public ActivationRecord(ast.FunctionDefinition fd, ActivationRecord parent)
    {
        this.func = fd;
        this.parent = parent;
        this.stackSize = 0;
        this.locals = new HashMap<Symbol, Integer>();
        
        // map this function's parameters
        this.arguments = new HashMap<Symbol, Integer>();
        int offset = 0;
        for (int i=fd.arguments().size()-1; i>=0; --i) {
            Symbol arg = fd.arguments().get(i);
            arguments.put(arg, offset);
            offset += numBytes(arg.type());
        }
    }
    
    public String name()
    {
        return func.symbol().name();
    }
    
    public ActivationRecord parent()
    {
        return parent;
    }
    
    public int stackSize()
    {
        return stackSize;
    }
    
    public void add(Program prog, ast.VariableDeclaration var)
    {
    	Symbol v = var.symbol();
    	int i = numBytes(v.type());
    	this.stackSize += i;
    	this.locals.put(v, this.stackSize);
//    	prog.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
//        throw new RuntimeException("implement adding variable to local function space");
    }
    
    public void add(Program prog, ast.ArrayDeclaration array)
    {
    	Symbol a = array.symbol();
    	int i = numBytes(a.type());
    	this.stackSize += i;
    	this.locals.put(a, this.stackSize);
//    	prog.appendInstruction("subu    $sp, $sp, " + Integer.toString(i));
//        throw new RuntimeException("implement adding array to local function space");
    }
    
    public void getAddress(Program prog, String reg, Symbol sym)
    {
    	if (this.locals.containsKey(sym))
    	{
    		prog.appendInstruction("addi    " + reg + ", $fp, " + Integer.toString(-8 - this.locals.get(sym)));
    		prog.appendInstruction("subu    $sp, $sp, 4");
    		prog.pushInt(reg);
    	}
    	else if(this.arguments.containsKey(sym))
    	{
    		prog.appendInstruction("addi    " + reg + ", $fp, " + Integer.toString(this.arguments.get(sym)));
    		prog.appendInstruction("subu    $sp, $sp, 4");
    		prog.pushInt(reg);
    	}
    	else
    	{
    		parent.getAddress(prog, reg, sym);
    	}
//        throw new RuntimeException("implement accessing address of local or parameter symbol");
    }
}

class GlobalFrame extends ActivationRecord
{
    public GlobalFrame()
    {
    }
    
    private String mangleDataname(String name)
    {
        return "cruxdata." + name;
    }
    
    @Override
    public void add(Program prog, ast.VariableDeclaration var)
    {
    	Symbol v = var.symbol();
    	int i = numBytes(v.type());
    	prog.appendData(mangleDataname(var.symbol().name()) + ": .space " + Integer.toString(i));
//    	prog.appendInstruction("subu    $gp, $gp, " + Integer.toString(i));
//    	this.stackSize += i;
//    	this.locals.put(v, i);
//        throw new RuntimeException("implement adding variable to global data space");
    }    
    
    @Override
    public void add(Program prog, ast.ArrayDeclaration array)
    {
    	Symbol v = array.symbol();
    	int i = numBytes(v.type());
    	prog.appendData(mangleDataname(array.symbol().name()) + ": .space " + Integer.toString(i));
//    	prog.appendInstruction("subu    $gp, $gp, " + Integer.toString(i));
//        throw new RuntimeException("implement adding array to global data space");
    }
        
    @Override
    public void getAddress(Program prog, String reg, Symbol sym)
    {
    	prog.appendInstruction("la    " + reg + ", " + mangleDataname(sym.name()));
    	prog.appendInstruction("subu    $sp, $sp, 4");
		prog.pushInt(reg);
//        throw new RuntimeException("implement accessing address of global symbol");
    }
}
