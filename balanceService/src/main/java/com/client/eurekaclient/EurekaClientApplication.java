package com.client.eurekaclient;

import com.fasterxml.jackson.core.json.JsonReadContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EurekaClientApplication {
    public static void main(String[] args) {
//        String json = """
//                {
//                    "glossary": {
//                        "title": "example glossary",
//                		"GlossDiv": {
//                            "title": "S",
//                			"GlossList": {
//                                "GlossEntry": {
//                                    "ID": "SGML",
//                					"SortAs": "SGML",
//                					"GlossTerm": "Standard Generalized Markup Language",
//                					"Acronym": "SGML",
//                					"Abbrev": "ISO 8879:1986",
//                					"GlossDef": {
//                                        "para": "A meta-markup language, used to create markup languages such as DocBook.",
//                						"GlossSeeAlso": ["GML", "XML"]
//                                    },
//                					"GlossSee": "markup"
//                                }
//                            }
//                        }
//                    }
//                }
//                """;
//        JsonReadContext jsonReadContext = JsonPath.read(json,)
//        String test = JsonPath.read(json, "$.glossary.GlossDiv.GlossList.GlossEntry.Abbrev");
//        System.out.println(test);
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}
