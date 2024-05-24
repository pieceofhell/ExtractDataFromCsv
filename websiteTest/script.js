const fs = require('fs');
const readline = require('readline');

class InvoiceRegister {
  constructor() {
    this.data = null;
    this.dataNum = 0;
    this.dataString = '';
    this.metodoPagamento = '';
    this.historico = '';
    this.valor = 0;
    this.isUber = false;
    this.isComida = false;
  }

  static isUber(historico) {
    const lowerCaseHistorico = historico.toLowerCase();
    return lowerCaseHistorico.includes('uber');
  }

  static isFood(historico) {
    const lowerCaseHistorico = historico.toLowerCase();
    return (
      lowerCaseHistorico.includes('pizzaria') ||
      lowerCaseHistorico.includes('trigopane') ||
      lowerCaseHistorico.includes('hamburguer') ||
      lowerCaseHistorico.includes('sorvete') ||
      lowerCaseHistorico.includes('acai') ||
      lowerCaseHistorico.includes('subway') ||
      lowerCaseHistorico.includes('batata') ||
      lowerCaseHistorico.includes('mcdonalds') ||
      lowerCaseHistorico.includes('burger') ||
      lowerCaseHistorico.includes('burguer') ||
      lowerCaseHistorico.includes('burgueria') ||
      lowerCaseHistorico.includes('attelier') ||
      lowerCaseHistorico.includes('sucos')
    );
  }

  static removeDoubleSpaces(str) {
    return str.replace(/\s+/g, ' ');
  }

  static removeGarbage(str) {
    str = str
      .replace(/\s+/g, ' ')
      .replace(/[éêè]/g, 'e')
      .replace(/[óôò]/g, 'o')
      .replace(/[áãâà]/g, 'a')
      .replace(/[í]/g, 'i')
      .replace(/[ú]/g, 'u')
      .replace(/[ç]/g, 'c')
      .replace(/[Ã]/g, '')
      .replace(/©/g, 'é')
      .replace(/¢/g, 'â');

    if (str.includes('*')) {
      str = str.substring(str.indexOf('*') + 1);
    }
    str = str.replace('Belo Horizont Bra', '');
    if (str.charAt(0).toLowerCase() !== str.charAt(0)) {
      str = str.charAt(0).toUpperCase() + str.slice(1);
    }
    return str;
  }

  static findCSVFiles(folder) {
    const csvFiles = [];
    const files = fs.readdirSync(folder);
    files.forEach(file => {
      if (file.toLowerCase().endsWith('.csv')) {
        csvFiles.push(folder + '/' + file);
      }
    });
    return csvFiles;
  }

  static removeDotsReplaceComma(valor) {
    return valor.replace('.', '').replace(',', '.');
  }

  static replaceDotToComma(value) {
    return value.toString().replace('.', ',');
  }

  static dateToLong(date) {
    const baseDate = new Date(1900, 0, -2); // 00/01/1900
    const diffInMillies = Math.abs(date.getTime() - baseDate.getTime());
    const diff = Math.floor(diffInMillies / (1000 * 60 * 60 * 24));
    return diff;
  }

  static removeNegative(num) {
    if (num < 0) {
      num = 0;
    }
    return num;
  }

  static removePositive(num) {
    if (num > 0) {
      num = 0;
    }
    return num;
  }

  static stringToDate(date) {
    const parts = date.split('/');
    return new Date(parts[2], parts[1] - 1, parts[0]);
  }

  static async read(nomeArquivo) {
    const fileStream = fs.createReadStream(nomeArquivo);
    const rl = readline.createInterface({
      input: fileStream,
      crlfDelay: Infinity
    });

    const allTransactions = [];
    let lineCount = 0;

    for await (const line of rl) {
      lineCount++;
      if (lineCount <= 5) {
        continue;
      }

      const fields = line.split(';');

      const transaction = new InvoiceRegister();
      transaction.dataString = fields[0] || 'null';
      transaction.metodoPagamento = InvoiceRegister.removeGarbage(fields[1]) || 'null';
      transaction.historico = InvoiceRegister.removeGarbage(fields[2]) || 'Poupanca';
      transaction.valor = fields[3] ? parseFloat(InvoiceRegister.removeDotsReplaceComma(fields[3])) : -1;

      transaction.data = InvoiceRegister.stringToDate(transaction.dataString);
      transaction.dataNum = InvoiceRegister.dateToLong(transaction.data);

      transaction.isUber = InvoiceRegister.isUber(transaction.historico);
      transaction.isComida = InvoiceRegister.isFood(transaction.historico);

      allTransactions.push(transaction);
    }

    return allTransactions;
  }

  static writeToCsv(filePath, transactions) {
    try {
      const file = fs.createWriteStream(filePath);
      transactions.reverse().forEach(transaction => {
        const negativeValue = InvoiceRegister.removeNegative(transaction.valor) === 0 ? '' : InvoiceRegister.replaceDotToComma(InvoiceRegister.removeNegative(transaction.valor));
        const positiveValue = InvoiceRegister.removePositive(transaction.valor) === 0 ? '' : InvoiceRegister.replaceDotToComma(InvoiceRegister.removePositive(transaction.valor) * -1);

        if (transaction.isUber) {
          file.write(`${transaction.dataNum};;${InvoiceRegister.removeGarbage(transaction.historico)};${positiveValue};;;;;\n`);
        } else if (transaction.isComida) {
          file.write(`${transaction.dataNum};;${InvoiceRegister.removeGarbage(transaction.historico)};;${positiveValue};;;;\n`);
        } else {
          file.write(`${transaction.dataNum};;${InvoiceRegister.removeGarbage(transaction.historico)};;;${negativeValue};;${positiveValue}\n`);
        }
      });

      file.end();
    } catch (e) {
      console.error('ERRO NA ESCRITA DO ARQUIVO!');
      console.error(e);
    }
  }

  print() {
    console.log(this.toString());
  }

  toString() {
    return `[${this.dataString} ## ${this.dataNum} ## ${this.metodoPagamento} ## ${this.historico} ## ${this.valor}]`;
  }
}

async function main() {
  let chosenFilePath = '';
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
  });

  rl.question('Por favor, insira o caminho do arquivo CSV: ', async (filePath) => {
    chosenFilePath = filePath.trim();
    try {
      const allTransactions = await InvoiceRegister.read(chosenFilePath);

      let countRegisters = 0;

      allTransactions.forEach(transaction => {
        transaction.print();
        countRegisters++;
      });

      console.log('\nTotal de registros: ' + countRegisters);

      const outputPath = chosenFilePath.substring(0, chosenFilePath.lastIndexOf('/') + 1);
      rl.question('Digite o nome do arquivo de saída (com ou sem extensão): ', (fileName) => {
        let csv = '.csv';
        let outputPathFile = outputPath + fileName.trim();

        if (!fileName.trim().toLowerCase().endsWith('.csv')) {
          outputPathFile += csv;
        }

        InvoiceRegister.writeToCsv(outputPathFile, allTransactions);
        console.log('Arquivo de saída criado com sucesso em: ' + outputPathFile);
        rl.close();
      });
    } catch (e) {
      console.error('ERRO NA LEITURA DO ARQUIVO!');
      console.error(e);
      rl.close();
    }
  });
}

main();
