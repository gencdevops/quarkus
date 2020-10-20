/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jaxrs.api.rs.ext.interceptor.reader.readerinterceptorcontext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.sun.ts.tests.jaxrs.common.util.JaxrsUtil;

@Provider
public class ExceptionThrowingStringBeanEntityProvider
        implements MessageBodyReader<ExceptionThrowingStringBean>,
        MessageBodyWriter<ExceptionThrowingStringBean> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return type == ExceptionThrowingStringBean.class;
    }

    @Override
    public long getSize(ExceptionThrowingStringBean t, Class<?> type,
            Type genericType, Annotation[] annotations, MediaType mediaType) {
        return t.get().length();
    }

    @Override
    public void writeTo(ExceptionThrowingStringBean t, Class<?> type,
            Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        entityStream.write(t.get().getBytes());
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return isWriteable(type, genericType, annotations, mediaType);
    }

    @Override
    public ExceptionThrowingStringBean readFrom(
            Class<ExceptionThrowingStringBean> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        String entity = JaxrsUtil.readFromStream(entityStream);
        entityStream.close();
        return new ExceptionThrowingStringBean(entity);
    }

}
