package bool;

public class AndExpression implements BooleanNode{

    private BooleanNode expression1;
    private BooleanNode expression2;

    public AndExpression(BooleanNode expression1, BooleanNode expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public boolean compute() {
        
        return expression1.compute() && expression2.compute();
    }
    
}
