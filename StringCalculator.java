import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringCalculator {
  public static void main(String[] args) {
    System.out.println("\n1) Test stringa vuota: " + add(""));
    System.out.println("\n2) Test delimitatore di default: " + add("1,2"));
    System.out.println("\n3) Test controllo numeri switch numeri superiori a 1000: " + add("1001,2"));
    System.out.println("\n4) Test delimitatore personalizzato (-): " + add("//[|]//1|2|3"));
    System.out.println("\n5) Test doppio delimitatore personalizzato (--) " + add("//[--]//1--3--6"));
  }
  /**
   * 
   * @param input
   * Il metodo copre tutti i casi di test previsti nell'assegnazione
   * @return int result
   */
  public static int add(String input) {
    if(input.isBlank() == true) return 0;

    // *** Porzione di codice da approfondire (stack-overflow) **
    String delimiter = ",";
        
        // ** controllo se c'è un delimitatore personalizzato **

        // Matcher --> tenta di trovare una corrispondenza tra "espressione regolare" (regex) e stringa di input
        // con "Pattern.compile" definisco un modello di ricerca (praticamente è "regex")
        // Ricorda: vedi significato degli scomparti di regex.
        Matcher matcher = Pattern.compile("//\\[(.*?)\\]//(.*)").matcher(input);
        if (matcher.matches()) {
            delimiter = Pattern.quote(matcher.group(1)); // estraggo delimitatore personalizzato
            input = matcher.group(2); // estraggo scomparto numeri
        }

    List<Integer> addends = Arrays.stream(input.split(delimiter)).map(Integer::parseInt).collect(Collectors.toList());

    List<Integer> negatives = addends.stream().filter(n -> n < 0).collect(Collectors.toList());
        if (!negatives.isEmpty()) throw new IllegalArgumentException("negatives not allowed: " + negatives);
    

    int result = 0;
    for (Integer integer : addends) {
      if(integer > 1000) continue;
      result += integer;
    }
    return result;
  }
}
