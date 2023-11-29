/*
 * The MIT License
 *
 * Copyright 2023 Luis Daniel Mesa Velásquez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.luismesa.microservice.resources.floors.boundary;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import io.luismesa.microservice.resources.floors.entity.FloorsEntity;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
public class FloorsEndpointIT {

    private Client client;
    private WebTarget target;
    Map<String, String> floorNumbers;

    public FloorsEndpointIT() {
        this.floorNumbers = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("00000000-0000-0000-0000-000000000000", "10"),
                new AbstractMap.SimpleEntry<>("00000000-0000-0000-ffff-ffffffffffff", "11"),
                new AbstractMap.SimpleEntry<>("ffffffff-ffff-ffff-ffff-ffffffffffff", "14")
        );
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        this.client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
        this.target = this.client
                .target("http://localhost:8080/resources/floors");
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of create method, of class FloorsEndpoint.
     */
    @Test
    @Disabled
    public void testCreate() {
        System.out.println("create");
//        Buildings entity = null;
//        FloorsEndpoint instance = new FloorsEndpoint();
//        instance.create(entity);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class FloorsEndpoint.
     */
    @Test
    @Disabled
    public void testEdit() {
        System.out.println("edit");
        String id = "";
//        Buildings entity = null;
//        FloorsEndpoint instance = new FloorsEndpoint();
//        instance.edit(id, entity);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class FloorsEndpoint.
     */
    @Test
    @Disabled
    public void testRemove() {
        System.out.println("remove");
        String id = "";
//        FloorsEndpoint instance = new FloorsEndpoint();
//        instance.remove(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of find method, of class FloorsEndpoint.
     */
    @Test
    @Disabled
    public void testFind() {
        System.out.println("find");
        String id = "";
//        FloorsEndpoint instance = new FloorsEndpoint();
//        Buildings expResult = null;
//        Buildings result = instance.find(id);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAll method, of class FloorsEndpoint.
     */
    @Test
    public void testFindAll() {
        Response response = this.target.request(MediaType.APPLICATION_JSON_TYPE).get();
//        try {
        assertEquals(200, response.getStatus());
        List<FloorsEntity> floors = response.readEntity(new GenericType<List<FloorsEntity>>() {
        });
        assertEquals(3, floors.size());
        Map<String, String> collectedNumbers = floors.stream().collect(toMap(floor -> floor.getId().toLowerCase(), FloorsEntity::getNumber));
        System.out.println("collectedNumbers = " + collectedNumbers);
        System.out.println("floorNumbers = " + floorNumbers);

        assertTrue(areEqual(floorNumbers, collectedNumbers));

        //        } catch (AssertionError ex) {
        //            if (response != null) {
        //                response = this.target.request().get();
        //                assertEquals(200, response.getStatus());
        //            } else {
        //                fail("No response", ex);
        //            }
        //        }
    }

    private static boolean areEqual(Map<String, String> first, Map<String, String> second) {
        if (first.size() != second.size()) {
            return false;
        }

        return first.entrySet().stream().allMatch(e -> e.getValue().equals(second.get(e.getKey())))
                && second.entrySet().stream().allMatch(e -> e.getValue().equals(first.get(e.getKey())));
    }

//    public static final String LOCAL_MICROPROFILE_CONFIG = "microprofile-config"
//            + ".properties";
//    private static String username;
//    private static String password;
//    private static String environment;
//
//    private Client client;
//    private WebTarget target;
//
//    public AuthorizationTokenResourceIT() {
//    }
//
//    @BeforeAll
//    public static void setUpClass() {
//        try {
//            Properties properties = new Properties();
//            properties.load(AuthorizationTokenResourceIT.class.getClassLoader()
//                    .getResourceAsStream("META-INF/"
//                            + LOCAL_MICROPROFILE_CONFIG));
//            username = loadAsMicroprofileProperties(SIXDOCHUB_USERNAME_PROPERTY,
//                    properties);
//            Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                    .log(Level.INFO, "USERNAME: {0}", username);
//            password = loadAsMicroprofileProperties(SIXDOCHUB_PASSWORD_PROPERTY,
//                    properties);
//            Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                    .log(Level.INFO, "PASSWORD: {0}", password);
//            environment = loadAsMicroprofileProperties(
//                    SIXDOCHUB_ENVIRONMENT_PROPERTY, properties);
//            Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                    .log(Level.INFO, "ENVIRONMENT: {0}", environment);
//        } catch (FileNotFoundException fnfe) {
//            Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                    .log(Level.SEVERE, null, fnfe);
//        } catch (NoSuchElementException | IOException nsee) {
//            Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                    .log(Level.SEVERE, null, nsee);
//        }
//    }
//
//    private static String loadAsMicroprofileProperties(final String key,
//            final Properties properties) {
//        Optional<String> fromEnvironment = Optional.ofNullable(
//                System.getenv(key.toUpperCase().replaceAll("\\.", "_")));
//        Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                .log(Level.INFO, "FROM ENVIRONMENT: {0}", fromEnvironment);
//        Optional<String> fromSystemProperty = Optional.ofNullable(
//                System.getProperty(key));
//        Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                .log(Level.INFO, "FROM SYSTEM: {0}", fromSystemProperty);
//        Optional<String> fromPropertiesFile = Optional.ofNullable(
//                properties.getProperty(key));
//        Logger.getLogger(AuthorizationTokenResourceIT.class.getName())
//                .log(Level.INFO, "FROM FILE: {0}", fromPropertiesFile);
//        return fromEnvironment.orElseGet(()
//                -> fromSystemProperty.orElseGet(()
//                        -> fromPropertiesFile.orElse("")));
//    }
//
//    @BeforeEach
//    public void setUp() {
//        this.client = ClientBuilder.newClient();
//        this.target = this.client
//                .target("http://localhost:8080/resources/auth");
//    }
//
//    /**
//     * Test of getAuthorizationToken method, of class
//     * AuthorizationTokenResource.
//     *
//     */
//    @Test
//    @DisplayName("Integration Test getAuthorizationToken on "
//            + "AuthorizatonTokenResource")
//    public void testGetAuthorizationToken() {
//        GetAuthorizationTokenRequest request
//                = new GetAuthorizationTokenRequest();
//        request.setUsername(username);
//        request.setPassword(password);
//        Entity<GetAuthorizationTokenRequest> entity = Entity.entity(request,
//                MediaType.APPLICATION_JSON);
//        Response response = this.target.request().post(entity);
//        try {
////            fail("BOOM");
//            assertEquals(200, response.getStatus());
//        } catch (AssertionError ex) {
//            if (response != null) {
//                assertTrue(response.getStatus() >= 500);
//                response = this.target.request().post(entity);
//                assertEquals(200, response.getStatus());
//            } else {
//                fail("No response", ex);
//            }
//        }
//        Optional.ofNullable(response)
//                .filter(r -> r.getStatus() == 200)
//                .ifPresent(r -> Logger.getLogger(AuthorizationTokenResourceIT
//                        .class.getName())
//                        .log(Level.INFO, "AUTHORIZATION TOKEN: {0}",
//                                r.readEntity(GetAuthorizationTokenResponse
//                                        .class).getAuthorizationToken()));
//    }
}
