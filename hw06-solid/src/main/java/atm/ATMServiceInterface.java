package atm;

import java.util.List;
import java.util.Map;

public interface ATMServiceInterface {

    int getBalance();

    void depositCash(List<Banknote> banknotes);

    Map<Banknote, Integer> withdrawCash(int sum);
}
