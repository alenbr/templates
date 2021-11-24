package springrabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueueService {

	private final RabbitTemplate rabbitTemplate;

	@Value("${spring.rabbitmq.exchange.name}")
	private String exchangeName;
	@Value("${spring.rabbitmq.exchange.routingKey}")
	private String routingKey;

	@Autowired
	public QueueService(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	public void send(String messageToSend) {
		rabbitTemplate.invoke(template -> {
			template.convertAndSend(exchangeName, routingKey, messageToSend);
			template.waitForConfirmsOrDie(20_000);
			return true;
		},(tag, multiple) -> {
			// ToDo - Implement here the statements for after ack confirm  
		},(tag, multiple) -> {
			// ToDo - Implement here the statements for after unAck confirm  
		});
	}
}