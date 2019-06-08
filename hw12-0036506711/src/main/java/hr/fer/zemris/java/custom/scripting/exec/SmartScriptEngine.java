package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that represents engine which can execute parsed SmarScript document.
 * It uses visitor design pattern.
 * 
 * @author Filip Husnjak
 */
public class SmartScriptEngine {

	/**
	 * Document node to be executed
	 */
	private DocumentNode documentNode;
	
	/**
	 * {@link RequestContext} used in execution
	 */
	private RequestContext requestContext;
	
	/**
	 * Multistack used to store local variables in script.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * Constructs new {@link SmartScriptEngine} with specified parameters.
	 * 
	 * @param documentNode
	 *        document node to be executed
	 * @param requestContext
	 *        {@link RequestContext} to be used in execution
	 * @throws NullPointerException if either given document node or {@link RequestContext}
	 *         are {@code null}
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = Objects.requireNonNull(
				documentNode, 
				"Given document node cannot be null!");
		this.requestContext = Objects.requireNonNull(
				requestContext, 
				"Given request context cannot be null!");
	}
	
	/**
	 * Executes this engine.
	 */
	public void execute() {
		try {
			documentNode.accept(visitor);
		} catch (NoSuchElementException e) {
			throw new SmartScriptEngineException("Not enough arguments provided!");
		} catch (EmptyStackException e) {
			throw new SmartScriptEngineException("Variable used but does not exist!");
		} catch (RuntimeException e) {
			throw new SmartScriptEngineException("Number expected but not provided!");
		}
	}
	
