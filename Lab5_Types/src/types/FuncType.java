package types;

public class FuncType extends Type {
   
   private TypeList args;
   private Type ret;
   
   public FuncType(TypeList args, Type returnType)
   {
//      throw new RuntimeException("implement operators");
      this.args = args;
      this.ret = returnType;
   }
   
   public Type returnType()
   {
      return ret;
   }
   
   public TypeList arguments()
   {
      return args;
   }
   
   @Override
   public String toString()
   {
      return "func(" + args + "):" + ret;
   }

   @Override
   public Type call(Type arguments) {
       if (!(args.equivalent(arguments)))// instanceof TypeList))
           return super.call(arguments);
       return ret;
   }
   
   @Override
   public Type assign(Type that)
   {
   	if (!(that instanceof FuncType))
   		return super.assign(that);
   	return new FuncType(args, ret);
   }
   
   @Override
   public boolean equivalent(Type that)
   {
      if (that == null)
         return false;
      if (!(that instanceof FuncType))
         return false;
      
      FuncType aType = (FuncType)that;
      return this.ret.equivalent(aType.ret) && this.args.equivalent(aType.args);
   }
}
