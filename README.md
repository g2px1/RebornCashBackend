# RebornCashBackend

## Useful Links

1) https://github.com/Netflix/ribbon
2) https://spring.io/blog/2015/01/20/microservice-registration-and-discovery-with-spring-cloud-and-netflix-s-eureka
3) https://medium.com/@kirill.sereda/spring-cloud-netflix-zuul-api-gateway-по-русски-c1e819f042e1
4) https://stackoverflow.com/questions/59482401/trouble-using-eureka-to-register-a-microservice-with-webflux/59495811#59495811
5) https://habr.com/ru/company/otus/blog/681448/
6) https://habr.com/ru/post/486126/?ysclid=l6y3tq786r230315796
7) https://habr.com/ru/post/565752/
8) https://github.com/in28minutes/spring-boot-examples/tree/master/spring-boot-basic-microservice
9) [Feign](https://medium.com/@kirill.sereda/spring-cloud-netflix-feign-по-русски-7b8272e8e110)
10) [Feign + Hystrix](https://medium.com/@kirill.sereda/spring-cloud-netflix-hystrix-по-русски-e60e91a6770f)
11) https://github.com/senoritadeveloper01/nils-spring-microservices
12) https://medium.com/@kirill.sereda/reactive-programming-reactor-и-spring-webflux-3f779953ed45
13) https://www.baeldung.com/java-dto-pattern
14) https://spring.io/guides/topicals/spring-security-architecture
15) https://docs.spring.io/spring-security/reference/
16) https://ard333.medium.com/authentication-and-authorization-using-jwt-on-spring-webflux-29b81f813e78
17) https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.RELEASE/single/spring-cloud-netflix.html#_how_to_include_hystrix
18) https://stackoverflow.com/questions/58929693/how-to-get-hystrix-dashboard-working-again-in-spring-boot-admin-2-x
19) https://www.devglan.com/spring-boot/spring-boot-admin
20) https://www.tutorialspoint.com/spring_boot/spring_boot_oauth2_with_jwt.htm#
21) https://medium.com/@mool.smreeti/microservices-with-spring-boot-authentication-with-jwt-and-spring-security-6e10155d9db0
22) https://www.baeldung.com/spring-security-oauth-auth-server
23) https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
24) https://habr.com/ru/post/545610/
25) https://dzone.com/articles/using-nimbus-jose-jwt-in-spring-applications-why-a
26) https://habr.com/ru/company/ruvds/blog/686628/
27) https://medium.com/nuances-of-programming/как-создавать-надежные-ssl-сертификаты-для-локальной-разработки-8f73f76df3d4
28) https://stackoverflow.com/questions/58918896/how-to-use-oauth2authorizedclientmanager-vs-oauth2authorizedclientservice

### Spring tutorials
1) https://github.com/spring-projects/spring-authorization-server/tree/main/samples/default-authorizationserver/src/main/java/sample
2) https://habr.com/ru/company/otus/blog/570130/
3) https://docs.spring.io/spring-authorization-server/docs/current-SNAPSHOT/api/org/springframework/security/oauth2/server/authorization/client/JdbcRegisteredClientRepository.html
4) https://docs.spring.io/spring-authorization-server/docs/current-SNAPSHOT/api/org/springframework/security/oauth2/server/authorization/client/RegisteredClient.html#hashCode()
5) https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
6) https://docs.spring.io/spring-authorization-server/docs/current/reference/html/core-model-components.html#registered-client-repository
7) https://docs.spring.io/spring-authorization-server/docs/current/reference/html/configuration-model.html
8) https://supunbhagya.medium.com/spring-oauth2-resourceserver-oauth2-security-authorization-code-grant-flow-9eb72fd5d27d


## Some ideas:
`spring-boot-starter-oauth2-client` - should be added on microsevice which needs to have aouthorization
1) Need to copy UserDetailsImplementation to `@Bean` which returns UserDetails cause UserDetails contains authorities(such as ADMIN, MODERATOR, USER)
2) Need to authorities to `scope` function
------

# Microservices architecture

![Microservice architecture](https://raw.githubusercontent.com/Abuzik/RebornCashBackend/main/documents/images/Architecture_of_microservices.png?token=GHSAT0AAAAAABM4Z4FGBLQCGCQY3WVE5GNUYYNFGFA)

# To do list:
1) Fixed bug with increasing balance by sending NFT to our address and increasing user's balance by nftID

# MongoDB installing

```bash
sudo service mongod stop 
sudo apt purge mongo*
wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | sudo apt-key add -
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list
sudo apt-get update
sudo apt-get install -y mongodb-org=4.4.16 mongodb-org-server=4.4.16 mongodb-org-shell=4.4.16 mongodb-org-mongos=4.4.16 mongodb-org-tools=4.4.16
sudo systemctl start mongod
sudo systemctl status mongod
sudo chown -R mongodb:mongodb /var/lib/mongodb
sudo chown mongodb:mongodb /tmp/mongodb-27017.sock
```
```bash
db.createUser(
  {
    user: "admin",
    pwd: "password",
    roles: [ 
      { 
        role: "userAdminAnyDatabase", 
        db: "admin" 
      }, 
      "readWriteAnyDatabase" 
    ]
  }
)
```
