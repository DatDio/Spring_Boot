import { Injectable } from '@angular/core';
import { ApiService } from 'src/app/core/services/api/api.service';
import { Category } from '@models/category.model';
import { Observable } from 'rxjs';
@Injectable({
    providedIn: 'root'
})
export class CategoryService {
    private baseUrl = '/Category';

    constructor(private apiService: ApiService) { }
    // Lấy danh sách danh mục
    getAllCategories(): Observable<Category[]> {
        return this.apiService.getTypeRequest<Category[]>(this.baseUrl);
    }

    // Lấy danh mục theo ID
    getCategoryById(id: number): Observable<Category> {
        return this.apiService.getTypeRequest<Category>(`${this.baseUrl}/${id}`);
    }
}
