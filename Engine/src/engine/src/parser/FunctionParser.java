package parser;

import basics.cell.api.CellType;
import calc.api.Expression;
import calc.impl.system.IdentityExpression;
import basics.coordinate.api.Coordinate;
import basics.coordinate.impl.CoordinateFactory;
import calc.impl.math.*;
import calc.impl.string.Concat;
import calc.impl.string.Sub;
import calc.impl.system.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public enum FunctionParser {
    IDENTITY {
        @Override
        public Expression parse(List<String> arguments) {
            // validations of the function. it should have exactly one argument
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for IDENTITY function. Expected 1, but got " + arguments.size());
            }



            // all is good. create the relevant function instance
            String actualValue = arguments.get(0);
            if (actualValue.isEmpty())
            {
                return new IdentityExpression("",CellType.STRING);
            }
            if (isBoolean(actualValue)) {
                return new IdentityExpression(Boolean.parseBoolean(actualValue), CellType.BOOLEAN);
            } else if (isNumeric(actualValue)) {
                return new IdentityExpression(Double.parseDouble(actualValue), CellType.NUMERIC);
            } else {
                return new IdentityExpression(actualValue, CellType.STRING);
            }
        }

        private boolean isBoolean(String value) {
            return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
        }

        private boolean isNumeric(String value) {
            try {
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    },
    PLUS {
        @Override
        public Expression parse(List<String> arguments) {
            // validations of the function (e.g. number of arguments)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function. Expected 2, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Expression left = parseExpression(arguments.get(0).trim());
            Expression right = parseExpression(arguments.get(1).trim());

            // more validations on the expected argument types
            ///Do I need to chance it for all ? its working fine without it
            if (!left.getFunctionResultType().equals(CellType.NUMERIC) || !right.getFunctionResultType().equals(CellType.NUMERIC)) {
                if(left.getFunctionResultType().equals(CellType.UNKNOWN)|| right.getFunctionResultType().equals(CellType.UNKNOWN)) {
                    return new Plus(left, right);
                }
                throw new IllegalArgumentException("Invalid argument types for PLUS function. Expected NUMERIC, but got " + left.getFunctionResultType() + " and " + right.getFunctionResultType());
            }

            // all is good. create the relevant function instance
            return new Plus(left, right);
        }
    },
    MINUS {
        @Override
        public Expression parse(List<String> arguments) {
            // validations of the function. it should have exactly two arguments
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for MINUS function. Expected 2, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Expression left = parseExpression(arguments.get(0).trim());
            Expression right = parseExpression(arguments.get(1).trim());

            // more validations on the expected argument types
            if (((!left.getFunctionResultType().equals(CellType.NUMERIC))
                    && (!left.getFunctionResultType().equals(CellType.UNKNOWN)))
                    ||
                    ((!right.getFunctionResultType().equals(CellType.NUMERIC))
                            && (!right.getFunctionResultType().equals(CellType.UNKNOWN)))) {
                throw new IllegalArgumentException("Invalid argument types for MINUS function. Expected NUMERIC, but got " + left.getFunctionResultType() + " and " + right.getFunctionResultType());
            }

            // all is good. create the relevant function instance
            return new Minus(left, right);
        }
    },
    TIMES {
        @Override
        public Expression parse(List<String> arguments) {
            // Validate the number of arguments (should be exactly 2)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for TIMES function. Expected 2, but got " + arguments.size());
            }

            // Parse both arguments
            Expression left = parseExpression(arguments.get(0).trim());
            Expression right = parseExpression(arguments.get(1).trim());

            // Validate that both arguments are numeric
            if (((!left.getFunctionResultType().equals(CellType.NUMERIC))
                    && (!left.getFunctionResultType().equals(CellType.UNKNOWN)))
                    ||
                    ((!right.getFunctionResultType().equals(CellType.NUMERIC))
                            && (!right.getFunctionResultType().equals(CellType.UNKNOWN)))) {
                throw new IllegalArgumentException("Invalid argument types for TIMES function. Expected NUMERIC, but got " + left.getFunctionResultType() + " and " + right.getFunctionResultType());
            }

            // Create and return the TIMES expression instance
            return new Times(left, right);
        }
    },
    DIVIDE {
        @Override
        public Expression parse(List<String> arguments) {
            // Validate the number of arguments (should be exactly 2)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for DIVIDE function. Expected 2, but got " + arguments.size());
            }

            // Parse both arguments
            Expression numerator = parseExpression(arguments.get(0).trim());
            Expression denominator = parseExpression(arguments.get(1).trim());

            // Validate that both arguments are numeric
            if (((!numerator.getFunctionResultType().equals(CellType.NUMERIC))
                    && (!numerator.getFunctionResultType().equals(CellType.UNKNOWN)))
                    ||
                    ((!denominator.getFunctionResultType().equals(CellType.NUMERIC))
                            && (!denominator.getFunctionResultType().equals(CellType.UNKNOWN)))) {
                throw new IllegalArgumentException("Invalid argument types for DIVIDE function. Expected NUMERIC, but got " + numerator.getFunctionResultType() + " and " + denominator.getFunctionResultType());
            }

            // Create and return the DIVIDE expression instance
            return new Divide(numerator, denominator);
        }
    },
    MOD {
        @Override
        public Expression parse(List<String> arguments) {
            // Validate the number of arguments (should be exactly 2)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for MOD function. Expected 2, but got " + arguments.size());
            }

            // Parse both arguments
            Expression left = parseExpression(arguments.get(0).trim());
            Expression right = parseExpression(arguments.get(1).trim());

            // Validate that both arguments are numeric
            if (!left.getFunctionResultType().equals(CellType.NUMERIC) || !right.getFunctionResultType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for MOD function. Expected NUMERIC, but got " + left.getFunctionResultType() + " and " + right.getFunctionResultType());
            }

            // Create and return the MOD expression instance
            return new Mod(left, right);
        }
    },
    POW {
        @Override
        public Expression parse(List<String> arguments) {
            // Validate the number of arguments (should be exactly 2)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for POW function. Expected 2, but got " + arguments.size());
            }

            // Parse both arguments
            Expression base = parseExpression(arguments.get(0).trim());
            Expression exponent = parseExpression(arguments.get(1).trim());

            // Validate that both arguments are numeric
            if (!base.getFunctionResultType().equals(CellType.NUMERIC) || !exponent.getFunctionResultType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for POW function. Expected NUMERIC, but got " + base.getFunctionResultType() + " and " + exponent.getFunctionResultType());
            }

            // Create and return the POW expression instance
            return new Pow(base, exponent);
        }
    },
    ABS {
        @Override
        public Expression parse(List<String> arguments) {
            // Validate the number of arguments (should be exactly 1)
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for ABS function. Expected 1, but got " + arguments.size());
            }

            // Parse the argument
            Expression arg = parseExpression(arguments.get(0).trim());

            // Validate that the argument is numeric
            if (!arg.getFunctionResultType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument type for ABS function. Expected NUMERIC, but got " + arg.getFunctionResultType());
            }

            // Create and return the ABS expression instance
            return new Abs(arg);
        }
    },
    CONCAT {
        @Override
        public Expression parse(List<String> arguments) {
            // Validate the number of arguments (should be exactly 2)
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for CONCAT function. Expected 2, but got " + arguments.size());
            }

            // Parse both arguments
            Expression str1 = parseExpression(arguments.get(0).trim());
            Expression str2 = parseExpression(arguments.get(1).trim());

            // Validate that both arguments are of type STRING
            if (((!str1.getFunctionResultType().equals(CellType.STRING)) &&
                    (!str1.getFunctionResultType().equals(CellType.UNKNOWN))) ||
                    ((!str2.getFunctionResultType().equals(CellType.STRING)) &&
                    (!str2.getFunctionResultType().equals(CellType.UNKNOWN))) )   {
                throw new IllegalArgumentException("Invalid argument types for CONCAT function. Expected STRING, but got " + str1.getFunctionResultType() + " and " + str2.getFunctionResultType());
            }

            // Create and return the CONCAT expression instance
            return new Concat(str1, str2);
        }
    },
    SUB {
        @Override
        public Expression parse(List<String> arguments) {
            // Validate the number of arguments (should be exactly 3)
            if (arguments.size() != 3) {
                throw new IllegalArgumentException("Invalid number of arguments for SUB function. Expected 3, but got " + arguments.size());
            }

            // Parse the source string and the indices
            Expression source = parseExpression(arguments.get(0).trim());
            Expression startIndex = parseExpression(arguments.get(1).trim());
            Expression endIndex = parseExpression(arguments.get(2).trim());

            // Validate that the first argument is a string and the other two are numeric
            if (!source.getFunctionResultType().equals(CellType.STRING)) {
                throw new IllegalArgumentException("Invalid argument type for SUB function. Expected STRING for source, but got " + source.getFunctionResultType());
            }
            if (!startIndex.getFunctionResultType().equals(CellType.NUMERIC) || !endIndex.getFunctionResultType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for SUB function. Expected NUMERIC for start and end indices, but got " + startIndex.getFunctionResultType() + " and " + endIndex.getFunctionResultType());
            }

            // Create and return the SUB expression instance
            return new Sub(source, startIndex, endIndex);
        }
    },
    REF {
        @Override
        public Expression parse(List<String> arguments) {
            // validations of the function. it should have exactly one argument
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for REF function. Expected 1, but got " + arguments.size());
            }

            // verify indeed argument represents a reference to a cell and create a Coordinate instance. if not ok returns a null. need to verify it
            String cellId =arguments.get(0).trim();
            Coordinate target = CoordinateFactory.from(cellId);
            if (target == null) {
                throw new IllegalArgumentException("Invalid argument for REF function. Expected a valid cell reference, but got " + arguments.get(0));
            }

            // should verify if the coordinate is within boundaries of the sheet ?
            // ...

            return new Ref(target);
        }
    };


    abstract public Expression parse(List<String> arguments);

    public static Expression parseExpression(String input) {

        // Check for expressions enclosed in braces
        if (input.startsWith("{") && input.endsWith("}")) {

            String functionContent = input.substring(1, input.length() - 1);
            List<String> topLevelParts = parseMainParts(functionContent);
//////Add check for the function name
            // Extract the function name and ensure no trimming
            String functionName = topLevelParts.get(0).toUpperCase();

            // Remove the first element (function name) from the parts
            topLevelParts.remove(0);

            // Return the parsed function expression
            return FunctionParser.valueOf(functionName).parse(topLevelParts);
        }

        // If the input is not wrapped in {}, return it directly without trimming spaces
        return FunctionParser.IDENTITY.parse(List.of(input));
    }

    private static List<String> parseMainParts(String input) {
        List<String> parts = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        boolean insideQuotes = false;

        for (char c : input.toCharArray()) {
            // Toggle insideQuotes flag when encountering a quote
            if (c == '"') {
                insideQuotes = !insideQuotes;
            }

            if (c == '{' && !insideQuotes) {
                stack.push(c);
            } else if (c == '}' && !insideQuotes) {
                stack.pop();
            }

            // Detect commas that separate top-level parts, but preserve spaces within braces
            if (c == ',' && stack.isEmpty() && !insideQuotes) {
                // Add the current buffer to parts list without trimming
                parts.add(buffer.toString());
                buffer.setLength(0); // Clear the buffer for the next part
            } else {
                buffer.append(c); // Append the character to the buffer
            }
        }

        // Add the final part to the list
        if (buffer.length() > 0) {
            parts.add(buffer.toString());
        }

        return parts;
    }



    public static void main(String[] args) {

        //String input = "plus, {plus, 1, 2}, {plus, 1, {plus, 1, 2}}";
//        String input = "1";
//        parseMainParts(input).forEach(System.out::println);

        //String input = "{concat,   hello   ooo,     word}";
        String input = "{plus, {Plus, 44, 0}, {plus, 1, 2}}";
//        String input = "{upper_case, hello world}";
//        String input = "4";
        Expression expression = parseExpression(input);
        //EffectiveValue result = expression.eval();
        //System.out.println("result: " + result.getValue() + " of type " + result.getCellType());
    }
}

