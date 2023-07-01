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
    System.out.println("\n5) Test delimitatore personalizzato doppio (--) " + add("//[--]//1--3--6"));
    System.out.println("\n6) Test doppio delimitatore unico ( [/][*] ) " + add("//[/][*]//2/3*6"));
    System.out.println("\n7) Test doppio delimitatore doppio ( [//][**] ) " + add("//[//][**]//2//4**6"));
  }

  /**
   * 
   * @param input
   *              Il metodo copre tutti i casi di test previsti nell'assegnazione.
   * @return int result
   */
  public static int add(String input) {
    if (input.isBlank() == true)
      return 0;

    // *** Porzione di codice da approfondire (stack-overflow) **
    String delimiter = ",";

    // ** controllo se c'è un delimitatore personalizzato **

    // Matcher --> tenta di trovare una corrispondenza tra "espressione regolare"
    // (regex) e stringa di input
    // con "Pattern.compile" definisco un modello di ricerca (praticamente è
    // "regex")
    // SOTTO: descrizione degli scomparti di regex.
    Matcher matcher = Pattern.compile("//((?:\\[[^\\]]+\\])+)//(.*)").matcher(input);
    if (matcher.matches()) {
      String[] delimiters = matcher.group(1).split("\\]\\[");
      delimiter = Arrays.stream(delimiters).map(d -> Pattern.quote(d.replace("[", "").replace("]", "")))
          .collect(Collectors.joining("|"));
      input = matcher.group(2);
    }

    List<Integer> addends = Arrays.stream(input.split(delimiter)).map(Integer::parseInt).collect(Collectors.toList());

    List<Integer> negatives = addends.stream().filter(n -> n < 0).collect(Collectors.toList());
    if (!negatives.isEmpty())
      throw new IllegalArgumentException("negatives not allowed: " + negatives);

    int result = 0;
    for (Integer integer : addends) {
      if (integer > 1000)
        continue;
      result += integer;
    }
    return result;
  }
}

// ** FUNZIONAMENTO DEL PATTERN **

// java.util.regex --> fornisce supporto per le espressioni regolari. Gli oggettti Matcher e Pattern fanno entrambi parte del pacchetto. Mentre Prattern rappresenta un'espressione regolare che viene compilata, l'oggetto Matcher è uno strumento per la ricerca di quella espressione all'interno di una stringa.

// ** ESPRESSIONE REGOLARE X ASSEGNAZIONE **

// * // ..... // * --> cerco i doppi slash all'interno dell'input
// * ( ....... ) * --> primo gruppo di cattura. Il gruppo di cattura fotografa una parte della stringa di input che corrisponde all'espressione regolare al suo interno
// * (?: ...... ) * --> gruppo non catturante. Nel primo gruppo di cattura è inserito un gruppo non catturante. In questo caso (cioè nel caso di un gruppo non catturante all'interno di un gruppo di cattura) il gruppo non catturante mi serve per costuire la sequenza di caratteri che formerà il mio delimitatore personalizzato.
// * (?: \\[ ......\\]) * --> con l'escape dell'escape delle quadre. In questo modo rimuovo il significato speciale attribuito alle quadre all'interno di un'espressione regolare.
// * [^\\]] * --> con le quadre esterne, aventi il loro significato speciale all'interno del regex, posso raggruppare tutti i caratteri possibili che saranno parte della sequenza del delimitatore. NB con ^\\] intendo: tutti i caratteri tranne la quadra di chiusura (che preceduta dai \\ è svuotata del suo significato speciale. Fa quindi riferimento alla stringa di input)
// * ((?: \\[[^\\]]+\\])) * --> con il + in quella posizione intendo dire: tutte le occorrenze dei caratteri interni al delimitatore. Questo permette di gestire più caratteri all'interno del delimitatore.
// * ((?: \\[[^\\]]+\\])+) * --> con il + in seconda posizione faccio riferimento al gruppo non catturante, qundi all'intera sequenza [delim]. Quello che segnalo è la possibilità di più occorrenze della sequenza del delimitatore. Questo permette di gestire input con più delimitatori personalizzati, come //[*][%]\n1*2%3, dove * e % sono entrambi delimitatori.
// * //((?:\\[[^\\]]+\\])+)// * --> cerco i doppi slash che chiudono la sequenza del delimitatore. Questi slash sono quelli che seguono immediatamente la sequenza del delimitatore.
// * //((?:\\[[^\\]]+\\])+)//(.*) * --> con (.*) creo un secondo gruppo di cattura e fotografo tutto ciò che segue i doppi slash di chiusura. Questo sarà il resto della stringa di input da elaborare, cioè la stringa di numeri da sommare.
// * .* * --> Il punto . in un'espressione regolare corrisponde a qualsiasi carattere singolo, ad eccezione dei caratteri di fine riga. Quindi, in pratica, punto . è un segnaposto che dice "qualsiasi carattere può andare qui".
// * .* * --> L'asterisco * in un'espressione regolare significa "zero o più |OCCORRENZE DEL CARATTERE| o del gruppo precedente". Quindi, .* insieme significa "zero o più occorrenze di qualsiasi carattere", che è un modo per catturare il resto della stringa, indipendentemente da ciò che contiene.





