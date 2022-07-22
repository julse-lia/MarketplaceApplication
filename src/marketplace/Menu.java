package marketplace;

import java.util.*;

class Menu {
    private static ArrayList<Product> products = new ArrayList<Product>();
    private static ArrayList<User> users = new ArrayList<User>();
    private static HashMap<User, ArrayList<Product>> userProducts = new HashMap<User, ArrayList<Product>>();
    private static int userIdCount = 0;
    private static int productIdCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Let`s add new users to the system.");

        String answer = "y";
        do {
            try{
                System.out.println("Please, enter a new user name:");
                String newUserName = scanner.next();
                System.out.println("Please, enter a new user lastname:");
                String newUserLastName = scanner.next();
                System.out.println("Please, enter a new user amount of money:");
                float newUserAmountOfMoney = scanner.nextFloat();
                addUser(newUserName, newUserLastName, newUserAmountOfMoney);
                System.out.println("If you want to add more users, enter - y, else - n:");
                answer = scanner.next();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                answer = "y";
            }
        }
        while (answer.equals("y"));

        System.out.println("Let`s add new products to the system.");
        do {
            try {
                System.out.println("Please, enter a new product name:");
                String newProductName = scanner.next();
                System.out.println("Please, enter a new product price:");
                float newProductPrice = scanner.nextFloat();
                addProduct(newProductName, newProductPrice);
                System.out.println("If you want to add more products, enter - y, else - n:");
                answer = scanner.next();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                answer = "y";
            }
        }
        while (answer.equals("y"));

        displayUsers();
        displayProducts();

        System.out.println("Let`s make some user purchase.");

        do {
            System.out.println("Please, enter a user id: ");
            int userId = scanner.nextInt();
            System.out.println("Please, enter a product id: ");
            int productId = scanner.nextInt();
            purchaseProduct(productId, userId);
            System.out.println("If you want to make more purchases, enter - y, else - n:");
            answer = scanner.next();
        }
        while (answer.equals("y"));

        System.out.println("If you want to see some user list of products, enter - y, else - n");

        answer = scanner.next();
        while (answer.equals("y")){
            System.out.println("Please, enter the user id to see his/her list of products:");
            int user_id = scanner.nextInt();
            listOfProductsByUserId(user_id);

            System.out.println("If you want to check more user product list, enter - y, else - n:");
            answer = scanner.next();
        }

        System.out.println("If you want to see who bought certain product, enter - y, else - n");

        answer = scanner.next();
        while (answer.equals("y")) {
            System.out.println("Please, enter the product id to see list of users who bought this product:");
            int product_id = scanner.nextInt();
            listOfUsersByProductId(product_id);

            System.out.println("If you want to check other product buyers, enter - y, else - n:");
            answer = scanner.next();
        }

        System.out.println("If you want to delete some user, enter - y, else - n");

        answer = scanner.next();
        while (answer.equals("y")){
            System.out.println("Please, enter the user id:");
            int userIdToDelete = scanner.nextInt();
            deleteUser(userIdToDelete);

            if (!users.isEmpty()){
                System.out.println("If you want to delete more users, enter - y, else - n");
                answer = scanner.next();
            }
            else {
                answer = "n";
            }
        }

        System.out.println("If you want to delete some product, enter - y, else - n");

        answer = scanner.next();
        while (answer.equals("y")){
            System.out.println("Please, enter the product id:");
            int productIdToDelete = scanner.nextInt();
            deleteProduct(productIdToDelete);

            if (!products.isEmpty()){
                System.out.println("If you want to delete more products, enter - y, else - n");
                answer = scanner.next();
            }
            else {
                answer = "n";
            }
        }

