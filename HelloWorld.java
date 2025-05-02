public class HelloWorld {
     
    public static void main(String[] args) {
        // Print a message to the console
        System.out.println("Hello, World!");

        // Call another method to print another message
        printGreeting("Elnatan");
    }

    // Method to print a personalized greeting
    public static void printGreeting(String name) {
        System.out.println("Hello, " + name + "! Welcome to Java programming.");
    }
}
