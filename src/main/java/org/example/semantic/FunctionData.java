package org.example.semantic;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.parser.context.implementation.FunctionParameter;

import java.util.List;

@Data
@AllArgsConstructor
public class FunctionData {
    private String returnType;
    private List<FunctionParameter> functionParameterList;
}
