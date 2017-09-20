package crux;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ast.AddressOf;
import ast.Command;
//import crux.Parser.QuitParseException;
import types.*;

public class Parser {
    public static String studentName = "Paul Dao";
    public static String studentID = "";
    public static String uciNetID = "";
  
    // Grammar Rule Reporting ==========================================
    private int parseTreeRecursionDepth = 0;
    private StringBuffer parseTreeBuffer = new StringBuffer();

    public void enterRule(NonTerminal nonTerminal) {
        String lineData = new String();
        for(int i = 0; i < parseTreeRecursionDepth; i++)
        {
            lineData += "  ";
        }
        lineData += nonTerminal.name();
        parseTreeBuffer.append(lineData + "\n");
        parseTreeRecursionDepth++;
    }
    
    private void exitRule(NonTerminal nonTerminal)
    {
        parseTreeRecursionDepth--;
    }
    
    public String parseTreeReport()
    {
        return parseTreeBuffer.toString();
    }
    
 // SymbolTable Management ==========================
    private SymbolTable symbolTable;
    
    private void initSymbolTable()
    {
    	symbolTable = new SymbolTable();
    }
    
    private void enterScope()
    {
    	symbolTable = new SymbolTable(symbolTable);
    }
    
    private void exitScope()
    {
    	symbolTable = symbolTable.getparent();
    }

 // Error Reporting ==========================================
    private StringBuffer errorBuffer = new StringBuffer();
    
    private String reportSyntaxError(NonTerminal nt)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected a token from " + nt.name() + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
     
