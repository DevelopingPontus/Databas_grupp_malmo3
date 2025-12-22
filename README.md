# Databas_grupp_malmo3

## Usage

The program is a CLI application that allows users to manage products, customers, orders, and payments in a PostgreSQL database.

### Commands

- `--help`: Displays help information about available commands
- `import <json_file_path>`: Imports data from a JSON file into the database
- `exit`: Exits the application

#### Category Commands

- `category add <category_name>`: Adds a new category
- `category list`: Lists all categories

#### Cart Commands

- `cart add <customer_email> <product_sku> <quantity>`: Adds a product to a customer's cart
- `cart checkout <customer_email> <payment_method>`: Checks out the customer's cart using the specified payment method

#### Customer Commands

- `customer add <customer_name> <customer_email>`: Adds a new customer
- `customer list`: Lists all customers
- `customer find --id <customer_id>`: Finds a customer by ID
- `customer find --name <customer_name>`: Finds customers by name
- `customer find --email <customer_email>`: Finds customers by email
- `customer remove <customer_id>`: Removes a customer by ID

#### Product Commands

- `product add <product_sku> <product_name> <price> [<product_description>]`: Adds a new product with optional description
- `product list [--sku <product_sku>] [--name <product_name>]`: Lists products with optional filters
- `product remove <product_id> | [--sku <product_sku>]`: Removes a product by ID or SKU

#### Clear Commands

These commands clear data from specific tables in the database.
The options can be combined to clear multiple tables at once.

- `clear --all`: Clears all data from the database
- `clear --categories`: Clears all categories
- `clear --customers`: Clears all customers
- `clear --inventory`: Clears all inventory
- `clear --products`: Clears all products
- `clear --orders`: Clears all orders
- `clear --payments`: Clears all payments

## Building

### Prerequisites

- Java 24
- Maven 4.x
- A running instance of PostgreSQL

### Setup

1. Clone the repository
2. Configure the database connection in `src/main/resources/application.properties`
3. Run the schema setup script located in `src/main/resources/schema.sql` to create the necessary tables in your PostgreSQL database.
4. Build the project using Maven:

   ```bash
   mvn clean install
   ```

5. Run the application:

   ```bash
   mvn spring-boot:run
   ```

   or using the provided executable script:

   Linux /macOS:

   ```bash
   ./storeapp
   ```

   Windows:

   ```cmd
   storeapp.bat
   ```

## Database Schema

![ER Diagram](res/er_diagram.svg)

## Performance Testing

The performance tests uses a Python script to measure the execution time of cart operations (adding items to the cart and checking out) on datasets of varying sizes.

To run performance tests for the cart operations, first build the project without running tests:

```bash
mvn clean package -DskipTests
```

then execute the performance test using the following command:

```bash
./test_performance.py
```

The performance test script will run a series of add-to-cart and checkout operations on datasets of varying sizes (small, medium, large) and report the median execution times for each operation. Both the total time and the database interaction time are measured and displayed.

```text
Summary (Total / Internal):

Dataset    | Cart Add (median)      | Checkout (median)
-----------|------------------------|------------------------
small      |      1834.5ms / 89.0ms |        1817.5ms / 83ms
medium     |      1822.0ms / 89.0ms |      1826.0ms / 83.0ms
large      |      1869.5ms / 92.0ms |        1817.5ms / 88ms

Detailed results available in: performance_results.csv
```
