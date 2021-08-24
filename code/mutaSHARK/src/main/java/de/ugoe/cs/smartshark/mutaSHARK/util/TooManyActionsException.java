package de.ugoe.cs.smartshark.mutaSHARK.util;

public class TooManyActionsException extends Throwable
{
    public final int actionCount;

    public TooManyActionsException(int actionCount)
    {
        this.actionCount = actionCount;
    }
}
