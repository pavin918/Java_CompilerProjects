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
//		nextChar = ;
	}	
	
	// OPTIONAL: helper function for reading a single char from input
	//           can be used to catch and handle any IOExceptions,
	//           advance the charPos or lineNum, etc.
	
	private int readChar() {
		try 
		{
			nextChar = input.read();
			charPos++;
//			System.out.println(nextChar);
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
//		if((char) nextChar == '\n')
//		{
//			lineNum += 1;
//			charPos = 0;
//			readChar();
//		}
//		if((char) nextChar == ' ')
//		{
//			while((char) nextChar == ' ')
//				readChar();
////			charPos++;
//		}
		if(nextChar == -1)
			return Token.EOF(lineNum,charPos);
		int cpos = charPos;
//		System.out.println(cpos+"char pos");
		Token toks = checklex((char)nextChar, s, cpos);
		if(toks == null)
		{
			readChar();
			return next();
		}
		return toks;
		//return new Token(s, lineNum, charPos);
	}

	// OPTIONAL: any other methods that you find convenient for implementation or testing
	
	private Token checklex(char ch, String str, int chPos)
	{
//		System.out.println(ch);
		switch(ch)
		{
			case 'a': 
			{ 
				str += Character.toString((ch));
				ch = (char) readChar();
//				System.out.println(chPos+" char pos1");
				return check_and_array(ch, str, chPos);
			}
			case 'o':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
//				System.out.println(chPos+"char pos1");
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
//				oparen(ch);
			}
			case ')':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				cparen(ch);
			}
			case '{':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				obrace(ch);
			}
			case '}':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				cbrace(ch);
//				break;
			}
			case '[':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				obracket(ch);
//				break;
			}
			case ']':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				cbracket(ch);
//				break;
			}
			case '+':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				addition(ch);
//				break;
			}
			case '-':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				subtraction(ch);
//				break;
			}
			case '*':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				multiplication(ch);
//				break;
			}
			case '/':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				if (ch == '/')
				{
					while(ch != '\n')
					{
						int i = readChar();
						if(i == -1)
						{
							return null;
						}
						ch = (char) i;
//						System.out.println(ch);
					}
					charPos = 0;
					lineNum += 1;
					return null;
				}
				return new Token(str, lineNum, chPos);
//				division(ch);
//				break;
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
//				System.out.println(ch +" in !");
				str += Character.toString((ch));
				ch = (char) readChar();
				return notequal(ch, str, chPos);
			}
			case '=':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				//System.out.println(str);
				return equal_or_assign(ch, str, chPos);
			}
			case ',':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				comma(ch);
