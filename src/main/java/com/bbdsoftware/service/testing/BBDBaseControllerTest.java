package com.bbdsoftware.service.testing;

import io.restassured.*;
import io.restassured.http.*;
import io.restassured.response.*;
import io.restassured.specification.*;

import org.junit.*;
import org.springframework.boot.web.server.*;
import org.springframework.restdocs.*;
import org.springframework.restdocs.constraints.*;
import org.springframework.restdocs.payload.*;
import org.springframework.restdocs.snippet.*;
import org.springframework.util.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;


// TODO: ideally this would be in a distinct test artifact
public abstract class BBDBaseControllerTest
{
    @LocalServerPort
    private void initRestAssured(final int localPort) {
        RestAssured.port = localPort;
    }

    @LocalServerPort
    public int port;

    private RequestSpecification documentationSpec;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    public RequestSpecification getDocumentationSpec() {
        return documentationSpec;
    }

    // Create the default base controller documentation spec to be used for all documented tests
    // Defines default HTTP Request settings and adds the REST documentation snippet generation
    public void setUp() throws Exception {
        //this.documentationSpec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)).build();

        CreateSwaggerJsonForDocs();
    }

    private void CreateSwaggerJsonForDocs() throws Exception {

        String outputDir = System.getProperty("io.springfox.staticdocs.outputDir");
        Response content = given()
                    .contentType(ContentType.JSON)
                .when()
                    .port(port)
                    .queryParam("group","Api")
                    .get("/v2/api-docs")

                .then()
                    
                    .assertThat()
                    .statusCode(is(200))
                    .extract().response();

        if (null != outputDir) {
            Files.createDirectories(Paths.get(outputDir));
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"), StandardCharsets.UTF_8)) {
                writer.write(content.getBody().prettyPrint());
            }
        }
    }


    public static class ConstrainedFields {
        private final ConstraintDescriptions constraintDescriptions;

        public ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        public FieldDescriptor withPath(String path) {
            return PayloadDocumentation
                    .fieldWithPath(path)
                    .attributes(Attributes.key("constraints").value(
                            StringUtils.collectionToDelimitedString(
                                    this.constraintDescriptions.descriptionsForProperty(path), ". "
                            )
                    ));
        }
    }
}
