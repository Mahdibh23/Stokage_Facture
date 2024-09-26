import { Facture } from './facture';

export class Entreprise {
  id: number;
  name: string;
  description: string;
  adresse: string;
  code: string;
  factures: Facture[];
  totalAmount: number = 0;
  disabled: boolean = false;
  constructor(
    id: number,
    name: string,
    description: string,
    adresse: string,
    code: string,
    factures: Facture[],
    totalAmount: number,
    disabled: boolean
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.adresse = adresse;
    this.code = code;
    this.factures = factures;
    this.totalAmount = totalAmount;
    disabled = disabled;
  }
}
