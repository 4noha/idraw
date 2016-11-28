package test.idraw;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import idraw.WebsocketEndpoint;
import idraw.orm.DbStaticDao;
import idraw.orm.DbUtil;
import test.idraw.orm.WebSocketSessionMock;

public class WebSocketEndpointTest {
	WebsocketEndpoint socket = new WebsocketEndpoint();
	@Before
	public void setUp() throws Exception {
		socket.open(WebSocketSessionMock.session);
		DbUtil.close();
		DbUtil.connect(DbStaticDao.toMap(m -> {
			m.put("env", "test");
			m.put("host", "127.0.0.1:3306");
			m.put("db_name", "idraw");
		}));
	}
	@After
	public void close() throws Exception {
		socket.close(WebSocketSessionMock.session);
	}

	// Websocket受信処理のテストの例
	@Test
	public void コマンドがpenの時に期待値が返ってくるかの確認() throws InvalidKeyException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SQLException {
		String penExpect = "{\"cmd\":\"pen\",\"page\":0,\"fx\":652,\"fy\":363,\"tx\":653,\"ty\":364,\"color\":\"#000000\"}";
		socket.onMessage(penExpect);
		assertEquals(WebSocketSessionMock.result.get(0), penExpect);
		WebSocketSessionMock.result.clear();
	}

	@Test
	public void コマンドがimageの時に期待値が返ってくるかの確認() throws InvalidKeyException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SQLException {
		String imageExpect = "{\"cmd\":\"image\", \"x\": 10, \"y\":20, \"image\":\"dsakfldsklajflkjfgl;dsl;fg=\"}";
		socket.onMessage(imageExpect);
		assertEquals(WebSocketSessionMock.result.get(0), imageExpect);
		WebSocketSessionMock.result.clear();
	}

	@Test
	public void Mapを作ってjsonを吐くmapToJsonString() throws JsonProcessingException {
		String message = WebsocketEndpoint.mapToJsonString(m -> {
			m.put("a", 1);
			m.put("c", null);
		});
		assertEquals(message, "{\"a\":1,\"c\":null}");
	}
}
