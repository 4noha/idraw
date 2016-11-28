package test.idraw.orm;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.MessageHandler.Partial;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class WebSocketSessionMock{
	public static final ArrayList<String> result = new ArrayList<String>();
	public static final Session session = new Session() {
		@Override
		public Basic getBasicRemote() {
			return new Basic() {
				@Override public void setBatchingAllowed(boolean arg0) throws IOException {}
				@Override public void sendPong(ByteBuffer arg0) throws IOException, IllegalArgumentException {}
				@Override public void sendPing(ByteBuffer arg0) throws IOException, IllegalArgumentException {}
				@Override public boolean getBatchingAllowed() { return false; }
				@Override public void flushBatch() throws IOException {}
				@Override public void sendText(String arg0, boolean arg1) throws IOException {}
				@Override public void sendText(String message) throws IOException {
					result.add(message);
				}
				@Override public void sendObject(Object arg0) throws IOException, EncodeException {}
				@Override public void sendBinary(ByteBuffer arg0, boolean arg1) throws IOException {}
				@Override public void sendBinary(ByteBuffer arg0) throws IOException {}
				@Override public Writer getSendWriter() throws IOException { return null; }
				@Override public OutputStream getSendStream() throws IOException { return null; }
			};
		}
		@Override public void setMaxTextMessageBufferSize(int arg0) {}
		@Override public void setMaxIdleTimeout(long arg0) { }
		@Override public void setMaxBinaryMessageBufferSize(int arg0) { }
		@Override public void removeMessageHandler(MessageHandler arg0) { }
		@Override public boolean isSecure() { return false; }
		@Override public boolean isOpen() { return false; }
		@Override public Map<String, Object> getUserProperties() { return null; }
		@Override public Principal getUserPrincipal() { return null; }
		@Override public URI getRequestURI() { return null; }
		@Override public Map<String, List<String>> getRequestParameterMap() { return null; }
		@Override public String getQueryString() { return null; }
		@Override public String getProtocolVersion() { return null; }
		@Override public Map<String, String> getPathParameters() { return null; }
		@Override public Set<Session> getOpenSessions() { return null; }
		@Override public String getNegotiatedSubprotocol() { return null; }
		@Override public List<Extension> getNegotiatedExtensions() { return null; }
		@Override public Set<MessageHandler> getMessageHandlers() { return null; }
		@Override public int getMaxTextMessageBufferSize() { return 0; }
		@Override public long getMaxIdleTimeout() { return 0; }
		@Override public int getMaxBinaryMessageBufferSize() { return 0; }
		@Override public String getId() { return null; }
		@Override public WebSocketContainer getContainer() { return null; }
		@Override public Async getAsyncRemote() { return null; }
		@Override public void close(CloseReason arg0) throws IOException {}
		@Override public void close() throws IOException {}
		@Override public <T> void addMessageHandler(Class<T> arg0, Whole<T> arg1) throws IllegalStateException {}
		@Override public <T> void addMessageHandler(Class<T> arg0, Partial<T> arg1) throws IllegalStateException {}
		@Override public void addMessageHandler(MessageHandler arg0) throws IllegalStateException {}
	};
}