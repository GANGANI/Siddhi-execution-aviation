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
import org.wso2.extension.siddhi.execution.aviation.internal.exception.AviationDataResolverException;
import org.wso2.extension.siddhi.execution.aviation.internal.utils.DatabaseUtils;
import org.wso2.siddhi.core.util.config.ConfigReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is the implementation class that provides the RDBMS based approach to get country,city based on the ip.
 */
public class RDBMSAviationDataResolver {
    private static final Log log = LogFactory.getLog(RDBMSAviationDataResolver.class);
    private static final RDBMSAviationDataResolver instance = new RDBMSAviationDataResolver();

    private static final String CONFIG_KEY_ISPERSIST_IN_DATABASE = "isPersistInDatabase";
    private static final String CONFIG_KEY_DATASOURCE = "datasource";
    private static final String DEFAULT_DATASOURCE_NAME = "aviationDB";

    private AtomicBoolean isInitialized = new AtomicBoolean(false);
    private DatabaseUtils dbUtils;
    private boolean isPersistInDatabase;

    private static final String SQL_SELECT_FLIGHTCOUNT_FROM_FLIGHTS = "SELECT Flight FROM " +
            "Flights WHERE FlightTo = ? AND FlightFrom = ? AND Arrival < ?  AND Departure > ?";

    public static RDBMSAviationDataResolver getInstance() {
        return instance;
    }

    public void init(ConfigReader configReader) throws AviationDataResolverException {
        log.info("klkl");
        if (isInitialized.get()) {
            return;
        }
        isPersistInDatabase = Boolean.parseBoolean(configReader.readConfig(CONFIG_KEY_ISPERSIST_IN_DATABASE, "true"));

        dbUtils = DatabaseUtils.getInstance();
        dbUtils.initialize(configReader.readConfig(CONFIG_KEY_DATASOURCE, DEFAULT_DATASOURCE_NAME));
        isInitialized.set(true);
    }

    public AviationData getAviationData(String currentLocation, String lastLocation,
                                        String currentLoginTime, String lastLoginTime) {
        AviationData aviationData = null;
        Connection connection = null;
        try {
            connection = dbUtils.getConnection();
            aviationData = loadAviationData(currentLocation, lastLocation,
                        currentLoginTime, lastLoginTime, connection);
        } catch (SQLException e) {
            log.error("Cannot retrieve the location from database", e);
        } finally {
            log.info("ttt");
            dbUtils.closeAllConnections(null, connection, null);
        }
        return aviationData;
    }

    /**
     * Calls external system or database database to find the IPv6 adress to location details.
     * Can be used by an extended class.
     * @param currentLocation currentLocation
     * @param lastLocation lastLocation
     * @param currentLoginTime currentLoginTime
     * @param lastLoginTime lastLoginTime
     * @param connection the Db connection to be used. Do not close this connection within this method.
     * @return null
     */
    private AviationData loadAviationData(String currentLocation, String lastLocation,
                                          String currentLoginTime, String lastLoginTime,
                                          Connection connection) throws SQLException {
        AviationData aviationData = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (isPersistInDatabase) {
                statement = connection.prepareStatement(SQL_SELECT_FLIGHTCOUNT_FROM_FLIGHTS);
                statement.setString(1, currentLocation);
                statement.setString(2, lastLocation);
                statement.setInt(3, Integer.parseInt(currentLoginTime));
                statement.setInt(4, Integer.parseInt(lastLoginTime));
                resultSet = statement.executeQuery();
            }
            if (resultSet != null && resultSet.next()) {
                log.warn(resultSet.getString(1));
                aviationData = new AviationData(resultSet.getString(1));
            }
        } finally {
            dbUtils.closeAllConnections(statement, null, resultSet);
        }
        return aviationData;
    }
}
