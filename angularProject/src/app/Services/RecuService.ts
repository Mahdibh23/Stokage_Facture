import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { recu } from '../models/recu';

@Injectable({
  providedIn: 'root',
})
export class RecuService {
  private baseUrl = 'http://localhost:8080/api/recus';

  constructor(private http: HttpClient) {}

  getAllRecus(): Observable<recu[]> {
    return this.http.get<recu[]>(`${this.baseUrl}`);
  }

  getRecuById(id: number): Observable<recu> {
    return this.http.get<recu>(`${this.baseUrl}/${id}`);
  }

  createRecu(recu: recu): Observable<recu> {
    return this.http.post<recu>(`${this.baseUrl}`, recu);
  }

  updateRecu(id: number, recu: recu): Observable<recu> {
    return this.http.put<recu>(`${this.baseUrl}/${id}`, recu);
  }

  deleteRecu(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  uploadRecu(file: FormData): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/upload`, file);
  }
  getRecuByEntrepriseId(entrepriseId: number): Observable<recu[]> {
    return this.http.get<recu[]>(`${this.baseUrl}/entreprises/${entrepriseId}`);
  }
}
