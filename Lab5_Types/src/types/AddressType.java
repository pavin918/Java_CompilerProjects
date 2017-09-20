package types;

public class AddressType extends Type {
    
    private Type base;
    
    public AddressType(Type base)
    {
//        throw new RuntimeException("implement operators");
        this.base = base;
    }
    
    public Type base()
    {
        return base;
    }

    @Override
    public String toString()
    {
        return "Address(" + base + ")";
    }
    
    @Override
    public Type index(Type that)
    {
    	if (!(that instanceof IntType) || !(base instanceof ArrayType))
    		return super.index(that);
    	return new AddressType(base.index(that));
    }

    @Override
    public Type deref()
    {
    	return base;
    }
    
    @Override
    public Type assign(Type that)
    {
    	if(that.equivalent(base))
    	{
    		return base;
    	}
    	if (!(that instanceof AddressType))
    	{
    		return super.assign(that);
    	}
    	else
    	{
    		return new AddressType(base);
    	}
    }
    
    @Override
    public boolean equivalent(Type that) {
        if (that == null)
            return false;
        if (!(that instanceof AddressType))
            return false;
        
        AddressType aType = (AddressType)that;
        return this.base.equivalent(aType.base);
    }
}
