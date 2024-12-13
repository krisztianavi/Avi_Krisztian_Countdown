package com.example.countdown;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CountdownController {

    @FXML
    private TextField dateTimeInput;

    @FXML
    private Button startButton;

    @FXML
    private Label countdownLabel;

    private Timeline timeline;

    @FXML
    public void initialize() {
        startButton.setOnAction(event -> startCountdown());
    }

    private void startCountdown() {
        String input = dateTimeInput.getText();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
            LocalDateTime targetDateTime = LocalDateTime.parse(input, formatter);
            LocalDateTime now = LocalDateTime.now();

            if (targetDateTime.isBefore(now)) {
                countdownLabel.setText("Jövőbeli dátumot adjon meg!.");
                return;
            }

            if (timeline != null) {
                timeline.stop();
            }

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                LocalDateTime currentTime = LocalDateTime.now();
                long years = currentTime.until(targetDateTime, ChronoUnit.YEARS);
                currentTime = currentTime.plusYears(years);
                long months = currentTime.until(targetDateTime, ChronoUnit.MONTHS);
                currentTime = currentTime.plusMonths(months);
                long days = currentTime.until(targetDateTime, ChronoUnit.DAYS);
                currentTime = currentTime.plusDays(days);
                long hours = currentTime.until(targetDateTime, ChronoUnit.HOURS);
                currentTime = currentTime.plusHours(hours);
                long minutes = currentTime.until(targetDateTime, ChronoUnit.MINUTES);
                currentTime = currentTime.plusMinutes(minutes);
                long seconds = currentTime.until(targetDateTime, ChronoUnit.SECONDS);

                countdownLabel.setText(years + " év " + months + " hó " + days + " nap " +
                        String.format("%02d:%02d:%02d", hours, minutes, seconds));

                if (LocalDateTime.now().isAfter(targetDateTime)) {
                    countdownLabel.setText("A számlálás befejeződött!");
                    timeline.stop();
                }
            }));

            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        } catch (Exception e) {
            countdownLabel.setText("A dátum formátuma a következő kell, hogy legyen: év.hó.nap óra:perc:másodperc.");
        }
    }
}