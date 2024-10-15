package com.mongodb.starter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.mongodb.starter.models.SensorData;
import com.mongodb.starter.repository.SensorDataRepository;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;

@Service
public class MqttService implements CommandLineRunner {

    @Autowired
    private SensorDataRepository sensorDataRepository;
    private final String host = "6b447ffc5465442e968e7bbdefe6fd17.s1.eu.hivemq.cloud";
    private final String username = "B21DCCN042";
    private final String password = "Thuha0612@";
    private Mqtt5BlockingClient client;
    private CallbackHandler callbackHandler; // Callback handler for message reception
    private ScheduledExecutorService windScheduler;
    private int wind; // Lưu biến gió

    @Override
    public void run(String... args) throws Exception {
        startMqtt();
        startWindScheduler(); // Bắt đầu scheduler cho gió
    }

    public void startMqtt() {
        try {
            client = MqttClient.builder()
                    .useMqttVersion5()
                    .serverHost(host)
                    .serverPort(8883)
                    .sslWithDefaultConfig()
                    .buildBlocking();

            client.connectWith()
                    .simpleAuth()
                    .username(username)
                    .password(StandardCharsets.UTF_8.encode(password))
                    .applySimpleAuth()
                    .send();

            System.out.println("Connected successfully");

            client.subscribeWith()
                    .topicFilter("Thuha/esp32/sensor_data")
                    .send();

            // Set a callback handler for incoming messages
            client.toAsync().publishes(ALL, publish -> handleIncomingMessage(publish));
        } catch (Exception e) {
            System.err.println("Error starting MQTT client: " + e.getMessage());
        }
    }

    private void startWindScheduler() {
        windScheduler = Executors.newScheduledThreadPool(1);
        windScheduler.scheduleAtFixedRate(() -> {
            wind = new Random().nextInt(101); // Tạo số ngẫu nhiên từ 0 đến 100
            System.out.println("New wind speed: " + wind);
        }, 0, 10, TimeUnit.SECONDS); // Khởi động ngay lập tức và sau đó mỗi 10 giây
    }

    private void handleIncomingMessage(Mqtt5Publish publish) {
        try {
            String message = StandardCharsets.UTF_8.decode(publish.getPayload().get()).toString();
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();

            float temperature = json.get("temperature").getAsFloat();
            int roundedTemperature = Math.round(temperature);

            float humidity = json.get("humidity").getAsFloat();
            int roundedHumidity = Math.round(humidity);

            float light = json.get("light").getAsFloat();
            int roundedLight = Math.round(light);
            long timestamp = json.get("timestamp").getAsLong();

            // Sử dụng giá trị gió đã được cập nhật
            saveSensorData(roundedTemperature, roundedHumidity, roundedLight, wind, timestamp);

            // Call the callback handler if it is set
            if (callbackHandler != null) {
                callbackHandler.onMessageReceived(message);
            }
        } catch (Exception e) {
            System.err.println("Error processing incoming message: " + e.getMessage());
        }
    }

    private void saveSensorData(int temperature, int humidity, int light, int wind, long timestamp) {
        LocalDateTime now = LocalDateTime.now();
        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

        // Chuyển LocalDateTime sang String
        String formattedTime = now.format(formatter);
        SensorData sensorData = new SensorData()
                .temperature(temperature)
                .humidity(humidity)
                .light(light)
                .wind(wind)
                .timeStr(formattedTime)
                .timestamp(timestamp)
                .time(now);

        sensorDataRepository.save(sensorData);
    }

    public void sendMessage(String topic, String message) {
        System.out.println("Sending message to topic: " + topic);
        System.out.println("Message content: " + message);

        Mqtt5AsyncClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildAsync();

        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(StandardCharsets.UTF_8.encode(password))
                .applySimpleAuth()
                .send()
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        System.err.println("Connection failed: " + throwable.getMessage());
                    } else {
                        System.out.println("Connected successfully");
                        client.publishWith()
                                .topic(topic)
                                .payload(StandardCharsets.UTF_8.encode(message))
                                .send()
                                .whenComplete((pubResult, pubThrowable) -> {
                                    if (pubThrowable != null) {
                                        System.err.println("Publish failed: " + pubThrowable.getMessage());
                                    } else {
                                        System.out.println("Message published successfully");
                                    }
                                });
                    }
                });
    }

    // Method to set the callback handler
    public void setCallback(CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    // Callback interface for handling messages
    public interface CallbackHandler {
        void onMessageReceived(String message);
    }
}