package com.bibik.university.mmi.math.vector;

import com.bibik.university.mmi.math.exception.VectorException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;

public class BaseBooleanVector implements BooleanVector {

    private final boolean[] array;

    private BaseBooleanVector(boolean[] values) {
        this.array = values;
    }

    @Override
    public BooleanVector xor(BooleanVector other) {
        verifySize(other);
        boolean[] result = new boolean[this.array.length];
        for (int i = 0; i < this.array.length; i++) {
            result[i] = this.array[i] ^ other.get(i);
        }
        return of(result);
    }

    @Override
    public int getSize() {
        return array.length;
    }

    @Override
    public boolean get(int index) {
        return this.array[index];
    }

    @Override
    public BooleanVector set(int index, boolean value) {
        boolean[] booleans = Arrays.copyOf(this.array, this.array.length);
        booleans[index] = value;
        return of(booleans);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseBooleanVector that = (BaseBooleanVector) o;

        return new EqualsBuilder().append(array, that.array).isEquals();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<");
        for (int i = 0; i < this.array.length; i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(array[i] ? "1" : "0");
        }
        builder.append(">");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(array).toHashCode();
    }

    private void verifySize(BooleanVector other) throws VectorException {
        if (other.getSize() != array.length) {
            String errorMessage = String.format(
                    "Vector must have the same size! left:%s (%d) right:%s (%d)",
                    this, this.array.length,
                    other, other.getSize()
            );
            throw new VectorException(errorMessage);
        }
    }

    public static BooleanVector of(boolean... values) {
        return new BaseBooleanVector(values);
    }
}
