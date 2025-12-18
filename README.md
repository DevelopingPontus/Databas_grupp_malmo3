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

## Building

### Prerequisites

- Java 24
- Maven 4.x
- A running instance of PostgreSQL

### Setup

1. Clone the repository
2. Configure the database connection in `src/main/resources/application.properties`
3. Build the project using Maven:

   ```bash
   mvn clean install
   ```

4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## Database Schema

![ER Diagram](res/er_diagram.svg)
