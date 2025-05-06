package com.tld.configuration;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.core.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc  
public class RabbitMQConfigTest {
	@Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private RabbitMQConfig rabbitMQConfig;

    @Test
    void testQueueBean() {
        Queue queue = rabbitMQConfig.queue();
        assertNotNull(queue, "Queue should not be null");
        assertEquals(RabbitMQConfig.QUEUE_NAME, queue.getName(), "Queue name should match");
        assertTrue(queue.isDurable(), "Queue should be durable");
    }

    @Test
    void testExchangeBean() {
        DirectExchange exchange = rabbitMQConfig.exchange();
        assertNotNull(exchange, "Exchange should not be null");
        assertEquals(RabbitMQConfig.EXCHANGE_NAME, exchange.getName(), "Exchange name should match");
    }

    @Test
    void testBindingBean() {
        // Crear mocks de Queue y DirectExchange
        Queue queue = mock(Queue.class);
        DirectExchange exchange = mock(DirectExchange.class);

        // Configurar el comportamiento de los mocks
        when(queue.getName()).thenReturn(RabbitMQConfig.QUEUE_NAME);  // Simula el nombre de la cola
        when(exchange.getName()).thenReturn(RabbitMQConfig.EXCHANGE_NAME);  // Simula el nombre del exchange

        // Crear el Binding utilizando la configuración de RabbitMQConfig
        Binding binding = rabbitMQConfig.binding(queue, exchange);

        // Verificar que el binding no es nulo y que el routing key es el correcto
        assertNotNull(binding, "Binding should not be null");
        assertEquals(RabbitMQConfig.ROUTING_KEY, binding.getRoutingKey(), "Routing key should match");

        // Verificar que los métodos getName() fueron llamados al menos una vez
        verify(queue, atLeastOnce()).getName();
        verify(exchange, atLeastOnce()).getName();
    }
}
