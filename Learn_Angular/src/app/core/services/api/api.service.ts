import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080'; 

  constructor(private _http: HttpClient) {}

  // ✅ GET request
  getTypeRequest<T>(url: string): Observable<T> {
    console.log('url', url);
    return this._http.get<{ success: boolean; message: string; data: T }>(`${this.baseUrl}${url}`).pipe(
      map(this.handleResponse)
    );
  }

  // ✅ POST request
  postTypeRequest<T>(url: string, payload: any): Observable<T> {
    var repo = this._http.post<{ success: boolean; message: string; data: T }>(`${this.baseUrl}${url}`, payload);
    return repo.pipe(
      map(this.handleResponse)
    );
  }

  // ✅ PUT request (Cập nhật toàn bộ)
  putTypeRequest<T>(url: string, payload: any): Observable<T> {
    return this._http.put<{ success: boolean; message: string; data: T }>(`${this.baseUrl}${url}`, payload).pipe(
      map(this.handleResponse)
    );
  }

  // ✅ PATCH request (Cập nhật một phần)
  patchTypeRequest<T>(url: string, payload: any): Observable<T> {
    return this._http.patch<{ success: boolean; message: string; data: T }>(`${this.baseUrl}${url}`, payload).pipe(
      map(this.handleResponse)
    );
  }

  // ✅ DELETE request
  deleteTypeRequest<T>(url: string): Observable<T> {
    return this._http.delete<{ success: boolean; message: string; data: T }>(`${this.baseUrl}${url}`).pipe(
      map(this.handleResponse)
    );
  }

  // ✅ Xử lý response: kiểm tra success và trả về data hoặc lỗi
  private handleResponse<T>(response: { success: boolean; message: string; data: T }): T {
    if (response.success) {
      return response.data; // ✅ Nếu thành công, trả về data
    } else {
      throw new Error(response.message); // ❌ Nếu thất bại, ném lỗi
    }
  }
}
