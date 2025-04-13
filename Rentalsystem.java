import java.util.*;

class Car {
    String brand;
    String model;
    int seats;
    int rentPerDay;

    Car(String brand, String model, int seats, int rentPerDay) {
        this.brand = brand;
        this.model = model;
        this.seats = seats;
        this.rentPerDay = rentPerDay;
    }
}

class Customer {
    String name;
    String id;
    String rentedCar;
    int rentDays;
    int rentAmount;
    Date rentDate;
    Date returnDate;

    Customer(String name, String id, String rentedCar, Date rentDate, Date returnDate, int rentAmount) {
        this.name = name;
        this.id = id;
        this.rentedCar = rentedCar;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.rentAmount = rentAmount;
        this.rentDays = calculateDays(rentDate, returnDate);
    }

    int calculateDays(Date from, Date to) {
        long diffInMillies = to.getTime() - from.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24));
    }
}

class RentalService {
    List<Car> cars = new ArrayList<>();
    List<Customer> rentedCars = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public RentalService() {
        initializeCars();
    }

    void initializeCars() {
        for (int i = 1; i <= 9; i++) {
            cars.add(new Car("BRAND" + i, "Model" + i, (i % 3 == 0) ? 8 : 4, 250));
        }
    }

    void displayCars() {
        System.out.println("################################################");
        System.out.println("# CAR BRAND   # MODEL   # RENT PER DAY (Php)   #");
        System.out.println("################################################");
        for (Car car : cars) {
            System.out.printf("# %-12s # %-7s # %-21d #\n", car.brand, car.model, car.rentPerDay);
        }
        System.out.println("################################################");
    }

    Car findCar(String brand) {
        for (Car car : cars) {
            if (car.brand.equalsIgnoreCase(brand)) return car;
        }
        return null;
    }

    boolean isAvailable(String brand, Date rentDate, Date returnDate) {
        for (Customer c : rentedCars) {
            if (c.rentedCar.equalsIgnoreCase(brand)) {
                if (!(returnDate.before(c.rentDate) || rentDate.after(c.returnDate))) {
                    return false;
                }
            }
        }
        return true;
    }

    void rentCar() {
        displayCars();
        System.out.print("Enter brand of car you want to rent: ");
        String brand = scanner.next();

        Car car = findCar(brand);
        if (car == null) {
            System.out.println("Car brand not found.");
            return;
        }

        System.out.print("Enter rent date (dd MM yyyy): ");
        Date rentDate = inputDate();
        System.out.print("Enter return date (dd MM yyyy): ");
        Date returnDate = inputDate();

        if (!isAvailable(brand, rentDate, returnDate)) {
            System.out.println("Car not available in selected dates.");
            return;
        }

        System.out.print("Enter your name: ");
        String name = scanner.next();
        System.out.print("Enter your ID: ");
        String id = scanner.next();

        int days = (int) ((returnDate.getTime() - rentDate.getTime()) / (1000 * 60 * 60 * 24));
        int amount = days * car.rentPerDay;

        Customer customer = new Customer(name, id, brand, rentDate, returnDate, amount);
        rentedCars.add(customer);

        System.out.println("Booking Confirmed:");
        System.out.printf("Name: %s\nID: %s\nCar: %s\nDays: %d\nRent: Php %d\n", name, id, brand, days, amount);
    }

    void returnCar() {
        System.out.print("Enter your ID: ");
        String id = scanner.next();

        for (Customer c : rentedCars) {
            if (c.id.equals(id)) {
                System.out.printf("Welcome %s! Please confirm your original rent and return dates:\n", c.name);
                System.out.print("Enter original rent date (dd MM yyyy): ");
                Date originalRentDate = inputDate();
                System.out.print("Enter actual return date (dd MM yyyy): ");
                Date actualReturnDate = inputDate();

                int actualDays = (int) ((actualReturnDate.getTime() - originalRentDate.getTime()) / (1000 * 60 * 60 * 24));
                int lateFee = 0;

                if (actualDays > c.rentDays) {
                    lateFee = (actualDays - c.rentDays) * 50;
                }

                int finalAmount = c.rentAmount + lateFee;
                System.out.println("Final amount to pay: Php " + finalAmount);
                rentedCars.remove(c);
                return;
            }
        }

        System.out.println("Customer not found.");
    }

    Date inputDate() {
        int d = scanner.nextInt();
        int m = scanner.nextInt() - 1; // month is 0-based in Calendar
        int y = scanner.nextInt();
        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d, 0, 0, 0);
        return cal.getTime();
    }
}

public class CarRentalApp {
    public static void main(String[] args) {
        RentalService service = new RentalService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Rent a Car\n2. Return a Car\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    service.rentCar();
                    break;
                case 2:
                    service.returnCar();
                    break;
                case 3:
                    System.out.println("Thanks for visiting!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
