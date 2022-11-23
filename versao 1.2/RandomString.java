

class RandomString{
    

    // Constructor
    public RandomString()
    {
        
    }


   public static String newString() {
        int x = (int) (Math.random() * 1000);
        
        String threadID = Integer.toString(x);

        return threadID;
   }     
}