//				break;
			}
			case ';':
			{
				str += Character.toString((ch));
				ch = (char) readChar();
				return new Token(str, lineNum, chPos);
//				semicolon(ch);
//				break;
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
//				System.out.println(nums);
				if(nums.contains(Character.toString(ch)))
				{
					
					return intfloat(ch, str, chPos);
				}
				else if(Character.isLetter(ch) || ch == '_')
				{
//					System.out.println("check id");
					str += Character.toString(ch);
					ch = (char) readChar();
					System.out.println(ch+"ha");
					if(ch == ' ')
					{
						readChar();
					}
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
//		if(ch == 'o')
//		{
//			ch = (char) readChar();
			
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
			//new Token(, lineNum, charPos);
		}
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token checknot(char ch, String str, int chPos)
	{
//		if(ch == 'n')
//		{
			//ch = (char) readChar();
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
				return new Token(str, lineNum, chPos);
			}
		}
		else
		{
			return new Token(str, lineNum, chPos);
		}
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token checklet(char ch, String str, int chPos)
	{
//		if(ch == 'l')
//		{
//		ch = (char) readChar();
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token checkvar(char ch, String str, int chPos)
	{
//		if(ch == 'v')
//		{
//			ch = (char) readChar();
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token check_func_false(char ch, String str, int chPos)
	{
//		if(ch == 'f')
//		{
//			ch = (char) readChar();
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	private Token checkif(char ch, String str, int chPos)
	{
//		if(ch == 'i')
//		{
//			ch = (char) readChar();
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token checkelse(char ch, String str, int chPos)
	{
//		if(ch == 'e')
//		{
//			ch = (char) readChar();
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token checkwhile(char ch, String str, int chPos)
	{
//		if(ch == 'w')
//		{
//			ch = (char) readChar();
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token checktrue(char ch, String str, int chPos)
	{
//		if(ch == 't')
//		{
//			ch = (char) readChar();
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private Token checkreturn(char ch, String str, int chPos)
	{
//		if(ch == 'r')
//		{
//			ch = (char) readChar();
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
//							System.out.println((int)ch+" wtffff");
							if(Character.isLetterOrDigit(ch))
							{
//								System.out.println("in return if");
								return identify(ch, str, chPos);
							}
								
							else
							{
//								System.out.println("in return else");
//								System.out.println(str);
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
//		}
//		else
//		{
//			readChar();
//		}
	}
	
	private void oparen(char ch)
	{
		if(ch == '(')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void cparen(char ch)
	{
		if(ch == ')')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void obrace(char ch)
	{
		if(ch == '{')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void cbrace(char ch)
	{
		if(ch == '}')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void obracket(char ch)
	{
		if(ch == '[')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void cbracket(char ch)
	{
		if(ch == ']')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void addition(char ch)
	{
		if(ch == '+')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void subtraction(char ch)
	{
		if(ch == '-')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void multiplication(char ch)
	{
		if(ch == '*')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void division(char ch)
	{
		if(ch == '/')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private void greater_or_equal(char ch)
	{
		if(ch == '>')
		{
			readChar();
			if(ch == '=')
			{
				readChar();
			}
			else
			{
				readChar();
			}
		}
		else
		{
			readChar();
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
//				System.out.println(chPos+" in equal or assign");
//				System.out.println(ch+" in equal or assign");
//				if(ch != '\n')
//				{
//					ch = (char) readChar();
//				}
//				System.out.println(str + " hi");
//				System.out.println(chPos+" in equal or assign again");
//				System.out.println(ch+" in equal or assign again");
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
//			System.out.println(str);
			return Token.Error(str, lineNum, chPos);
		}
	}
	
	private void comma(char ch)
	{
		if(ch == ',')
		{
			readChar();
		}
		else
		{
			readChar();
		}
	}
	
	private Token semicolon(char ch, String str, int chPos)
	{
		if(ch == ';')
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
	
//	private void decimal(char ch)
//	{
//		if(Character.getNumericValue(ch) >= 0 && Character.getNumericValue(ch) <= 9)
//		{
//			//for(int i = 1; i < str.length(); i++)
//			ch = (char) readChar();
//			while(Character.getNumericValue(ch) >= 0 && Character.getNumericValue(ch) <= 9)
//			{
//				ch = (char) readChar();
//			}
//			if(ch != '.')
//			{
//				ch = (char) readChar();
//				while(Character.getNumericValue(ch) >= 0 && Character.getNumericValue(ch) <= 9)
//				{
//					ch = (char) readChar();
//				}
//			}
//		}
//		else
//		{
//			ch = (char) readChar();
//		}
//	}
	
	private Token identify(char ch, String str, int chPos)
	{
//		System.out.println(str);
		String startchar = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		if(startchar.contains(Character.toString(ch)))
//		{
//			System.out.println(str + " check first char of identify");
//			str += Character.toString((ch));
//			ch = (char) readChar();
			String otherchar = startchar + "123456789";
			while(otherchar.contains(Character.toString(ch)))
			{
				str += Character.toString((ch));
				ch = (char) readChar();
			}
//			System.out.println(str + " check final of identify");
			return Token.Identifier(str, lineNum, chPos);
//		}
//		return Token.Identifier(str, lineNum, chPos);
	}
}
