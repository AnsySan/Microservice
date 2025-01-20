package com.clone.twitter.project_service.model;

import com.clone.twitter.project_service.dto.client.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donation")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long paymentNumber;
    private BigDecimal amount;
    @CreationTimestamp
    private LocalDateTime donationTime;
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Long userId;
}
