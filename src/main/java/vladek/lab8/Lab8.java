//package vladek.lab8;
//
//import vladek.lab7.services.SHA256Service;
//import vladek.lab8.dto.ECDSACheckSign;
//import vladek.lab8.dto.ECDSAPoint;
//import vladek.lab8.dto.ECDSASign;
//import vladek.lab8.services.ECDSAService;
//
//import java.math.BigInteger;
//
//public class Lab8 {
//    public static void main(String[] args) {
//        ECDSAService ecdsaService = new ECDSAService();
//        SHA256Service sha256Service = new SHA256Service();
//        String text = "abc";
//
//        byte[][] blocks = sha256Service.prepareMessageBlocks(text);
//        byte[] hashCodeBytes = sha256Service.getHashCode(blocks);
//        BigInteger hashCode = new BigInteger(1, hashCodeBytes);
//
//        BigInteger x = ecdsaService.generateSecretKey();
//        ECDSAPoint p = ecdsaService.generateOpenKey(x);
//        ECDSASign sign = ecdsaService.sign(hashCode, x);
//
//        ECDSACheckSign checkSign = ecdsaService.checkSign(p, sign, hashCode);
//
//        System.out.println("Секретный ключ (x): " + x.toString(16));
//        System.out.println("Открытый ключ (P): (" + p.getX().toString(16) + ", " + p.getY().toString(16) + ")");
//        System.out.println("Подпись (r, s): (" + sign.getR() + ", " + sign.getS() + ")");
//        System.out.println("Проверка подписи (v, r): (" + checkSign.getX() + ", " + checkSign.getR()+ ")");
//    }
//}
