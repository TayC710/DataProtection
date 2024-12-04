/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

/**
 * @author aldenml
 */
public abstract class BaseData implements DataLike {

    protected final Data data;

    public BaseData(Data data, int size) {
        if (data.size() != size)
            throw new IllegalArgumentException("data should be of size: " + size);

        this.data = data;
    }

    public Data getData() {
        return data;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public byte get(int index) {
        return data.get(index);
    }

    @Override
    public String toHex() {
        return data.toHex();
    }

    @Override
    public byte[] toBytes() {
        return data.toBytes();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseData))
            return false;
        if (this == obj)
            return true;

        return data.equals(((BaseData) obj).data);
    }
}
