package idraw.manager;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class EncryptManager {
    /**
     * 公開鍵と秘密鍵の生成.
     * @param user User型(Userテーブル)
     * @return 公開鍵(16進数文字列)
     */
	public String GetEncrypt(User user) throws InvalidParameterException,NoSuchAlgorithmException {
		//現在は

		// RSA暗号化キーを生成する.
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(1024); // 1024bit - 88bit = 117byte (最大平文サイズ)
		KeyPair keyPair = keygen.generateKeyPair();

		// 秘密キー
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		// 公開キー
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();

		// 秘密キーと公開キーを16進数文字列に変換
		Key[] keys = new Key[] {privateKey, publicKey};
		byte[] binPrivate = keys[0].getEncoded();
		byte[] binPublic = keys[1].getEncoded();
		String encodedPrivate = toHexString(binPrivate);
		String encodedPublic = toHexString(binPublic);

		// 公開鍵を戻す
		return encodedPublic;
	}

    /**
     * バイト列を16進数文字列に変換する.
     * @param data バイト列
     * @return 16進数文字列
     */
    private static String toHexString(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte d : data) {
            buf.append(String.format("%02X", d));
        }
        return buf.toString();
    }
}