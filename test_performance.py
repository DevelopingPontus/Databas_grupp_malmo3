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
    """Measure command execution time and extract internal timing from output"""
    start = time.perf_counter()
    stdout, stderr = run_command_args(jar_file, *args)
    end = time.perf_counter()
    
    total_time = int((end - start) * 1000)
    
    # Parse internal timing from output
    # Look for patterns like "Add to cart time: 45ms" or "Checkout time: 45ms"
    internal_time = None
    for line in stdout.split('\n'):
        if 'time:' in line.lower() and 'ms' in line:
            try:
                # Extract number before "ms"
                time_str = line.split('time:')[1].strip().split('ms')[0].strip()
                internal_time = int(time_str)
                break
            except (IndexError, ValueError):
                pass
    
    return total_time, internal_time


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
        add_times_total = []
        add_times_internal = []
        checkout_times_total = []
        checkout_times_internal = []

        # Run iterations
        for i in range(1, ITERATIONS + 1):
            print(f"  Iteration {i}/{ITERATIONS}... ", end='', flush=True)

            # Clear database (non-interactive mode)
            run_command_args(JAR_FILE, "clear", "--all")

            # Import data (non-interactive mode)
            run_command_args(JAR_FILE, "import", data_file)

            # Measure: cart add (non-interactive mode)
            add_time_total, add_time_internal = measure_time_args(JAR_FILE, "cart", "add", email, sku, "10")
            add_times_total.append(add_time_total)
            if add_time_internal:
                add_times_internal.append(add_time_internal)
            
            all_results.append({
                'dataset': size,
                'operation': 'cart_add',
                'iteration': i,
                'total_time_ms': add_time_total,
                'internal_time_ms': add_time_internal or 0
            })

            # Measure: cart checkout (non-interactive mode)
            checkout_time_total, checkout_time_internal = measure_time_args(JAR_FILE, "cart", "checkout", email, "CARD")
            checkout_times_total.append(checkout_time_total)
            if checkout_time_internal:
                checkout_times_internal.append(checkout_time_internal)
            
            all_results.append({
                'dataset': size,
                'operation': 'cart_checkout',
                'iteration': i,
                'total_time_ms': checkout_time_total,
                'internal_time_ms': checkout_time_internal or 0
            })

            internal_add_str = f"{add_time_internal}ms" if add_time_internal else "N/A"
            internal_checkout_str = f"{checkout_time_internal}ms" if checkout_time_internal else "N/A"
            print(colored("✓", GREEN) + f" add: {add_time_total}ms ({internal_add_str}), checkout: {checkout_time_total}ms ({internal_checkout_str})")

        # Calculate medians
        median_add_total = statistics.median(add_times_total)
        median_checkout_total = statistics.median(checkout_times_total)
        median_add_internal = statistics.median(add_times_internal) if add_times_internal else None
        median_checkout_internal = statistics.median(checkout_times_internal) if checkout_times_internal else None

        print()
        print(colored(f"Results for {size} dataset:", YELLOW))
        print(f"  Cart Add - Total: {median_add_total}ms, Internal: {median_add_internal}ms" if median_add_internal else f"  Cart Add - Total: {median_add_total}ms")
        print(f"  Checkout - Total: {median_checkout_total}ms, Internal: {median_checkout_internal}ms" if median_checkout_internal else f"  Checkout - Total: {median_checkout_total}ms")
        print()
        print("-" * 50)
        print()

    # Save results to CSV
    with open(RESULTS_FILE, 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['dataset', 'operation', 'iteration', 'total_time_ms', 'internal_time_ms'])
        writer.writeheader()
        writer.writerows(all_results)

    print(colored("=" * 50, GREEN))
    print(colored("  Test Complete!", GREEN))
    print(colored("=" * 50, GREEN))
    print()
    print(f"Results saved to: {RESULTS_FILE}")
    print()

    # Print summary table
    print("Summary (Total / Internal):")
    print()
    print("Dataset    | Cart Add (median)      | Checkout (median)")
    print("-----------|------------------------|------------------------")

    for size in TEST_SIZES:
        size_results = [r for r in all_results if r['dataset'] == size]
        if size_results:
            add_total = [r['total_time_ms'] for r in size_results if r['operation'] == 'cart_add']
            add_internal = [r['internal_time_ms'] for r in size_results if r['operation'] == 'cart_add' and r['internal_time_ms'] > 0]
            checkout_total = [r['total_time_ms'] for r in size_results if r['operation'] == 'cart_checkout']
            checkout_internal = [r['internal_time_ms'] for r in size_results if r['operation'] == 'cart_checkout' and r['internal_time_ms'] > 0]

            if add_total and checkout_total:
                median_add_total = statistics.median(add_total)
                median_add_internal = statistics.median(add_internal) if add_internal else 0
                median_checkout_total = statistics.median(checkout_total)
                median_checkout_internal = statistics.median(checkout_internal) if checkout_internal else 0
                
                add_str = f"{median_add_total}ms / {median_add_internal}ms" if median_add_internal else f"{median_add_total}ms / N/A"
                checkout_str = f"{median_checkout_total}ms / {median_checkout_internal}ms" if median_checkout_internal else f"{median_checkout_total}ms / N/A"
                print(f"{size:<11}| {add_str:>22} | {checkout_str:>22}")

    print()
    print(f"Detailed results available in: {RESULTS_FILE}")


if __name__ == "__main__":
    main()
