package crux;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SymbolTable {
	
	private LinkedHashMap<String, Symbol> current;
	private SymbolTable parent;
	private int depth;
    
    public SymbolTable()
    {
    	this.current = new LinkedHashMap<String, Symbol>();//new ArrayList<LinkedHashMap<String,Symbol>>();
    	this.parent = null;
    	this.depth = current.size();
    	
    	this.insert("readInt");
    	current.get("readInt").setType(new types.FuncType(new types.TypeList(), new types.IntType()));
    	
    	this.insert("readFloat");
    	current.get("readFloat").setType(new types.FuncType(new types.TypeList(),new types.FloatType()));
    	
    	this.insert("printBool");
    	types.TypeList blist = new types.TypeList();
    	blist.append(new types.BoolType());
    	current.get("printBool").setType(new types.FuncType(blist,new types.VoidType()));
    	
    	this.insert("printInt");
    	types.TypeList ilist = new types.TypeList();
    	ilist.append(new types.IntType());
    	current.get("printInt").setType(new types.FuncType(ilist,new types.VoidType()));
    	
    	this.insert("printFloat");
    	types.TypeList flist = new types.TypeList();
    	flist.append(new types.FloatType());
    	current.get("printFloat").setType(new types.FuncType(flist,new types.VoidType()));
    	
    	this.insert("println");
    	current.get("println").setType(new types.FuncType(new types.TypeList(),new types.VoidType()));
    }
    
    public SymbolTable(SymbolTable par)
    {
    	this.current = new LinkedHashMap<String, Symbol>();//new ArrayList<LinkedHashMap<String,Symbol>>();
    	this.parent = par;
    	this.depth = par.get_depth() + 1;
    }
    
    public Symbol lookup(String name) throws SymbolNotFoundError
    {
    	if(current.containsKey(name))
    	{
    		return current.get(name);
    	}
    	if(parent==null)
    		throw new SymbolNotFoundError(name);
    	else
    		return parent.lookup(name);
    }
       
    public Symbol insert(String name) throws RedeclarationError
    {
    	Symbol test = new Symbol(name);
    	if(current.containsKey(name))
    	{
    		throw new RedeclarationError(test);
    	}
    	current.put(name, test);
    	return test;
    }
    
    public int add_scope()
    {
    	return ++depth;
    }
    
    public int sub_scope()
    {
    	return --depth;
    }
    
    public int get_depth()
    {
    	return depth;
    }
    
    public void set_depth()
    {
    	depth = parent.get_depth() + 1;
    }
    
    public SymbolTable getparent()
    {
    	return parent;
    }
    
    public void addparent(SymbolTable st)
    {
    	parent = st;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (parent != null/*I have a parent table*/)
            sb.append(parent.toString());
        
        String indent = new String();
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        
        for (Symbol s : current.values()/*Every symbol, s, in this table*/)
        {
            sb.append(indent + s.toString() + "\n");
        }
        return sb.toString();
    }
}

class SymbolNotFoundError extends Error
{
    private static final long serialVersionUID = 1L;
    private String name;
    
    SymbolNotFoundError(String name)
    {
        this.name = name;
    }
    
    public String name()
    {
        return name;
    }
}

class RedeclarationError extends Error
{
    private static final long serialVersionUID = 1L;

    public RedeclarationError(Symbol sym)
    {
        super("Symbol " + sym + " being redeclared.");
    }
}
