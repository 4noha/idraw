package test.idraw;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import idraw.WebsocketEndpoint;
import idraw.manager.ImageManager;
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
	public void コマンドがsaveの時に期待値が返ってくるかの確認() throws InvalidKeyException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SQLException {
		BufferedImage bi = ImageIO.read(new File("WebContent\\images\\brush.png"));
		String png64 = ImageManager.png2string(bi);
		String repPng64 = png64.replaceAll("\n|\r|\r\n", "");
		String saveExpect = "{\"cmd\": \"save\", \"page_num\": 0, \"uuid\": \"4c397895-5914-4c45-b893-803c1c7cca0d\", \"count\": 2, \"image\": \"" + repPng64 + "\"}";
		socket.onMessage(saveExpect);
		assertEquals(WebSocketSessionMock.result.get(0), saveExpect);
		WebSocketSessionMock.result.clear();
	}

	@Test
	public void コマンドがchatの時に期待値が返ってくるかの確認() throws InvalidKeyException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SQLException {
		String chatInput = "{\"cmd\":\"chat\", \"session\": \"9990F2C3BE9DC57B39B136726C3A45D9\", \"message\": \"aaaaaa\"}";
		socket.onMessage(chatInput);
		String chatExpect = "{\"cmd\":\"chat\",\"text\":\"　匿名さん：aaaaaa\"}";
		assertEquals((Object)WebSocketSessionMock.result.get(0), chatExpect);
		WebSocketSessionMock.result.clear();

		//NGワードが入っている場合
		chatInput = "{\"cmd\":\"chat\", \"session\": \"9990F2C3BE9DC57B39B136726C3A45D9\", \"message\": \"aaaahouaa\"}";
		socket.onMessage(chatInput);
		chatExpect = "{\"cmd\":\"chat\",\"text\":\"　匿名さん：## NGワードを検出した為、発言の表示不可 ##\"}";
		assertEquals((Object)WebSocketSessionMock.result.get(0), chatExpect);
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
