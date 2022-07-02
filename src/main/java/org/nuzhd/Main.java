package org.nuzhd;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Kirov")
    public void restartApp() {
        Restarter.getInstance().restart();
    }

    @EventListener
    public void handleContextClosedEvent(ContextClosedEvent event) {
        File[] files = new File("src/main/resources/photos").listFiles();

        assert files != null;
        for (File f : files) {
            try {
                FileUtils.deleteDirectory(f);
                log.info("Папка пользователя c chatId " + f.getName() + " успешно удалена");
            } catch (IOException e) {
                log.error("Не удалось удалить папку пользователя " + f.getName());
            }
        }


    }
}