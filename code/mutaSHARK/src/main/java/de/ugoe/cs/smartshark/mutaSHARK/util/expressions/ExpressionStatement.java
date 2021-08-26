package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

public class ExpressionStatement extends Expression
{
    public ExpressionStatement(Expression childExpression)
    {
        children.clear();
        children.add(childExpression);
    }

    @Override
    protected String getTypeName()
    {
        return "ExpressionStatement";
    }

    @Override
    protected String getLabel()
    {
        return "";
    }

    @Override
    public String toString()
    {
        return "ExpressionStatement: " + getTypeName() + " " + getLabel();
    }
}
