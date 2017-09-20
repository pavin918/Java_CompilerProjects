package crux;
//i added these imports
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.ListIterator;

public class SymbolTable {
	
	private ArrayList<HashMap<String,Symbol>> parent;
	private int depth;
    
    public SymbolTable()
    {
    	this.parent = new ArrayList<HashMap<String,Symbol>>();
    	this.depth = parent.size();
        throw new RuntimeException("implement this");
    }
    
    public Symbol lookup(String name) throws SymbolNotFoundError
    {
    	for(HashMap<String,Symbol> hm : parent)
    	{
    		if(hm.containsKey(name))
    			return hm.get(name);
    	}
    	throw new SymbolNotFoundError(name);
//    	if(LN.element().containsKey(name))
//    		return LN.element().get(name);
//    	else
    		
//        throw new RuntimeException("implement this");
    }
       
    public Symbol insert(String name) throws RedeclarationError
    {
    	Symbol test = new Symbol(name);
//    	HashMap<String,Symbol> use = new HashMap<String,Symbol>();
//    	use.put(name, test);
    	parent.get(0).put(name, test);
    	return test;
//        throw new RuntimeException("implement this");
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (!parent.isEmpty()/*I have a parent table*/)
            sb.append(parent.toString());
        
        String indent = new String();
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        
        for (Symbol s : parent.get(0).values()/*Every symbol, s, in this table*/)
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
