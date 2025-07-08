package org.javadominicano.servicios;

import org.javadominicano.dto.DashboardUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DashboardWebSocketPublisher {

    @Autowired
    private DashboardDataService dashboardDataService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 8000)
    public void publishUpdate() {
        DashboardUpdateDTO dto = new DashboardUpdateDTO(
                dashboardDataService.obtenerSeries(),
                dashboardDataService.obtenerMediciones());
        messagingTemplate.convertAndSend("/topic/dashboard", dto);
    }
}
