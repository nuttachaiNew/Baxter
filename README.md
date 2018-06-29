#Tech 
jdk 1.8

### Application
change configuration at file `./src/main/resources/application.properties`
#### default port 
server.port = 8888


###Run
mvn clean compile spring-boot:run -Pstand-alone


###TEST 
$ mvn clean compile test -Ptest
$ mvn clean compile test -Dtest=<ClassName>#<method> -Ptest


##deploy 

mvn clean compile package -Dmaven.test.skip=true -P deploy-jboss
