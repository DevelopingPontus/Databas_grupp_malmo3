#!/usr/bin/env python3
"""
CLI Performance Test Script
Tests cart operations with different dataset sizes
"""

import json
import subprocess
import time
import statistics
import csv
import sys
from pathlib import Path

# Configuration
JAR_FILE = "target/storeapp-1.0-SNAPSHOT.jar"
TEST_SIZES = ["small", "medium", "large"]
ITERATIONS = 10
RESULTS_FILE = "performance_results.csv"

# ANSI color codes
GREEN = '\033[0;32m'
YELLOW = '\033[1;33m'
RED = '\033[0;31m'
NC = '\033[0m'  # No Color


def colored(text, color):
    """Wrap text in ANSI color codes"""
    return f"{color}{text}{NC}"


def run_command_args(jar_file, *args):
    """Run CLI command in non-interactive mode with arguments"""
    try:
        cmd = ['java', '-jar', jar_file] + list(args)
        result = subprocess.run(
            cmd,
            capture_output=True,
            text=True,
            timeout=60
        )
        return result.stdout, result.stderr
    except subprocess.TimeoutExpired:
        return "", "Command timed out"


def measure_time_args(jar_file, *args):
    """Measure command execution time in milliseconds (non-interactive mode)"""
    start = time.perf_counter()
    run_command_args(jar_file, *args)
    end = time.perf_counter()
    return int((end - start) * 1000)


def load_test_data(data_file):
    """Load test data and extract customer email and product SKU"""
    try:
        with open(data_file, 'r') as f:
            data = json.load(f)
        
        email = data['customers'][0]['email']
        sku = data['products'][0]['sku']
        return email, sku
    except (FileNotFoundError, KeyError, IndexError) as e:
        print(colored(f"Error loading {data_file}: {e}", RED))
        return None, None


def main():
    # Check if JAR exists
    if not Path(JAR_FILE).exists():
        print(colored(f"Error: JAR file not found: {JAR_FILE}", RED))
        print("Please run: mvn clean package -DskipTests")
        sys.exit(1)

    print(colored("=" * 50, GREEN))
    print(colored("  CLI Performance Test Suite", GREEN))
    print(colored("=" * 50, GREEN))
    print()

    # Initialize results
    all_results = []

    # Test each dataset size
    for size in TEST_SIZES:
        data_file = f"test_data_{size}.json"

        # Check if data file exists
        if not Path(data_file).exists():
            print(colored(f"Warning: Skipping {size} - file not found: {data_file}", YELLOW))
            print()
            continue

        print(colored(f"Testing with {size} dataset", GREEN))
        print(f"Data file: {data_file}")
        print()

        # Load test data
        email, sku = load_test_data(data_file)
        if not email or not sku:
            continue

        print(f"Using customer: {email}")
        print(f"Using product: {sku}")
        print()

        # Arrays to store timing results
        add_times = []
        checkout_times = []

        # Run iterations
        for i in range(1, ITERATIONS + 1):
            print(f"  Iteration {i}/{ITERATIONS}... ", end='', flush=True)

            # Clear database (non-interactive mode)
            run_command_args(JAR_FILE, "clear", "--all")

            # Import data (non-interactive mode)
            run_command_args(JAR_FILE, "import", data_file)

            # Measure: cart add (non-interactive mode)
            add_time = measure_time_args(JAR_FILE, "cart", "add", email, sku, "10")
            add_times.append(add_time)
            all_results.append({
                'dataset': size,
                'operation': 'cart_add',
                'iteration': i,
                'time_ms': add_time
            })

            # Measure: cart checkout (non-interactive mode)
            checkout_time = measure_time_args(JAR_FILE, "cart", "checkout", email, "CARD")
            checkout_times.append(checkout_time)
            all_results.append({
                'dataset': size,
                'operation': 'cart_checkout',
                'iteration': i,
                'time_ms': checkout_time
            })

            print(colored("✓", GREEN) + f" add: {add_time}ms, checkout: {checkout_time}ms")

        # Calculate medians
        median_add = statistics.median(add_times)
        median_checkout = statistics.median(checkout_times)

        print()
        print(colored(f"Results for {size} dataset:", YELLOW))
        print(f"  Cart Add - Median: {median_add}ms")
        print(f"  Checkout - Median: {median_checkout}ms")
        print()
        print("-" * 50)
        print()

    # Save results to CSV
    with open(RESULTS_FILE, 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['dataset', 'operation', 'iteration', 'time_ms'])
        writer.writeheader()
        writer.writerows(all_results)

    print(colored("=" * 50, GREEN))
    print(colored("  Test Complete!", GREEN))
    print(colored("=" * 50, GREEN))
    print()
    print(f"Results saved to: {RESULTS_FILE}")
    print()

    # Print summary table
    print("Summary:")
    print()
    print("Dataset    | Cart Add (median) | Checkout (median)")
    print("-----------|-------------------|------------------")

    for size in TEST_SIZES:
        size_results = [r for r in all_results if r['dataset'] == size]
        if size_results:
            add_times = [r['time_ms'] for r in size_results if r['operation'] == 'cart_add']
            checkout_times = [r['time_ms'] for r in size_results if r['operation'] == 'cart_checkout']

            if add_times and checkout_times:
                median_add = statistics.median(add_times)
                median_checkout = statistics.median(checkout_times)
                print(f"{size:<11}| {median_add:>15}ms | {median_checkout:>14}ms")

    print()
    print(f"Detailed results available in: {RESULTS_FILE}")


if __name__ == "__main__":
    main()
