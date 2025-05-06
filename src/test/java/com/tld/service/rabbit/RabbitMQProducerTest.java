package com.tld.service.rabbit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMQProducerTest {
	 @Mock
	    private RabbitTemplate rabbitTemplate;

	    @Mock
	    private RetryTemplate retryTemplate;

	    @InjectMocks
	    private RabbitMQProducer rabbitMQProducer;

	    @BeforeEach
	    void setUp() {
	        rabbitMQProducer = new RabbitMQProducer(rabbitTemplate);
	    }

	    @Test
	    void testSendMessage_success() throws Throwable {
	        String message = "Test message";

	        doAnswer(invocation -> {
	            return null;  
	        }).when(retryTemplate).execute(Mockito.any());

	        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString());

	        boolean result = rabbitMQProducer.sendMessage(message);

	        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), eq(message));

	        assert(result);
	    }
	   
}

	   
