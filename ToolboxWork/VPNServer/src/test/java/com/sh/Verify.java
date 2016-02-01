package com.sh;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class Verify {

    /**
        String  public_key = "-----BEGIN PUBLIC KEY-----\n" . chunk_split(google_public_key, 64, "\n") . "-----END PUBLIC KEY-----";
        String  public_key_handle = openssl_get_publickey(public_key);
        int result = openssl_verify(inapp_purchase_data, base64_decode(inapp_data_signature, public_key_handle, OPENSSL_ALGO_SHA1);
        if (1 == result) {
            // 支付验证成功！
        }
     */
    public static void main(String[] args) {
        
        String inapp_purchase_data = "dshfgsdkjfh";
        String  inapp_data_signature = "fiewudy219873";
        String  google_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnBgMRathuwaPeiqWlIgjir3iKc054"//
                + "2GB4C6xIpSX7Mu9gZTudb5LbZL+rYS8MrSGcoFu7+kq4JC17CAB0uVM+xScQEY2V26GTPJ3JXenv3zml6OYRHHQtS/"//
                + "MPZ/ZqGgZ9LCT8NLdRN0i4CBRmxgjZZKiirC5ylsElY6FXqU7RxfCL94IDBwHFNGao0NHk7u+VQlv/8FOLWnJawRsog0"//
                + "2c7+j/UHAY6sataoG6a05DEwYJrUnxl8cpU7rkmXAXgRCeK9LTrXleSrffPUseHwLfjFzeUlxxQnX7vpi4NzZ6iwxaiPYvv83"//
                + "Pgu4I3pYf5vOoOGnGBySwtjewsuDocgRiQIDAQAB";

//        google_public_key = UtilString.bytesToHexStr(google_public_key.getBytes());
//        boolean b = RSAEncrypt.verifySHA1withRSASigature(google_public_key, inapp_data_signature, inapp_purchase_data);
//        System.out.println(b);
        
        try {
            boolean m = RSAEncrypt.getPublicKey(google_public_key, inapp_data_signature,inapp_purchase_data);
            System.out.println(m);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
