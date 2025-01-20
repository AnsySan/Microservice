package com.clone.twitter.project_service.service.initiative.filters;

import com.clone.twitter.project_service.dto.initiative.InitiativeFilterDto;
import com.clone.twitter.project_service.model.initiative.Initiative;
import com.clone.twitter.project_service.model.initiative.InitiativeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class InitiativeStatusFilterTest {
    @Spy
    private InitiativeStatusFilter statusFilter;

    private InitiativeFilterDto filterDto;
    private Initiative initiative1, initiative2, initiative3;

    @BeforeEach
    void init() {
        filterDto = InitiativeFilterDto.builder()
                .status(InitiativeStatus.ACCEPTED)
                .build();

        initiative1 = Initiative.builder()
                .id(1L)
                .status(InitiativeStatus.IN_PROGRESS)
                .build();

        initiative2 = Initiative.builder()
                .id(2L)
                .status(InitiativeStatus.ACCEPTED)
                .build();

        initiative3 = Initiative.builder()
                .id(3L)
                .status(InitiativeStatus.ACCEPTED)
                .build();
    }

    @Test
    void isAcceptableBadDto() {
        filterDto.setStatus(null);
        assertFalse(statusFilter.isAcceptable(filterDto));
    }

    @Test
    void isAcceptableGoodDto() {
        assertTrue(statusFilter.isAcceptable(filterDto));
    }

    @Test
    void apply() {
        Initiative[] expected = new Initiative[]{initiative2, initiative3};
        Stream<Initiative> out = statusFilter.apply(Stream.of(initiative1, initiative2, initiative3), filterDto);
        assertArrayEquals(expected, out.toArray());
    }
}