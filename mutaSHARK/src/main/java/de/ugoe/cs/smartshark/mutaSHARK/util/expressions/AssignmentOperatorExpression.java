package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

public abstract class AssignmentOperatorExpression extends Expression
{
    @Override
    protected final String getTypeName()
    {
        return "ASSIGNEMENT_OPERATOR";
    }
}

