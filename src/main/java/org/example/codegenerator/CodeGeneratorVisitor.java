package org.example.codegenerator;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.parser.context.ParseTree;
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

   private Map<String, Variable> variableIndexMap;
   private Map<String, String> functionDescriptorMap;
   private int variableIndex;

   private MethodVisitor mainMethodVisitor;

   public CodeGeneratorVisitor() {
      this.classWriter = new ClassWriter(0);
      variableIndexMap = new HashMap<>();
      functionDescriptorMap = new HashMap<>();

      variableIndex = 1;
   }

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
   public Void visitFunction(FunctionContext context) {
      MethodVisitor methodVisitorForMain = mainMethodVisitor;
      int indexForMain = variableIndex;
      Map<String, Variable> indexMapForMain = variableIndexMap;

      variableIndexMap = new HashMap<>();
      variableIndex = 0;

      for(FunctionParameter functionParameter : context.getParameters()) {
         variableIndexMap.put(functionParameter.getParameterName(), new Variable(variableIndex, functionParameter.getType()));
         variableIndex++;
      }

      String descriptor = Descriptor.getDescriptor(context.getParameters(), context.getReturnType());
      functionDescriptorMap.put(context.getFunctionName(), descriptor);

      mainMethodVisitor =
             classWriter.visitMethod(ACC_PUBLIC + ACC_STATIC, context.getFunctionName(), descriptor, null, null);
      mainMethodVisitor.visitCode();

      super.visitFunction(context);
      mainMethodVisitor.visitEnd();

      mainMethodVisitor = methodVisitorForMain;
      variableIndex = indexForMain;
      variableIndexMap = indexMapForMain;
      return null;
   }

   @Override
   public Void visitFunctionCall(FunctionCallContext context) {

      super.visitFunctionCall(context);
      mainMethodVisitor.visitMethodInsn(INVOKESTATIC, "CgSample", context.getFunctionName(), functionDescriptorMap.get(context.getFunctionName()), false);

      return null;
   }

   @Override
   public Void visitReturn(ReturnContext context) {
      super.visitReturn(context);
      mainMethodVisitor.visitInsn(IRETURN);

      return null;
   }

   @Override
   public Void visitIfStatement(IfStatementContext context) {
      Label elseLabel = new Label();

      visitExpression((ExpressionContext) context.getCondition());
      mainMethodVisitor.visitJumpInsn(IF_ICMPNE, elseLabel);
      super.visitStatements(context.getIfStatement());
      mainMethodVisitor.visitLabel(elseLabel);

      Label endLabel = new Label();

      if(context.getElseStatement() != null) {
         mainMethodVisitor.visitJumpInsn(GOTO, endLabel);
         mainMethodVisitor.visitLabel(elseLabel);
         super.visitStatements(context.getElseStatement());
         mainMethodVisitor.visitLabel(endLabel);
      }

      return null;
   }

   @Override
   public Void visitLet(LetContext context) {
      String variableName = context.getVariableName().getText();
      String variableValue = context.getVariableValue().getText();
      super.visitLet(context);

      if(variableIndexMap.get(variableName) == null) {
         mainMethodVisitor.visitVarInsn(ASTORE, variableIndex);
         variableIndexMap.put(variableName, new Variable(variableIndex, variableValue));
         variableIndex++;
      }
      else {
         mainMethodVisitor.visitVarInsn(ASTORE, variableIndexMap.get(variableName).getIndex());
      }

      return null;
   }

   @Override
   public Void visitShow(ShowContext context) {
      mainMethodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

      super.visitShow(context);

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





   @Override
   public Void visitExpression(ExpressionContext context) {
      super.visitExpression(context);

      if(context.getOperator() != null) {
         switch (context.getOperator()) {
            case ADD -> mainMethodVisitor.visitInsn(IADD);
            case SUBTRACT -> mainMethodVisitor.visitInsn(ISUB);
            case MULTIPLY -> mainMethodVisitor.visitInsn(IMUL);
            case DIVIDE -> mainMethodVisitor.visitInsn(IDIV);
            //case EQUALS -> generateEqual();
         }
      }
      return null;
   }

   @Override
   public Void visitExpressionNode(ExpressionNode expressionNode) {
      super.visitExpressionNode(expressionNode);
      generateExpressionNode(expressionNode);
      return null;
   }

   private void generateExpressionNode(ParseTree tree) {
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
   }

   @Override
   public Void visitInput(InputContext inputContext) {

      mainMethodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      mainMethodVisitor.visitLdcInsn(inputContext.getInputText().getText());
      mainMethodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

      mainMethodVisitor.visitTypeInsn(NEW, "java/util/Scanner");
      mainMethodVisitor.visitInsn(DUP);
      mainMethodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
      mainMethodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
      mainMethodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);

      return null;
   }

//   private void generateEqual() {
//      Label trueLabel = new Label();
//      Label endLabel = new Label();
//   }

   private void writeToFile(byte[] code) {
      try (FileOutputStream fos = new FileOutputStream("output/CgSample.class")) {
         fos.write(code);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private boolean isBooleanValue(ParseTree context) {
      return (variableIndexMap.get(context.getText()).getValue().equals("true") ||
             variableIndexMap.get(context.getText()).getValue().equals("false") ||
             variableIndexMap.get(context.getText()).getValue().equalsIgnoreCase("boolean"));
   }

}
