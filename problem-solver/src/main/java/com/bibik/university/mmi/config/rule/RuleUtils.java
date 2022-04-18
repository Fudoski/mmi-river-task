package com.bibik.university.mmi.config.rule;

import com.bibik.university.mmi.math.vector.BooleanVector;

import java.util.List;
import java.util.stream.Collectors;

public final class RuleUtils {

    public static List<BooleanVector> filter(List<BooleanVector> vectors, List<Rule> rules) {
        return vectors.stream()
                .filter(vector -> rules.stream()
                        .map(rule -> rule.check(vector))
                        .reduce(false, (left, right) -> left || right)
                ).collect(Collectors.toList());
    }

    private RuleUtils() {
    }
}
