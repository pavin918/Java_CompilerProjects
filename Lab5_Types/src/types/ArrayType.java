package types;

public class ArrayType extends Type {
    
    private Type base;
    private int extent;
    
    public ArrayType(int extent, Type base)
    {
//        throw new RuntimeException("implement operators");
        this.extent = extent;
        this.base = base;
    }
    
    public int extent()
    {
        return extent;
    }
    
    public Type base()
    {
        return base;
    }
    
    public void setBase(Type that)
    {
    	base = that;
    }
    
    @Override
    public String toString()
    {
        return "array[" + extent + "," + base + "]";
    }
    
    @Override
    public Type index(Type that)
    {
    	if (!(that instanceof IntType))
    		return super.index(that);
    	return base;
    }
    
    @Override
    public Type assign(Type that)
    {
    	if (!(that instanceof ArrayType))
    		return super.assign(that);
    	return new ArrayType(extent, base);
    }
    
    @Override
    public boolean equivalent(Type that)
    {
        if (that == null)
            return false;
        if (!(that instanceof IntType))
            return false;
        
        ArrayType aType = (ArrayType)that;
        return this.extent == aType.extent && base.equivalent(aType.base);
    }
}
