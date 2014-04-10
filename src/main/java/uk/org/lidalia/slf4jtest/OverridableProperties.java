package uk.org.lidalia.slf4jtest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.function.Function;
import java.util.Optional;


class OverridableProperties
{
    private static final Properties EMPTY_PROPERTIES = new Properties();
    private final String propertySourceName;
    private final Properties properties;


    OverridableProperties(final String propertySourceName) throws IOException
    {
        this.propertySourceName = propertySourceName;
        this.properties = getProperties();
    }


    private Properties getProperties() throws IOException
    {
        final Optional<InputStream> resourceAsStream = Optional
                .ofNullable(Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(propertySourceName + ".properties"));
        return resourceAsStream.map(loadProperties).orElse(EMPTY_PROPERTIES);
    }

    private static final Function<InputStream, Properties> loadProperties = (final InputStream propertyResource) ->
    {
        try(InputStream closablePropertyResource = propertyResource)
        {
            final Properties loadedProperties = new Properties();
            loadedProperties.load(closablePropertyResource);
            return loadedProperties;
        }
        catch(IOException ioException)
        {
            //Invariant: ioException is not null
            assert ioException != null;
            
            //Wrap the exception and throw it as an unchecked exception.  
            throw new RuntimeException(ioException);
        }
    };


    String getProperty(final String propertyKey,
                       final String defaultValue)
    {
        final String propertyFileProperty = properties
                .getProperty(propertyKey, defaultValue);
        return System.getProperty(propertySourceName + "." + propertyKey,
                                  propertyFileProperty);
    }
}
