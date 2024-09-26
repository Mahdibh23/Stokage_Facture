import { Component, EventEmitter, Output } from '@angular/core';
import { EntrepriseService } from '../Services/entreprise.service';

@Component({
  selector: 'app-formulaire',
  templateUrl: './formulaire.component.html',
  styleUrls: ['./formulaire.component.scss'],
})
export class FormulaireComponent {
  @Output() companyAdded = new EventEmitter<any>();

  company = {
    name: '',
    description: '',
    adresse: '',
    code: '',
    email: '',
  };

  constructor(private entrepriseService: EntrepriseService) {}

  onSubmit() {
    this.entrepriseService.createEntreprise(this.company).subscribe(
      (response) => {
        this.companyAdded.emit(response);
        this.closeModal();
      },
      (error) => {
        console.error('Error creating entreprise:', error);
      }
    );
  }

  closeModal() {
    (document.getElementById('addCompanyModal') as HTMLElement).style.display =
      'none';
  }
}
