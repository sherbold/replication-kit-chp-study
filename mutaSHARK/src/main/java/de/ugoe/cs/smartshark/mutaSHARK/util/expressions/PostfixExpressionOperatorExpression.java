package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

public abstract class PostfixExpressionOperatorExpression extends Expression
{
    @Override
    protected final String getTypeName()
    {
        return "POSTFIX_EXPRESSION_OPERATOR";
    }
}

