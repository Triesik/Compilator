package org.example.parser.context.implementation;

import org.example.domain.TokenType;

public class FunctionParameter {

   private final String type;
   private final String parameterName;

   public FunctionParameter(TokenType type, String parameterName) {
      this.type = type.name().toUpperCase();
      this.parameterName = parameterName;
   }

   public String getType() {
      return type;
   }

   public String getParameterName() {
      return parameterName;
   }
}
