/*
 *
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *
 *  contributor license agreements. See the NOTICE file distributed with
 *
 *  this work for additional information regarding copyright ownership.
 *
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *
 *  (the "License"); you may not use this file except in compliance with
 *
 *  the License. You may obtain a copy of the License at
 *
 *
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 *
 *  Unless required by applicable law or agreed to in writing, software
 *
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *
 *  limitations under the License.
 *
 * /
 */
package org.apache.kylin.engine.streaming;

import java.util.Map;

import org.apache.kylin.measure.hllc.HyperLogLogPlusCounter;
import org.apache.kylin.common.util.StreamingBatch;
import org.apache.kylin.cube.inmemcubing.ICuboidWriter;
import org.apache.kylin.dimension.Dictionary;
import org.apache.kylin.metadata.model.IBuildable;
import org.apache.kylin.metadata.model.TblColRef;

/**
 */
public interface StreamingBatchBuilder {

    IBuildable createBuildable(StreamingBatch streamingBatch);

    Map<Long, HyperLogLogPlusCounter> sampling(StreamingBatch streamingBatch);

    Map<TblColRef, Dictionary<String>> buildDictionary(StreamingBatch streamingBatch, IBuildable buildable);

    void build(StreamingBatch streamingBatch, Map<TblColRef, Dictionary<String>> dictionaryMap, ICuboidWriter cuboidWriter);

    void commit(IBuildable buildable);
}
