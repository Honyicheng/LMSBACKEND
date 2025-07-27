package com.library.LMS;

import com.library.notification.config.SchedulerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@SpringBootTest
class ComLibraryApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private SchedulerConfig schedulerConfig;

	@Test
	public void testTriggerReminderEmailTask() {
		schedulerConfig.triggerReminderEmailTask();
		// 可手动查看 Kafka 是否收到消息，或搭配 Kafka Consumer 模拟接收
	}

	// 注意：在实际应用中，Kafka Consumer 通常会在独立的服务中运行，
	// 测试 Kafka Consumer
	// 注意：在实际应用中，Kafka Consumer 通常会在独立的服务中运行，
	// 这里为了测试方便，直接在测试类中定义一个 Consumer。
	@Component
	public class TestKafkaConsumer {

		@KafkaListener(topics = "email-reminder-topic", groupId = "test-consumer-group")
		public void listen(String message) {
			System.out.println("Test Kafka Consumer received: " + message);
		}
	}

}
