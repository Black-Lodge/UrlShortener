package urlshortener.blacklodge.metrics;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Class that configures the websockets
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    /**
     * Configures the queues
     * @param config message Broker config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue","/user");
        config.setApplicationDestinationPrefixes("/app","/topic");
        config.setUserDestinationPrefix("/user");
    }

    /**
     * Adds websockets endpoint with sockJS support
     * @param registry registry of endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websockets").setAllowedOrigins("*").withSockJS();
    }

}