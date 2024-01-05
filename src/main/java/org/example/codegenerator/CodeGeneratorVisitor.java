package org.example.codegenerator;

import org.example.domain.Token;
import org.example.domain.TokenType;
import org.example.parser.context.ParseTree;
import org.example.parser.context.implementation.*;
import org.example.visitor.BaseVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class CodeGeneratorVisitor extends BaseVisitor {

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
   public void visitProgram(ProgramContext context) {

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

      
   }

   @Override
   public void visitFunction(FunctionContext context) {
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
      
   }

   @Override
   public void visitFunctionCall(FunctionCallContext context) {

      super.visitFunctionCall(context);
      mainMethodVisitor.visitMethodInsn(INVOKESTATIC, "CgSample", context.getFunctionName(), functionDescriptorMap.get(context.getFunctionName()), false);

      
   }

   @Override
   public void visitReturn(ReturnContext context) {
      super.visitReturn(context);
      mainMethodVisitor.visitInsn(IRETURN);

      
   }

   @Override
   public void visitIfStatement(IfStatementContext context) {
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

      
   }

   @Override
   public void visitLet(LetContext context) {
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
   }

   @Override
   public void visitShow(ShowContext context) {
      mainMethodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

      super.visitShow(context);

       if (context.getType() == TokenType.STRING) {
         mainMethodVisitor.visitLdcInsn(context.getChild(0).getText());
         mainMethodVisitor.visitMethodInsn(
               INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
      } else if (context.getType() == TokenType.NUMB) {
         if (variableIndexMap.get(context.getChild(0).getText()) != null && isBooleanValue(context.getChild(0))) {
            mainMethodVisitor.visitMethodInsn(
                  INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
         } else if (context.getChild(0).getText().matches("-?\\d+(\\.\\d+)?")) {
            mainMethodVisitor.visitMethodInsn(
                  INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
         } else {
            mainMethodVisitor.visitMethodInsn(
                  INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
         }
      }
      
   }





   @Override
   public void visitExpression(ExpressionContext context) {
      super.visitExpression(context);

      if(context.getOperator() != null) {
         switch (context.getOperator()) {
            case ADD -> mainMethodVisitor.visitInsn(IADD);
            case SUBTRACT -> mainMethodVisitor.visitInsn(ISUB);
            case MULTIPLY -> mainMethodVisitor.visitInsn(IMUL);
            case DIVIDE -> mainMethodVisitor.visitInsn(IDIV);
         }
      }
      
   }

   @Override
   public void visitExpressionNode(ExpressionNode expressionNode) {
      super.visitExpressionNode(expressionNode);
      generateExpressionNode(expressionNode);
      
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
   public void visitInput(InputContext inputContext) {

      mainMethodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      mainMethodVisitor.visitLdcInsn(inputContext.getInputText().getText());
      mainMethodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

      mainMethodVisitor.visitTypeInsn(NEW, "java/util/Scanner");
      mainMethodVisitor.visitInsn(DUP);
      mainMethodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
      mainMethodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
      mainMethodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);

      
   }

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
