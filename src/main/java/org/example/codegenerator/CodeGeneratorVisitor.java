package org.example.codegenerator;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.parser.context.ParseTree;
import org.example.parser.context.StatementsContext;
import org.example.parser.context.implementation.*;
import org.example.visitor.SimplerLangBaseVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class CodeGeneratorVisitor extends SimplerLangBaseVisitor {

   private final ClassWriter classWriter;

   private final Map<String, Variable> variableIndexMap;
   private int variableIndex;

   private MethodVisitor mainMethodVisitor;

   public CodeGeneratorVisitor() {
      this.classWriter = new ClassWriter(0);
      variableIndexMap = new HashMap<>();

      variableIndex = 1;
   }

   /**
    * Called when the program node is visited. The main entry point.
    */

   @Override
   public Void visitProgram(ProgramContext context) {

      classWriter.visit(
            V1_8,
            ACC_PUBLIC + ACC_SUPER,
            "CgSample",
            null,
            "java/lang/Object",
            null
      );

      mainMethodVisitor =
            classWriter.visitMethod(
                  ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);

      super.visitProgram(context);
      mainMethodVisitor.visitEnd();

      mainMethodVisitor.visitInsn(RETURN);
      classWriter.visitEnd();
      byte[] code = classWriter.toByteArray();
      writeToFile(code);

      return null;
   }

   @Override
   public Void visitIfStatement(IfStatementContext context) {
      Label elseLabel = new Label();
      Label endLabel = new Label();

      // Visit the condition expression
      visitExpression(context.getCondition());

      // Jump to elseLabel if the condition is false
      mainMethodVisitor.visitJumpInsn(IFEQ, elseLabel);

      // Visit the true branch
      super.visitStatements(context.getIfStatement());

      // Jump to endLabel after executing the true branch
      mainMethodVisitor.visitJumpInsn(GOTO, endLabel);

      // Mark elseLabel
      mainMethodVisitor.visitLabel(elseLabel);

      // Visit the false branch if it exists
      super.visitStatements(context.getElseStatement());

      // Mark endLabel
      mainMethodVisitor.visitLabel(endLabel);

      return null;
   }

   @Override
   public Void visitLet(LetContext context) {
      String variableName = context.getVariableName().getText();
      String variableValue = context.getVariableValue().getText();

      if (context.getVariableValue() instanceof ExpressionContext || context.getVariableValue() instanceof ExpressionNode) {
         visitExpression(context.getVariableValue());
      }
      else if(context.getVariableValue().getPayload() != null) {
         mainMethodVisitor.visitLdcInsn(variableValue);
      }

      mainMethodVisitor.visitVarInsn(ASTORE, variableIndex);
      variableIndexMap.put(variableName, new Variable(variableIndex, variableValue));
      variableIndex++;

      return null;
   }

   @Override
   public Void visitShow(ShowContext context) {
      mainMethodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

      if (context.getVariableName() != null) {
         int index = variableIndexMap.get(context.getVariableName().getText()).getIndex();
         mainMethodVisitor.visitVarInsn(ALOAD, index);
         mainMethodVisitor.visitMethodInsn(
               INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
      } else if (context.getStringValue() != null) {
         String text = context.getStringValue().getText();
         mainMethodVisitor.visitLdcInsn(text);
         mainMethodVisitor.visitMethodInsn(
               INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
      } else if (context.getExpressionContext() != null) {
         visitExpression(context.getExpressionContext());
         if (variableIndexMap.get(context.getExpressionContext().getText()) != null && isBooleanValue(context.getExpressionContext())) {
            mainMethodVisitor.visitMethodInsn(
                  INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
         } else if (context.getExpressionContext().getText().matches("-?\\d+(\\.\\d+)?")) {
            mainMethodVisitor.visitMethodInsn(
                  INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
         } else {
            mainMethodVisitor.visitMethodInsn(
                  INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
         }
      }
      return null;
   }

   private void visitExpression(ParseTree context) {

      if (context instanceof ExpressionNode) {
         visitExpressionNode(context);
      } else {
         visitExpressionContext((ExpressionContext) context);
      }

   }

   private void visitExpressionContext(ExpressionContext context) {
      visitExpressionNode(context.getLeftOperand());

      visitExpressionNode(context.getRightOperand());

      switch (context.getOperator()) {
         case ADD -> mainMethodVisitor.visitInsn(IADD);
         case SUBTRACT -> mainMethodVisitor.visitInsn(ISUB);
         case MULTIPLY -> mainMethodVisitor.visitInsn(IMUL);
         case DIVIDE -> mainMethodVisitor.visitInsn(IDIV);
         case EQUALS -> visitEqual();
      }
   }

   private void visitExpressionNode(ParseTree tree) {
      if (tree instanceof ExpressionNode) {
         Token symbol = (Token) tree.getPayload();
         if (symbol.getType() == TokenType.NUMBER) {
            int intValue = Integer.parseInt(symbol.getValue());
            mainMethodVisitor.visitIntInsn(BIPUSH, intValue);
            mainMethodVisitor.visitMethodInsn(INVOKESTATIC, Type.getType(Integer.class).getInternalName(), "valueOf", "(I)Ljava/lang/Integer;", false);
         } else if (symbol.getType() == TokenType.TEXT) {
            int index = variableIndexMap.get(symbol.getValue()).getIndex();
            mainMethodVisitor.visitVarInsn(ALOAD, index);
         } else if (symbol.getType() == TokenType.TRUE || symbol.getType() == TokenType.FALSE) {
            mainMethodVisitor.visitIntInsn(BIPUSH, Boolean.parseBoolean(tree.getText()) ? 1 : 0);
         }
      } else if (tree instanceof ExpressionContext) {
         visitExpressionContext((ExpressionContext) tree);
      }
   }

   private void visitEqual() {
      Label trueLabel = new Label();
      Label endLabel = new Label();
      mainMethodVisitor.visitJumpInsn(IF_ICMPEQ, trueLabel);
      mainMethodVisitor.visitInsn(ICONST_0);
      mainMethodVisitor.visitJumpInsn(GOTO, endLabel);
      mainMethodVisitor.visitLabel(trueLabel);
      mainMethodVisitor.visitInsn(ICONST_1);
      mainMethodVisitor.visitLabel(endLabel);
   }

   private void writeToFile(byte[] code) {
      try (FileOutputStream fos = new FileOutputStream("output/CgSample.class")) {
         fos.write(code);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private boolean isBooleanValue(ParseTree context) {
      return (variableIndexMap.get(context.getText()).getValue().equals("true") || variableIndexMap.get(context.getText()).getValue().equals("false"));
   }

}
