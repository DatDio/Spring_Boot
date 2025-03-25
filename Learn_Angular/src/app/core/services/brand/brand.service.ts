import { Injectable } from '@angular/core';
import { ApiService } from 'src/app/core/services/api/api.service';
import { Brand } from '@models/brand.model';
import { Observable } from 'rxjs';
@Injectable({
    providedIn: 'root'
})
export class BrandService {
    private baseUrl = '/brands';

    constructor(private apiService: ApiService) { }
    
    // Lấy danh sách danh mục
    getAllBrands(): Observable<Brand[]> {
        return this.apiService.getTypeRequest<Brand[]>(this.baseUrl+'/getAll');
    }

    // Lấy danh mục theo ID
    getCategoryById(id: number): Observable<Brand> {
        return this.apiService.getTypeRequest<Brand>(`${this.baseUrl}/${id}`);
    }
}
