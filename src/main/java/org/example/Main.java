package org.example;

import org.example.codegenerator.CodeGeneratorVisitor;
import org.example.parser.Parser;
import org.example.parser.context.ParseTree;
import org.example.scanner.Scanner;
import org.example.semantic.SemanticAnalyzer;
import org.example.utils.FileUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileUtil.chooseAndReadFile();
        List<Character> codeArray = FileUtil.chooseAndReadFile();

        // 1. Lexer
        Scanner scanner = new Scanner(codeArray);

        // 2. Parser
        Parser parser = new Parser(scanner);
        ParseTree tree = parser.parseProgram();

        // 3. Semantic Analyzer Visitor
        tree.accept(new SemanticAnalyzer());

        // 4.2 Compiler Visitor
        tree.accept(new CodeGeneratorVisitor());
    }
}