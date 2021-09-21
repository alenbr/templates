package springrabbitmq.listener;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QueueListener {

	private static final String TAG_HEADER = "amqp_deliveryTag";

	@Autowired
	public QueueListener() {
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value ="${spring.rabbitmq.queue}", durable = "true"),
	        exchange = @Exchange(value = "${spring.rabbitmq.exchange.name}", ignoreDeclarationExceptions = "true") ))
	public void receiveMessages(@Payload String message, Channel channel, @Header(TAG_HEADER) long tag) throws IOException {
		try {
			log.info("Receiving message: " + message);
				channel.basicAck(tag, true);
		} catch (Exception ex) {
			log.error("Error receiving message from queue: {}", ex.getMessage(), ex);
		}
	}

}
