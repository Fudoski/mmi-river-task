package com.bibik.university.mmi.config.reader;

import com.bibik.university.mmi.config.TaskConfig;
import com.bibik.university.mmi.config.rule.Rule;
import com.bibik.university.mmi.config.rule.RuleUtils;
import com.bibik.university.mmi.entity.TaskEntity;
import com.bibik.university.mmi.math.vector.BooleanVector;
import com.bibik.university.mmi.math.vector.factory.VectorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class BaseConfigReaderTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "/labels-test.properties"
    })
    void testConfig(String file) throws IOException {
        shouldReadConfigProperties(getInputStream(file));
    }

    private InputStream getInputStream(String file) throws IOException {
        URL resource = this.getClass().getClassLoader().getResource(this.getClass().getPackageName());
        assert resource != null;
        return new FileInputStream(new File(resource.getFile(), file));
    }

    private void shouldReadConfigProperties(InputStream inputStream) throws IOException {
        BaseConfigReader configReader = new BaseConfigReader(inputStream);
        TaskConfig config = configReader.read();

        TaskEntity boat = config.getEntity("boat");
        TaskEntity wolf = config.getEntity("wolf");
        TaskEntity goat = config.getEntity("goat");
        TaskEntity cabbage = config.getEntity("cabbage");

        Assertions.assertNotNull(boat);
        Assertions.assertNotNull(wolf);
        Assertions.assertNotNull(goat);
        Assertions.assertNotNull(cabbage);

        Assertions.assertEquals(0, boat.getVectorIndex());
        Assertions.assertEquals(1, wolf.getVectorIndex());
        Assertions.assertEquals(2, goat.getVectorIndex());
        Assertions.assertEquals(3, cabbage.getVectorIndex());

        BooleanVector expectedStartPosition = VectorFactory.of(false, false, true, false);
        BooleanVector startPosition = config.getStartPosition();
        Assertions.assertEquals(expectedStartPosition, startPosition);

        List<BooleanVector> expectedDeadlocks = new ArrayList<>();
        expectedDeadlocks.add(VectorFactory.of(false, true, true, false));
        expectedDeadlocks.add(VectorFactory.of(true, false, false, false));

        List<BooleanVector> actualDeadlocks = new ArrayList<>();
        actualDeadlocks.add(VectorFactory.of(false, true, true, false));
        actualDeadlocks.add(VectorFactory.of(true, false, false, false));
        actualDeadlocks.add(VectorFactory.of(false, false, false, false));
        actualDeadlocks.add(VectorFactory.of(true, true, true, true));
        actualDeadlocks = RuleUtils.filter(actualDeadlocks, config.getDeadlockRules());

        Assertions.assertEquals(expectedDeadlocks, actualDeadlocks);

        List<Rule> actionRules = config.getActionRules();

        List<BooleanVector> expectedStates = new ArrayList<>();
        expectedStates.add(VectorFactory.of(true, false, false, false));

        List<BooleanVector> actualStates = new ArrayList<>();
        actualStates.add(VectorFactory.of(false, false, false, false));
        actualStates.add(VectorFactory.of(true, false, false, false));
        actualStates = RuleUtils.filter(actualStates, actionRules);

        Assertions.assertEquals(expectedStates, actualStates);

        final int expectedActionPower = 2;
        Assertions.assertEquals(expectedActionPower, config.getActionPower());
    }
}