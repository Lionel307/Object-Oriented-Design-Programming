package average;

public class Average {
    /**
     * Returns the average of an array of numbers
     * 
     * @param the array of integer numbers
     * @return the average of the numbers
     */
    public float computeAverage(int[] nums) {
        float result = 0;
        // Add your code
        
        for(int i = 0; i < nums.length; i++) { 
            result += nums[i];
        } 
        result = result/nums.length;
        return result;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4, 5, 6};
        int[] arr2 = {2, 4, 6, 8, 10, 12, 100, 32, 0};
        int[] arr3 = {-12, 2, -32, 4, 5, 6};

        Average aver = new Average();

        System.out.println("The average of all the elements = "+aver.computeAverage(arr1));
        System.out.println("The average of all the elements = "+aver.computeAverage(arr2));
        System.out.println("The average of all the elements = "+aver.computeAverage(arr3));
    }
}
