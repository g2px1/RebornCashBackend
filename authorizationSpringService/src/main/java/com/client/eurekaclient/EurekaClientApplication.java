package com.client.eurekaclient;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.text.ParseException;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EurekaClientApplication {
    public static void main(String[] args) throws JOSEException, ParseException {
//        JWTAccess jwtAccess = new JWTAccess();
//        System.out.println(jwtAccess.generateJWS("g2px1"));
        ECKey ecKey = ECKey.parse("""
        {kty=EC, d=ACC3M93DHIUXjF9xmpLZDS_GZorwfHeq8pyWWsMX4QLyO1dX-Z-8XdOe2itOceONwsOUmpvA_bwLCF3_oM0Og9ty, crv=P-521, kid=381afe98-4f77-49eb-b053-39481fbd0052, x=AZ3XwlsN0ZZnTv4Cqu9hgUmbfT1zmEmbASAAeVIzuI995SnLfv6shCU0pCSXWJVd_l7m-2bfUPtHB3hSkf330pRO, y=AUX14TQEq7g2WRqcUcdo1QgmDEpZXshhqueGezFkGJr2ugKClpcPS2CC1ibV8fAzLhhjgyO3If0DOnJ54jqqQSHc}
                """);
        JWSObject jwsObject = JWSObject.parse("eyJraWQiOiIzODFhZmU5OC00Zjc3LTQ5ZWItYjA1My0zOTQ4MWZiZDAwNTIiLCJ0eXAiOiJKV1QiLCJhbGciOiJFUzUxMiJ9.eyJpc3MiOiJodHRwczovL3JlYm9ybi5jYXNoIiwiYXVkIjoiaHR0cHM6Ly9yZWJvcm4uY2FzaCIsImV4cCI6MTY2MjY1NDUzNSwidXNlcm5hbWUiOiJnMnB4MSJ9.AOJ0HbmWKQpR5XM6K5HXk7rhvqlm8Kb289XyGYc3RO96gTddcHLyNqnMopc4OHuJ5t32cuH49s98MWW_GHz2JnlKAZPFLpAUTlRS-tjhM72Bh3VyV0smVZEWuTz2-6qYkWbwk-pSKG85fQkc_KPM9DpYbv_NFstRx_TQC0PNFqU_rN9H");
        JWSVerifier jwsVerifier = new ECDSAVerifier(ecKey);
        System.out.println(jwsObject.verify(jwsVerifier));
//        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
