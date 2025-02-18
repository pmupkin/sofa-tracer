/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.common.tracer.core.async;

import com.alipay.common.tracer.core.SofaTracer;
import com.alipay.common.tracer.core.context.trace.SofaTraceContext;
import com.alipay.common.tracer.core.holder.SofaTraceContextHolder;
import io.opentracing.Scope;

import java.util.concurrent.Callable;

/**
 * Callable that passes Span between threads. The Span name is
 * taken either from the passed value or from the interface.
 *
 * @author luoguimu123
 * @version $Id: Callable.java, v 0.1 June 19, 2017 5:52 PM luoguimu123 Exp $
 */
public class SofaTracerCallable<T> implements Callable<T> {
    private Callable<T>            wrappedCallable;
    private FunctionalAsyncSupport functionalAsyncSupport;


    public SofaTracerCallable(Callable<T> wrappedCallable, SofaTracer tracer) {
        this.initCallable(wrappedCallable, tracer);
    }

    private void initCallable(Callable<T> wrappedCallable, SofaTracer tracer) {
        this.wrappedCallable = wrappedCallable;
        this.functionalAsyncSupport = new FunctionalAsyncSupport(tracer);
    }

    @Override
    public T call() throws Exception {
       Scope scope =  functionalAsyncSupport.doBefore();
        try {
            return wrappedCallable.call();
        } finally {
            functionalAsyncSupport.doFinally(scope);
        }
    }

}