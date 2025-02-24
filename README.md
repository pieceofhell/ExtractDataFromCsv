# Invoice Register

## Descrição

O **Invoice Register** é uma aplicação Java executada no terminal que processa arquivos CSV contendo registros de transações financeiras. Ele permite a leitura, processamento e exportação desses dados em um formato organizado, identificando transações relacionadas a serviços de transporte (como por exemplo Uber, 99) e alimentação. 
Este projeto foi desenvolvido com o propósito de automatizar a manipulação dos dados bancários em formato CSV garantindo uma formatação mais polida para então poder transferir e colar os dados para uma planilha do Excel, por exemplo.

## Funcionalidades

- **Leitura de Arquivos CSV:**
  - Importa registros de transações a partir de um arquivo CSV.
  - Filtra e formata os dados, removendo caracteres indesejados.
  
- **Processamento de Transações:**
  - Extrai informações como data, método de pagamento, histórico e valores monetários.
  - Converte datas de string para objetos `Date` e calcula sua representação numérica.
  - Identifica transações relacionadas a Uber e comida.

- **Exportação de Dados:**
  - Gera um novo arquivo CSV formatado com as transações processadas.
  - Separa os valores positivos e negativos de acordo com o tipo da transação.

## Requisitos

- Java 8 ou superior.
- Biblioteca Swing (JFileChooser) para seleção de arquivos.

## Como Usar

1. Execute a classe `InvoiceRegister`.
2. Selecione um arquivo CSV para processar.
3. O programa exibira as transações processadas no console.
4. Insira o nome do arquivo CSV de saída.
5. O programa salvará os dados processados no mesmo diretório do arquivo de entrada.

## Exemplo de Entrada

Arquivo CSV de entrada:
```csv
 Extrato Conta Corrente 				
Conta 	XXXXXXX			
Perí­odo 	01/01/2025 a 31/01/2025			
Saldo 	333,79			
				
Data Lançamento	Histórico	Descrição	Valor	Saldo
28/01/2025	Pix recebido	Joao	30	33,79
28/01/2025	Compra no débito	Buritis Conveniencia   Belo Horizont Bra	-21,49	3,79
![image](https://github.com/user-attachments/assets/31a3ddb8-5ae4-4ca7-a5c2-6542da964da9)

```

## Exemplo de Saída

Arquivo CSV gerado:
```csv
45685		Joao						30	
45685		Buritis Conveniencia 							21,49
![image](https://github.com/user-attachments/assets/d4be2293-42e9-4d0c-bc62-1bdc26b1a620)

```

## Estrutura do Código

O projeto é composto pelas seguintes classes:

- **`Invoice`**: Representa uma transação, armazenando informações sobre data, histórico, valor, etc.
- **`InvoiceRegister`**: Contém os métodos para ler e processar o arquivo CSV e exportar os resultados.
- **`Util`**: Contém métodos auxiliares para manipulação de strings e datas.

## Melhorias Futuras

- Implementação de uma interface gráfica para facilitar a seleção e visualização dos dados.
- Melhor tratamento de erros e logs detalhados.
