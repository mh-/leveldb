/*
 * ---------------------------------------------------------------------------------------------------
 * Michael Huebler, 2020-08: As required by the license
 * ("You must cause any modified files to carry prominent notices stating that You changed the files")
 * I hereby state that I changed this file.
 * ---------------------------------------------------------------------------------------------------
 */
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
package org.iq80.leveldb.fileenv;

import com.google.common.base.Throwables;
import java.lang.reflect.Method;

import java.nio.MappedByteBuffer;

public final class ByteBufferSupport
{
    private static Method getCleaner;
    private static Method free;
    private static Method clean;
    private static boolean useOpenJdk = true;

    static {
        try {
            getCleaner = Class.forName("java.nio.DirectByteBuffer").getDeclaredMethod("cleaner");
            getCleaner.setAccessible(true);
        }
        catch (ReflectiveOperationException e) {
            useOpenJdk = false;
        }

        if (useOpenJdk) {
            try {
                Class<?> returnType = getCleaner.getReturnType();
                if (Runnable.class.isAssignableFrom(returnType)) {
                    clean = Runnable.class.getMethod("run");
                }
                else {
                    clean = returnType.getMethod("clean");
                }
            }
            catch (NoSuchMethodException e) {
                throw new AssertionError(e);
            }
        }
        else {
            try {
                free = Class.forName("java.nio.DirectByteBuffer").getDeclaredMethod("free");
                free.setAccessible(true);
            }
            catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        }
    }

    private ByteBufferSupport()
    {
    }

    public static void unmap(MappedByteBuffer buffer)
    {
        try {
            if (useOpenJdk) {
                Object cleaner = getCleaner.invoke(buffer);
                clean.invoke(cleaner);
            }
            else {
                free.invoke(buffer);
            }
        }
        catch (Exception ignored) {
            throw Throwables.propagate(ignored);
        }
    }
}
