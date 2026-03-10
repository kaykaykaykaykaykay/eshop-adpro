package id.ac.ui.cs.advprog.eshop.model;
import lombok.Getter;
import java.util.Map;
import java.util.List;
@Getter
public class Payment {
    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = "WAITING_PAYMENT";
    }

    public void setStatus(String status) {
        if (List.of("WAITING_PAYMENT", "SUCCESS", "REJECTED").contains(status)) {
            this.status = status;
        }
    }
}
