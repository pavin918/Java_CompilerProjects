package crux;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ast.Command;

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
        //System.out.println("descending " + lineData);
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
    public void designator()
    {
        expect(Token.Kind.IDENTIFIER);
        while (accept(Token.Kind.OPEN_BRACKET)) {
            expression0();
            expect(Token.Kind.CLOSE_BRACKET);
        }        
    }
    
    //type := IDENTIFIER .
    public void type()
    {
    	expect(Token.Kind.IDENTIFIER);
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
    	Token op0;
    	ast.Expression right;
    	if(have(NonTerminal.OP0))
    	{
    		op0 = op0();
    		right = expression1();
    	}
    	ast.Expression expr0 = Command.newExpression(left, op0, right);
    	return expr0;
    }
    
    //expression1 := expression2 { op1  expression2 } .
    public ast.Expression expression1()
    {
    	ast.Expression left = expression2();
    	Token op1;
    	ast.Expression right;
    	while(have(NonTerminal.OP1))
    	{
    		op1 = op1();
    		right = expression2();
    	}
    	ast.Expression expr1 = Command.newExpression(left, op1, right);
    	return expr1;
    }
    
    //expression2 := expression3 { op2 expression3 } .
    public ast.Expression expression2()
    {
    	ast.Expression left = expression3();
    	Token op2;
    	ast.Expression right;
    	while(have(NonTerminal.OP2))
    	{
    		op2 = op2();
    		right = expression3();
    	}
    	ast.Expression expr2 = Command.newExpression(left, op2, right);
    	return expr2;
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
    		designator();
    	}
    	else if(have(NonTerminal.CALL_EXPRESSION))
    	{
    		call_expression();
    	}
    	else
    	{
    		return literal();
    	}
    }
    
    //call-expression := "::" IDENTIFIER "(" expression-list ")" .
    public void call_expression()
    {
    	Token call = expectRetrieve(Token.Kind.CALL);
    	Token ident = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryResolveSymbol(ident);
    	expect(Token.Kind.OPEN_PAREN);
    	ast.ExpressionList explist = expression_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	Call(call.lineNumber(), call.charPosition(), ident, explist);
    }
    
    //expression-list := [ expression0 { "," expression0 } ] .
    public void expression_list()
    {
    	if(have(NonTerminal.EXPRESSION0))
    	{
    		ast.Expression expr0 = expression0();
    		ast.ExpressionList explist = ast.ExpressionList(expr0.lineNumber(), expr0.charPosition());
        	while(accept(Token.Kind.COMMA))
        	{
        		expression0();
        	}
    	}
    }
    
    //parameter := IDENTIFIER ":" type .
    public void parameter()
    {
    	tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
    	type();
    }
    
    //parameter-list := [ parameter { "," parameter } ] .
    public void parameter_list()
    {
    	if(have(NonTerminal.PARAMETER))
    	{
    		parameter();
        	while(accept(Token.Kind.COMMA))
        	{
        		parameter();
        	}
    	}
    }
    
    //variable-declaration := "var" IDENTIFIER ":" type ";"
    public void variable_declaration()
    {
    	expect(Token.Kind.VAR);
    	tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
    	type();
    	expect(Token.Kind.SEMICOLON);
    }
    
    //array-declaration := "array" IDENTIFIER ":" type "[" INTEGER "]" { "[" INTEGER "]" } ";"
    public void array_declaration()
    {
    	expect(Token.Kind.ARRAY);
    	tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
    	type();
    	expect(Token.Kind.OPEN_BRACKET);
    	expect(Token.Kind.INTEGER);
    	expect(Token.Kind.CLOSE_BRACKET);
    	while(accept(Token.Kind.OPEN_BRACKET))
    	{
    		expect(Token.Kind.INTEGER);
    		expect(Token.Kind.CLOSE_BRACKET);
    	}
    	expect(Token.Kind.SEMICOLON);
    }
    
    //function-definition := "func" IDENTIFIER "(" parameter-list ")" ":" type statement-block .
    public void function_definition()
    {
    	expect(Token.Kind.FUNC);
    	tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	enterScope();
    	expect(Token.Kind.OPEN_PAREN);
    	parameter_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	expect(Token.Kind.COLON);
    	type();
    	statement_block();
    	exitScope();
    }
    
    //declaration := variable-declaration | array-declaration | function-definition .
    public void declaration()
    {
    	if(have(NonTerminal.VARIABLE_DECLARATION))
    	{
    		variable_declaration();
    	}
    	else if(have(NonTerminal.ARRAY_DECLARATION))
    	{
    		array_declaration();
    	}
    	else
    	{
    		function_definition();
    	}
    }
    
    //declaration-list := { declaration } .
    public void declaration_list()
    {
    	while(have(NonTerminal.DECLARATION))
    	{
    		declaration();
    	}
    }
    
    //assignment-statement := "let" designator "=" expression0 ";"
    public void assignment_statement()
    {
    	expect(Token.Kind.LET);
    	tryResolveSymbol(currentToken);
    	designator();
    	expect(Token.Kind.ASSIGN);
    	expression0();
    	expect(Token.Kind.SEMICOLON);
    }
    
    //call-statement := call-expression ";"
    public void call_statement()
    {
    	call_expression();
    	expect(Token.Kind.SEMICOLON);
    }
    
    //if-statement := "if" expression0 statement-block [ "else" statement-block ] .
    public void if_statement()
    {
    	expect(Token.Kind.IF);
    	enterScope();
    	expression0();
    	statement_block();
    	if(accept(Token.Kind.ELSE))
    	{
    		enterScope();
    		statement_block();
    		exitScope();
    	}
    	exitScope();
    }
    
    //while-statement := "while" expression0 statement-block .
    public void while_statement()
    {
    	expect(Token.Kind.WHILE);
    	enterScope();
    	expression0();
    	statement_block();
    	exitScope();
    }
    
    //return-statement := "return" expression0 ";" .
    public void return_statement()
    {
    	expect(Token.Kind.RETURN);
    	expression0();
    	expect(Token.Kind.SEMICOLON);
    }
    
    //statement := variable-declaration | call-statement | assignment-statement | if-statement | while-statement | return-statement .
    public void statement()
    {
    	if(have(NonTerminal.VARIABLE_DECLARATION))
    	{
    		variable_declaration();
    	}
    	else if(have(NonTerminal.CALL_STATEMENT))
    	{
    		call_statement();
    	}
    	else if(have(NonTerminal.ASSIGNMENT_STATEMENT))
    	{
    		assignment_statement();
    	}
    	else if(have(NonTerminal.IF_STATEMENT))
    	{
    		if_statement();
    	}
    	else if(have(NonTerminal.WHILE_STATEMENT))
    	{
    		while_statement();
    	}
    	else
    	{
    		return_statement();
    	}
    }

    //statement-list := { statement } .
    public void statement_list()
    {
    	while(have(NonTerminal.STATEMENT))
    	{
    		statement();
    	}
    }
    
    //statement-block := "{" statement-list "}" .
    public void statement_block()
    {
    	while(accept(Token.Kind.OPEN_BRACE))
    	{
    		statement_list();
    		expect(Token.Kind.CLOSE_BRACE);
    	}
    }


    // program := declaration-list EOF .
    public ast.DeclarationList program()
    {
    	ast.DeclarationList dl;
    	
    	return dl;
        throw new RuntimeException("add code to each grammar rule, to build as ast.");
    }
    
}
