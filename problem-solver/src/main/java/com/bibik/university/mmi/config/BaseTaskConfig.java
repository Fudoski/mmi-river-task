package com.bibik.university.mmi.config;

import com.bibik.university.mmi.config.rule.Rule;
import com.bibik.university.mmi.entity.TaskEntity;
import com.bibik.university.mmi.math.vector.BooleanVector;
import com.bibik.university.mmi.math.vector.factory.VectorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTaskConfig implements TaskConfig {

    private final HashMap<String, TaskEntity> entityMap;
    private final Map<Integer, Boolean> startEntityPosition;
    private final List<Rule> deadlockRules;

    private final List<Rule> actionRules;

    private int actionPower;

    public BaseTaskConfig() {
        this.entityMap = new HashMap<>();
        this.startEntityPosition = new HashMap<>();
        this.deadlockRules = new ArrayList<>();
        this.actionRules = new ArrayList<>();
    }

    public void addEntity(TaskEntity taskEntity) {
        entityMap.put(taskEntity.getLabel(), taskEntity);
    }

    @Override
    public TaskEntity getEntity(String label) {
        return entityMap.get(label);
    }

    @Override
    public BooleanVector getStartPosition() {
        boolean[] booleans = new boolean[startEntityPosition.entrySet().size()];
        startEntityPosition.forEach((key, value) -> booleans[key] = value);
        return VectorFactory.of(booleans);
    }

    @Override
    public List<Rule> getDeadlockRules() {
        return this.deadlockRules;
    }

    @Override
    public List<Rule> getActionRules() {
        return this.actionRules;
    }

    @Override
    public int getActionPower() {
        return this.actionPower;
    }

    public void addStartPosition(int position, boolean value) {
        this.startEntityPosition.put(position, value);
    }

    public void addDeadlockRule(Rule rule) {
        this.deadlockRules.add(rule);
    }

    public void addActionRule(Rule rule) {
        this.actionRules.add(rule);
    }

    public void setActionPower(Integer power) {
        this.actionPower = power;
    }
}
