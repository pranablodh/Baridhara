package com.consistentservices.baridhara.miscellaneousFunctions;

public class insertStringPublish
{
    private static final String stringToBeInserted ="CS/PC/PUB/";

    public String insertString(String originalString)
    {
        return String.format("%s%s", stringToBeInserted, originalString);
    }
}
