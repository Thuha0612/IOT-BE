package com.mongodb.starter.controllers;

import com.mongodb.starter.controllers.errors.BadRequestAlertException;
import com.mongodb.starter.models.ActionHistory;
import com.mongodb.starter.models.SensorData;
import com.mongodb.starter.repository.ActionHistoryRepository;
import com.mongodb.starter.services.ActionHistoryService;
import com.mongodb.starter.services.MqttService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * REST controller for managing {@link com.mongodb.starter.models.ActionHistory}.
 */
@RestController
@RequestMapping("/api/action-histories")
public class ActionHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ActionHistoryResource.class);

    private static final String ENTITY_NAME = "actionHistory";

    @Autowired
    private MqttService mqttService;
    // Map để lưu trạng thái thiết bị
    private final Map<String, String> deviceStatusMap = new ConcurrentHashMap<>();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

    @Autowired
    private ActionHistoryService actionHistoryService;


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActionHistoryRepository actionHistoryRepository;

    public ActionHistoryResource(ActionHistoryRepository actionHistoryRepository) {
        this.actionHistoryRepository = actionHistoryRepository;
    }

    @PostMapping("")
    public ResponseEntity<ActionHistory> createActionHistory(@Valid @RequestBody ActionHistory actionHistory) throws URISyntaxException {
        LOG.debug("REST request to save ActionHistory : {}", actionHistory);
        if (actionHistory.getId() != null) {
            throw new BadRequestAlertException("A new actionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        actionHistory = actionHistoryRepository.save(actionHistory);
        return ResponseEntity.created(new URI("/api/action-histories/" + actionHistory.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, actionHistory.getId()))
                .body(actionHistory);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ActionHistory> updateActionHistory(
            @PathVariable(value = "id", required = false) final String id,
            @Valid @RequestBody ActionHistory actionHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update ActionHistory : {}, {}", id, actionHistory);
        if (actionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actionHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        actionHistory = actionHistoryRepository.save(actionHistory);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, actionHistory.getId()))
                .body(actionHistory);
    }


    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActionHistory> partialUpdateActionHistory(
            @PathVariable(value = "id", required = false) final String id,
            @NotNull @RequestBody ActionHistory actionHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ActionHistory partially : {}, {}", id, actionHistory);
        if (actionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actionHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActionHistory> result = actionHistoryRepository
                .findById(actionHistory.getId())
                .map(existingActionHistory -> {
                    if (actionHistory.getDevice() != null) {
                        existingActionHistory.setDevice(actionHistory.getDevice());
                    }
                    if (actionHistory.getAction() != null) {
                        existingActionHistory.setAction(actionHistory.getAction());
                    }
                    if (actionHistory.getTime() != null) {
                        existingActionHistory.setTime(actionHistory.getTime());
                    }

                    return existingActionHistory;
                })
                .map(actionHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, actionHistory.getId())
        );
    }


    @GetMapping("")
    public List<ActionHistory> getAllActionHistories() {
        LOG.debug("REST request to get all ActionHistories");
        return actionHistoryRepository.findAll();
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActionHistory(@PathVariable("id") String id) {
        LOG.debug("REST request to delete ActionHistory : {}", id);
        actionHistoryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
    //
    @PostMapping("/{device}/OFF")
    public ResponseEntity<String> turnOffDevice(@PathVariable String device) {
        String topic = "Thuha/esp32/" + device + "/status";

        // Gửi lệnh tắt thiết bị qua MQTT
        mqttService.sendMessage("Thuha/esp32/" + device, "OFF");

        // Cập nhật trạng thái tạm thời của thiết bị thành "turning-off"
        deviceStatusMap.put(device, "OFF");

        saveDeviceAction(device, "OFF");


        // Trả về thông báo là lệnh tắt đã được gửi
        return ResponseEntity.ok(device + " đang được tắt...");
    }

    @PostMapping("/{device}/ON")
    public ResponseEntity<String> turnOnDevice(@PathVariable String device) {
        String topic = "Thuha/esp32/" + device + "/status";

        // Gửi lệnh tắt thiết bị qua MQTT
        mqttService.sendMessage("Thuha/esp32/" + device, "ON");

        // Cập nhật trạng thái tạm thời của thiết bị thành "turning-off"
        deviceStatusMap.put(device, "ON");

        saveDeviceAction(device, "ON");


        // Trả về thông báo là lệnh tắt đã được gửi
        return ResponseEntity.ok(device + " đang được bật...");
    }

    public void onMessageReceived(String topic, String message) {
        String[] topicParts = topic.split("/");
        String device = topicParts[2]; // Lấy tên thiết bị từ topic

        // Nếu nhận được trạng thái thiết bị, cập nhật vào Map
        if (topic.endsWith("/status")) {
            deviceStatusMap.put(device, message);
        }
    }

    @PostMapping("/warning_light/ON")
    public ResponseEntity<String> turnOnWarning() {
        String topic = "Thuha/esp32/warning_light"; // Chủ đề của MQTT
        String message = "ON"; // Thông điệp gửi đi, có thể là trạng thái ON

        // Gửi lệnh bật thiết bị qua MQTT với chủ đề và thông điệp
        mqttService.sendMessage(topic, message);

        // Trả về thông báo là lệnh bật đã được gửi
        return ResponseEntity.ok("Đèn cảnh báo đang được bật...");
    }






    /**
     * API để client polling trạng thái thiết bị
     */
    @GetMapping("/{device}/status")
    public ResponseEntity<String> getDeviceStatus(@PathVariable String device) {
        String status = deviceStatusMap.getOrDefault(device, "unknown");
        return ResponseEntity.ok(status);
    }
//

    private void saveDeviceAction(String device, String status) {
        // Lưu trạng thái thiết bị vào MongoDB
        if (Objects.equals(device, "fan")) {device = "Fan";}
        else if (Objects.equals(device, "light")) {device = "Light";}
        else if (Objects.equals(device, "airconditioner")) {device = "Air Conditioner";}

        // Lưu lịch sử hành động
        ActionHistory actionHistory = new ActionHistory();
        actionHistory.setDevice(device);
        actionHistory.setAction(status);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        String formattedTime = LocalDateTime.now().format(formatter);
        actionHistory.setTime(LocalDateTime.now());
        actionHistory.setTimeStr(formattedTime);
        actionHistoryRepository.save(actionHistory);
    }

}
