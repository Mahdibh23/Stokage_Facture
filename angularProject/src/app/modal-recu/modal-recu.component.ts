import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import imageCompression from 'browser-image-compression';

@Component({
  selector: 'app-modal-recu',
  templateUrl: './modal-recu.component.html',
  styleUrls: ['./modal-recu.component.scss'],
})
export class ModalRecuComponent implements OnInit {
  recuForm: FormGroup;
  @Output() companyAdded = new EventEmitter<any>();

  constructor(
    private http: HttpClient,
    public dialogRef: MatDialogRef<ModalRecuComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder
  ) {
    this.recuForm = this.fb.group({
      date: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit(): void {}

  async reloadPage() {
    window.location.reload();
  }

  onSubmit(): void {
    if (this.recuForm.valid) {
      this.onSubmitRecu(this.recuForm.value);
    }
  }

  async onSubmitRecu(form: any): Promise<void> {
    if (!form.amount || !form.date) {
      console.error('Invoice number, amount, and date are required.');
      return;
    }
    const amount = parseFloat(form.amount);
    const date = this.parseDate(form.date);

    const formData = new FormData();
    formData.append('date', date.toISOString().split('T')[0]);
    formData.append('amount', amount.toString());
    formData.append('entreprise_id', this.data.entreprise.id.toString());

    if (this.data.file) {
      const compressedImage = await this.compressImage(this.data.file);
      formData.append('file', compressedImage);
      console.log('file comprise :', formData);
    }

    this.sendFormData(formData);
  }

  sendFormData(formData: FormData): void {
    console.log(formData);
    this.http.post('http://localhost:8080/api/recus/', formData).subscribe(
      (response: any) => {
        console.log('Facture créée:', response);
        this.companyAdded.emit(response);
        this.close();
        this.reloadPage();
      },
      (error: any) => {
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
}
