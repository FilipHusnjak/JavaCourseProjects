package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.*;

/**
 * Representation of a {@code ForLoop TAG} that stores its variable, startExpression,
 * endExpression and stepExpression. It can have children since {@code ForLoop TAGs} are non-empty tags.
 * 
 * @author Filip Husnjak
 *
 */
public class ForLoopNode extends Node {

	/**
	 * Variable of {@code this} {@code ForLoop TAG}. Cannot be {@code null}.
	 */
	private final ElementVariable variable;
	
	/**
	 * StartExpression of {@code this} {@code ForLoop TAG}. Cannot be {@code null}.
	 */
	private final Element startExpression;
	
	/**
	 * EndExpression of {@code this} {@code ForLoop TAG}. Cannot be {@code null}.
	 */
	private final Element endExpression;
	
	/**
	 * StepExpression of {@code this} {@code ForLoop TAG}. Can be {@code null}.
	 */
	private final Element stepExpression;

	/**
	 * Constructs {@code ForLoopNode} with given parameters.
	 * 
	 * @param variable
	 *        variable of {@code ForLoop TAG}
	 * @param startExpression
	 *        startExpression of {@code ForLoop TAG}
	 * @param endExpression
	 *        endExpression of {@code ForLoop TAG}
	 * @param stepExpression
	 *        stepExpression of {@code ForLoop TAG}
	 * @throws NullPointerException any of the given parameters, except stepExpression,
	 *         is {@code null}
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		super();
		Objects.requireNonNull(variable, "Given variable cannot be null!");
		Objects.requireNonNull(startExpression, "Given startExpression cannot be null!");
		Objects.requireNonNull(endExpression, "Given endExpression cannot be null!");
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Returns {@code variable} of this {@code ForLoopNode}.
	 * 
	 * @return {@code variable} of this {@code ForLoopNode}
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Returns {@code startExpression} of this {@code ForLoopNode}.
	 * 
	 * @return {@code startExpression} of this {@code ForLoopNode}
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Returns {@code endExpression} of this {@code ForLoopNode}.
	 * 
	 * @return {@code endExpression} of this {@code ForLoopNode}
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Returns {@code stepExpression} of this {@code ForLoopNode}.
	 * 
	 * @return {@code stepExpression} of this {@code ForLoopNode}
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String accept(NodeVisitor visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof ForLoopNode)) return false;
		ForLoopNode forLoopNode = (ForLoopNode) obj;
		return super.equals(obj) && forLoopNode.getVariable().equals(variable) && forLoopNode.getStartExpression().equals(startExpression)
				&& forLoopNode.getEndExpression().equals(endExpression) 
				&& (stepExpression == null ? forLoopNode.getStepExpression() == null : stepExpression.equals(forLoopNode.getStepExpression()));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode() + variable.hashCode() + startExpression.hashCode() + endExpression.hashCode() + (stepExpression == null ? 0 : stepExpression.hashCode()));
	}
	
}
