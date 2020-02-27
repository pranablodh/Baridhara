package com.consistentservices.baridhara.miscellaneousFunctions;

public class insertStringSubscribe
{
    private static final String stringToBeInserted ="CS/PC/SUB/";

    public String insertString(String originalString)
    {
        return String.format("%s%s", stringToBeInserted, originalString);
    }
}
