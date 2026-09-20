package com.civicresolve.service;

import com.civicresolve.model.Complaint;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** Deterministic Java fallback for the native token-similarity module. */
@Service
public class DuplicateDetector {
    public double score(String title, String description, String location, Complaint candidate) {
        double text = similarity(title + " " + description, candidate.title + " " + candidate.description);
        double place = similarity(location, candidate.location);
        double category = candidate.category != null ? 1.0 : 0.0;
        return Math.min(1.0, (text * 0.75) + (place * 0.20) + (category * 0.05));
    }

    private double similarity(String first, String second) {
        Set<String> a = words(first), b = words(second);
        if (a.isEmpty() || b.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(a); intersection.retainAll(b);
        return (2.0 * intersection.size()) / (a.size() + b.size());
    }

    private Set<String> words(String value) {
        if (value == null) return Set.of();
        Set<String> result = new HashSet<>();
        Arrays.stream(value.toLowerCase(Locale.ROOT).split("[^\\p{Alnum}]+"))
            .filter(word -> word.length() > 1).forEach(result::add);
        return result;
    }
}
