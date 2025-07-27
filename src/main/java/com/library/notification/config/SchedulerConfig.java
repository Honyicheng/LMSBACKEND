package com.library.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron = "0 0 0 * * *")
//     @Scheduled(cron = "0 * * * * *") // 每分钟 0 秒执行*/


    public void triggerReminderEmailTask() {
        kafkaTemplate.send("email-reminder-topic", "run_daily_check");
    }
}