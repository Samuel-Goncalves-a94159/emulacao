import java.nio.charset.Charset;
import java.util.Random;
class RandomString{


    // Constructor
    public RandomString()
    {

    }


   public static String newString() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 5;
    Random random = new Random();

    String generatedString = random.ints(leftLimit, rightLimit + 1)
      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
      .limit(targetStringLength)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();

        return generatedString;
   }
   public static void main(String[] args) {
    RandomString rs = new RandomString();    
    System.out.println("String Aleatoria-->" + rs.newString());
   }
}