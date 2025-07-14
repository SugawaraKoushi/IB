package vladek.lab7;

import org.apache.commons.codec.binary.Hex;
import vladek.lab7.services.SHA256Service;

import java.io.IOException;

public class Lab7 {
    public static void main(String[] args) throws  IOException {
        SHA256Service sha256Service = new SHA256Service();
        String msg = "abc";
        byte[][] a = sha256Service.prepareMessageBlocks(msg);
        byte[] b = sha256Service.getHashCode(a);
        System.out.println(Hex.encodeHex(b));
    }
}
