package homework;

import static java.util.Map.entry;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> customerData;

    public CustomerService() {
        customerData = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        return makeEntryCopy(customerData.firstEntry());
    }

    private Map.Entry<Customer, String> makeEntryCopy(Map.Entry<Customer, String> customerData) {
        if (customerData == null) {
            return null;
        }
        return entry(new Customer(customerData.getKey()), customerData.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return makeEntryCopy(customerData.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customerData.put(customer, data);
    }
}
