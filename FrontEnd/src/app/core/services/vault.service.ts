import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VaultService {

  private baseUrl = '/api/vault';

  constructor(private http: HttpClient){}

  // GET ALL PASSWORDS
  getAllPasswords(): Observable<any[]>{
    return this.http.get<any[]>(this.baseUrl);
  }

  // ADD PASSWORD 
  addPassword(payload:any): Observable<any>{
    return this.http.post(this.baseUrl, payload);
  }
}