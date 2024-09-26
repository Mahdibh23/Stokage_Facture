import { Component, OnInit, Inject, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import imageCompression from 'browser-image-compression';

@Component({
  selector: 'app-modal-facture',
  templateUrl: './modal-facture.component.html',
  styleUrls: ['./modal-facture.component.scss'],
})
export class ModalFactureComponent implements OnInit {
  factureForm: FormGroup;

  @Output() companyAdded = new EventEmitter<any>();

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    public dialogRef: MatDialogRef<ModalFactureComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.factureForm = this.fb.group({
      invoiceNumber: [
        data.serverData?.invoice_number || '',
        Validators.required,
      ],
      date: [data.serverData?.date || '', Validators.required],
      amount: [data.serverData?.amount_number || '', Validators.required],
      objet: [data.serverData?.objet || '', Validators.required],
    });
  }

  ngOnInit(): void {}
  async reloadPage() {
    window.location.reload();
  }

  onSubmit(): void {
    if (this.factureForm.valid) {
      this.onSubmitFacture(this.factureForm.value);
    }
  }
  async compressImage(file: File): Promise<File> {
    const options = {
      maxSizeMB: 1,
      maxWidthOrHeight: 1024,
      useWebWorker: true,
    };
    try {
      const compressedFile = await imageCompression(file, options);
      return compressedFile;
    } catch (error) {
      console.error('Error during image compression:', error);
      throw error;
    }
  }

  async onSubmitFacture(form: any): Promise<void> {
    if (!form.invoiceNumber || !form.amount || !form.date) {
      console.error('Invoice number, amount, and date are required.');
      return;
    }
    const amount = parseFloat(form.amount);
    const date = this.parseDate(form.date);

    const formData = new FormData();
    formData.append('invoice_number', form.invoiceNumber);
    formData.append('date', date.toISOString().split('T')[0]);
    formData.append('amount', amount.toString());
    formData.append('objet', form.objet);
    formData.append('entreprise_id', this.data.entreprise.id.toString());

    if (this.data.file) {
      const compressedImage = await this.compressImage(this.data.file);
      formData.append('file', compressedImage);
      console.log('file comprise :', formData);
    }

    this.sendFormData(formData);
  }

  sendFormData(formData: FormData): void {
    this.http.post('http://localhost:8080/api/factures/', formData).subscribe(
      (response) => {
        console.log('Facture créée:', response);
        this.companyAdded.emit(response);
        this.close();
        this.reloadPage();
      },
      (error) => {
        console.error("Erreur lors de l'envoi de la facture:", error);
      }
    );
  }

  private parseDate(dateString: string): Date {
    const dateParts = dateString.split('/');
    if (dateParts.length !== 3) {
      console.error('Invalid date format.');
      return new Date();
    }
    const [day, month, year] = dateParts;
    return new Date(`${year}-${month}-${day}`);
  }

  close(): void {
    this.dialogRef.close();
  }
}
