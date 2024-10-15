package com.mongodb.starter.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.mongodb.starter.models.ActionHistory;
import com.mongodb.starter.models.DeviceStatus;
import com.mongodb.starter.models.SensorData;
import com.mongodb.starter.repository.ActionHistoryRepository;
import com.mongodb.starter.repository.DeviceStatusRepository;
import com.mongodb.starter.repository.SensorDataRepository;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;

@Service
public class MqttDeviceStatusService implements CommandLineRunner {

    @Autowired
    private DeviceStatusRepository deviceStatusRepository;

    @Autowired
    private ActionHistoryRepository actionHistoryRepository;

    private final String host = "6b447ffc5465442e968e7bbdefe6fd17.s1.eu.hivemq.cloud";
    private final String username = "B21DCCN042";
    private final String password = "Thuha0612@";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Override
    public void run(String... args) throws Exception {
        startMqtt();
    }

    public void startMqtt() {
        try {
            final Mqtt5BlockingClient client = MqttClient.builder()
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
                    .topicFilter("Thuha/esp32/+/status") // Use + to match any single level
                    .send();


            client.toAsync().publishes(ALL, publish -> handleIncomingMessage(publish));
        } catch (Exception e) {
            System.err.println("Error starting MQTT client: " + e.getMessage());
        }
    }

    private void handleIncomingMessage(Mqtt5Publish publish) {
        try {
            String message = StandardCharsets.UTF_8.decode(publish.getPayload().get()).toString();
            System.out.println(message);
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();

            String topic = publish.getTopic().toString();
            System.out.println(topic);
            String[] topicParts = topic.split("/");
            String device = topicParts[2]; // Assuming the device name is in the 3rd position (0-indexed)
            String state = json.get("state").getAsString().toUpperCase(); // Extract state

            if (Objects.equals(device, "fan")) {device = "Fan";}
            else if (Objects.equals(device, "light")) {device = "Light";}
            else if (Objects.equals(device, "airconditioner")) {device = "Air Conditioner";}


            // Check the state and save accordingly
            if (state.equals("ON") || state.equals("OFF")) {
                saveDeviceStatus(device, state); // Pass the uppercase state
                String messageSend = "{\"device\":\"" + device + "\", \"action\":\"" + state + "\"}";
                messagingTemplate.convertAndSend("/topic/deviceStatus", messageSend);
                System.out.println("Device: " + device + " State: " + state);
            } else {
                System.err.println("Invalid state received: " + state);
            }
        } catch (Exception e) {
            System.err.println("Error processing incoming message: " + e.getMessage());
        }
    }

    private void saveDeviceStatus(String device, String status) {

        LocalDateTime now = LocalDateTime.now();
        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

        // Chuyển LocalDateTime sang String
        String formattedTime = now.format(formatter);
        DeviceStatus deviceStatus = new DeviceStatus()
                .status(status)
                .device(device)
                .time(now)
                .timeStr(formattedTime);



        deviceStatusRepository.save(deviceStatus);

//        System.out.println("Stored data in MongoDB: " + sensorData);
    }



}

