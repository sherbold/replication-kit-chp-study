package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

public class Defects4JProjectName
{
    public static final Defects4JProjectName Chart = new Defects4JProjectName("Chart");
    public static final Defects4JProjectName Closure = new Defects4JProjectName("Closure");
    public static final Defects4JProjectName Lang = new Defects4JProjectName("Lang");
    public static final Defects4JProjectName Math = new Defects4JProjectName("Math");
    public static final Defects4JProjectName Mockito = new Defects4JProjectName("Mockito");
    public static final Defects4JProjectName Time = new Defects4JProjectName("Time");

    public final String name;

    Defects4JProjectName(String name)
    {
        this.name = name;
    }
}
