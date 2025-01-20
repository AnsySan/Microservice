package com.clone.twitter.payment_service.mapper;

import com.clone.twitter.payment_service.dto.payment.PaymentDto;
import com.clone.twitter.payment_service.dto.payment.PaymentDtoToCreate;
import com.clone.twitter.payment_service.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    Payment toEntity(PaymentDtoToCreate dto);

    PaymentDto toDto(Payment entity);
}