Hello, welcome to exchange rate sample application!
Before running the application please check the application.properties file under /exchange-app/src/main/resources and provide missing configuration
To run the application execute `gradlew bootRun` from the root directory
To run test execute `gradlew test` from the root directory.
To run postgresql database in docker container please use provided docker-compose file.
The default integegration to obtain exchange rates is https://openexchangerates.org/ In order for rates to be downloaded please provide corresponding api_key
There is s swagger ui under /swagger-ui/index.html# for easier api testing.
Thanks for reading!