	/**
	 * {@link INodeVisitor} used for execution.
	 */
	private INodeVisitor visitor = new INodeVisitor() {
		
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			Element startExpression = node.getStartExpression();
			Element stepExpression = node.getStepExpression();
			Element endExpression = node.getEndExpression();
			multistack.push(variable, getValue(startExpression));
			while (multistack.peek(variable).numCompare(getValue(endExpression).getValue()) <= 0) {
				for (int i = 0, n = node.numberOfChildren(); i < n; ++i) {
					node.getChild(i).accept(this);
				}
				multistack.peek(variable).add(getValue(stepExpression).getValue());
			}
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Deque<ValueWrapper> stack = new ArrayDeque<>();
			for (Element element: node.getElements()) {
				execute(stack, element);
			}
			Iterator<ValueWrapper> it = stack.descendingIterator();
			while (it.hasNext()) {
				try {
					requestContext.write(it.next().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; ++i) {
				node.getChild(i).accept(this);
			}
		}
		
		/**
		 * Executes the proper operation for the given element.
		 * Three possible scenarios are:
		 * <ul>
		 * <li> If element is function this method applies the proper function
		 * 		and updates the stack with proper values
		 * <li> If element is operator this method performs proper operation
		 * 		upon values at the top of the stack
		 * <li> If element is variable this method extracts value and pushes
		 * 		it onto the stack
		 * <li> If element is just a value, that value is pushed onto the stack
		 * </ul>
		 * 
		 * @param stack
		 *        stack used to store values
		 * @param element
		 *        element upon which operation is executed
		 */
		private void execute(Deque<ValueWrapper> stack, Element element) {
			if (element instanceof ElementFunction) {
				applyFunction(stack, element.asText());
			} else if (element instanceof ElementOperator) {
				ValueWrapper second = stack.pop();
				ValueWrapper first = stack.pop();
				stack.push(performOperation(first.getValue(), second.getValue(), element.asText()));
			} else {
				stack.push(getValue(element));
			}
		}
		
		/**
		 * Executes the given function and updates all appropriate values.
		 * 
		 * @param stack
		 *        stack used for values
		 * @param function
		 *        function to be applied
		 * @throws IllegalArgumentException if the given function does not exist
		 */
		private void applyFunction(Deque<ValueWrapper> stack, String function) {
			switch (function) {
			case "sin":
				double valueSin = stack.pop().doubleValue();
				stack.push(new ValueWrapper(Math.sin(Math.toRadians(valueSin))));
				break;
			case "decfmt":
				DecimalFormat format = new DecimalFormat(stack.pop().toString());
				stack.push(new ValueWrapper(format.format(stack.pop().doubleValue())));
				break;
			case "dup":
				stack.push(new ValueWrapper(stack.peek().getValue()));
				break;
			case "swap":
				ValueWrapper a = stack.pop();
				ValueWrapper b = stack.pop();
				stack.push(a);
				stack.push(b);
				break;
			case "setMimeType":
				requestContext.setMimeType(stack.pop().toString());
				break;
			case "paramGet":
				stack.push(getParamValue(
						stack.pop(),
						stack.pop().toString(),
						requestContext::getParameter));
				break;
			case "pparamGet":
				stack.push(getParamValue(
						stack.pop(), 
						stack.pop().toString(),
						requestContext::getPersistentParameter));
				break;
			case "pparamSet":
				requestContext.setPersistentParameter(
						stack.pop().toString(), 
						stack.pop().toString());
				break;
			case "pparamDel":
				requestContext.removePersistentParameter(stack.pop().toString());
				break;
			case "tparamGet":
				stack.push(getParamValue(
						stack.pop(), 
						stack.pop().toString(),
						requestContext::getTemporaryParameter));
				break;
			case "tparamSet":
				requestContext.setTemporaryParameter(
						stack.pop().toString(),
						stack.pop().toString());
				break;
			case "tparamDel":
				requestContext.removeTemporaryParameter(stack.pop().toString());
				break;
			default:
				throw new IllegalArgumentException("The given function does not exist!");
			}
		}
		
		/**
		 * Returns the value with the given name if it exists and is not {@code null},
		 * otherwise default value is returned.
		 * 
		 * @param defaultValue
		 *        value which is returned if the requested value is illegal or does
		 *        not exist
		 * @param name
		 *        name of the value to be returned
		 * @param supplier
		 *        returns the proper value with the given name
		 * @return value with the given name or default value
		 */
		private ValueWrapper getParamValue(ValueWrapper defaultValue, String name, 
				Function<String, String> supplier) {
			ValueWrapper value = new ValueWrapper(supplier.apply(name));
			return value.getValue() == null ? defaultValue : value;
		}

		/**
		 * Performs specified operation upon given Objects which should be
		 * instances of a {@link Double} or {@link Integer} or parsable to
		 * number.
		 * 
		 * @param first
		 *        first value of the operation
		 * @param second
		 *        second value of the operation
		 * @param operation
		 *        operation to be performed
		 * @return result of an operation wrapped in {@link ValueWrapper} object
		 * @throws IllegalArgumentException if the given operator does not exist
		 */
		private ValueWrapper performOperation(Object first, Object second, String operation) {
			ValueWrapper toReturn = new ValueWrapper(first);
			switch (operation) {
			case "+":
				toReturn.add(second);
				break;
			case "-":
				toReturn.subtract(second);
				break;
			case "*":
				toReturn.multiply(second);
				break;
			case "/":
				toReturn.divide(second);
				break;
			default:
				throw new IllegalArgumentException("Illegal oprattion: " + operation);
			}
			return toReturn;
		}

		/**
		 * Returns value of an element. If its variable, the value of the most
		 * current occurrence is returned, otherwise this 
		 * 
		 * @param element
		 *        element whose value is to be determined
		 * @return determined value of an element
		 * @throws IllegalArgumentException if the given operator does not exist
		 */
		private ValueWrapper getValue(Element element) {
			if (element instanceof ElementVariable) {
				return multistack.peek(element.asText());
			} else if (element instanceof ElementConstantDouble || 
					element instanceof ElementConstantInteger || 
					element instanceof ElementString) {
				return new ValueWrapper(element.asText());
			} else {
				throw new IllegalArgumentException("Wrong type of element object!");
			}
		}
		
	};
	
}
