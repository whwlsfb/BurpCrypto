package burp.utils;

public class CipherInfo {
    public final String Name;
    public final String Algorithm;
    public final String Mode;
    public final String Padding;

    public CipherInfo(String name) {
        String[] nameParts = name.split("/");
        this.Name = name;
        this.Algorithm = nameParts[0];
        this.Mode = nameParts[1];
        this.Padding = nameParts[2];
    }
}
