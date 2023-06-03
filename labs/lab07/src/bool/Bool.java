package bool;

public class Bool implements BooleanNode{

    private boolean bool;

    public Bool(boolean bool) {
        this.bool = bool;
    }
    
    @Override
    public boolean compute() {
        // TODO Auto-generated method stub
        return bool;
    }
    
}
