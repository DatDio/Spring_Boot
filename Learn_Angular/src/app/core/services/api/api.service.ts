import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080';

  constructor(private _http: HttpClient) { }

  // ✅ GET request
  getTypeRequest<T>(url: string): Observable<T> {
    console.log('url', url);
    var u = `${this.baseUrl}${url}`;
    return this._http.get<{ success: boolean; message: string; data: T }>(`${this.baseUrl}${url}`)
    .pipe(
      map(response => this.handleResponse<T>(response)) // 👈 Ép kiểu rõ ràng
    );

  }
  // ✅ POST request
  postTypeRequest<T>(url: string, payload: any): Observable<T> {
    return this._http.post<any>(`${this.baseUrl}${url}`, payload).pipe(
      map(response => this.handleResponse<T>(response))
    );
  }

  // ✅ PUT request (Cập nhật toàn bộ)
  putTypeRequest<T>(url: string, payload: any): Observable<T> {
    return this._http.put<any>(`${this.baseUrl}${url}`, payload).pipe(
      map(response => this.handleResponse<T>(response))
    );
  }

  // ✅ PATCH request (Cập nhật một phần)
  patchTypeRequest<T>(url: string, payload: any): Observable<T> {
    return this._http.patch<any>(`${this.baseUrl}${url}`, payload).pipe(
      map(response => this.handleResponse<T>(response))
    );
  }

  // ✅ DELETE request
  deleteTypeRequest<T>(url: string): Observable<T> {
    return this._http.delete<any>(`${this.baseUrl}${url}`).pipe(
      map(response => this.handleResponse<T>(response))
    );
  }

  // ✅ Xử lý response: kiểm tra success và trả về data hoặc lỗi
  private handleResponse<T>(response: any): T {
    // Kiểm tra response có đúng format không (có đủ success, message, data)
    if (response && typeof response === 'object' && 'success' in response && 'message' in response && 'data' in response) {
      if (response.success) {
        return response.data;
      } else {
        throw new Error(response.message);
      }
    } else {
      throw new Error('Có lỗi xảy ra. Vui lòng thử lại sau');
    }
  }

}
