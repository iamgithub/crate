/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.operation.scalar;

import com.google.common.collect.ImmutableList;
import io.crate.metadata.FunctionIdent;
import io.crate.metadata.FunctionInfo;
import io.crate.metadata.Functions;
import io.crate.operation.Input;
import io.crate.DataType;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CollectionCountFunctionTest {

    final FunctionIdent ident = new FunctionIdent(CollectionCountFunction.NAME, ImmutableList.of(DataType.STRING_SET));

    @Test
    public void testLookup() throws Exception {
        Injector injector = new ModulesBuilder().add(
                new ScalarFunctionModule()
        ).createInjector();

        Functions functions = injector.getInstance(Functions.class);

        assertEquals(ident, functions.get(ident).info().ident());
    }

    @Test
    public void testEvaluate() throws Exception {
        CollectionCountFunction function = new CollectionCountFunction(
                new FunctionInfo(
                        ident,
                        DataType.LONG)
        );

        Input inputSet = new Input<Set<String>>() {
            @Override
            public Set<String> value() {
                return new HashSet<String>(){{
                    add("foo");
                    add("bar");
                }};
            }
        };

        assertEquals(new Long(2), function.evaluate(inputSet));
    }

}
