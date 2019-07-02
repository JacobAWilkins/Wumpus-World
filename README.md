# Wumpus-World
A knowledge base and inference engine for the wumpus world
### Compile
```
javac *.java
```
### Run
##### Format:
```
java CheckTrueFalse wumpus_rules.txt [additional_knowledge_file] [statement_file]
```
##### Example:
```
java CheckTrueFalse wumpus_rules.txt kb1.txt statement1.txt
```
### Structure
##### CheckTrueFalse class:
The program begins in the CheckTrueFalse class. This class defines functions for reading the logical expressions, reading the subexpressions, checking if an expression is valid, and checking if a symbol is valid. The main function makes a HashMap to keep track of the true or false values to determine where the monsters and pits are. Lastly, it calls the Entails class to check if the statement given. It will decide whether the statement is 'definitely true', 'definitely false', 'possibly true, possibly false', or 'both true and false'. 
	
##### LogicalExpression class:
This class is responsible for treating each logical expression from the knowledge base as an object that can have multiple operations performed on it. It creates a linked list of the symbols in the expression and a vector of the subexpressions which are logical expressions in themselves. The setUniqueSymbol function determines whether a symbol is valid or not. The setConnective function determines whether the connective is valid and before setting it. More getter and setter functions are defined to give the other classes easy access to the logical expression's information. Lastly, the print_expression function prints the logical expression.
	 
##### Entails class: 
A model is defined as a HashMap model to keep track of symbols that are not present in the knowledge base. The TT_Entails uses the knowledge base, statement, and HashMap model to determine if the statement is entailed by the knowledge base. The TT_Check_All function that determines whether the statement can be present in the model, based on the knowledge base. The PL_True function is used to check whether a symbol is unique or not, and returns true or false accordingly. The Extend function is used to add symbols and their corresponding truth value to the HashMap model.
