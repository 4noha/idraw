package test.idraw.manager;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.SQLException;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import idraw.manager.EncryptManager;
import idraw.model.User;
import idraw.orm.DbStaticDao;
import idraw.orm.DbUtil;

public class EncryptManagerTest {
	@Before
	public void setUp() throws Exception {
		DbUtil.connect(DbStaticDao.toMap(m -> {
			m.put("env", "test");
			m.put("host", "127.0.0.1:3306");
			m.put("db_name", "idraw");
		}));
	}
	@After
	public void close() throws Exception {
		DbUtil.close();
	}
	
	@Test
	public void RSA暗号化と複合化() throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		User user = new User();
		user.username = "aaa";
		user.save();
		String hexPubKey = EncryptManager.generateKeyPair(user);
		PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(DatatypeConverter.parseHexBinary(hexPubKey));
		// KeySpecから、秘密RSAキーを復元する
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);
		
		assertEquals(user, null);
	}
}
