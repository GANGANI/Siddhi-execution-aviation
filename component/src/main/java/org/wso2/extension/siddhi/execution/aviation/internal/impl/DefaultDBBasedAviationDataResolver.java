/*
*  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.extension.siddhi.execution.aviation.internal.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.extension.siddhi.execution.aviation.api.AviationData;
import org.wso2.extension.siddhi.execution.aviation.api.AviationDataResolver;
import org.wso2.extension.siddhi.execution.aviation.internal.exception.AviationDataResolverException;
import org.wso2.siddhi.core.util.config.ConfigReader;

/**
 * The default implementation of the GeoLocationResolver interface. This is implemented based on RDBMS.
 */
public class DefaultDBBasedAviationDataResolver implements AviationDataResolver {
    private static final Log log = LogFactory.getLog(DefaultDBBasedAviationDataResolver.class);

    @Override
    public void init(ConfigReader configReader) throws AviationDataResolverException {
        RDBMSAviationDataResolver.getInstance().init(configReader);
    }

    @Override
    public AviationData getAviationDataInfo(String currentLocation, String lastLocation,
                                            String currentLoginTime, String lastLoginTime)   {
        AviationData aviationData;
        aviationData = RDBMSAviationDataResolver.getInstance().getAviationData
                (currentLocation, lastLocation, currentLoginTime, lastLoginTime);
        return aviationData != null ? aviationData : new AviationData("");
    }

}
