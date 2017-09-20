package crux;

public class Token {
	
	public static enum Kind {
		AND("and"),
		OR("or"),
		NOT("not"),
		LET("let"),
		VAR("var"),
		ARRAY("array"),
		FUNC("func"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		TRUE("true"),
		FALSE("false"),
		RETURN("return"),
		
		OPEN_PAREN("("),
		CLOSE_PAREN(")"),
		OPEN_BRACE("{"),
		CLOSE_BRACE("}"),
		OPEN_BRACKET("["),
		CLOSE_BRACKET("]"),
		ADD("+"),
		SUB("-"),
		MUL("*"),
		DIV("/"),
		GREATER_EQUAL(">="),
		LESSER_EQUAL("<="),
		NOT_EQUAL("!="),
		EQUAL("=="),
		GREATER_THAN(">"),
		LESS_THAN("<"),
		ASSIGN("="),
		COMMA(","),
		SEMICOLON(";"),
		COLON(":"),
		CALL("::"),
		
		IDENTIFIER(),
		INTEGER(),
		FLOAT(),
		ERROR(),
		EOF();
		
		// TODO: complete the list of possible tokens
		
		
		private String default_lexeme;
		
		Kind()
		{
			default_lexeme = "";
		}
		
		Kind(String lexeme)
		{
			default_lexeme = lexeme;
		}
		
		public boolean hasStaticLexeme()
		{
			return default_lexeme != null;
		}
		
		// OPTIONAL: if you wish to also make convenience functions, feel free
		//           for example, boolean matches(String lexeme)
		//           can report whether a Token.Kind has the given lexeme
		
		public boolean matches(String lexeme)
		{
			return default_lexeme.equals(lexeme);
		}
	}
	
	private int lineNum;
	private int charPos;
	Kind kind;
	private String lexeme = "";
	
	
	// OPTIONAL: implement factory functions for some tokens, as you see fit
	           
	public static Token EOF(int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.EOF;
		return tok;
	}
	
	public static Token Identifier(String name, int linePos, int charPos)
	{
		Token tok = new Token(name, linePos, charPos);
		tok.kind = Kind.IDENTIFIER;
		return tok;
	}
	
	public static Token Integer(String num, int linePos, int charPos)
	{
		Token tok = new Token(num, linePos, charPos);
		tok.kind = Kind.INTEGER;
		return tok;
	}
	
	public static Token Float(String dec, int linePos, int charPos)
	{
		Token tok = new Token(dec, linePos, charPos);
		tok.kind = Kind.FLOAT;
		return tok;
	}
	
	public static Token Error(String err, int linePos, int charPos)
	{
		Token tok = new Token(err, linePos, charPos);
		tok.kind = Kind.ERROR;
		return tok;
	}

	private Token(int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		
		// if we don't match anything, signal error
		this.kind = Kind.ERROR;
		this.lexeme = "No Lexeme Given";
	}
	
	public Token(String lexeme, int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
//		System.out.println("in token: "+lexeme);
//		System.out.println("in token: "+lineNum);
//		System.out.println("in token: "+charPos);
		// TODO: based on the given lexeme determine and set the actual kind
		for(Kind k : Token.Kind.values())
		{
			if(k.matches(lexeme))
			{
//				System.out.println(k.default_lexeme);
				this.kind = k;
				this.lexeme = lexeme;
				return;
			}
//			else if(k.integer(lexeme).equals(lexeme))
//			{
//				this.kind = Integer(lexeme, lineNum, charPos).kind;
//				this.lexeme = lexeme;
//				//return;
//			}
//			else if(k.decimal(lexeme).equals(lexeme))
//			{
//				this.kind = Float(lexeme, lineNum, charPos).kind;
//				this.lexeme = lexeme;
//				//return;
//			}
//			else if(k.identify(lexeme).equals(lexeme))
//			{
//				this.kind = Identifier(lexeme, lineNum, charPos).kind;
//				this.lexeme = lexeme;
//				//return;
//			}
		}
		this.lexeme = lexeme;
		// if we don't match anything, signal error
		//this.kind = Kind.ERROR;
		//this.lexeme = "Unrecognized lexeme: " + lexeme;
	}
	
	public int lineNumber()
	{
		return lineNum;
	}
	
	public int charPosition()
	{
		return charPos;
	}
	
	// Return the lexeme representing or held by this token
	public String lexeme()
	{
		// TODO: implement
		return lexeme;
		//return null;
	}
	
	public String toString()
	{
		// TODO: implement this
		String mess = "(lineNum:" + lineNum + ", charPos:" + charPos + ")";
		if (kind == Kind.ERROR)
			return kind.toString() + "(Unexpected character: " + lexeme + ")" + mess;
		else if(kind == Kind.INTEGER || kind == Kind.FLOAT)
			return kind.toString() + "(" + lexeme + ")" + mess;
		else if(kind == Kind.IDENTIFIER)
			return kind.toString() + "("+lexeme+")" + mess;
		else
			return kind.toString() + mess;
		//return "Not Yet Implemented";
	}
	
	// OPTIONAL: function to query a token about its kind
	//           boolean is(Token.Kind kind)
	public boolean is(Token.Kind k)
	{
		return kind.equals(k);
	}
	
	// OPTIONAL: add any additional helper or convenience methods
	//           that you find make for a clean design

}
