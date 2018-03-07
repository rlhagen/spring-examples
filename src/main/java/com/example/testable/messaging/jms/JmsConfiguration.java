package com.example.testable.messaging.jms;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import com.example.testable.messaging.Message;
import com.example.testable.messaging.MessageRouter;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DestinationResolver;

import com.example.testable.messaging.jms.JmsMessageRouter.Destination;


@Configuration
@EnableJms
public class JmsConfiguration {

    static Logger logger = Logger.getLogger(JmsConfiguration.class);

    public class DynamicDestinationResolver implements DestinationResolver {

        @Override
        public javax.jms.Destination resolveDestinationName(Session session, String destinationName,
                                                            boolean pubSubDomain) throws JMSException {
            Destination destination = Destination.valueOf(destinationName.toUpperCase());
            if (destination.isTopic()) {
                return session.createTopic(destinationName);
            }
            return session.createQueue(destinationName);
        }

    }

    @Bean
    public DestinationResolver destinationResolver() {
        return new DynamicDestinationResolver();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("__TypeId__");
        return converter;
    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setDestinationResolver(destinationResolver());
        return factory;
    }

    @Bean
    public MessageRouter<Destination, Message> router() {
        return new JmsMessageRouter();
    }

}
