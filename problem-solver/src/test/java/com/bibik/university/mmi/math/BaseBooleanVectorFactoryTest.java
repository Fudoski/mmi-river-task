package com.bibik.university.mmi.math;

import com.bibik.university.mmi.math.exception.VectorException;
import com.bibik.university.mmi.math.vector.BaseBooleanVector;
import com.bibik.university.mmi.math.vector.BooleanVector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseBooleanVectorFactoryTest {

    @Test
    void vectorInitialization() {
        final String expected = "<0,1,0,1>";
        final String actual = BaseBooleanVector.of(false, true, false, true).toString();
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void shouldThrowExceptionOnDifferentSize() {
        Assertions.assertThrows(VectorException.class, () -> {
            BooleanVector left = BaseBooleanVector.of(true, false, true);
            BooleanVector right = BaseBooleanVector.of(false, true);
            left.xor(right);
        });
    }

    @Test
    void shouldReturnResultOfXorOperationWhenSizeIsSame() {
        final BooleanVector expected = BaseBooleanVector.of(false, false, true);
        final BooleanVector left = BaseBooleanVector.of(false, true, false);
        final BooleanVector right = BaseBooleanVector.of(false, true, true);
        final BooleanVector actual = left.xor(right);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void shouldReturnSameHashcode() {
        final long expected = BaseBooleanVector.of(true, false).hashCode();
        final long actual = BaseBooleanVector.of(true, false).hashCode();
        Assertions.assertEquals(actual, expected);
    }
}