# TripMeter REST API

To get up and running:
1. Install gradle which is the build tool. 
2. Make sure gradle is in your path (gradle -v from the command line should show some output).
3. Go to TripMeter/rest> and type: gradle jettyRunWar
4. Once the server's up, hit http://localhost:8080/trip/sample/ping to check if the application is running.
5. An alternate url is http://localhost:8080/trip/sample/user/{id} where id is any number. The response will be a simple user object with the specified number as the userid.
6. Responses are in JSON.
