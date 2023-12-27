package org.example.codegenerator;

import org.example.parser.context.implementation.FunctionParameter;

import java.util.List;
import java.util.stream.Collectors;

public enum Descriptor {
    NUMB("Ljava/lang/Integer;"),
    STRING("Ljava/lang/String;"),
    BOOLEAN("Ljava/lang/Boolean;");

    private final String className;

    Descriptor(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public static String getDescriptor(List<FunctionParameter> parameters, String returnType) {

        return parameters.stream()
               .map(FunctionParameter::getType)
               .map(Descriptor::valueOf)
               .map(Descriptor::getClassName)
               .collect(Collectors.joining("", "(", ")" + Descriptor.valueOf(returnType).getClassName()));
    }

}

