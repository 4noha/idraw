package idraw.manager;

import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;

import idraw.model.User;

public class EncryptManager {
    /**
     * 公開鍵と秘密鍵の生成.
     * @param user User型(Userテーブル)
     * @return 公開鍵(16進数文字列)
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
	public String generateKeyPair(User user) throws NoSuchAlgorithmException, NoSuchFieldException,
	SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
	InvocationTargetException, SQLException {

		// RSA暗号化キーを生成する
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

		// Userテーブルへ秘密鍵を書き出す
		user.secret_key = encodedPrivate;
		user.save();

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