    private String reportSyntaxError(Token.Kind kind)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected " + kind + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    private class QuitParseException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        public QuitParseException(String errorMessage) {
            super(errorMessage);
        }
    }
    
    private int lineNumber()
    {
        return currentToken.lineNumber();
    }
    
    private int charPosition()
    {
        return currentToken.charPosition();
    }
    
    private Symbol tryResolveSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.lookup(name);
        } catch (SymbolNotFoundError e) {
            String message = reportResolveSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportResolveSymbolError(String name, int lineNum, int charPos)
    {
        String message = "ResolveSymbolError(" + lineNum + "," + charPos + ")[Could not find " + name + ".]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }

    private Symbol tryDeclareSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.insert(name);
        } catch (RedeclarationError re) {
            String message = reportDeclareSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportDeclareSymbolError(String name, int lineNum, int charPos)
    {
        String message = "DeclareSymbolError(" + lineNum + "," + charPos + ")[" + name + " already exists.]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }    
    
    
// Parser ==========================================
    private Scanner scanner;
    private Token currentToken;
   
    public Parser(Scanner scanner)
    {
    	this.scanner = scanner;
    	this.currentToken = this.scanner.next();
    }
    
    public ast.Command parse()
    {
        initSymbolTable();
        try {
            return program();
        } catch (QuitParseException q) {
            return new ast.Error(lineNumber(), charPosition(), "Could not complete parsing.");
        }
    }
    
// Helper Methods ==========================================
    private boolean have(Token.Kind kind)
    {
        return currentToken.is(kind);
    }
    
    private boolean have(NonTerminal nt)
    {
        return nt.firstSet().contains(currentToken.kind());
    }
    
    private boolean accept(Token.Kind kind)
    {
        if (have(kind)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }    
    
    private boolean accept(NonTerminal nt)
    {
        if (have(nt)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }

    private boolean expect(Token.Kind kind)
    {
        if (accept(kind))
            return true;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return false;
    }
        
    private boolean expect(NonTerminal nt)
    {
        if (accept(nt))
            return true;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return false;
    }
     
    private Token expectRetrieve(Token.Kind kind)
    {
        Token tok = currentToken;
        if (accept(kind))
            return tok;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
        
    private Token expectRetrieve(NonTerminal nt)
    {
        Token tok = currentToken;
        if (accept(nt))
            return tok;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
   
// Grammar Rules =====================================================
    
    // literal := INTEGER | FLOAT | TRUE | FALSE .
    public ast.Expression literal()
    {
        ast.Expression expr;
        enterRule(NonTerminal.LITERAL);
        
        Token tok = expectRetrieve(NonTerminal.LITERAL);
        expr = Command.newLiteral(tok);
        
        exitRule(NonTerminal.LITERAL);
        return expr;
    }
    
 // designator := IDENTIFIER { "[" expression0 "]" } .
    public ast.Expression designator()
    {
        Token ident = expectRetrieve(Token.Kind.IDENTIFIER);
        Symbol identify = tryResolveSymbol(ident);
        ast.Expression base = new AddressOf(ident.lineNumber(), ident.charPosition(), identify);
        while (accept(Token.Kind.OPEN_BRACKET)) {
        	Token curr = currentToken;
            ast.Expression index = expression0();
            expect(Token.Kind.CLOSE_BRACKET);
            base = new ast.Index(curr.lineNumber(), curr.charPosition(), base, index);
        }
//        identify.setType(new AddressType());
        return base;//new AddressOf(ident.lineNumber(), ident.charPosition(), identify);
    }

    //type := IDENTIFIER .
    public Type type()
    {
    	Token tok = expectRetrieve(Token.Kind.IDENTIFIER);
    	return tryResolveType(tok.lexeme());
    }
    
    //op0 := ">=" | "<=" | "!=" | "==" | ">" | "<" .
    public Token op0()
    {
    	Token tok = expectRetrieve(NonTerminal.OP0);
    	return tok;
    }
    
    //op1 := "+" | "-" | "or" .
    public Token op1()
    {
    	Token tok = expectRetrieve(NonTerminal.OP1);
    	return tok;
    }
    
    //op2 := "*" | "/" | "and" .
    public Token op2()
    {
    	Token tok = expectRetrieve(NonTerminal.OP2);
    	return tok;
    }
    
    //expression0 := expression1 [ op0 expression1 ] .
    public ast.Expression expression0()
    {
    	ast.Expression left = expression1();
    	if(have(NonTerminal.OP0))
    	{
    		Token op0 = op0();
    		ast.Expression right = expression1();
    		left = Command.newExpression(left, op0, right);
    	}
    	return left;
    }
    
    //expression1 := expression2 { op1  expression2 } .
    public ast.Expression expression1()
    {
    	ast.Expression left = expression2();
    	while(have(NonTerminal.OP1))
    	{
    		Token op1 = op1();
    		ast.Expression right = expression2();
    		left = Command.newExpression(left,  op1,  right);
    	}
//    	ast.Expression expr1 = Command.newExpression(left, op1, right);
    	return left;
    }
    
    //expression2 := expression3 { op2 expression3 } .
    public ast.Expression expression2()
    {
    	ast.Expression left = expression3();
    	while(have(NonTerminal.OP2))
    	{
    		Token op2 = op2();
    		ast.Expression right = expression3();
    		left = Command.newExpression(left, op2, right);
    	}
//    	ast.Expression expr2 = Command.newExpression(left, op2, right);
    	return left;
    }
    
    //expression3 := "not" expression3 | "(" expression0 ")" | designator | call-expression | literal .
    public ast.Expression expression3()
    {
    	if(have(Token.Kind.NOT))
    	{
    		Token tok = expectRetrieve(Token.Kind.NOT);
    		ast.Expression expr3 = expression3();
    		return Command.newExpression(expr3, tok, expr3);
    	}
    	else if(have(Token.Kind.OPEN_PAREN))
    	{
    		expect(Token.Kind.OPEN_PAREN);
    		ast.Expression expr0 = expression0();
    		expect(Token.Kind.CLOSE_PAREN);
    		return expr0;
    	}
    	else if(have(NonTerminal.DESIGNATOR))
    	{
    		Token curr = currentToken;
    		ast.Expression desig = designator();
    		return new ast.Dereference(curr.lineNumber(), curr.charPosition(), desig);
    	}
    	else if(have(NonTerminal.CALL_EXPRESSION))
    	{
    		return call_expression();
    	}
    	else
    	{
    		return literal();
    	}
    }
    
    //call-expression := "::" IDENTIFIER "(" expression-list ")" .
    public ast.Call call_expression()
    {
    	Token call = expectRetrieve(Token.Kind.CALL);
    	Token ident = expectRetrieve(Token.Kind.IDENTIFIER);
    	Symbol id = tryResolveSymbol(ident);
    	expect(Token.Kind.OPEN_PAREN);
    	ast.ExpressionList explist = expression_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	return new ast.Call(call.lineNumber(), call.charPosition(), id, explist);
    }
    
    //expression-list := [ expression0 { "," expression0 } ] .
    public ast.ExpressionList expression_list()
    {
    	ast.ExpressionList explist = new ast.ExpressionList(currentToken.lineNumber(), currentToken.charPosition());
    	if(have(NonTerminal.EXPRESSION0))
    	{
    		explist.add(expression0());
        	while(accept(Token.Kind.COMMA))
        	{
        		explist.add(expression0());
        	}
    	}
    	return explist;
    }
    
    //parameter := IDENTIFIER ":" type .
    public Symbol parameter()
    {
    	Token par = expectRetrieve(Token.Kind.IDENTIFIER);
    	Symbol param = tryDeclareSymbol(par);
    	expect(Token.Kind.COLON);
    	param.setType(type());
    	return param;
    }
    
    //parameter-list := [ parameter { "," parameter } ] .
    public List<Symbol> parameter_list()
    {
    	List<Symbol> parlist = new ArrayList<Symbol>();
    	if(have(NonTerminal.PARAMETER))
    	{
    		parlist.add(parameter());
        	while(accept(Token.Kind.COMMA))
        	{
        		parlist.add(parameter());
        	}
    	}
    	return parlist;
    }
    
    //variable-declaration := "var" IDENTIFIER ":" type ";"
    public ast.VariableDeclaration variable_declaration()
    {
    	Token variable = expectRetrieve(Token.Kind.VAR);
    	Symbol identifier = tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
    	identifier.setType(type());
    	expect(Token.Kind.SEMICOLON);
    	return new ast.VariableDeclaration(variable.lineNumber(), variable.charPosition(), identifier);
    }
    
    //array-declaration := "array" IDENTIFIER ":" type "[" INTEGER "]" { "[" INTEGER "]" } ";"
    public ast.ArrayDeclaration array_declaration()
    {
    	Token array = expectRetrieve(Token.Kind.ARRAY);
    	Symbol array_name = tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
    	Type t = type();
    	expect(Token.Kind.OPEN_BRACKET);
    	Token extent = expectRetrieve(Token.Kind.INTEGER);
    	expect(Token.Kind.CLOSE_BRACKET);
    	int i = Integer.parseInt(extent.lexeme());
    	array_name.setType(new ArrayType(i, t));
    	while(accept(Token.Kind.OPEN_BRACKET))
    	{
    		extent = expectRetrieve(Token.Kind.INTEGER);
    		expect(Token.Kind.CLOSE_BRACKET);
    		i = Integer.parseInt(extent.lexeme());
    		addBaseType(((ArrayType) array_name.type()), i, t);
//    		((ArrayType)array_name.type()).setBase(new ArrayType(i, t));
//        	array_name.setType(new ArrayType(i, array_name.type()));
    	}
    	expect(Token.Kind.SEMICOLON);
    	return new ast.ArrayDeclaration(array.lineNumber(), array.charPosition(), array_name);
    }
    
    //function-definition := "func" IDENTIFIER "(" parameter-list ")" ":" type statement-block .
    public ast.FunctionDefinition function_definition()
    {
    	Token func = expectRetrieve(Token.Kind.FUNC);
    	Symbol func_name = tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	enterScope();
    	expect(Token.Kind.OPEN_PAREN);
    	List<Symbol> parlist = parameter_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	TypeList tl = new TypeList();
    	for(Symbol s : parlist)
    	{
    		tl.append(s.type());
    	}
//    	System.out.println("TypeList: "+tl);
    	expect(Token.Kind.COLON);
    	func_name.setType(new FuncType(tl, type()));
//    	System.out.println("in function_definition(): " + func_name.type());
    	ast.StatementList func_body = statement_block();
    	exitScope();
    	return new ast.FunctionDefinition(func.lineNumber(), func.charPosition(), func_name, parlist, func_body);
    }
    
    //declaration := variable-declaration | array-declaration | function-definition .
    public ast.Declaration declaration()
    {
    	if(have(NonTerminal.VARIABLE_DECLARATION))
    	{
    		return variable_declaration();
    	}
    	else if(have(NonTerminal.ARRAY_DECLARATION))
    	{
    		return array_declaration();
    	}
    	else
    	{
    		return function_definition();
    	}
    }
    
    //declaration-list := { declaration } .
    public ast.DeclarationList declaration_list()
    {
    	ast.DeclarationList declist = new ast.DeclarationList(currentToken.lineNumber(), currentToken.charPosition());
    	while(have(NonTerminal.DECLARATION))
    	{
    		declist.add(declaration());
    	}
    	return declist;
    }
    
    //assignment-statement := "let" designator "=" expression0 ";"
    public ast.Assignment assignment_statement()
    {
    	Token let = expectRetrieve(Token.Kind.LET);
//    	tryResolveSymbol(currentToken);
    	ast.Expression designator = designator();
    	expect(Token.Kind.ASSIGN);
    	ast.Expression expr0 = expression0();
    	expect(Token.Kind.SEMICOLON);
    	return new ast.Assignment(let.lineNumber(), let.charPosition(), designator, expr0);
    }
    
    //call-statement := call-expression ";"
    public ast.Call call_statement()
    {
    	ast.Call call = call_expression();
    	expect(Token.Kind.SEMICOLON);
    	return call;
    }
    
    //if-statement := "if" expression0 statement-block [ "else" statement-block ] .
    public ast.IfElseBranch if_statement()
    {
    	Token if_tok = expectRetrieve(Token.Kind.IF);
    	enterScope();
    	ast.Expression condition = expression0();
    	ast.StatementList then_block = statement_block();
    	exitScope();
    	ast.StatementList else_block = new ast.StatementList(currentToken.lineNumber(), currentToken.charPosition());
    	if(accept(Token.Kind.ELSE))
    	{
    		enterScope();
    		else_block = statement_block();
    		exitScope();
    	}
    	return new ast.IfElseBranch(if_tok.lineNumber(), if_tok.charPosition(), condition, then_block, else_block);
    }
    
    //while-statement := "while" expression0 statement-block .
    public ast.WhileLoop while_statement()
    {
    	Token while_tok = expectRetrieve(Token.Kind.WHILE);
    	enterScope();
    	ast.Expression condition = expression0();
    	ast.StatementList while_body = statement_block();
    	exitScope();
    	return new ast.WhileLoop(while_tok.lineNumber(), while_tok.charPosition(), condition, while_body);
    }
    
    //return-statement := "return" expression0 ";" .
    public ast.Return return_statement()
    {
    	Token return_tok = expectRetrieve(Token.Kind.RETURN);
    	ast.Expression expr = expression0();
    	expect(Token.Kind.SEMICOLON);
    	return new ast.Return(return_tok.lineNumber(), return_tok.charPosition(), expr);
    }
    
    //statement := variable-declaration | call-statement | assignment-statement | if-statement | while-statement | return-statement .
    public ast.Statement statement()
    {
    	if(have(NonTerminal.VARIABLE_DECLARATION))
    	{
    		return variable_declaration();
    	}
    	else if(have(NonTerminal.CALL_STATEMENT))
    	{
    		return call_statement();
    	}
    	else if(have(NonTerminal.ASSIGNMENT_STATEMENT))
    	{
    		return assignment_statement();
    	}
    	else if(have(NonTerminal.IF_STATEMENT))
    	{
    		return if_statement();
    	}
    	else if(have(NonTerminal.WHILE_STATEMENT))
    	{
    		return while_statement();
    	}
    	else
    	{
    		return return_statement();
    	}
    }

    //statement-list := { statement } .
    public ast.StatementList statement_list()
    {
    	ast.StatementList sl = new ast.StatementList(currentToken.lineNumber(), currentToken.charPosition());
    	while(have(NonTerminal.STATEMENT))
    	{
    		sl.add(statement());
    	}
    	return sl;
    }
    
    //statement-block := "{" statement-list "}" .
    public ast.StatementList statement_block()
    {
    	ast.StatementList sl = new ast.StatementList(currentToken.lineNumber(), currentToken.charPosition());
    	if(accept(Token.Kind.OPEN_BRACE))
    	{
    		sl = statement_list();
    		expect(Token.Kind.CLOSE_BRACE);
    	}
    	return sl;
    }


    // program := declaration-list EOF .
    public ast.DeclarationList program()
    {
    	return declaration_list();
    }
    
    
// Typing System ===================================
    
    private Type tryResolveType(String typeStr)
    {
        return Type.getBaseType(typeStr);
    }
        
    private void addBaseType(ArrayType at, int i, Type t)
    {
    	if (!(at.base() instanceof ArrayType))
    	{
    		at.setBase(new ArrayType(i, t));
    	}
    	else
    	{
    		addBaseType((ArrayType)at.base(), i, t);
    	}
    }
}
