package com.bibik.university.mmi.config.reader;

import com.bibik.university.mmi.config.BaseTaskConfig;
import com.bibik.university.mmi.config.TaskConfig;
import com.bibik.university.mmi.config.rule.Rule;
import com.bibik.university.mmi.entity.TaskEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.ACTION_POWER;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.ACTION_RULE;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.DEADLOCK_RULE;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.ENTITY;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.LABELS;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.NEXT_LINE;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.POSITION;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.POSITION_GOAL;
import static com.bibik.university.mmi.config.reader.ConfigReaderConstants.POSITION_START;

public class BaseConfigReader implements ConfigReader {

    private static final String EQUAL = "==";

    private static final String ONLY = "only";
    private static final String AND = "and";

    private final InputStream inputStream;
    private BaseTaskConfig config;

    public BaseConfigReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public TaskConfig read() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            config = new BaseTaskConfig();
            String prevLine = null;
            while (reader.ready()) {
                String line = reader.readLine().strip();
                if (line.endsWith(NEXT_LINE)) {
                    String subLine = line.substring(0, line.length() - 1);
                    if (prevLine == null) {
                        prevLine = subLine;
                    } else {
                        prevLine = prevLine.concat(subLine);
                    }
                    continue;
                }
                if (prevLine != null) {
                    handle(prevLine.concat(line));
                    prevLine = null;
                } else {
                    handle(line);
                }
            }
        }
        return config;
    }

    private void handle(String line) {
        line = line.strip();
        if (line.startsWith(LABELS)) {
            handleLabels(line);
        }
        if (line.startsWith(ENTITY)) {
            handleEntity(line);
        }
        if (line.startsWith(DEADLOCK_RULE)) {
            handleDeadlockRules(line);
        }
        if (line.startsWith(ACTION_RULE)) {
            handleActionRule(line);
        }
        if (line.startsWith(ACTION_POWER)) {
            handleActionPower(line);
        }
    }

    private void handleLabels(String line) {
        String[] labels = line.substring(line.indexOf("=") + 1).split(",");
        for (int i = 0; i < labels.length; i++) {
            this.config.addEntity(new TaskEntity(labels[i], i));
        }
    }

    private void handleEntity(String line) {
        line = line.substring(ENTITY.length() + 1);
        String label = line.substring(0, line.indexOf("."));
        TaskEntity entity = config.getEntity(label);
        if (line.contains(POSITION)) {
            handleEntityPosition(entity, line.substring(line.indexOf("=") + 1));
        }
    }

    private void handleEntityPosition(TaskEntity entity, String position) {
        if (POSITION_START.equals(position)) {
            config.addStartPosition(entity.getVectorIndex(), false);
        }
        if (POSITION_GOAL.equals(position)) {
            config.addStartPosition(entity.getVectorIndex(), true);
        }
    }

    private void handleDeadlockRules(String line) {
        parseRules(line).forEach(rule -> config.addDeadlockRule(rule));
    }

    private void handleActionRule(String line) {
        parseRules(line).forEach(rule -> config.addActionRule(rule));
    }

    private void handleActionPower(String line) {
        String value = line.substring(line.indexOf("=") + 1);
        config.setActionPower(Integer.valueOf(value));
    }

    private List<Rule> parseRules(String line) {
        List<Rule> rules = new ArrayList<>();
        line = line.substring(line.indexOf("=") + 1);
        String[] ruleExpression = line.strip().split(",");
        for (String expression : ruleExpression) {
            String[] groups = expression.split(AND);
            Rule rule = null;
            for (String group : groups) {
                String[] operands = group.strip().split(" ");
                TaskEntity left = config.getEntity(operands[0]);
                TaskEntity right = config.getEntity(operands[2]);
                Rule newRule;
                if (ONLY.equals(operands[1]) && left.equals(right)) {
                    newRule = (vector -> vector.get(left.getVectorIndex()));
                } else if (EQUAL.equals(operands[1])) {
                    newRule = (vector -> vector.get(left.getVectorIndex()) == vector.get(right.getVectorIndex()));
                } else {
                    newRule = (vector -> vector.get(left.getVectorIndex()) != vector.get(right.getVectorIndex()));
                }
                if (rule == null) {
                    rule = newRule;
                } else {
                    Rule finalRule = rule;
                    rule = (vector -> finalRule.check(vector) && newRule.check(vector));
                }
            }
            rules.add(rule);
        }
        return rules;
    }
}
