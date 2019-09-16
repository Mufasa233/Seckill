package cn.wolfcode.cloud.websocketserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //ServerEndpointExporter 用来发布websocket的服务地址
        return new ServerEndpointExporter();
    }
}