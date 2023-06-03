package bool;

public class BooleanEvaluator {

    public static boolean evaluate(BooleanNode expression) {
        // Return the expression evaluated
        return expression.compute();
    }

    public static String prettyPrint(BooleanNode expression) {
        // Pretty print the expression
        String printExpression = "(AND";
        
        if (expression instanceof OrExpression) {
            printExpression = "(OR ";
        }
        return printExpression;
    }

    public static void main(String[] args) {
        BooleanNode e = new OrExpression(new Bool(true), new NotExpression(new AndExpression(new Bool(false), new OrExpression(new Bool(true), new Bool(false)))));
        BooleanNode b = new OrExpression(new Bool(false), new NotExpression(new Bool(false)));
        System.out.println(BooleanEvaluator.evaluate(e));
        System.out.println(BooleanEvaluator.evaluate(b));
        System.out.println(BooleanEvaluator.prettyPrint(e));
        // BooleanEvaluator.evaluate(...)
    }


}