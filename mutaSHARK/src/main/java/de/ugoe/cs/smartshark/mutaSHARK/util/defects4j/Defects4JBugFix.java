package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

public class Defects4JBugFix
{
    public final String name;
    public final String buggyClassFile;
    public final String fixedClassFile;

    public Defects4JBugFix(String name, String buggyClassFile, String fixedClassFile)
    {
        this.name = name;
        this.buggyClassFile = buggyClassFile;
        this.fixedClassFile = fixedClassFile;
    }
}
