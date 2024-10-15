package com.mongodb.starter.controllers;

import com.mongodb.starter.models.ActionHistory;
import com.mongodb.starter.repository.ActionHistoryRepository;
import com.mongodb.starter.services.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class MessageController {

    @Autowired
    private MqttService mqttService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ActionHistoryRepository actionHistoryRepository;

    @MessageMapping("/sendDeviceCommand")
    @SendTo("/topic/deviceCommand")
    public String sendDeviceCommand(String message) {
        // Parse the incoming message which is expected to be formatted as "topic,command"
        String[] parts = message.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid message format. Expected format: topic,command");
        }

        String topic = parts[0];  // e.g., "B21DCCN042// /esp32 /fan"
        String command = parts[1]; // e.g., "ON"


        // Use the MqttService to send the command to the specified topic
        mqttService.sendMessage(topic, command);
        LocalDateTime now = LocalDateTime.now();
        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        String[] partss = topic.split("/");

        // Lấy phần tử cuối cùng
        String device = partss[partss.length - 1];
        if (device.equals("fan")) {
            device = "Fan";
        } else if (device.equals("light")) {
            device = "Light";
        } else if (device.equals("airconditioner")) {
            device = "Air Conditioner";
        }
        String formattedTime = now.format(formatter);
        ActionHistory actionHistory = new ActionHistory()
                .action(command)
                .device(device)
                .time(now)
                .timeStr(formattedTime);

        actionHistoryRepository.save(actionHistory);

        // Return a success message or any relevant information if needed
        return "Message sent to topic: " + topic + " with command: " + command;
    }

}
