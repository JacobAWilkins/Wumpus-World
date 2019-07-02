import java.util.*;

public class Entails {
	
	static HashMap<String, Boolean> trueModel;
	
	public static boolean TT_Entails(LogicalExpression knowledge_base, LogicalExpression statement, HashMap<String, Boolean> model) {
		
		trueModel = new HashMap<String, Boolean>(model);
		return TT_Check_All(knowledge_base, statement, getsymbols(knowledge_base, model), model);
		
  }
	
  public static boolean TT_Check_All(LogicalExpression knowledge_base, LogicalExpression statement, LinkedList<String> symbol, HashMap<String, Boolean> model) {
		
		if (symbol.isEmpty()) {
			if (PL_True(knowledge_base, model)) {
				return PL_True(statement, model);
			} else {
				return true;
      }
		} else {
			String head = symbol.poll();
      HashMap<String, Boolean> tempTrueModel = new HashMap<String, Boolean>(model);
      HashMap<String, Boolean> tempFalseModel = new HashMap<String, Boolean>(model);
      LinkedList<String> tempList = new LinkedList<String>();
      ListIterator<String> listIterator = symbol.listIterator();
      while (listIterator.hasNext()) {
				tempList.add(listIterator.next());
			}
			boolean trueSymbol = TT_Check_All(knowledge_base, statement, symbol, Extend(head, true, tempTrueModel));
      symbol = tempList;
      boolean falseSymbol = TT_Check_All(knowledge_base, statement, symbol, Extend(head, false, tempFalseModel));
      return trueSymbol && falseSymbol;
		}
		
	}
		
  public static boolean PL_True(LogicalExpression statement, HashMap<String, Boolean> model) {
		
		if (statement.getUniqueSymbol() != null) {
			boolean value;
			String stateSym = statement.getUniqueSymbol();
      if (trueModel.containsKey(stateSym)) {
				value = trueModel.get(stateSym);
			} else {
				value = model.get(stateSym);
			}
			return value;
		} else if (statement.getConnective().equalsIgnoreCase("not")) {
			LogicalExpression nextExpression;
			for (Enumeration<LogicalExpression> e = statement.getSubexpressions().elements(); e.hasMoreElements();) {
				nextExpression = e.nextElement();
				if (PL_True(nextExpression, model) == false) {
					return true;
				}
			}
			return false;
		} else if (statement.getConnective().equalsIgnoreCase("and")) {
			LogicalExpression nextExpression;
			for (Enumeration<LogicalExpression> e = statement.getSubexpressions().elements(); e.hasMoreElements();) {
				nextExpression = e.nextElement();
				if (PL_True(nextExpression, model) == false) {
					return false;
				}
      }
			return true;
    } else if (statement.getConnective().equalsIgnoreCase("if")) {
			LogicalExpression leftExpression;
			LogicalExpression rightExpression;
      boolean leftChild = false;
			boolean rightChild = false;
      String trueExp = null;
			int count = 0;
			for (Enumeration<LogicalExpression> e = statement.getSubexpressions().elements(); e.hasMoreElements();) {
				if (count == 0) {
					leftExpression = e.nextElement();
					leftChild = PL_True(leftExpression, model);
					count++;
        } else if (count > 0) {
					rightExpression = e.nextElement();
          rightChild = PL_True(rightExpression, model);
					count++;
        }
      }
			if (leftChild == true && rightChild == false) {
				if (!trueModel.containsKey(trueExp)) {
					trueModel.put(trueExp, leftChild);
				}
				return false;
      }
			return true;
		} else if (statement.getConnective().equalsIgnoreCase("iff")) {
			LogicalExpression leftExpression;
			LogicalExpression rightExpression;
      boolean leftChild = false;
			boolean rightChild = false;
      String trueExp = null;
      int count = 0;
			for (Enumeration<LogicalExpression> e = statement.getSubexpressions().elements(); e.hasMoreElements();) { 
				if (count == 0) {
					leftExpression = e.nextElement();
          trueExp = leftExpression.getUniqueSymbol();
          leftChild = PL_True(leftExpression, model);
					count++;
        } else if (count > 0) {
					rightExpression = e.nextElement();
          rightChild = PL_True(rightExpression, model);
					count++;
        }
      }
			if(leftChild == rightChild) {
				if(!trueModel.containsKey(trueExp)) {
					trueModel.put(trueExp, leftChild);
				}
				return true;
      }
			return false;
		} else if (statement.getConnective().equalsIgnoreCase("or")) {
			LogicalExpression nextExpression;
			for (Enumeration<LogicalExpression> e = statement.getSubexpressions().elements(); e.hasMoreElements();) {
				nextExpression = e.nextElement();
				if (PL_True(nextExpression, model) == true) {
					return true;
				} 
      }
			return false;
    } else if (statement.getConnective().equalsIgnoreCase("xor")) {
			LogicalExpression nextExpression;
      int count = 0;
			for (Enumeration<LogicalExpression> e = statement.getSubexpressions().elements(); e.hasMoreElements();) {
				nextExpression = e.nextElement();
				if (PL_True(nextExpression, model) == true) {
					count++;
				}
			}
			if (count == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	public static LinkedList<String> getsymbols(LogicalExpression knowledge_base, HashMap<String, Boolean> model) {
		
		LinkedList<String> symbol = new LinkedList<String>();
		boolean contain = false;
		for (String s1 : LogicalExpression.symbol) {
			contain = false;
      for (String s2 : symbol) {
				if (s2.equalsIgnoreCase(s1)) {
					contain = true;
					break;
        }
      } 
			if (contain == false && !model.containsKey(s1)) {
				symbol.add(s1);
			}
		}
		return symbol;
		
	}
	
	public static HashMap<String, Boolean> Extend(String head, boolean value, HashMap<String, Boolean> model) {
		
		model.put(head, value);
		return model;
		
	}
	
}
