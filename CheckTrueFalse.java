import java.io.*;
import java.util.*;

public class CheckTrueFalse {

	public static void main(String[] args) {
		
		if (args.length != 3) {
			System.out.println("Usage: " + args[0] +  " [wumpus-rules-file] [additional-knowledge-file] [input_file]\n");
			exit_function(0);
		}
		
		String buffer;
		BufferedReader inputStream;
		BufferedWriter outputStream;
		
		LogicalExpression knowledge_base = new LogicalExpression();
		LogicalExpression statement = new LogicalExpression();
		LogicalExpression statement_negated = new LogicalExpression();

		try {
			inputStream = new BufferedReader(new FileReader(args[0]));
			
			System.out.println("loading the wumpus rules...");
			knowledge_base.setConnective("and");
		
			while ((buffer = inputStream.readLine()) != null) {
				if (!(buffer.startsWith("#") || (buffer.equals("")))) {
					LogicalExpression subExpression = readExpression(buffer);
					knowledge_base.setSubexpression(subExpression);
				}
			}		
			
			inputStream.close();

		} catch (Exception e) {
			System.out.println("failed to open " + args[0]);
			e.printStackTrace();
			exit_function(0);
		}
		
		HashMap<String, Boolean> model = new HashMap<String, Boolean>();
		try {
			inputStream = new BufferedReader(new FileReader(args[1]));
			
			System.out.println("loading the additional knowledge...");
			
			model.put("M_1_1", Boolean.FALSE);
      model.put("M_1_2", Boolean.FALSE);
      model.put("M_2_1", Boolean.FALSE);
      model.put("M_2_2", Boolean.FALSE);
      model.put("P_1_1", Boolean.FALSE);
      model.put("P_1_2", Boolean.FALSE);
      model.put("P_2_1", Boolean.FALSE);
      model.put("P_2_2", Boolean.FALSE);
			
			while ((buffer = inputStream.readLine()) != null) {
				if (!(buffer.startsWith("#") || (buffer.equals("")))) {
					if(!(buffer.contains("and") || buffer.contains("AND") || buffer.contains("if") || buffer.contains("IF") || buffer.contains("iff") || buffer.contains("IFF") || buffer.contains("or") || buffer.contains("OR") || buffer.contains("xor") || buffer.contains("XOR"))) {
						if(buffer.contains(" ")) {
							model.put(buffer.substring(buffer.indexOf(" ") + 1, buffer.indexOf(")")), Boolean.FALSE);
						} else {
							model.put(buffer.trim(), Boolean.TRUE);
						}
					}
						LogicalExpression subExpression = readExpression(buffer);
						knowledge_base.setSubexpression(subExpression);
        }
			}
			
			inputStream.close();

		} catch (Exception e) {
			System.out.println("failed to open " + args[1]);
			e.printStackTrace();
			exit_function(0);
		}
		
		if (!valid_expression(knowledge_base)) {
			System.out.println("invalid knowledge base");
			exit_function(0);
		}
		
		knowledge_base.print_expression("\n");
		
		try {
			inputStream = new BufferedReader(new FileReader(args[2]));
			
			System.out.println("\n\nLoading the statement file...");
			
			while ((buffer = inputStream.readLine()) != null) {
				if (!buffer.startsWith("#")) {
					statement = readExpression(buffer);
					statement_negated = readExpression("(not " + buffer + ")");
          break;
				}
			}
			
			inputStream.close();

		} catch (Exception e) {
			System.out.println("failed to open " + args[2]);
			e.printStackTrace();
			exit_function(0);
		}
		
		if (!valid_expression(statement)) {
			System.out.println("invalid statement");
			exit_function(0);
		}
		
		statement.print_expression("");
		
		System.out.println("\n");
		
		String result = "";
		boolean state_entail = Entails.TT_Entails(knowledge_base, statement, model);
		boolean negate_state_entail = Entails.TT_Entails(knowledge_base, statement_negated, model);
		
		if (state_entail && !negate_state_entail) {
			result = "definitely true.";
		} else if (!state_entail && negate_state_entail) {
			result = "definitely false.";
		} else if (!state_entail && !negate_state_entail) {
			result = "possibly true, possibly false.";
		} else if (state_entail && negate_state_entail) {
			result = "both true and false.";
		}
		System.out.println(result);
		
		try {
			outputStream = new BufferedWriter(new FileWriter("result.txt"));
			outputStream.write(result);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static LogicalExpression readExpression(String input_string) {
		
		LogicalExpression result = new LogicalExpression();
		
    input_string = input_string.trim();
          
    if (input_string.startsWith("(")) {
      String symbolString = "";
            
      symbolString = input_string.substring(1);
				  
      if (!symbolString.endsWith(")")) {
        System.out.println("missing ')' !!! - invalid expression! - readExpression():-" + symbolString );
        exit_function(0);
      } else {
        symbolString = symbolString.substring(0 , (symbolString.length() - 1));
        symbolString.trim();
							
        symbolString = result.setConnective(symbolString);
      }
            
      result.setSubexpressions(read_subexpressions(symbolString));
            
    } else {
      result.setUniqueSymbol(input_string);
    }
    return result;
		
  }
	
	public static Vector<LogicalExpression> read_subexpressions(String input_string) {

		Vector<LogicalExpression> symbolList = new Vector<LogicalExpression>();
		LogicalExpression newExpression;
		String newSymbol = new String();

		input_string.trim();

		while (input_string.length() > 0) {
		
			newExpression = new LogicalExpression();

			if (input_string.startsWith("(")) {
				int parenCounter = 1;
				int matchingIndex = 1;
				while ((parenCounter > 0) && (matchingIndex < input_string.length())) {
						if (input_string.charAt(matchingIndex) == '(') {
							parenCounter++;
						} else if (input_string.charAt(matchingIndex) == ')') {
							parenCounter--;
						}
					matchingIndex++;
				}
			
				newSymbol = input_string.substring(0, matchingIndex);
			
				newExpression = readExpression(newSymbol);

				symbolList.add(newExpression);

				input_string = input_string.substring(newSymbol.length(), input_string.length());

			} else {
				if (input_string.contains(" ")) {
					newSymbol = input_string.substring(0, input_string.indexOf(" "));
					input_string = input_string.substring((newSymbol.length() + 1), input_string.length());
				} else {
					newSymbol = input_string;
					input_string = "";
				}
			
				newExpression.setUniqueSymbol(newSymbol);

				symbolList.add(newExpression);
			
			}
		
			input_string.trim();
		
			if (input_string.startsWith(" ")) {
				input_string = input_string.substring(1);
			}
		
		}
		return symbolList;

	}

	public static boolean valid_expression(LogicalExpression expression) {

		if (!(expression.getUniqueSymbol() == null) && (expression.getConnective() == null)) {
			return valid_symbol(expression.getUniqueSymbol());
		}
		
		if ((expression.getConnective().equalsIgnoreCase("if")) ||
				( expression.getConnective().equalsIgnoreCase("iff"))) {
			if (expression.getSubexpressions().size() != 2) {
				System.out.println("error: connective \"" + expression.getConnective() +
													 "\" with " + expression.getSubexpressions().size() + " arguments\n" );
				return false;
			}
		} else if (expression.getConnective().equalsIgnoreCase("not")) {
			if (expression.getSubexpressions().size() != 1) {
				System.out.println("error: connective \""+ expression.getConnective() + "\" with "+ expression.getSubexpressions().size() +" arguments\n" ); 
				return false;
			}
		} else if ((!expression.getConnective().equalsIgnoreCase("and"))  &&
						 (!expression.getConnective().equalsIgnoreCase("or")) &&
						 (!expression.getConnective().equalsIgnoreCase("xor"))) {
			System.out.println("error: unknown connective " + expression.getConnective() + "\n");
			return false;
		}
		
		for (Enumeration<LogicalExpression> e = expression.getSubexpressions().elements(); e.hasMoreElements();) {
			LogicalExpression testExpression = e.nextElement();
			if (!valid_expression(testExpression)) {
				return false;
			}
		}
		return true;
	
	}
	
	public static boolean valid_symbol(String symbol) {
		
		if (symbol == null || (symbol.length() == 0)) {
			return false;
		}

		for (int counter = 0; counter < symbol.length(); counter++) {
			if ((symbol.charAt(counter) != '_') &&
					(!Character.isLetterOrDigit(symbol.charAt(counter)))) {
				
				System.out.println("String: " + symbol + " is invalid! Offending character:---" + symbol.charAt(counter) + "---\n");
				
				return false;
			}
		}
		return true;
		
	}

	private static void exit_function(int value) {
		
		System.out.println("exiting from checkTrueFalse");
		System.exit(value);
		
  }	
}
