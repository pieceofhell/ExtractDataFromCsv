package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
)

// Invoice represents an invoice with 7 string attributes.
type Invoice struct {
	Attribute1 string
	Attribute2 string
	Attribute3 string
	Attribute4 string
	Attribute5 string
	Attribute6 string
	Attribute7 string
}

func readInvoices(filePath string) ([]Invoice, error) {
	// Open the file.
	file, err := os.Open(filePath)
	if err != nil {
		return nil, fmt.Errorf("failed to open file: %w", err)
	}
	defer file.Close()

	// Create a scanner to read the file line by line.
	scanner := bufio.NewScanner(file)

	var invoices []Invoice

	// Read each line and process it.
	for scanner.Scan() {
		line := scanner.Text()
		parts := strings.Split(line, ";")
		if len(parts) != 7 {
			log.Printf("line does not have 7 parts: %s", line)
			continue
		}

		invoice := Invoice{
			Attribute1: parts[0],
			Attribute2: parts[1],
			Attribute3: parts[2],
			Attribute4: parts[3],
			Attribute5: parts[4],
			Attribute6: parts[5],
			Attribute7: parts[6],
		}
		invoices = append(invoices, invoice)
	}

	// Check for errors during the scanning process.
	if err := scanner.Err(); err != nil {
		return nil, fmt.Errorf("error reading file: %w", err)
	}

	return invoices, nil
}

func main() {
	// Open the file.
	var filePath string = "C:/Users/henri/code/github/ExtractDataFromCsv/mercantilgo/1.txt"

	// Create a slice of invoices.
	var invoices []Invoice

	// Read the invoices from the file.
	invoices, err := readInvoices(filePath)

	if err != nil {
		log.Fatal(err)
	}

	// Print the invoices to verify the output.
	for _, invoice := range invoices {
		fmt.Printf("%+v\n", invoice)
	}
}