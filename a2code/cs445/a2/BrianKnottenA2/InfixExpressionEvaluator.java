package BrianKnottenA2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

/**
 * This class uses two stacks to evaluate an infix arithmetic expression from an
 * InputStream.
 */
public class InfixExpressionEvaluator {
    // Tokenizer to break up our input into tokens
    StreamTokenizer tokenizer;

    // Stacks for operators (for converting to postfix) and operands (for
    // evaluating)
    StackInterface<Character> operators;
    StackInterface<Double> operands;

    /**
     * Initializes the solver to read an infix expression from input.
     */
    public InfixExpressionEvaluator(InputStream input) {
        // Initialize the tokenizer to read from the given InputStream
        tokenizer = new StreamTokenizer(new BufferedReader(
                        new InputStreamReader(input)));

        // Declare that - and / are regular characters (ignore their regex
        // meaning)
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('/');

        // Allow the tokenizer to recognize end-of-line
        tokenizer.eolIsSignificant(true);

        // Initialize the stacks
        operators = new ArrayStack<Character>();
        operands = new ArrayStack<Double>();
    }

    /**
     * A type of runtime exception thrown when the given expression is found to
     * be invalid
     */
    class ExpressionError extends RuntimeException {
        ExpressionError(String msg) {
            super(msg);
        }
    }

    /**
     * Creates an InfixExpressionEvaluator object to read from System.in, then
     * evaluates its input and prints the result.
     */
    public static void main(String[] args) {
        InfixExpressionEvaluator solver =
                        new InfixExpressionEvaluator(System.in);
        Double value = solver.evaluate();
        if (value != null) {
            System.out.println(value);
        }
    }

