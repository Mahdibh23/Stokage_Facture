import { Entreprise } from './entreprise';

export class Facture {
  id: number;
  invoice_number: string;
  date: Date;
  amount: number;
  objet: string;
  entreprise: Entreprise;
  filePath: string; // Name of the file

  constructor(
    id: number,
    invoiceNumber: string,
    date: Date,
    amount: number,
    objet: string,
    entreprise: Entreprise,
    filePath: string
  ) {
    this.id = id;
    this.invoice_number = invoiceNumber;
    this.date = date;
    this.amount = amount;
    this.objet = objet;
    this.entreprise = entreprise;
    this.filePath = filePath;
  }
}
