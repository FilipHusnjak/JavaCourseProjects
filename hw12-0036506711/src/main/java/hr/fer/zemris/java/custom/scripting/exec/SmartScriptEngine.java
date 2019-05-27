package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
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

public class SmartScriptEngine {

	private DocumentNode documentNode;
	
	private RequestContext requestContext;
	
	private ObjectMultistack multistack = new ObjectMultistack();
	
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = Objects.requireNonNull(
				documentNode, 
				"Given document node cannot be null!");
		this.requestContext = Objects.requireNonNull(
				requestContext, 
				"Given request context cannot be null!");
	}
	
	public void execute() {
		documentNode.accept(visitor);
	}
	
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
				executeOperation(stack, element);
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
		
		private void executeOperation(Deque<ValueWrapper> stack, Element element) {
			if (element instanceof ElementFunction) {
				applyFunction(stack, element.asText());
			} else if (element instanceof ElementOperator) {
				ValueWrapper second = stack.pop();
				ValueWrapper first = stack.pop();
				stack.push(doOperation(first.getValue(), second.getValue(), element.asText()));
			} else {
				stack.push(getValue(element));
			}
		}
		
		private void applyFunction(Deque<ValueWrapper> stack, String function) {
			switch (function) {
			case "sin":
				double valueSin = stack.pop().doubleValue();
				stack.push(new ValueWrapper(Math.sin(valueSin)));
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
			}
		}
		
		private ValueWrapper getParamValue(ValueWrapper defaultValue, String name, 
				Function<String, String> supplier) {
			ValueWrapper value = new ValueWrapper(supplier.apply(name));
			return value.getValue() == null ? defaultValue : value;
		}

		private ValueWrapper doOperation(Object first, Object second, String operation) {
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
