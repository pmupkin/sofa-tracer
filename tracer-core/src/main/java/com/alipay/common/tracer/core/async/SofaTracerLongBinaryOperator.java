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
import com.alipay.common.tracer.core.holder.SofaTraceContextHolder;
import io.opentracing.Scope;

import java.util.function.LongBinaryOperator;

/**
 * @author khotyn
 * @version v0.1 2021.02.18
 */
public class SofaTracerLongBinaryOperator implements LongBinaryOperator {
    private final FunctionalAsyncSupport functionalAsyncSupport;
    private final LongBinaryOperator     wrappedLongBinaryOperator;

    public SofaTracerLongBinaryOperator(LongBinaryOperator wrappedLongBinaryOperator, SofaTracer tracer) {
        this.wrappedLongBinaryOperator = wrappedLongBinaryOperator;
        functionalAsyncSupport = new FunctionalAsyncSupport(
                tracer);
    }

    @Override
    public long applyAsLong(long left, long right) {
        Scope scope = functionalAsyncSupport.doBefore();
        try {
            return wrappedLongBinaryOperator.applyAsLong(left, right);
        } finally {
            functionalAsyncSupport.doFinally(scope);
        }
    }
}