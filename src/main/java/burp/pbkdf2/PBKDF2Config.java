package burp.pbkdf2;

import burp.utils.OutFormat;

public class PBKDF2Config {
    public PBKDF2Algorithms Algorithms;
    public int KeyLength;
    public int IterationCount;
    public OutFormat OutFormat;
    public byte[] Salt;
}
