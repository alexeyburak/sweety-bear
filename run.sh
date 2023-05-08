#!/bin/bash

DB_URL="jdbc:mysql://localhost:3306/sweetybeer"
DB_USERNAME=""
DB_PASSWORD=""
SPRING_MAIL_HOST="smtp.yandex.ru"
SPRING_MAIL_USERNAME=""
SPRING_MAIL_PASSWORD="smtp.yandex.ru"
SPRING_MAIL_PORT=""
SPRING_MAIL_PROTOCOL="smtps"

# you can insert random data here, but oauth2 with google and github will not work
GOOGLE_CLIENT_ID=""
GOOGLE_CLIENT_SECRET=""
GITHUB_CLIENT_ID=""
GITHUB_CLIENT_SECRET=""

mvn spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=$DB_URL \
                    --spring.datasource.username=$DB_USERNAME \
                    --spring.datasource.password=$DB_PASSWORD \
                    --spring.mail.host=$SPRING_MAIL_HOST \
                    --spring.mail.username=$SPRING_MAIL_USERNAME \
                    --spring.mail.password=$SPRING_MAIL_PASSWORD \
                    --spring.mail.port=$SPRING_MAIL_PORT \
                    --spring.mail.protocol=$SPRING_MAIL_PROTOCOL \
                    --spring.security.oauth2.client.registration.google.client-id=$GOOGLE_CLIENT_ID \
                    --spring.security.oauth2.client.registration.google.client-secret=$GOOGLE_CLIENT_SECRET \
                    --spring.security.oauth2.client.registration.google.scope=email,profile \
                    --spring.security.oauth2.client.registration.github.client-id=$GITHUB_CLIENT_ID \
                    --spring.security.oauth2.client.registration.github.client-secret=$GITHUB_CLIENT_SECRET \
                    --spring.security.oauth2.client.registration.github.scope=user"
