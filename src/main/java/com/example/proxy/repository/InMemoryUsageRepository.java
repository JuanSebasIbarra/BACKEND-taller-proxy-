package com.example.proxy.repository;

import com.example.proxy.model.DailyUsageEntry;
import com.example.proxy.model.PlanType;
import com.example.proxy.model.UserUsageState;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUsageRepository implements UsageRepository {

    private final ConcurrentHashMap<String, UserUsageState> userStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<LocalDate, DailyUsageEntry>> userHistory = new ConcurrentHashMap<>();

    @Override
    public UserUsageState getOrCreateUserState(String userId) {
        return userStates.computeIfAbsent(userId, ignored -> new UserUsageState(PlanType.FREE));
    }

    @Override
    public Optional<UserUsageState> findUserState(String userId) {
        return Optional.ofNullable(userStates.get(userId));
    }

    @Override
    public DailyUsageEntry getOrCreateDailyEntry(String userId, LocalDate date) {
        ConcurrentHashMap<LocalDate, DailyUsageEntry> historyByDate =
                userHistory.computeIfAbsent(userId, ignored -> new ConcurrentHashMap<>());
        return historyByDate.computeIfAbsent(date, DailyUsageEntry::new);
    }

    @Override
    public List<DailyUsageEntry> findLastDailyEntries(String userId, int maxDays) {
        ConcurrentHashMap<LocalDate, DailyUsageEntry> historyByDate = userHistory.get(userId);
        if (historyByDate == null) {
            return List.of();
        }
        return historyByDate.values().stream()
                .sorted(Comparator.comparing(DailyUsageEntry::getDate).reversed())
                .limit(maxDays)
                .toList();
    }

    @Override
    public Map<String, UserUsageState> getAllUserStates() {
        return userStates;
    }

    @Override
    public void pruneHistoryBefore(LocalDate cutoffDate) {
        userHistory.values().forEach(historyByDate ->
                historyByDate.keySet().removeIf(date -> date.isBefore(cutoffDate))
        );
    }
}

