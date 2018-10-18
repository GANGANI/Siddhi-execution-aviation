package org.wso2.extension.siddhi.execution.aviation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.extension.siddhi.execution.aviation.api.AviationData;
import org.wso2.extension.siddhi.execution.aviation.api.AviationDataResolver;
import org.wso2.extension.siddhi.execution.aviation.internal.exception.AviationDataResolverException;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.ReturnAttribute;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.util.Map;

/**
 * This is a sample class-level comment, explaining what the extension class does.
 */

@Extension(
        name = "FindFlightAvailability",
        namespace = "aviation",
        description = "Returns whether the flight is available or not",
        parameters = {
                @Parameter(
                        name = "current.location",
                        description = "current login location",
                        type = {DataType.STRING}),
                @Parameter(
                        name = "last.location",
                        description = "current login location",
                        type = {DataType.STRING}),
                @Parameter(
                        name = "current.login.time",
                        description = "current login Time",
                        type = {DataType.STRING}),
                @Parameter(
                        name = "last.login.time",
                        description = "current login Time",
                        type = {DataType.STRING})
        },
        returnAttributes = @ReturnAttribute(
                description = "Check if there is any available fligh among two locations within that time period",
                type = {DataType.BOOL}),
        examples = @Example(
                description = "This will return the flight count among the two locations within the given time period",
                syntax = "define stream FlightStream(current.location string,last.location string," +
                        "current.login.time string,last.login.time string);\n" +
                        "from FlightStream\n" +
                        "select geo:FindFlightAvailability(current.location,last.location," +
                        "current.login.time,last.login.time) as flightcount \n" +
                        "insert into outputStream;")
)
public class FindFlight extends FunctionExecutor {

    private static final Log log = LogFactory.getLog(FindFlight.class);
    private AviationDataResolver aviationDataResolverImpl;


    /**
     * The initialization method for {@link FunctionExecutor}, which will be called before other methods and validate
     * the all configuration and getting the initial values.
     *
     * @param attributeExpressionExecutors are the executors of each attributes in the Function
     * @param configReader                 this hold the {@link FunctionExecutor} extensions configuration reader.
     * @param siddhiAppContext             Siddhi app runtime context
     */
    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {
        log.warn("jjjjjjjjjjjjjjjjjjjjj");
        try {
            aviationDataResolverImpl = (AviationDataResolver) Class.forName("org.wso2.extension.siddhi.execution." +
                    "aviation.internal.impl.DefaultDBBasedAviationDataResolver").newInstance();
            aviationDataResolverImpl.init(configReader);
        } catch (InstantiationException e) {
            log.error("instantiaiton error");
        } catch (IllegalAccessException e) {
            log.error("illigal actionn error");
        } catch (ClassNotFoundException e) {
            log.error("class not found");
        } catch (AviationDataResolverException e) {
            log.error("avaiation error");
        }
    }

    /**
     * The main execution method which will be called upon event arrival
     * when there are more than one Function parameter
     *
     * @param data the runtime values of Function parameters
     * @return the Function result
     */
    @Override
    protected Object execute(Object[] data) {
        AviationData aviationData;
        String currentLocation = data[0].toString();
        String lastLocation = data[1].toString();
        String currentLoginTime = data[2].toString();
        String lastLoginTime = data[3].toString();
        aviationData = aviationDataResolverImpl.getAviationDataInfo(currentLocation,
                lastLocation, currentLoginTime, lastLoginTime);
        return aviationData != null && !aviationData.getflight().equals("");
    }

    /**
     * The main execution method which will be called upon event arrival
     * when there are zero or one Function parameter
     *
     * @param data null if the Function parameter count is zero or
     *             runtime data value of the Function parameter
     * @return the Function result
     */
    @Override
    protected Object execute(Object data) {
        return null;
    }

    /**
     * return a Class object that represents the formal return type of the method represented by this Method object.
     *
     * @return the return type for the method this object represents
     */
    @Override
    public Attribute.Type getReturnType() {
        return Attribute.Type.BOOL;
    }

    /**
     * Used to collect the serializable state of the processing element, that need to be
     * persisted for reconstructing the element to the same state on a different point of time
     *
     * @return stateful objects of the processing element as an map
     */
    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    /**
     * Used to restore serialized state of the processing element, for reconstructing
     * the element to the same state as if was on a previous point of time.
     *
     * @param state the stateful objects of the processing element as a map.
     *              This is the same map that is created upon calling currentState() method.
     */
    @Override
    public void restoreState(Map<String, Object> state) {

    }
}