    /**
     * Evaluates the expression parsed by the tokenizer and returns the
     * resulting value.
     */
    public Double evaluate() throws ExpressionError {
        // Get the first token. If an IO exception occurs, replace it with a
        // runtime exception, causing an immediate crash.
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Continue processing tokens until we find end-of-line
        while (tokenizer.ttype != StreamTokenizer.TT_EOL) {
            // Consider possible token types
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER:
                    // If the token is a number, process it as a double-valued
                    // operand
                    processOperand((double)tokenizer.nval);
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    // If the token is any of the above characters, process it
                    // is an operator
                    processOperator((char)tokenizer.ttype);
                    break;
                case '(':
                case '[':
                    // If the token is open bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    processOpenBracket((char)tokenizer.ttype);
                    break;
                case ')':
	    case ']':
                    // If the token is close bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    processCloseBracket((char)tokenizer.ttype);
                    break;
                case StreamTokenizer.TT_WORD:
                    // If the token is a "word", throw an expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    tokenizer.sval);
                default:
                    // If the token is any other type or value, throw an
                    // expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    String.valueOf((char)tokenizer.ttype));
            }

            // Read the next token, again converting any potential IO exception
            try {
                tokenizer.nextToken();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Almost done now, but we may have to process remaining operators in
        // the operators stack
        processRemainingOperators();

        // Return the result of the evaluation
        // TODO: Fix this return statement
        return operands.peek();
    }

    /**
     * Processes an operand.
     */
    void processOperand(double operand) {
	operands.push(operand);
    }

    /**
     * Processes an operator.
     */
    void processOperator(char operator) {
	while(!operators.isEmpty() && operators.peek() != '(' && operators.peek() != '[' &&  prec(operator) <= prec(operators.peek())){
	    char topOperator = operators.pop();
	    double operandTwo;
	    double operandOne;
	    try{
		operandTwo = operands.pop();
	    }catch(EmptyStackException e){
		throw new ExpressionError("Operator " + topOperator +
					  " provided without " +
					  "appropriate operands.");
	    }
	    try{
		operandOne = operands.pop();
	    }catch(EmptyStackException e){
		throw new ExpressionError("Operator " + topOperator +
					  " provided without " +
					  "appropriate operands.");
	    }
	    double result;
	    switch (topOperator) {
	    case '+':
		result = operandOne + operandTwo;
		break;
	    case '-':
		result = operandOne - operandTwo;
		break;
	    case '*':
		result = operandOne * operandTwo;
		break;
	    case '/':
		if(operandTwo == 0){
		    throw new ExpressionError("Divide-by-zero");
		}
		result = operandOne / operandTwo;
		break;
	    case '^':
		if(operandTwo == 0){
		    result = 1;
		}else{
		    result = operandOne;
			for(int i=1; i < operandTwo; i++)
			    result *= operandOne;
		}
		break;
	    default:
		result = 0.0;
		break;
	    }
	    operands.push(result);
	}
	operators.push(operator);
    }

    /**
     * Processes an open bracket.
     */
    void processOpenBracket(char openBracket) {
	operators.push(openBracket);
    }

    /**
     * Processes a close bracket.
     */
    void processCloseBracket(char closeBracket) {
	char topOperator = operators.pop();
	while(topOperator != '(' && topOperator != '['){
	   double  operandTwo = operands.pop();
	   double operandOne = operands.pop();
	   double result;
	   switch (topOperator) {
	   case '+':
		result = operandOne + operandTwo;
		break;
	    case '-':
		result = operandOne - operandTwo;
		break;
	    case '*':
		result = operandOne * operandTwo;
		break;
	    case '/':
		result = operandOne / operandTwo;
		if(operandTwo == 0){
		    throw new ExpressionError("Divide-by-zero");
		}		
		break;
	    case '^':
		if(operandTwo == 0){
		    result = 1;
		}else{
		    result = operandOne;
			for(int i=1; i < operandTwo; i++)
			    result *= operandOne;
		}
		break;
	   default:
	       result = 0.0;
		break;
	   }
	   operands.push(result);
	   topOperator = operators.pop();
	}
	if(topOperator == '(' && closeBracket == ']'){
	    throw new ExpressionError("Mismatched brackets: " +
				      topOperator + " and " + closeBracket);
	}else if(topOperator == '[' && closeBracket == ')'){
	    throw new ExpressionError("Mismatched brackets: " +
				      topOperator + " and " + closeBracket);
	}
    }

    /**
     * Processes any remaining operators leftover on the operators stack
     */
    void processRemainingOperators() {
	while(!operators.isEmpty()){
	    char topOperator = operators.pop();
	    double operandTwo;
	    double operandOne;
	    try{
		operandTwo = operands.pop();
	    }catch(EmptyStackException e){
		throw new ExpressionError("Operator " + topOperator +
					  " provided without " +
					  "appropriate operands.");
	    }
	    try{
		operandOne = operands.pop();
	    }catch(EmptyStackException e){
		throw new ExpressionError("Operator " + topOperator +
					  " provided without " +
					  "appropriate operands.");
	    }
	    double result;
	    switch (topOperator) {
	    case '+':
		result = operandOne + operandTwo;
		break;
	    case '-':
		result = operandOne - operandTwo;
		break;
	    case '*':
		result = operandOne * operandTwo;
		break;
	    case '/':
		result = operandOne / operandTwo;
		if(operandTwo == 0){
		    throw new ExpressionError("Divide-by-zero");
		}		
		break;
	    case '^':
		if(operandTwo == 0){
		    result = 1;
		}else{
		    result = operandOne;
			for(int i=1; i < operandTwo; i++)
			    result *= operandOne;
		}
		break;
	    default:
		result = 0.0;
		break;
	    }
	    if(!operands.isEmpty()){
		throw new ExpressionError("Too many operands provided.");
	    }
	    operands.push(result);
	}
    }
	int prec(char operator){
	int num = 3;
	if(operator == '*' || operator == '/'){
	    num = 2;
	}else if(operator == '+' || operator == '-'){
	    num = 1;
	}
	return num;
    }

}

