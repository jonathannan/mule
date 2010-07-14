/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.util;

import static org.mule.util.ExceptionUtils.containsType;
import static org.mule.util.ExceptionUtils.getDeepestOccurenceOfType;

import org.mule.api.transport.DispatchException;
import org.mule.tck.AbstractMuleTestCase;

import java.io.IOException;

public class ExceptionUtilsTestCase extends AbstractMuleTestCase
{

    public void testContainsType()
    {
        assertTrue(containsType(new IllegalArgumentException(), IllegalArgumentException.class));

        assertTrue(containsType(new Exception(new IllegalArgumentException()), IllegalArgumentException.class));

        assertTrue(containsType(new Exception(new IllegalArgumentException(new NullPointerException())),
            NullPointerException.class));

        assertTrue(containsType(new Exception(new IllegalArgumentException(new NullPointerException())),
            RuntimeException.class));

        assertTrue(containsType(new Exception(new IllegalArgumentException(new NullPointerException())),
            Exception.class));

        assertFalse(containsType(new Exception(new IllegalArgumentException(new NullPointerException())),
            IOException.class));

        // see if we can detect an interface implemented by the exception
        assertTrue(containsType(
            new Exception(new IllegalArgumentException(new DispatchException(null, null))),
            MuleExceptionHandleStatus.class));
    }

    public void testLastIndexOfType_deepestIsTheOneWeWant() throws Exception
    {
        IllegalArgumentException expected = new IllegalArgumentException("something");
        assertExpectationsForDeepestOccurence(expected);
    }

    public void testLastIndexOfType_theOneWeWantIsNotTheDeepest() throws Exception
    {
        IllegalArgumentException expected = new IllegalArgumentException("something",
            new NullPointerException("somenull"));
        assertExpectationsForDeepestOccurence(expected);

    }

    private void assertExpectationsForDeepestOccurence(IllegalArgumentException expected)
    {
        assertSame(expected, getDeepestOccurenceOfType(expected, IllegalArgumentException.class));

        assertSame(expected, getDeepestOccurenceOfType(new Exception(expected), IllegalArgumentException.class));

        assertSame(
            expected,
            getDeepestOccurenceOfType(new IllegalArgumentException(new Exception(expected)),
                IllegalArgumentException.class));

        assertNull(getDeepestOccurenceOfType(new IllegalArgumentException(new Exception(expected)),
            IOException.class));
    }

    public void testLastIndexOfType_nullParameters() throws Exception
    {
        assertNull(getDeepestOccurenceOfType(null, null));

        assertNull(getDeepestOccurenceOfType(new Exception(), null));

        assertNull(getDeepestOccurenceOfType(null, Exception.class));
    }
}
