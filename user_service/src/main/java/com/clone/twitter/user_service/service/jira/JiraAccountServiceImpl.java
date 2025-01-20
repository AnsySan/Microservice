package com.clone.twitter.user_service.service.jira;

import com.clone.twitter.user_service.dto.jira.JiraAccountDto;
import com.clone.twitter.user_service.entity.jira.JiraAccount;
import com.clone.twitter.user_service.exception.NotFoundException;
import com.clone.twitter.user_service.mapper.jira.JiraAccountMapper;
import com.clone.twitter.user_service.repository.jira.JiraAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraAccountServiceImpl implements JiraAccountService {

    private final JiraAccountRepository jiraAccountRepository;
    private final JiraAccountMapper jiraAccountMapper;

    @Override
    public JiraAccountDto addJiraAccount(long userId, JiraAccountDto jiraAccountDto) {

        jiraAccountDto.setUserId(userId);
        JiraAccount jiraAccount = jiraAccountMapper.toEntity(jiraAccountDto);
        jiraAccount = jiraAccountRepository.save(jiraAccount);

        return jiraAccountMapper.toDto(jiraAccount);
    }

    @Override
    public JiraAccountDto getJiraAccountInfo(long userId) {

        JiraAccount account = jiraAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Jira account with userId=" + userId + " not found"));

        return jiraAccountMapper.toDto(account);
    }
}
