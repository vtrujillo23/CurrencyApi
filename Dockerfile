FROM openjdk:11
ADD target/CurrencyApi.jar CurrencyApi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "CurrencyApi.jar"]