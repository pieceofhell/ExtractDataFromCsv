#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>

using namespace std;

class Invoice
{
public:
    const int nextId = 1;
    int id;
    string numeroBanco;
    string numeroAgencia;
    string numeroConta;
    // Date data;
    string numeroDocumento;
    long dataNum;
    string dataString;
    string historico;
    float valor;

    Invoice()
    {
        this->id = 0;
        this->numeroBanco = "";
        this->numeroAgencia = "";
        this->numeroConta = "";
        this->numeroDocumento = "";
        this->dataNum = 0;
        this->dataString = "";
        this->historico = "";
        this->valor = 0;
    }

    Invoice(string numeroBanco, string numeroAgencia, string numeroConta, string numeroDocumento, long dataNum, string dataString, string historico, float valor)
    {
        this->id = nextId;
        this->numeroBanco = numeroBanco;
        this->numeroAgencia = numeroAgencia;
        this->numeroConta = numeroConta;
        this->numeroDocumento = numeroDocumento;
        this->dataNum = dataNum;
        this->dataString = dataString;
        this->historico = historico;
        this->valor = valor;
    }

    void print()
    {
        cout << "ID: " << this->id << endl;
        cout << "Numero Banco: " << this->numeroBanco << endl;
        cout << "Numero Agencia: " << this->numeroAgencia << endl;
        cout << "Numero Conta: " << this->numeroConta << endl;
        cout << "Numero Documento: " << this->numeroDocumento << endl;
        cout << "Data: " << this->dataString << endl;
        cout << "Historico: " << this->historico << endl;
        cout << "Valor: " << this->valor << endl;
    }

    void read(const string &filename)
    {
        ifstream myfile;
        myfile.open(filename);
        string myline;
        if (myfile.is_open())
        {
            while (myfile)
            {
                getline(myfile, myline);
                cout << myline << '\n';
                vector<string> fields = split(myline, ';');
                if (fields.size() >= 7)
                {
                    this->numeroBanco = fields[0];
                    this->numeroAgencia = fields[1];
                    this->numeroConta = fields[2];
                    this->dataString = fields[3];
                    this->numeroDocumento = fields[4];
                    this->historico = fields[5];
                    this->valor = stof(fields[6]);
                }
            }
        }
        else
        {
            cout << "Couldn't open file\n";
        }
    }

    vector<string> split(const string &s, char delimiter)
    {
        vector<string> fields;
        string token;
        stringstream ss(s);

        while (getline(ss, token, delimiter))
        {
            fields.push_back(token);
        }

        return fields;
    }
};

int main()
{
    cout << "gyat" << endl;
    Invoice i;
    i.read("1.txt");
    return 0;
}