import { Entreprise } from './entreprise';

export class recu {
  id: number;
  date: Date;
  amount: number;
  entreprise: Entreprise;
  filePath: string;

  constructor(
    id: number,
    date: Date,
    amount: number,
    entreprise: Entreprise,
    filePath: string
  ) {
    this.id = id;
    this.date = date;
    this.amount = amount;
    this.entreprise = entreprise;
    this.filePath = filePath;
  }
}
