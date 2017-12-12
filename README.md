# exampleRestService

## Installation

    mvn clean install

## Configuration
Create a properties file called **app.properties** with following content

    MARVEL_PUBLIC_KEY=......
    MARVEL_PRIVATE_KEY=......
    GOOGLE_API_KEY=.....

Please replace the dot.dots above with proper API keys

## Run

    mvn jetty:run "-Denv_config_path=/PATH_TO_app.properties_FILE"
    
**env_config_path** is directory path inside which app.properties file is present.

## WebAppp

A simple AngularJs based webapp will be available at http://localhost:8080

## Swagger-UI

Swagger UI avaialble at  http://localhost:8080/swagger


