package ru.nsu.basargina.actions;

import java.util.Map;
import ru.nsu.basargina.data.Expression;

/**
 * Class that represents addition operation.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates left and right part of the expression with "+".
     *
     * @param left - left part
     * @param right - right part
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * String representation of the expression with '+'.
     *
     * @return string with '+'
     */
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }

    /**
     * Differentiation of the left and right parts.
     *
     * @param variable - variable for differentiating
     * @return derivative by given variable
     */
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Evaluates left and right part.
     *
     * @param variables - variables being signified
     * @return - result of the evaluation
     */
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) + right.eval(variables);
    }
}
