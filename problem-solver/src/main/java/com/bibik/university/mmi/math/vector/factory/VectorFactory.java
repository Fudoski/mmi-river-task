package com.bibik.university.mmi.math.vector.factory;

import com.bibik.university.mmi.math.vector.BaseBooleanVector;
import com.bibik.university.mmi.math.vector.BooleanVector;

public class VectorFactory {

    private VectorFactory() {
    }

    public static BooleanVector of(boolean... booleans) {
        return BaseBooleanVector.of(booleans);
    }
}
