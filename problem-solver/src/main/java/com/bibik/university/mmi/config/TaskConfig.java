package com.bibik.university.mmi.config;

import com.bibik.university.mmi.config.rule.Rule;
import com.bibik.university.mmi.entity.TaskEntity;
import com.bibik.university.mmi.math.vector.BooleanVector;

import java.util.List;

public interface TaskConfig {

    TaskEntity getEntity(String label);

    BooleanVector getStartPosition();

    List<Rule> getDeadlockRules();

    List<Rule> getActionRules();

    int getActionPower();
}
