import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../api/api.service';
import { Product } from '@models/product.model';
import { PaginationResponse } from '@models/pagination.model';
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl = '/products'; // Đường dẫn API của Product

  constructor(private apiService: ApiService) { }

  // ✅ Lấy danh sách sản phẩm
  getAllProducts(): Observable<Product[]> {
    return this.apiService.getTypeRequest<Product[]>(this.baseUrl);
  }

  // ✅ Lấy sản phẩm theo ID
  getProductById(id: number): Observable<Product> {
    return this.apiService.getTypeRequest<Product>(`${this.baseUrl}/${id}`);
  }

  // ✅ Sửa createProduct để gửi FormData
  createProduct(product: Product): Observable<Product> {
    const formData = new FormData();
    formData.append('name', product.name);
    formData.append('description', product.description);
    formData.append('price', product.price);
    if (product.imageFile) {
      formData.append('image', product.imageFile);
    }

    return this.apiService.postTypeRequest<Product>(this.baseUrl + '/create', formData);
  }

  // ✅ Cập nhật toàn bộ sản phẩm (PUT)
  updateProduct(id: number, product: Product): Observable<Product> {
    const formData = new FormData();
    if (id !== undefined && id !== null) {
      formData.append('id', id.toString());  
    }
    formData.append('name', product.name);
    formData.append('description', product.description);
    formData.append('price', product.price);
    if (product.imageFile) {
      formData.append('image', product.imageFile);
    }
    
    return this.apiService.postTypeRequest<Product>(`${this.baseUrl}/update`, formData);
  }

  // ✅ Cập nhật một phần sản phẩm (PATCH)
  patchProduct(id: number, product: Partial<Product>): Observable<Product> {
    return this.apiService.patchTypeRequest<Product>(`${this.baseUrl}/${id}`, product);
  }

  // ✅ Xóa sản phẩm
  deleteProduct(id: number): Observable<void> {
    return this.apiService.postTypeRequest<void>(`${this.baseUrl}/delete?id=${id}`,null);
  }

  // ✅ Tìm kiếm sản phẩm (POST)
  searchProducts(searchRequest: any): Observable<PaginationResponse<Product>> {
    return this.apiService.postTypeRequest<PaginationResponse<Product>>(`${this.baseUrl}/search`, searchRequest);
  }

}