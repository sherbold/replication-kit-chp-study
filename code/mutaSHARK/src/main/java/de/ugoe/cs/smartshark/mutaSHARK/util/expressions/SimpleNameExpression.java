package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

public class SimpleNameExpression extends Expression
{
    private final String name;

    public SimpleNameExpression(String name)
    {
        this.name = name;
    }

    @Override
    protected String getTypeName()
    {
        return "SimpleName";
    }

    @Override
    protected String getLabel()
    {
        return name;
    }
}
