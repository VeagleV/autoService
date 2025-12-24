package org.application.autoService;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.application.autoService.model.Order;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<Order.PaymentStatus, String> {

    @Override
    public String convertToDatabaseColumn(Order.PaymentStatus attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case PAID -> "оплачен";
            case NOT_PAID -> "не оплачен"; // обратите внимание на пробелы
            default -> throw new IllegalArgumentException("Unknown " + attribute);
        };
    }

    @Override
    public Order.PaymentStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData.toLowerCase()) {
            case "оплачен" -> Order.PaymentStatus.PAID;
            case "не оплачен" -> Order.PaymentStatus.NOT_PAID;
            default -> throw new IllegalArgumentException("Unknown " + dbData);
        };
    }
}
