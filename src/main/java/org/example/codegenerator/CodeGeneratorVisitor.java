package org.example.codegenerator;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.parser.context.ParseTree;
import org.example.parser.context.ProgramContext;
import org.example.parser.context.implementation.*;
import org.example.visitor.SimplerLangBaseVisitor;
import org.objectweb.asm.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

/**
 * Visitor that converts AST to .class java byte code.
 *
 * <p>NOTE 1: To generate ASM code from Java Class you can use ASMifier. This will help you write
 * complex ASM codes. Ref:- @see <a
 * href="https://github.com/arjunsk/java-bytecode/tree/master/java-asm/ow2-asm-example/src/main/java/com/arjunsk/asm/asmifier">Java
 * ASMifier</a>
 *
 * <p>NOTE 2: Ops Code reference: @see <a
 * href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html">Java Ops Code</a>
 */
public class CodeGeneratorVisitor extends SimplerLangBaseVisitor {

   private final ClassWriter classWriter;

   private final Map<String, Variable> variableIndexMap;
   private int variableIndex;

   private MethodVisitor mainMethodVisitor;

   public CodeGeneratorVisitor() {
      this.classWriter = new ClassWriter(0);
      variableIndexMap = new HashMap<>();

      // Variable0 is reserved for args[] in :  `main(String[] var0)`
      variableIndex = 1;
   }

   /**
    * Called when the program node is visited. The main entry point.
    */
   @Override
   public Void visitProgram(ProgramContext context) {

      /* ASM = CODE : public class CgSample. */
      // BEGIN 1: creates a ClassWriter for the `CgSample.class` public class,
      classWriter.visit(
            V1_8, // Java 1.8
            ACC_PUBLIC + ACC_SUPER, // public static
            "CgSample", // Class Name
            null, // Generics <T>
            "java/lang/Object", // Interface extends Object (Super Class),
            null // interface names
      );

      /* ASM = CODE : public static void main(String args[]). */
      // BEGIN 2: creates a MethodVisitor for the 'main' method
      mainMethodVisitor =
            classWriter.visitMethod(
                  ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);

      super.visitProgram(context);
      // END 2: Close main()
      mainMethodVisitor.visitEnd();

      // END 1: Close class()
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
      visitStatement(context.getIfStatement());

      // Jump to endLabel after executing the true branch
      mainMethodVisitor.visitJumpInsn(GOTO, endLabel);

      // Mark elseLabel
      mainMethodVisitor.visitLabel(elseLabel);

      // Visit the false branch if it exists
      if (context.getElseStatement() != null) {
         visitStatement(context.getElseStatement());
      }

      // Mark endLabel
      mainMethodVisitor.visitLabel(endLabel);
      mainMethodVisitor.visitInsn(RETURN);

      return null;
   }

   @Override
   public Void visitLet(LetContext context) {
      String variableName = context.getVariableName().getText();
      String variableValue = context.getVariableValue().getText();

      if (variableValue.matches("^[-+]?\\d+$")) {
         if (context.getVariableValue() instanceof ExpressionContext) {
            visitExpressionContext((ExpressionContext) context.getVariableValue());
            mainMethodVisitor.visitVarInsn(ASTORE, variableIndex);
         } else {
            int variableIntegerVal = Integer.parseInt(context.getVariableValue().getText());
            mainMethodVisitor.visitIntInsn(BIPUSH, variableIntegerVal);
            mainMethodVisitor.visitMethodInsn(
                  INVOKESTATIC,
                  Type.getType(Integer.class).getInternalName(),
                  "valueOf",
                  "(I)Ljava/lang/Integer;",
                  false);
            mainMethodVisitor.visitVarInsn(ASTORE, variableIndex);
         }
      } else if (variableValue.equals("true") || variableValue.equals("false")) {
         mainMethodVisitor.visitInsn(variableValue.equals("true") ? ICONST_1 : ICONST_0);
         mainMethodVisitor.visitVarInsn(ISTORE, variableIndex);
      } else {
          if (context.getVariableValue() instanceof ExpressionContext) {
              visitExpressionContext((ExpressionContext) context.getVariableValue());
              mainMethodVisitor.visitVarInsn(ASTORE, variableIndex);
          }
          else {
              mainMethodVisitor.visitLdcInsn(variableValue);
              mainMethodVisitor.visitVarInsn(ASTORE, variableIndex);
          }
      }

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
         if(variableIndexMap.get(context.getExpressionContext().getText()) != null && (variableIndexMap.get(context.getExpressionContext().getText()).getValue().equals("true") || variableIndexMap.get(context.getExpressionContext().getText()).getValue().equals("false"))) {
            mainMethodVisitor.visitMethodInsn(
                  INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
         } else if(context.getExpressionContext().getText().matches("-?\\d+(\\.\\d+)?")) {
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
         } else if (symbol.getType() == TokenType.TEXT) {
            int index = variableIndexMap.get(symbol.getValue()).getIndex();
            mainMethodVisitor.visitVarInsn(ILOAD, index);
         }
      } else if (tree instanceof ExpressionContext) {
         visitExpressionContext((ExpressionContext) tree);
      }
   }

   private void visitEqual() {
      // Equality comparison
      Label trueLabel = new Label();
      Label endLabel = new Label();
      mainMethodVisitor.visitJumpInsn(IF_ICMPEQ, trueLabel); // Jump to trueLabel if the values are equal
      mainMethodVisitor.visitInsn(ICONST_0); // False: Push 0 onto the stack
      mainMethodVisitor.visitJumpInsn(GOTO, endLabel);
      mainMethodVisitor.visitLabel(trueLabel); // Mark the true label
      mainMethodVisitor.visitInsn(ICONST_1); // True: Push 1 onto the stack
      mainMethodVisitor.visitLabel(endLabel); // Mark the end label
   }

   private void writeToFile(byte[] code) {
      try (FileOutputStream fos = new FileOutputStream("output/CgSample.class")) {
         fos.write(code);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}
