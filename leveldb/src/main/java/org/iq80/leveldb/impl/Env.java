/*
 * Copyright (C) 2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iq80.leveldb.impl;

import org.iq80.leveldb.util.RandomInputFile;
import org.iq80.leveldb.util.SequentialFile;
import org.iq80.leveldb.util.WritableFile;

import java.io.File;
import java.io.IOException;

public interface Env
{
    long nowMicros();

    /**
     * Create a brand new sequentially-readable file with the specified file name.
     *
     * @return new file that can only be accessed by one thread at a time.
     * @throws IOException If the file does not exist or inaccessible.
     */
    SequentialFile newSequentialFile(File file) throws IOException;

    /**
     * Create a brand new random access read-only file with the
     * specified file name.
     *
     * @return new file that may be concurrently accessed by multiple threads.
     * @throws IOException If the file does not exist or inaccessible.
     */
    RandomInputFile newRandomAccessFile(File file) throws IOException;

    /**
     * Create an object that writes to a new file with the specified
     * name.  Deletes any existing file with the same name and creates a
     * new file.
     * <p>
     *
     * @return new file that can be accessed by one thread at a time.
     * @throws IOException If the file not writable.
     */
    WritableFile newWritableFile(File file) throws IOException;

    /**
     * Create an WritableFile that either appends to an existing file, or
     * writes to a new file (if the file does not exist to begin with).
     * <p>
     * May return an IsNotSupportedError error if this Env does
     * not allow appending to an existing file.  Users of Env (including
     * the leveldb implementation) must be prepared to deal with
     * an Env that does not support appending. TODO
     *
     * @return new or existing writable file only accessible by one thread at a time.
     * @throws IOException If the file is inaccessible.
     */
    WritableFile newAppendableFile(File file) throws IOException;
}
