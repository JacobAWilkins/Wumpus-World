import java.util.*;

public class LogicalExpression {
	
	  private String uniqueSymbol = null;
	  private String connective = null;
	  static LinkedList<String> symbol = new LinkedList<String>();
		private Vector<LogicalExpression> subexpressions = null;
		
		public LogicalExpression() {
			this.subexpressions = new Vector<LogicalExpression>();
		}
	
		public LogicalExpression(LogicalExpression oldExpression) {
			
			if (oldExpression.getUniqueSymbol() == null) {
				this.uniqueSymbol = oldExpression.getUniqueSymbol();
			} else {
				this.connective = oldExpression.getConnective();
				for (Enumeration<LogicalExpression> e = oldExpression.getSubexpressions().elements(); e.hasMoreElements();) {
					LogicalExpression nextExpression = e.nextElement();
					this.subexpressions.add(nextExpression);
				}
			}
			
		}
	
		public void setUniqueSymbol(String newSymbol) {
			
			boolean valid = true;
			
			newSymbol.trim();
			
			if (this.uniqueSymbol != null) {
				System.out.println("setUniqueSymbol(): - this LE already has a unique symbol!!!" + "\nswapping :->" + this.uniqueSymbol + "<- for ->" + newSymbol +"<-\n");
			} else if (valid) {
				this.uniqueSymbol = newSymbol;
			}
			
		}
	
		public String setConnective(String inputString) {
			
			String connect;
			
			inputString.trim();
			
			if (inputString.startsWith("(")) {
				inputString = inputString.substring(inputString.indexOf('('), inputString.length());
				
				inputString.trim();
			}
			
			if (inputString.contains(" ")) {
				connect = inputString.substring(0, inputString.indexOf(" ")) ;
				inputString = inputString.substring((connect.length() + 1), inputString.length());
			} else {
				connect = inputString;
				inputString = "";
			}
			
			if (connect.equalsIgnoreCase("if") || connect.equalsIgnoreCase("iff") ||
					connect.equalsIgnoreCase("and") || connect.equalsIgnoreCase("or") ||
					connect.equalsIgnoreCase("xor") || connect.equalsIgnoreCase("not")) {
				this.connective = connect;
				return inputString;
			} else {
				System.out.println("unexpected character!!! : invalid connective!! - setConnective():-" + inputString);
				LogicalExpression.exit_function(0);
			}
			System.out.println(" invalid connective! : setConnective:-" + inputString);
			return inputString;
			
		}
		
		public void setSubexpression(LogicalExpression newSub) {
			
			this.subexpressions.add(newSub);
			
		}
		
		public void setSubexpressions(Vector<LogicalExpression> symbols) {
			
			this.subexpressions = symbols;
			
		}
		
		public String getUniqueSymbol() {
			
			return this.uniqueSymbol;
			
		}
		
		public String getConnective() {
			
			return this.connective;
			
		}
		
		public LogicalExpression getNextSubexpression() {
			
			return this.subexpressions.lastElement();
			
		}
		
		public Vector<LogicalExpression> getSubexpressions() {
			
			return this.subexpressions;
			
		}

		public void print_expression(String separator) {

		  if (!(this.uniqueSymbol == null)) {
			  System.out.print(this.uniqueSymbol.toUpperCase());
				String currentSymbol;
        currentSymbol = this.uniqueSymbol;
        symbol.add(currentSymbol);
		  } else {
			  LogicalExpression nextExpression;
			  
			  System.out.print("(" + this.connective.toUpperCase());

			  for (Enumeration<LogicalExpression> e = this.subexpressions.elements(); e.hasMoreElements();) {
				  nextExpression = e.nextElement();
				  
				  System.out.print(" ");
				  nextExpression.print_expression("");
				  System.out.print(separator);
				}
			  System.out.print(")");
			}
			
		}

    private static void exit_function(int value) {
			
			System.out.println("exiting from LogicalExpression");
			System.exit(value);
			
    }
	
	}
