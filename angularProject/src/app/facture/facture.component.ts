import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { EntrepriseService } from '../Services/entreprise.service';
import { FactureService } from '../Services/factures.services';
import { Entreprise } from '../models/entreprise';
import { Facture } from '../models/facture';
import { ModalFactureComponent } from '../modal-facture/modal-facture.component';
import { Location } from '@angular/common';
import { recu } from '../models/recu';
import { RecuService } from '../Services/RecuService';
import { ModalRecuComponent } from '../modal-recu/modal-recu.component';
import { SafePipe } from '../safe.pipe';

@Component({
  selector: 'app-facture',
  templateUrl: './facture.component.html',
  styleUrls: ['./facture.component.scss'],
})
export class FactureComponent implements OnInit {
  entreprise: Entreprise | undefined;
  factures: Facture[] = [];
  selectedImage: File | undefined;
  uploadMessage: string | undefined;
  serverData: Facture | undefined;
  entreprises: Entreprise[] = [];
  filteredEntreprises: Entreprise[] = [];
  currentUser: any;
  searchTerm: string = '';
  totalAmount: number = 0;
  isDisabledEntreprisesPage: boolean = false;
  formData = new FormData();
  recus: recu[] = [];
  selectedReceiptImage: File | undefined;
  uploadReceiptMessage: string | undefined;
  RecuData: any;
  safe: SafePipe | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private entrepriseService: EntrepriseService,
    private factureService: FactureService,
    private http: HttpClient,
    private location: Location,
    private dialog: MatDialog,
    private recuService: RecuService
  ) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      const entrepriseId = +params['id'];
      this.getEntreprise(entrepriseId);
      this.getFactures(entrepriseId);
      this.getRecu(entrepriseId);
    });

    if (!this.currentUser || Object.keys(this.currentUser).length === 0) {
      this.router.navigate(['/login']);
    } else {
      this.loadEntreprises();
    }
  }

  getEntreprise(id: number) {
    this.entrepriseService.getEntrepriseById(id).subscribe(
      (data) => {
        this.entreprise = data;
        this.isDisabledEntreprisesPage = data.disabled;
      },
      (error) => {
        console.error('Error fetching entreprise details', error);
      }
    );
  }

  getFactures(entrepriseId: number) {
    this.factureService.getFacturesByEntrepriseId(entrepriseId).subscribe(
      (data: Facture[]) => {
        this.factures = data;
      },
      (error: any) => {
        console.error('Error fetching factures', error);
      }
    );
  }

  onFileSelected(event: any) {
    this.selectedImage = event.target.files[0];
  }

  async onSubmitFacture(event: Event) {
    event.preventDefault();

    if (!this.selectedImage) {
      console.error('Aucune image sélectionnée.');
      return;
    }
    this.formData.append('facture', this.selectedImage);
    console.log(this.formData);

    fetch('http://127.0.0.1:5000/process_invoice', {
      method: 'POST',
      body: this.formData,
    })
      .then((response) => response.json())
      .then((data) => {
        this.uploadMessage = `Réponse du serveur: ${JSON.stringify(data)}`;
        this.serverData = data;
        console.log('Réponse du serveur:', data);
        this.openAddCompanyModal();
      })
      .catch((error) => {
        this.uploadMessage = `Erreur lors de l'envoi de l'image: ${error}`;
        console.error("Erreur lors de l'envoi de l'image:", error);
      });
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

  loadEntreprises() {
    this.entrepriseService.getAllEntreprises().subscribe(
      (data) => {
        this.entreprises = data;
        this.filteredEntreprises = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  async openAddCompanyModal(): Promise<void> {
    if (!this.formData.has('facture')) {
      console.error('Aucun fichier trouvé dans FormData.');
      return;
    }

    const file = this.formData.get('facture') as File;

    try {
      const blob = await this.convertFileToBlob(file);

      this.dialog.open(ModalFactureComponent, {
        width: '250px',
        data: {
          serverData: this.serverData,
          entreprise: this.entreprise,
          file: blob,
        },
      });
    } catch (error) {
      console.error('Erreur lors de la conversion du fichier en Blob:', error);
    }
  }

  convertFileToBlob(file: File): Promise<Blob> {
    console.log(file);
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const arrayBuffer = reader.result as ArrayBuffer;
        const blob = new Blob([arrayBuffer], { type: file.type });
        resolve(blob);
      };
      reader.onerror = (error) => reject(error);
      reader.readAsArrayBuffer(file);
    });
  }

  goBack() {
    this.location.back();
  }

  disableFunction() {
    if (confirm('Êtes-vous sûr de vouloir désactiver cette entreprise ?')) {
      this.entrepriseService
        .desactiverEntreprise(this.entreprise?.id!)
        .subscribe(
          () => {
            console.log('Entreprise désactivée:', this.entreprise);
            this.router.navigate(['/home']);
          },
          (error) => {
            console.error('Error disabling entreprise', error);
          }
        );
    }
  }
  scrollUp() {
    const tableWrapper = document.querySelector('.table-wrapper');
    if (tableWrapper) {
      tableWrapper.scrollBy({ top: -100, behavior: 'smooth' });
    }
  }

  scrollDown() {
    const tableWrapper = document.querySelector('.table-wrapper');
    if (tableWrapper) {
      tableWrapper.scrollBy({ top: 100, behavior: 'smooth' });
    }
  }
  getRecu(entrepriseId: number) {
    this.recuService.getRecuByEntrepriseId(entrepriseId).subscribe(
      (data: recu[]) => {
        this.recus = data;
      },
      (error: any) => {
        console.error('Error fetching factures', error);
      }
    );
  }
  async onSubmitRecu(event: Event) {
    event.preventDefault();

    if (!this.selectedImage) {
      console.error('Aucune image sélectionnée.');
      return;
    }
    this.formData.append('file', this.selectedImage);
    console.log(this.formData);
    fetch('http://127.0.0.1:5000/process_recu', {
      method: 'POST',
      body: this.formData,
    })
      .then((response) => response.json())
      .then((data) => {
        this.uploadMessage = `Réponse du serveur: ${JSON.stringify(data)}`;
        this.RecuData = data;

        this.openAddRecuModal();
      })
      .catch((error) => {
        this.uploadMessage = `Erreur lors de l'envoi de l'image: ${error}`;
        console.error("Erreur lors de l'envoi de l'image:", error);
      });
  }
  async openAddRecuModal(): Promise<void> {
    if (!this.formData.has('file')) {
      console.error('Aucun fichier trouvé dans FormData.');
      return;
    }

    const file = this.formData.get('file') as File;

    try {
      const blob = await this.convertFileToBlob(file);

      this.dialog.open(ModalRecuComponent, {
        width: '250px',
        data: {
          serverData: this.RecuData,
          entreprise: this.entreprise,
          file: blob,
        },
      });
    } catch (error) {
      console.error('Erreur lors de la conversion du fichier en Blob:', error);
    }
  }
  openFile(filePath: string): void {
    window.open(filePath, '_blank');
  }
}
