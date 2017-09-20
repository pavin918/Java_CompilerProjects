package crux;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class Scanner /*implements Iterable<Token>*/ {
	public static String studentName = "Paul Dao";
	public static String studentID = "";
	public static String uciNetID = "";
	
	private int lineNum;  // current line count
	private int charPos;  // character offset for current line
	private int nextChar; // contains the next char (-1 == EOF)
	private Reader input;
	
	public Scanner(Reader reader)
	{
		// TODO: initialize the Scanner
		input = reader;
		lineNum = 1;
		charPos = 0;
		readChar();
	}	
	
	// OPTIONAL: helper function for reading a single char from input
	//           can be used to catch and handle any IOExceptions,
	//           advance the charPos or lineNum, etc.
	
	private int readChar() {
		try 
		{
			nextChar = input.read();
			charPos++;
			return nextChar;
		}
		catch (IOException e) 
		{
			nextChar = -1;
			return nextChar;
		}
	}
	

	/* Invariants:
	 *  1. call assumes that nextChar is already holding an unread character
	 *  2. return leaves nextChar containing an untokenized character
	 */
	public Token next()
	{
		// TODO: implement this
		String s = "";
		while(Character.isWhitespace((char) nextChar))
		{
			if((char) nextChar == '\n')
			{
				lineNum += 1;
				charPos = 0;
				readChar();
			}
			else if((char) nextChar == ' ')
			{
				readChar();
			}
			else if(((char) nextChar == '\t'))
			{
				for(int i = 0; i < 5; i++)
				{
					charPos++;
				}
				readChar();
			}
			
		}
		if(nextChar == -1)
			return Token.EOF(lineNum,charPos);
		int cpos = charPos;
		Token toks = checklex((char)nextChar, s, cpos);
		if(toks == null)
		{
			readChar();
			return next();
		}
		return toks;
	}

	// OPTIONAL: any other methods that you find convenient for implementation or testing
	
	private Token checklex(char ch, String str, int chPos)
	{
		switch(ch)
		{
			case 'a': 
			{ 
				str += Character.toString((ch));
				ch = (char) readChar();
				return check_and_array(ch, str, chPos);
			}
			case 'o':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checkor(ch, str, chPos);
			}
			case 'n':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checknot(ch, str, chPos);
			}
			case 'l':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checklet(ch, str, chPos);
			}
			case 'v':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checkvar(ch, str, chPos);
			}
			case 'f':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return check_func_false(ch, str, chPos);
			}
			case 'i':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checkif(ch, str, chPos);
			}
			case 'e':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checkelse(ch, str, chPos);
			}
			case 'w':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checkwhile(ch, str, chPos);
			}
			case 't':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checktrue(ch, str, chPos);
			}
			case 'r':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return checkreturn(ch, str, chPos);
			}
			case '(':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case ')':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case '{':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case '}':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case '[':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case ']':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case '+':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case '-':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case '*':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case '/':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				if (ch == '/')
				{
					int i = ch;
					while(ch != '\n')
					{
						
						if(i == -1)
						{
							charPos--;
							return null;
						}
						i = readChar();
						ch = (char) i;
					}
					charPos = 0;
					lineNum += 1;
					return null;
				}
				return new Token(str, lineNum, chPos);
			}
			case '>':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return less_or_equal(ch, str, chPos);
			}
			case '<':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return less_or_equal(ch, str, chPos);
			}
			case '!':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return notequal(ch, str, chPos);
			}
			case '=':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return equal_or_assign(ch, str, chPos);
			}
			case ',':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case ';':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			case ':':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return colon_or_call(ch, str, chPos);
			}
			default:
			{
				String nums = "0123456789";
				if(nums.contains(Character.toString(ch)))
				{
					
					return intfloat(ch, str, chPos);
				}
				else if(Character.isLetter(ch) || ch == '_')
				{
					str += Character.toString(ch);
					ch = (char) readChar();
					return identify(ch, str, chPos);
				}
				else
				{
					str += Character.toString(ch);
					readChar();
					return Token.Error(str, lineNum, chPos);
				}
			}
		}
	}
	
	private Token check_and_array(char ch, String str, int chPos)
	{
		if(ch == 'n')
		{
			str += Character.toString((ch));
			ch = (char) readChar();
			if(ch == 'd')
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				if(Character.isLetterOrDigit(ch))
					return identify(ch, str, chPos);
					
				else
					return new Token(str, lineNum, chPos);
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else if(ch == 'r')
		{
			str += Character.toString((ch));
			ch = (char) readChar();
			if(ch == 'r')
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				if(ch == 'a')
				{
					str += Character.toString((ch));
					ch = (char) readChar();
					if(ch == 'y')
					{
						str += Character.toString((ch));
						ch = (char) readChar();
						if(Character.isLetterOrDigit(ch))
							return identify(ch, str, chPos);
							
						else
							return new Token(str, lineNum, chPos);
					}
					else
					{
						return identify(ch, str, chPos);
					}
				}
				else
				{
					return identify(ch, str, chPos);
				}
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checkor(char ch, String str, int chPos)
	{			
		if(ch == 'r')
		{
			str += Character.toString((ch));
			ch = (char) readChar();
			if(Character.isLetterOrDigit(ch))
				return identify(ch, str, chPos);
				
			else
				return new Token(str, lineNum, chPos);
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checknot(char ch, String str, int chPos)
	{
		if(ch == 'o')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 't')
			{
				str += Character.toString(ch);
				ch = (char)readChar();
				if(Character.isLetterOrDigit(ch))
					return identify(ch, str, chPos);
					
				else
					return new Token(str, lineNum, chPos);
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checklet(char ch, String str, int chPos)
	{
		if(ch == 'e')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 't')
			{
				str += Character.toString(ch);
				ch = (char)readChar();
				if(Character.isLetterOrDigit(ch))
					return identify(ch, str, chPos);
					
				else
					return new Token(str, lineNum, chPos);
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checkvar(char ch, String str, int chPos)
	{
		if(ch == 'a')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 'r')
			{
				str += Character.toString(ch);
				ch = (char)readChar();
				if(Character.isLetterOrDigit(ch))
					return identify(ch, str, chPos);
					
				else
					return new Token(str, lineNum, chPos);
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token check_func_false(char ch, String str, int chPos)
	{
		if(ch == 'u')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 'n')
			{
				str += Character.toString(ch);
				ch = (char) readChar();
				if(ch == 'c')
				{
					str += Character.toString(ch);
					ch = (char)readChar();
					if(Character.isLetterOrDigit(ch))
						return identify(ch, str, chPos);
						
					else
						return new Token(str, lineNum, chPos);
				}
				else
				{
					return identify(ch, str, chPos);
				}
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else if(ch == 'a')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 'l')
			{
				str += Character.toString(ch);
				ch = (char) readChar();
				if(ch == 's')
				{
					str += Character.toString(ch);
					ch = (char) readChar();
					if(ch == 'e')
					{
						str += Character.toString(ch);
						ch = (char)readChar();
						if(Character.isLetterOrDigit(ch))
							return identify(ch, str, chPos);
							
						else
							return new Token(str, lineNum, chPos);
					}
					else
					{
						return identify(ch, str, chPos);
					}
				}
				else
				{
					return identify(ch, str, chPos);
				}
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	private Token checkif(char ch, String str, int chPos)
	{
		if(ch == 'f')
		{
			str += Character.toString(ch);
			ch = (char)readChar();
			if(Character.isLetterOrDigit(ch))
				return identify(ch, str, chPos);
				
			else
				return new Token(str, lineNum, chPos);
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checkelse(char ch, String str, int chPos)
	{
		if(ch == 'l')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 's')
			{
				str += Character.toString(ch);
				ch = (char) readChar();
				if(ch == 'e')
				{
					str += Character.toString(ch);
					ch = (char)readChar();
					if(Character.isLetterOrDigit(ch))
						return identify(ch, str, chPos);
						
					else
						return new Token(str, lineNum, chPos);
				}
				else
				{
					return identify(ch, str, chPos);
				}
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checkwhile(char ch, String str, int chPos)
	{
		if(ch == 'h')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 'i')
			{
				str += Character.toString(ch);
				ch = (char) readChar();
				if(ch == 'l')
				{
					str += Character.toString(ch);
					ch = (char) readChar();
					if(ch == 'e')
					{
						str += Character.toString(ch);
						ch = (char)readChar();
						if(Character.isLetterOrDigit(ch))
							return identify(ch, str, chPos);
							
						else
							return new Token(str, lineNum, chPos);
					}
					else
					{
						return identify(ch, str, chPos);
					}
				}
				else
				{
					return identify(ch, str, chPos);
				}
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checktrue(char ch, String str, int chPos)
	{
		if(ch == 'r')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 'u')
			{
				str += Character.toString(ch);
				ch = (char) readChar();
				if(ch == 'e')
				{
					str += Character.toString(ch);
					ch = (char)readChar();
					if(Character.isLetterOrDigit(ch))
						return identify(ch, str, chPos);
						
					else
						return new Token(str, lineNum, chPos);
				}
				else
				{
					return identify(ch, str, chPos);
				}
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token checkreturn(char ch, String str, int chPos)
	{
		if(ch == 'e')
		{
			str += Character.toString(ch);
			ch = (char) readChar();
			if(ch == 't')
			{
				str += Character.toString(ch);
				ch = (char) readChar();
				if(ch == 'u')
				{
					str += Character.toString(ch);
					ch = (char) readChar();
					if(ch == 'r')
					{
						str += Character.toString(ch);
						ch = (char) readChar();
						if(ch == 'n')
						{
							str += Character.toString(ch);
							ch = (char)readChar();
							if(Character.isLetterOrDigit(ch))
							{
								return identify(ch, str, chPos);
							}
								
							else
							{
								return new Token(str, lineNum, chPos);
							}
						}
						else
						{
							return identify(ch, str, chPos);
						}
					}
					else
					{
						return identify(ch, str, chPos);
					}
				}
				else
				{
					return identify(ch, str, chPos);
				}
			}
			else
			{
				return identify(ch, str, chPos);
			}
		}
		else
		{
			return identify(ch, str, chPos);
		}
	}
	
	private Token less_or_equal(char ch, String str, int chPos)
	{
		if(ch == '=')
		{
			str += Character.toString((ch));
			ch = (char) readChar();
			return new Token(str, lineNum, chPos);
		}
		else
		{
			return new Token(str, lineNum, chPos);
		}
	}
	
	private Token equal_or_assign(char ch, String str, int chPos)
	{
			if(ch == '=')
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
			}
			else
			{
				return new Token(str, lineNum, chPos);
			}
	}
	
	private Token notequal(char ch, String str, int chPos)
	{
		if(ch == '=')
		{
			str += Character.toString(ch);
			readChar();
			return new Token(str, lineNum, chPos);
		}
		else
		{
			return Token.Error(str, lineNum, chPos);
		}
	}
	
	private Token colon_or_call(char ch, String str, int chPos)
	{
		if(ch == ':')
		{
			str += Character.toString(ch);
			readChar();
			return new Token(str, lineNum, chPos);
		}
		else
		{
			return new Token(str, lineNum, chPos);
		}
	}
	
	private Token intfloat(char ch, String str,int chPos)
	{
		if(Character.getNumericValue(ch) >= 0 && Character.getNumericValue(ch) <= 9)
		{
			str += Character.toString((ch));
			ch = (char) readChar();
			while(Character.getNumericValue(ch) >= 0 && Character.getNumericValue(ch) <= 9)
			{
				str += Character.toString((ch));
				ch = (char) readChar();
			}
			if(ch == '.')
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				while(Character.getNumericValue(ch) >= 0 && Character.getNumericValue(ch) <= 9)
				{
					str += Character.toString((ch));
					ch = (char) readChar();
				}
				return Token.Float(str, lineNum, chPos);
			}
		}
		return Token.Integer(str, lineNum, chPos);
	}
	
	private Token identify(char ch, String str, int chPos)
	{
		if(Character.isWhitespace(ch))
		{
			readChar();
			return Token.Identifier(str, lineNum, chPos);
		}
		String startchar = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String otherchar = startchar + "123456789";
			while(otherchar.contains(Character.toString(ch)))
			{
				str += Character.toString((ch));
				ch = (char) readChar();
			}
			return Token.Identifier(str, lineNum, chPos);
	}
}
