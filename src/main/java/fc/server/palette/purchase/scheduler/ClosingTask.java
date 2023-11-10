package fc.server.palette.purchase.scheduler;

import fc.server.palette.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ClosingTask {
    private final PurchaseService purchaseService;

    @Scheduled(cron = "0 0 0 * * *")
    public void task(){
        purchaseService.editClosingStatus(LocalDate.now());
    }
}
