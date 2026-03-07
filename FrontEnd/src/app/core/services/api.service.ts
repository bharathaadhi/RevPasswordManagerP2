import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {

  BASE_URL = '';

  constructor(private http: HttpClient) { }

  private getLoggedUser(): string {
    return localStorage.getItem('username') || '';
  }

  // ================= LOGIN =================
  login(data: any) {
    return this.http.post<any>(
      `${this.BASE_URL}/api/auth/login`,
      data
    );
  }

  // ================= REGISTER =================
  register(data: any) {
    return this.http.post(`${this.BASE_URL}/api/auth/register`, data);
  }

  // ================= DASHBOARD =================
  dashboard() {
    const user = this.getLoggedUser();

    return this.http.get(
      `${this.BASE_URL}/api/dashboard?usernameOrEmail=${user}`
    );
  }

  // ================= VAULT =================
  getVault() {
    const user = this.getLoggedUser();

    return this.http.get<any[]>(
      `${this.BASE_URL}/api/vault?usernameOrEmail=${user}`
    );
  }

  addPassword(payload: any) {
    return this.http.post(
      `${this.BASE_URL}/api/vault`,
      payload,
      { responseType: 'text' }
    );
  }

  deletePassword(id: number) {
    return this.http.delete(
      `${this.BASE_URL}/api/vault/${id}`,
      { responseType: 'text' }
    );
  }

  favoritePassword(id: number, value: boolean) {
    return this.http.patch(
      `${this.BASE_URL}/api/vault/${id}/favorite?value=${value}`,
      {},
      { responseType: 'text' }
    );
  }

  searchVault(usernameOrEmail: string, keyword: string) {
    return this.http.get<any[]>(
      `${this.BASE_URL}/api/vault/search?usernameOrEmail=${usernameOrEmail}&keyword=${keyword}`
    );
  }

  filterVault(usernameOrEmail: string, category: string) {
    return this.http.get<any[]>(
      `${this.BASE_URL}/api/vault/filter/${category}?usernameOrEmail=${usernameOrEmail}`
    );
  }

  sortVault(usernameOrEmail: string, sortBy: string) {
    return this.http.get<any[]>(
      `${this.BASE_URL}/api/vault/sort?usernameOrEmail=${usernameOrEmail}&sortBy=${sortBy}`
    );
  }

  viewPassword(payload: any) {
    return this.http.post<any>(
      `${this.BASE_URL}/api/vault/view`,
      payload
    );
  }

  updatePassword(id: number, payload: any) {
    return this.http.put(
      `${this.BASE_URL}/api/vault/${id}`,
      payload,
      { responseType: 'text' }
    );
  }

  getFavorites() {
    const user = this.getLoggedUser();

    return this.http.get<any[]>(
      `${this.BASE_URL}/api/vault/favorites?usernameOrEmail=${user}`
    );
  }

  generateMultiplePasswords(payload: any) {
    return this.http.post<string[]>(
      `${this.BASE_URL}/api/security/generate-multiple`,
      payload
    );
  }

  getReusedPasswords(username: string) {
    return this.http.get<any[]>(
      `${this.BASE_URL}/api/audit/reused?usernameOrEmail=${username}`
    );
  }

  getWeakPasswords(username: string) {
    return this.http.get<any[]>(
      `${this.BASE_URL}/api/audit/weak?usernameOrEmail=${username}`
    );
  }

  getSecurityReport(username: string) {
    return this.http.get<any>(
      `${this.BASE_URL}/api/audit/report?usernameOrEmail=${username}`
    );
  }

  getOldPasswords(username: string) {
    return this.http.get<any[]>(
      `${this.BASE_URL}/api/vault/old?usernameOrEmail=${username}`
    );
  }

  getProfile(username: string) {
    return this.http.get(
      `${this.BASE_URL}/api/profile/me?usernameOrEmail=${username}`
    );
  }

  secureExportVault(payload: any) {
    return this.http.post(
      `${this.BASE_URL}/api/vault/export`,
      payload,
      { responseType: 'blob' }
    );
  }

  secureImportVault(payload: any) {
    return this.http.post(
      `${this.BASE_URL}/api/vault/import`,
      payload,
      { responseType: 'text' }
    );
  }

  generateVerificationCode(username: string) {
    return this.http.post<any>(
      `${this.BASE_URL}/api/security/generate-code?usernameOrEmail=${username}`,
      {}
    );
  }

  validateVerificationCode(username: string, code: string) {
    return this.http.post<boolean>(
      `${this.BASE_URL}/api/security/validate-code?usernameOrEmail=${username}&code=${code}`,
      {}
    );
  }

  getSecurityAudit(username: string) {
    return this.http.get(
      `${this.BASE_URL}/api/audit/report?usernameOrEmail=${username}`
    );
  }

  updateProfile(data: any) {

    return this.http.put(
      `${this.BASE_URL}/api/profile/update`,
      {
        userId: data.userId,
        name: data.name,
        email: data.email,
        phone: data.phone
      },
      { responseType: 'text' }
    );
  }

  getSecurityQuestions(username: string): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.BASE_URL}/api/auth/security-questions/${username}`
    );
  }

  getUserSecurityQuestions(username: string) {
    return this.http.get<string[]>(
      `${this.BASE_URL}/api/auth/security-questions/${username}`
    );
  }

  forgotPassword(data: any): Observable<any> {
    return this.http.post(
      `${this.BASE_URL}/api/auth/forgotPassword`,
      data
    );
  }

  secureDeletePassword(payload: any) {
    return this.http.post(
      `${this.BASE_URL}/api/vault/delete`,
      payload,
      { responseType: 'text' }
    );
  }

  updateSecurityAnswers(payload: any) {
    return this.http.post(
      `${this.BASE_URL}/api/security/update-answers`,
      payload,
      { responseType: 'text' }
    );
  }
  updateMasterPassword(data: any) {
    return this.http.post(
      `${this.BASE_URL}/api/auth/changePassword`,
      data,
      { responseType: 'text' }
    );
  }
  verify2FA(username: string, code: string) {

    return this.http.post<any>(
      `${this.BASE_URL}/api/auth/verify-2fa?usernameOrEmail=${username}&code=${code}`,
      {}
    );
  }
  toggle2FA(username: string, enabled: boolean) {

    return this.http.post(
      `${this.BASE_URL}/api/auth/toggle-2fa`,
      null,
      {
        params: {
          usernameOrEmail: username,
          enabled: enabled
        }
      }
    );
  }
  get2FAStatus(username: string) {
    return this.http.get(
      `${this.BASE_URL}/api/auth/2fa-status?usernameOrEmail=${username}`
    );
  }
}








