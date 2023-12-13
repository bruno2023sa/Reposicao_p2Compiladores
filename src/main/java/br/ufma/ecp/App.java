package br.ufma.ecp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java App <input_file.jack>");
            System.exit(1);
        }

        String inputFilePath = args[0];
        String outputFilePath = inputFilePath.replace(".jack", ".vm");

        try {
            String input = readFile(inputFilePath);
            var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
            var vmWriter = new VMWriter();

            while (parser.hasMoreCommands()) {
                parser.advance();
                parser.compile(vmWriter);
            }

            saveToFile(outputFilePath, vmWriter.vmOutput());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static void saveToFile(String fileName, String content) {
        try {
            Files.write(Paths.get(fileName), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
