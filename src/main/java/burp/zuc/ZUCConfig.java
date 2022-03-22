package burp.zuc;

import burp.utils.OutFormat;
import cn.hutool.crypto.symmetric.ZUC;

public class ZUCConfig {
    public ZUC.ZUCAlgorithm Algorithms;
    public byte[] Key, IV;
    public OutFormat OutFormat;
}
