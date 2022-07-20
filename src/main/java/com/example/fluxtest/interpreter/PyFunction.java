package com.example.fluxtest.interpreter;


import java.time.LocalTime;
import java.time.temporal.ChronoField;

import org.python.util.PythonInterpreter;

import com.example.fluxtest.model.FuncResult;

public class PyFunction {

    String code;
    String functionName;

    public PyFunction(String code) {
        this.code = code.trim();
        if (code.startsWith("def ")) {
            this.functionName = code.substring(3).trim().split("\\(")[0];
        } else {
            throw new RuntimeException("Code must start with 'def '");
        }
        if (code.contains("import ")) {
            throw new RuntimeException("Import statement not allowed");
        }
    }

    public FuncResult execute(Integer param) {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.exec("import math");
            String result;
            try {
                interpreter.exec(code);
            } catch (Throwable throwable) {
                result = throwable.getMessage().split(",")[0];
                return new FuncResult(param, result, 0L);
            }
            long start = LocalTime.now().getLong(ChronoField.MILLI_OF_DAY);
            try {
                result = interpreter.eval("str(" + functionName + "(" + param + "))").asString();
            } catch (Throwable throwable) {
                result = throwable.getMessage().split(",")[0];
            }
            long end = LocalTime.now().getLong(ChronoField.MILLI_OF_DAY);
            return new FuncResult(param, result, end - start);
        }
    }
}
