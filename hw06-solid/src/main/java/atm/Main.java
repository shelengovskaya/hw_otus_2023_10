package atm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ATMService atmService = new ATMService();
        System.out.println(atmService.getBalance());

        List<Banknote> banknotes = new ArrayList<>();
        banknotes.add(Banknote.FIFTY);
        banknotes.add(Banknote.TEN);
        atmService.depositCash(banknotes);
        System.out.println(atmService.getBalance());

        Map<Banknote, Integer> banknoteCount = atmService.withdrawCash(100);
        System.out.println(banknoteCount);

        System.out.println(atmService.getBalance());
    }
}
