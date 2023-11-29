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
package io.luismesa.microservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.sessions.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@ExtendWith(MockitoExtension.class)
public class EnvironmentDatabaseSessionTunerTest {

    ByteArrayOutputStream OUTPUT_STREAM;

    Handler TESTING_MESSAGE_HANDLER;

    @Spy
    Map<String, String> properties = new HashMap<>(16);

    @Mock
    Session session;

    public EnvironmentDatabaseSessionTunerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        OUTPUT_STREAM = new ByteArrayOutputStream();
        TESTING_MESSAGE_HANDLER = new Handler() {

            PrintStream printer = new PrintStream(OUTPUT_STREAM);

            @Override
            public void publish(LogRecord record) {
                printer.print(record.getMessage());
            }

            @Override
            public void flush() {
                try {
                    OUTPUT_STREAM.flush();
                    OUTPUT_STREAM.reset();
                } catch (IOException ex) {
                    Logger.getLogger(EnvironmentDatabaseSessionTunerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void close() throws SecurityException {
                try {
                    OUTPUT_STREAM.close();
                    OUTPUT_STREAM.reset();
                } catch (IOException ex) {
                    Logger.getLogger(EnvironmentDatabaseSessionTunerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of tunePreDeploy method, of class EnvironmentDatabaseSessionTuner.
     */
    @Test
    @DisplayName("testTunePreDeploy")
    public void testTunePreDeploy() {
        EnvironmentDatabaseSessionTuner instance = new EnvironmentDatabaseSessionTuner();
//        Properties properties = new Properties();
//        try {
//            properties.load(EnvironmentDatabaseSessionTuner.class.getClassLoader().getResourceAsStream(instance.getConfigFilename()));
//            properties.get()
//        } catch (Exception ex) {
//            Logger.getLogger(EnvironmentDatabaseSessionTuner.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
//        }

        instance.tunePreDeploy(properties);

        verify(properties, times(1)).put(eq(PersistenceUnitProperties.JDBC_DRIVER), eq("com.microsoft.sqlserver.jdbc.SQLServerDriver"));
        verify(properties, times(1)).put(eq(PersistenceUnitProperties.JDBC_URL), eq("jdbc:sqlserver://database:1433;encrypt=true;trustServerCertificate=true"));
        verify(properties, times(1)).put(eq(PersistenceUnitProperties.JDBC_USER), eq("MICROSERVICE_PROXY1"));
        verify(properties, times(1)).put(eq(PersistenceUnitProperties.JDBC_PASSWORD), eq("p455w0Rd"));
        verify(properties, times(1)).put(eq(PersistenceUnitProperties.SCHEMA_GENERATION_DATABASE_ACTION), eq("none"));
        verify(properties, times(1)).put(eq(PersistenceUnitProperties.SCHEMA_GENERATION_SQL_LOAD_SCRIPT_SOURCE), eq("META-INF/mssql/load.sql"));
        verify(properties, times(1)).remove(eq(PersistenceUnitProperties.TARGET_DATABASE));
        verify(properties, times(1)).remove(eq(PersistenceUnitProperties.TARGET_SERVER));
        verifyNoMoreInteractions(properties);
    }

    /**
     * Test of tunePreDeploy method, of class EnvironmentDatabaseSessionTuner.
     */
    @Test
    @DisplayName("testTunePreDeployWithNoConfig")
    public void testTunePreDeployWithNoConfig() {
        OUTPUT_STREAM.reset();
        Logger.getLogger(EnvironmentDatabaseSessionTuner.class.getName())
                .addHandler(TESTING_MESSAGE_HANDLER);

        Logger.getLogger(EnvironmentDatabaseSessionTuner.class.getName())
                .setUseParentHandlers(false);

        EnvironmentDatabaseSessionTuner instance = new EnvironmentDatabaseSessionTuner() {
            @Override
            String getConfigFilename() throws IOException {
                throw new IOException("No configuration file");
            }
        };

        instance.tunePreDeploy(properties);

        String log = OUTPUT_STREAM.toString();
        assertEquals("No configuration file", log);

        verifyNoInteractions(properties);

    }

    /**
     * Test of tuneDeploy method, of class EnvironmentDatabaseSessionTuner.
     */
    @Test
    @DisplayName("testTuneDeploy")
    public void testTuneDeploy() {
        EnvironmentDatabaseSessionTuner instance = new EnvironmentDatabaseSessionTuner();
        instance.tuneDeploy(session);
        verifyNoInteractions(session);
    }

    /**
     * Test of tunePostDeploy method, of class EnvironmentDatabaseSessionTuner.
     */
    @Test
    @DisplayName("testTunePostDeploy")
    public void testTunePostDeploy() {
        EnvironmentDatabaseSessionTuner instance = new EnvironmentDatabaseSessionTuner();
        instance.tunePostDeploy(session);
        verifyNoInteractions(session);
    }

}
