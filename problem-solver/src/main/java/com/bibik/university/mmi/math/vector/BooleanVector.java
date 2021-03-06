package com.bibik.university.mmi.math.vector;

import com.bibik.university.mmi.math.exception.VectorException;

public interface BooleanVector {

    BooleanVector xor(BooleanVector other) throws VectorException;

    int getSize();

    boolean get(int index);

    BooleanVector set(int index, boolean value);
}
