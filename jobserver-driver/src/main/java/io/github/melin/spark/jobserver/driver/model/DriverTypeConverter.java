package io.github.melin.spark.jobserver.driver.model;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import io.github.melin.spark.jobserver.core.enums.DriverType;

/**
 * huaixin 2022/4/14 19:13
 */
public class DriverTypeConverter implements IStringConverter<DriverType> {

    @Override
    public DriverType convert(String value) {
        DriverType convertedValue = DriverType.fromString(value);

        if (convertedValue == null) {
            throw new ParameterException("Value " + value + "can not be converted to DriverType.");
        }
        return convertedValue;
    }
}
