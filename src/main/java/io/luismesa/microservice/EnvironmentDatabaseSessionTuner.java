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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.tools.tuning.SessionTuner;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
public class EnvironmentDatabaseSessionTuner implements SessionTuner {

    protected static final String LOCAL_MICROPROFILE_CONFIG = "microprofile-config.properties";

    protected String configFileName = "META-INF/" + LOCAL_MICROPROFILE_CONFIG;

    protected static final String[] DEFAULT_PROPERTIES = {
        PersistenceUnitProperties.JDBC_URL,
        PersistenceUnitProperties.JDBC_USER,
        PersistenceUnitProperties.JDBC_PASSWORD,
        PersistenceUnitProperties.JDBC_DRIVER,
        PersistenceUnitProperties.TARGET_DATABASE,
        PersistenceUnitProperties.TARGET_SERVER,
        PersistenceUnitProperties.SCHEMA_GENERATION_DATABASE_ACTION,
        PersistenceUnitProperties.SCHEMA_GENERATION_SQL_LOAD_SCRIPT_SOURCE,};

    protected static final Map<String, Supplier<String>> CONFIGURATION_SUPPLIER = new HashMap<>(8);

    protected static final String NON_ALPHANUMERIC_REGEX = "[^a-zA-Z\\d_]";
    protected static final String UNDERSCORE = "_";
    //Replace all non-alphanumeric characters with underscore; this is the first form
    protected static final Function<String, String> FIRST_FORM = (property) -> property.replaceAll(NON_ALPHANUMERIC_REGEX, UNDERSCORE);
    //Replace all non-alphanumeric characters with underscore, and turn to uppercase; this is the second form
    protected static final Function<String, String> SECOND_FORM = (property) -> FIRST_FORM.apply(property).toUpperCase();
    //Search the property, if not found search the property in the first form, if not found search the property in the second form else null
    private static final BiFunction<Function<String, String>, String, Optional<String>> CONFIGURATION_READER = (Function<String, String> configReader, String property)
            -> Optional.ofNullable(Optional.
                    ofNullable(configReader.
                            apply(property)).
                    orElse(Optional.
                            ofNullable(configReader.
                                    apply(FIRST_FORM.
                                            apply(property))).
                            orElse(Optional.
                                    ofNullable(configReader.
                                            apply(SECOND_FORM.
                                                    apply(property))).
                                    orElse(null))));

    //Read from System properties
    private static final Function<String, Optional<String>> SYSTEM_CONFIGURATION_READER = (String property)
            -> CONFIGURATION_READER.apply(System::getProperty, property);
    //Read from Environment variables
    private static final Function<String, Optional<String>> ENVIRONMENT_CONFIGURATION_READER = (String property)
            -> CONFIGURATION_READER.apply(System::getenv, property);
    //Read from configuration file
    private static final BiFunction<Properties, String, Optional<String>> PROPERTY_CONFIGURATION_READER = (Properties properties, String property) -> CONFIGURATION_READER.apply(properties::getProperty, property);

    //The order of the search is: System properties, Environment variables, and configuration file
    //https://github.com/eclipse/microprofile-config/blob/main/spec/src/main/asciidoc/configsources.asciidoc#default-configsources
    private static final BiFunction<Properties, String, Optional<String>> PROPERTY_FINDER
            = (Properties properties, String property) -> Optional.ofNullable(
                    SYSTEM_CONFIGURATION_READER.
                            apply(property).
                            orElse(ENVIRONMENT_CONFIGURATION_READER.
                                    apply(property).
                                    orElse(PROPERTY_CONFIGURATION_READER.
                                            apply(properties, property).
                                            orElse(null))));

    //If the property is found, we store the value. If the property isn't found we store null (to remove it)
    private static final BiConsumer<Properties, String> CONFIGURATOR
            = (Properties properties, String property) -> PROPERTY_FINDER.
                    apply(properties, property).
                    ifPresentOrElse((String value) -> CONFIGURATION_SUPPLIER.
                    put(property, () -> value), () -> CONFIGURATION_SUPPLIER.
                    put(property, () -> null));

    String getConfigFilename() throws Exception {
        return configFileName;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void tunePreDeploy(Map map) {
        Properties properties = new Properties();
        try {
            properties.load(EnvironmentDatabaseSessionTuner.class.getClassLoader().getResourceAsStream(getConfigFilename()));
            //Populate CONFIGURATION_SUPPLIER
            Stream.of(DEFAULT_PROPERTIES).forEach(property -> CONFIGURATOR.accept(properties, property));
        } catch (Exception ex) {
            Logger.getLogger(EnvironmentDatabaseSessionTuner.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        //Apply from CONFIGURATION_SUPPLIER
        CONFIGURATION_SUPPLIER.entrySet().stream().forEach((Map.Entry<String, Supplier<String>> entry) -> {
            Optional.ofNullable(entry.getValue()).ifPresent((Supplier<String> valueSupplier) -> {
                String key = entry.getKey();
                String value = valueSupplier.get();
                if (value == null || value.isBlank()) {
                    map.remove(key);
                } else {
                    map.put(key, value);
                }
            });
        });
    }

    @Override
    public void tuneDeploy(Session sn) {
//        System.out.println("EnvironmentCredentialsSessionTuner.tuneDeploy -> sn = " + sn);
//        System.out.println("EnvironmentCredentialsSessionTuner.tuneDeploy -> sn.getName() = " + sn.getName());
    }

    @Override
    public void tunePostDeploy(Session sn) {
//        System.out.println("EnvironmentCredentialsSessionTuner.tunePostDeploy -> sn = " + sn);
//        System.out.println("EnvironmentCredentialsSessionTuner.tunePostDeploy -> sn.getName() = " + sn.getName());
    }

}
