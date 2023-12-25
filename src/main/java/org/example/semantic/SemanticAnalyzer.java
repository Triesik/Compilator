package org.example.semantic;

import org.example.parser.context.implementation.LetContext;
import org.example.parser.context.implementation.ShowContext;
import org.example.parser.context.implementation.StatementContext;
import org.example.parser.context.implementation.TerminalNode;
import org.example.visitor.SimplerLangBaseVisitor;

import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer extends SimplerLangBaseVisitor {

  private final Map<java.lang.String, java.lang.String> variableMap;

  public SemanticAnalyzer() {
    super();
    this.variableMap = new HashMap<>();
  }

  /** Validate Statement Semantics. */
  @Override
  public Void visitStatement(StatementContext context) {
    return (Void) super.visitStatement(context);
  }

  /** Validate LET Semantics. */
  @Override
  public Void visitLet(LetContext context) {

//    java.lang.String variableName = context.getVariableName().getText();
//    java.lang.String variableValue = context.getVariableValue().getText();
//
//    if (variableName == null || variableName.isEmpty()) {
//      throw new RuntimeException("Variable name cannot be empty.");
//    } else if (variableValue == null || variableValue.isEmpty()) {
//      throw new RuntimeException("Variable value cannot be empty.");
//    }
//    variableMap.put(variableName, variableValue);

    return (Void) super.visitLet(context);
  }

  @Override
  public Void visitShow(ShowContext context) {

    TerminalNode variableNameTN = context.getVariableName();
    TerminalNode integerValueTN = context.getIntegerValue();
    TerminalNode stringValueTN = context.getStringValue();

    /* 1. Checking whether either of VAR | INT is present.*/
    boolean isVarPresent = variableNameTN != null && !variableNameTN.getText().isEmpty();
    boolean isIntPresent = integerValueTN != null && !integerValueTN.getText().isEmpty();
    boolean isStringPresent = stringValueTN != null && !stringValueTN.getText().isEmpty();

    if (integerValueTN != null) {
      try {
        Integer.parseInt(integerValueTN.getText());
      } catch (NumberFormatException | NullPointerException ex) {
        throw new RuntimeException("SHOW argument is not a valid integer.", ex);
      }
    }

    return (Void) super.visitShow(context);
  }
}
