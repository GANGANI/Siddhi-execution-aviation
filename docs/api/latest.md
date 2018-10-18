# API Docs - v1.0.0-SNAPSHOT

## Aviation

### FindFlightAvailability *<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>*

<p style="word-wrap: break-word">Returns whether the flight is available or not</p>

<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>
```
<BOOL> aviation:FindFlightAvailability(<STRING> current.location, <STRING> last.location, <STRING> current.login.time, <STRING> last.login.time)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">current.location</td>
        <td style="vertical-align: top; word-wrap: break-word">current login location</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">last.location</td>
        <td style="vertical-align: top; word-wrap: break-word">current login location</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">current.login.time</td>
        <td style="vertical-align: top; word-wrap: break-word">current login Time</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">last.login.time</td>
        <td style="vertical-align: top; word-wrap: break-word">current login Time</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
define stream FlightStream(current.location string,last.location string,current.login.time string,last.login.time string);
from FlightStream
select geo:FindFlightAvailability(current.location,last.location,current.login.time,last.login.time) as flightcount 
insert into outputStream;
```
<p style="word-wrap: break-word">This will return the flight count among the two locations within the given time period</p>

