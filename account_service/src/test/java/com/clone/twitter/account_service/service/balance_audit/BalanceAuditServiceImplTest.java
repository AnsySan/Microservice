package com.clone.twitter.account_service.service.balance_audit;

import com.clone.twitter.account_service.dto.balance.BalanceUpdateDto;
import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditDto;
import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditFilterDto;
import com.clone.twitter.account_service.mapper.BalanceAuditMapper;
import com.clone.twitter.account_service.model.Account;
import com.clone.twitter.account_service.model.BalanceAudit;
import com.clone.twitter.account_service.repository.BalanceAuditRepository;
import com.clone.twitter.account_service.service.balance_audit.filter.service.BalanceAuditFilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceAuditServiceImplTest {

    @Mock
    private BalanceAuditMapper balanceAuditMapper;
    @Mock
    private BalanceAuditRepository balanceAuditRepository;
    @Mock
    private BalanceAuditFilterService balanceAuditFilterService;

    @InjectMocks
    private BalanceAuditServiceImpl balanceAuditService;

    private final long accountId = 1L;
    private BalanceAuditDto balanceAuditDto;
    private BalanceAudit balanceAudit;

    @BeforeEach
    void setUp() {

        long balanceId = 2L;
        BigDecimal actual = BigDecimal.valueOf(200);
        BigDecimal authorization = BigDecimal.valueOf(100);
        LocalDateTime createdAt = LocalDateTime.now();
        long version = 3L;

        Account account = Account.builder().id(accountId).build();

        balanceAudit = BalanceAudit.builder()
                .id(balanceId)
                .account(account)
                .actualBalance(actual)
                .authorizationBalance(authorization)
                .createdAt(createdAt)
                .version(version)
                .build();

        balanceAuditDto = BalanceAuditDto.builder()
                .id(balanceId)
                .accountId(accountId)
                .actualBalance(actual.longValue())
                .authorizationBalance(authorization.longValue())
                .createdAt(createdAt)
                .version(version)
                .build();
    }

    @Test
    void createNewAudit() {

        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto();

        when(balanceAuditMapper.toAudit(balanceUpdateDto)).thenReturn(balanceAudit);

        balanceAuditService.createNewAudit(balanceUpdateDto);

        InOrder inOrder = inOrder(balanceAuditRepository, balanceAuditMapper, balanceAuditFilterService);
        inOrder.verify(balanceAuditMapper).toAudit(balanceUpdateDto);
        inOrder.verify(balanceAuditRepository).save(balanceAudit);
    }

    @Test
    void findByAccountId() {

        BalanceAuditFilterDto balanceAuditFilterDto = new BalanceAuditFilterDto();

        when(balanceAuditRepository.findAllByAccountId(accountId)).thenReturn(List.of(balanceAudit));
        when(balanceAuditMapper.toDto(balanceAudit)).thenReturn(balanceAuditDto);
        when(balanceAuditFilterService.acceptAll(any(), eq(balanceAuditFilterDto))).thenReturn(Stream.of(balanceAudit));

        List<BalanceAuditDto> actual = balanceAuditService.findByAccountId(accountId, balanceAuditFilterDto);
        assertIterableEquals(List.of(balanceAuditDto), actual);

        InOrder inOrder = inOrder(balanceAuditRepository, balanceAuditMapper, balanceAuditFilterService);
        inOrder.verify(balanceAuditRepository).findAllByAccountId(accountId);
        inOrder.verify(balanceAuditFilterService).acceptAll(any(), eq(balanceAuditFilterDto));
        inOrder.verify(balanceAuditMapper).toDto(balanceAudit);
    }
}