        scanner.close();
    }

    public static void displayUsers() {
        for (User user: users) {
            System.out.println(user + "\n");
        }
    }

    public static void displayProducts() {
        for (Product product: products) {
            System.out.println(product + "\n");
        }
    }

    public static User findUserById(int id) {
        User userById = new User();

        for (User user: users) {
            if (user.getId() == id) {
                userById = user;
            }
        }
        return userById;
    }

    public static Product findProductById(int id) {
        Product productById = new Product();

        for (Product product : products) {
            if (product.getId() == id) {
                productById = product;
            }
        }
        return productById;
    }
    
    public static void purchaseProduct(int productId, int userId) {

        Product productToBuy = findProductById(productId);
        User buyer = findUserById(userId);

        if (productToBuy.getPrice() <= buyer.getAmountOfMoney()) {
            if (userProducts.containsKey(buyer)) {
                userProducts.get(buyer).add(productToBuy);
            }
            else {
                ArrayList<Product> productArray = new ArrayList<Product>();
                productArray.add(productToBuy);
                userProducts.put(buyer, productArray);
            }
            // Update user`s amount of total money
            buyer.setAmountOfMoney(buyer.getAmountOfMoney() - productToBuy.getPrice());
            System.out.println("Congratulations! You`ve made a successful purchase of " + productToBuy.getName() + "!");
        }
        else {
            System.out.println("Sorry, it seems you don`t have enough money to buy that product.");
        }
    }

    public static void listOfProductsByUserId(int id) {
        ArrayList<String> productsNamesList = new ArrayList<String>();
        User user = findUserById(id);
        ArrayList<Product> products = userProducts.get(user);

        try {
            if (!products.isEmpty()) {
                for (Product product : products) {
                    productsNamesList.add(product.getName());
                }
                System.out.println(Arrays.toString(productsNamesList.toArray()));
            }
        }
        catch (NullPointerException exception) {
//            System.out.println("Product list is empty yet.");
        }
    }

    public static void listOfUsersByProductId(int id) {
        ArrayList<String> userNamesWhoBoughtProduct = new ArrayList<String>();
        Product product = findProductById(id);
        // Iterate over all users
        for (User user: userProducts.keySet()) {
            ArrayList<Product> products = userProducts.get(user);
            // Check if user bought the product
            if (products.contains(product)) {
                userNamesWhoBoughtProduct.add(user.getName() + " " + user.getLastname());
            }
        }
        if (!userNamesWhoBoughtProduct.isEmpty()) {
            System.out.println(Arrays.toString(userNamesWhoBoughtProduct.toArray()));
        }

    }

    public static void addUser(String name, String lastname, float amountOfMoney) {
        if (name != null && !name.isBlank() && lastname != null && !lastname.isBlank() && !(amountOfMoney < 0)) {
            User user = new User();
            user.setId(userIdCount);
            user.setName(name);
            user.setLastname(lastname);
            user.setAmountOfMoney(amountOfMoney);
            users.add(user);
            userIdCount++;
        }
        else {
            throw new InputMismatchException("Invalid input! Please, ensure user`s name and surname" +
                    " is not empty and amount of money is not less than 0.");
        }
    }

    public static void addProduct(String name, float price) {
        if (name != null && !name.isBlank() && !(price <= 0)) {
            Product product = new Product();
            product.setId(productIdCount);
            product.setName(name);
            product.setPrice(price);
            products.add(product);
            productIdCount++;
        }
        else {
            throw new InputMismatchException("Invalid input product name or price. Please, ensure " +
                    "product name is not empty and price is not less than 0.");
        }
    }

    public static void deleteUser(int id) {
        User userById = findUserById(id);
        users.remove(userById);
        System.out.println("Total users left:");
        displayUsers();
        if (userProducts.containsKey(userById)) {
            userProducts.remove(userById);
        }
    }

    public static void deleteProduct(int id) {
        Product productById = findProductById(id);
        products.remove(productById);
        for (User user: userProducts.keySet()) {
            ArrayList<Product> products = userProducts.get(user);
            if (userProducts.get(user).contains(productById)) {
                userProducts.get(user).remove(productById);
                System.out.println(user.getName() + "`s list of left products: " + userProducts.get(user));
            }
        }
        System.out.println("Total products left:");
        displayProducts();
    }
}
