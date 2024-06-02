#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <iomanip>
#include <ctime>
#include <algorithm>

using namespace std;

// Function to trim spaces from a string
string trim(const string &s)
{
    auto start = s.begin();
    while (start != s.end() && isspace(*start))
    {
        start++;
    }

    auto end = s.end();
    do
    {
        end--;
    } while (distance(start, end) > 0 && isspace(*end));

    return string(start, end + 1);
}

bool isPositive(float num)
{
    return num > 0;
}

string floatToString(float num)
{
    ostringstream oss;
    oss << fixed << setprecision(2) << num;
    // now, after converting the float to a string, we need to replace the dot with a comma
    string result = oss.str();
    replace(result.begin(), result.end(), '.', ',');
    return result;
}

class Invoice
{
public:
    static int nextId;
    int id;
    string numeroBanco;
    string numeroAgencia;
    string numeroConta;
    tm data;
    string numeroDocumento;
    long dataNum;
    string dataString;
    string historico;
    float valor;

    Invoice() : id(nextId++) {}

    void print() const
    {
        cout << toString() << endl;
    }

    string toString() const
    {
        ostringstream oss;
        oss << "[" << id << " ## " << numeroBanco << " ## " << numeroAgencia << " ## "
            << numeroConta << " ## " << dataString << " ## " << numeroDocumento
            << " ## " << historico << " ## " << valor << "]";
        return oss.str();
    }
};

int Invoice::nextId = 1;

vector<Invoice> read(const string &fileName)
{
    ifstream file(fileName);
    vector<Invoice> allTransactions;
    string line;
    int lineCount = 0;

    while (getline(file, line))
    {
        lineCount++;
        if (lineCount <= 0)
            continue;

        istringstream iss(line);
        string field;
        Invoice transaction;

        getline(iss, field, ';');
        transaction.numeroBanco = trim(field);
        getline(iss, field, ';');
        transaction.numeroAgencia = trim(field);
        getline(iss, field, ';');
        transaction.numeroConta = trim(field);
        getline(iss, field, ';');
        transaction.dataString = trim(field);

        getline(iss, field, ';');
        transaction.numeroDocumento = trim(field);
        getline(iss, field, ';');
        transaction.historico = trim(field);
        getline(iss, field, ';');
        field.erase(remove(field.begin(), field.end(), ','), field.end());
        transaction.valor = field.empty() ? 0 : stof(field);

        allTransactions.push_back(transaction);
    }

    return allTransactions;
}

void writeToCsv(const string &fileName, vector<Invoice> &transactions)
{
    ofstream file(fileName);
    // file << "id;numeroBanco;numeroAgencia;numeroConta;data;numeroDocumento;historico;valor\n";

    for (auto &transaction : transactions)
    {
        if (isPositive(transaction.valor))
        {
            file << transaction.id << ";" << transaction.dataString << ";" << ";" << ";" << ";" << transaction.historico << ";" << ";" << ";" << ";" << floatToString(transaction.valor) << ";" << "\n";
        }
        else
        {
            file << transaction.id << ";" << transaction.dataString << ";" << ";" << ";" << ";" << transaction.historico << ";" << ";" << ";" << ";" << ";" << floatToString(transaction.valor * -1) << "\n";
            
        }
    }
}

int main()
{
    string chosenFilePath = "D:/gaming/site inovador/code/github/ExtractDataFromCsv/mercantilcpp/1.txt";
    // cout << "Enter the path to the CSV file: ";

    // cin >> chosenFilePath;

    try
    {
        vector<Invoice> allTransactions = read(chosenFilePath);
        int countRegisters = 0;

        for (const auto &transaction : allTransactions)
        {
            transaction.print();
            countRegisters++;
        }

        cout << "\nTotal de registros: " << countRegisters << endl;

        string outputPath = chosenFilePath.substr(0, chosenFilePath.find_last_of("/") + 1);
        string fileName = "arquivoSaida.csv";
        outputPath += fileName;

        writeToCsv(outputPath, allTransactions);
    }
    catch (const exception &e)
    {
        cerr << "ERRO NA LEITURA DO ARQUIVO! " << e.what() << endl;
    }

    return 0;
}
