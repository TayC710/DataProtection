/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

/**
 * Provide an immutable interface to a byte buffer.
 *
 * @author aldenml
 */
public interface DataLike {

    int size();

    byte get(int index);

    String toHex();

    byte[] toBytes();
}
