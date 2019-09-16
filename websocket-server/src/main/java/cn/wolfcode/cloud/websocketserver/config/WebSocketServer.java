package cn.wolfcode.cloud.websocketserver.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@Data
@ServerEndpoint("/{uuid}")
@Component
public class WebSocketServer {
    private Session session;//websocket的连接会话信息
    //用于缓存客户端对应的会话信息,key:uuid   value:websocket
    public static ConcurrentHashMap<String, WebSocketServer> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("uuid") String uuid) {
        System.out.println("66666666666===>" + uuid);
        this.session = session;
        clients.put(uuid, this);//添加到缓存中
    }

    @OnClose
    public void onClose(@PathParam("uuid") String uuid) {
        clients.remove(uuid);//从缓存列表中删除对应的客户端的连接
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}