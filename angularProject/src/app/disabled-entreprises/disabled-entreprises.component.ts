import { Component, OnInit } from '@angular/core';
import { EntrepriseService } from '../Services/entreprise.service';
import { Entreprise } from '../models/entreprise';
import { Router } from '@angular/router';

@Component({
  selector: 'app-disabled-entreprises',
  templateUrl: './disabled-entreprises.component.html',
  styleUrls: ['./disabled-entreprises.component.scss'],
})
export class DisabledEntreprisesComponent implements OnInit {
  entreprises: Entreprise[] = [];
  filteredEntreprises: Entreprise[] = [];
  currentUser: any;
  searchTerm: string = '';

  constructor(
    private entrepriseService: EntrepriseService,
    private router: Router
  ) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
  }

  ngOnInit(): void {
    if (Object.keys(this.currentUser).length === 0) {
      this.router.navigate(['/login']);
    } else {
      this.loadEntreprises();
    }
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

  loadEntreprises() {
    this.entrepriseService.getAllEntreprises().subscribe(
      (data) => {
        this.entreprises = data.filter(
          (entreprise: Entreprise) => entreprise.disabled === true
        );
        this.filteredEntreprises = this.entreprises;
        console.log(this.filteredEntreprises);
      },
      (error) => {
        console.error(error);
      }
    );
  }

  filterEntreprises() {
    if (this.searchTerm) {
      this.filteredEntreprises = this.entreprises.filter(
        (entreprise) =>
          entreprise.name
            .toLowerCase()
            .includes(this.searchTerm.toLowerCase()) ||
          entreprise.description
            .toLowerCase()
            .includes(this.searchTerm.toLowerCase()) ||
          entreprise.adresse
            .toLowerCase()
            .includes(this.searchTerm.toLowerCase()) ||
          entreprise.code.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    } else {
      this.filteredEntreprises = this.entreprises;
    }
  }

  openAddCompanyModal() {
    const modal = document.getElementById('addCompanyModal');
    if (modal) {
      modal.style.display = 'block';
    }
  }

  onCompanyAdded(company: Entreprise) {
    if (company.disabled) {
      this.entreprises.push(company);
      this.filterEntreprises();
    }
  }

  onSearchChange() {
    this.filterEntreprises();
  }
}
