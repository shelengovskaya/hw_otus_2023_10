package atm;

import java.util.EnumMap;
import java.util.Map;

public class Storage {
    private final Map<Banknote, Integer> balance;

    public Storage() {
        this.balance = new EnumMap<>(Banknote.class);
    }

    public void acceptBill(Banknote banknote) {
        if (balance.containsKey(banknote)) {
            balance.put(banknote, balance.get(banknote) + 1);
        } else {
            balance.put(banknote, 1);
        }
    }

    public int getBalance() {
        int sum = 0;
        for (Map.Entry<Banknote, Integer> banknoteCount : balance.entrySet()) {
            sum += banknoteCount.getKey().getBanknote() * banknoteCount.getValue();
        }
        return sum;
    }

    public Map<Banknote, Integer> getBills(int sum) {
        int currSum = sum;
        Map<Banknote, Integer> banknotes = new EnumMap<>(Banknote.class);
        for (Banknote banknote : Banknote.values()) {
            Integer count = balance.get(banknote);
            while (count > 0 && currSum >= banknote.getBanknote()) {
                if (banknotes.containsKey(banknote)) {
                    banknotes.put(banknote, banknotes.get(banknote) + 1);
                } else {
                    banknotes.put(banknote, 1);
                }
                balance.put(banknote, balance.get(banknote) - 1);
                currSum -= banknote.getBanknote();
                count -= 1;
            }
        }
        if (currSum != 0) {
            throw new RuntimeException("There are not enough banknotes.");
        }
        return banknotes;
    }
}
