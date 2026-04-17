package com.example.proxy.repository;

import com.example.proxy.model.DailyUsageEntry;
import com.example.proxy.model.UserUsageState;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UsageRepository {
    UserUsageState getOrCreateUserState(String userId);

    Optional<UserUsageState> findUserState(String userId);

    DailyUsageEntry getOrCreateDailyEntry(String userId, LocalDate date);

    List<DailyUsageEntry> findLastDailyEntries(String userId, int maxDays);

    Map<String, UserUsageState> getAllUserStates();

    void pruneHistoryBefore(LocalDate cutoffDate);
}

