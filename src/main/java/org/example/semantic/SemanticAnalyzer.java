package org.example.semantic;

import org.example.parser.context.implementation.LetContext;
import org.example.parser.context.implementation.ShowContext;
import org.example.parser.context.implementation.StatementContext;
import org.example.parser.context.implementation.TerminalNode;
import org.example.visitor.SimplerLangBaseVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * `Syntax` is the concept that concerns itself only whether or not the sentence is valid for the
 * grammar of the language. `Semantics` is about whether or not the sentence has a valid meaning.
 *
 * <p>NOTE: checking whether the variable is declared before "SHOW VAR" is an example of `Semantic`
 * check.
 */
public class SemanticAnalyzer extends SimplerLangBaseVisitor {

  private final Map<String, String> variableMap;

  public SemanticAnalyzer() {
    super();
    this.variableMap = new HashMap<>();
  }

  /** Validate Statement Semantics. */
  @Override
  public Void visitStatement(StatementContext context) {
//    if (context.getLetContext() == null && context.getShowContext() == null) {
//      throw new RuntimeException("Statement should of type LET or SHOW.");
//    } else if (context.getLetContext() != null && context.getShowContext() != null) {
//      throw new RuntimeException("Statement should be either of type LET or SHOW & not both.");
//    }

    return (Void) super.visitStatement(context);
  }

  /** Validate LET Semantics. */
  @Override
  public Void visitLet(LetContext context) {

    String variableName = context.getVariableName().getText();
    String variableValue = context.getVariableValue().getText();

    if (variableName == null || variableName.isEmpty()) {
      throw new RuntimeException("Variable name cannot be empty.");
    } else if (variableValue == null || variableValue.isEmpty()) {
      throw new RuntimeException("Variable value cannot be empty.");
    }

    // This will be used to check whether variable is declared using LET before invoking SHOW for
    // the variable.
    variableMap.put(variableName, variableValue);

    return (Void) super.visitLet(context);
  }

  /**
   * Validate SHOW Semantics.
   *
   * <p>NOTE: We validate if the variable is previously declared using LET.
   */
  @Override
  public Void visitShow(ShowContext context) {

    TerminalNode variableNameTN = context.getVariableName();
    TerminalNode integerValueTN = context.getIntegerValue();
    TerminalNode stringValueTN = context.getStringValue();

    /* 1. Checking whether either of VAR | INT is present.*/
    boolean isVarPresent = variableNameTN != null && !variableNameTN.getText().isEmpty();
    boolean isIntPresent = integerValueTN != null && !integerValueTN.getText().isEmpty();
    boolean isStringPresent = stringValueTN != null && !stringValueTN.getText().isEmpty();

//    if (!isVarPresent && !isIntPresent && !isStringPresent) {
//      throw new RuntimeException("SHOW should have integer or variable as argument");
//    } else if (isVarPresent && isIntPresent) {
//      throw new RuntimeException("SHOW should have either integer or variable as argument");
//    }

    /* 2. If SHOW Argument is Number, check if it is an integer. In our case, this will be
    already handled in the Tokenizer.*/
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
