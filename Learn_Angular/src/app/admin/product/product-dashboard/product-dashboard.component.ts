import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination'; // Import module
import { Product } from '@models/product.model';
import { ProductService } from 'src/app/core/services/product/product.service';
import { environment } from 'src/environments/environment'
import { error } from 'node:console';
import { NotificationToastService } from 'src/app/core/services/notificationToast/notificationtoast.service';

declare var bootstrap: any;
@Component({
  selector: 'app-product-dashboard',
  standalone: true,
  imports: [CommonModule, NgxPaginationModule, RouterModule], // Thêm module vào đây
  templateUrl: './product-dashboard.component.html',
  styleUrl: './product-dashboard.component.css'
})
export class ProductDashboardComponent implements OnInit {
  imageBaseUrl = environment.imageBaseUrl;
  products: Product[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';
  totalItems: number = 0;
  currentPage: number = 1;
  pageSize: number = 5;
  totalPages: number = 1;
  hasNextPage: boolean = true;
  pagesToShow: number[] = [];

  productIdToDelete: number | null = null;
  constructor(private productService: ProductService,
    private notificationToastService: NotificationToastService
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  //this.notificationToastService.showSuccess('Load thành công!');
  }

  loadProducts(): void {
    const searchParams = {
      name: '',
      price: 0,
      createdFrom: null,
      createdTo: null,
      page: this.currentPage,
      size: this.pageSize,
      sortBy: 'createAt',
      direction: 'desc'
    };

    this.productService.searchProducts(searchParams).subscribe({
      next: (data) => {
        this.products = data.data;
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.hasNextPage = data.hasNextPage;
        this.isLoading = false;
        this.updatePagination();
      },
      error: (err) => {
        this.errorMessage = 'Lỗi khi tải danh sách sản phẩm!';
        this.isLoading = false;
      }
    });
  }
  /** Cập nhật danh sách số trang hiển thị (tối đa 5 trang) */
  updatePagination(): void {
    const maxPagesToShow = 5;
    let startPage = Math.max(1, this.currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(this.totalPages, startPage + maxPagesToShow - 1);

    if (endPage - startPage < maxPagesToShow - 1) {
      startPage = Math.max(1, endPage - maxPagesToShow + 1);
    }

    this.pagesToShow = Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  }
  onPageChange(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadProducts();
    }
  }

  onSearch(event: any): void {
    this.currentPage = 1;
    const keyword = event.target.value.trim();
    // Gọi API tìm kiếm hoặc lọc danh sách
    this.loadProducts();
  }

  // Hiển thị modal xác nhận xóa
  openDeleteModal(productId: number | undefined) {
    if (productId === undefined) {
      console.error("Lỗi: productId bị undefined!");
      return;
    }
    
    this.productIdToDelete = productId;
    let modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();
  }
  
  // Thực hiện xóa khi người dùng xác nhận
  confirmDelete() {
    if (this.productIdToDelete !== null) {
      this.productService.deleteProduct(this.productIdToDelete).subscribe({
        next: (data) => {
          //alert('Xóa thành công!');
          this.notificationToastService.showSuccess('Xóa thành công!');
          this.loadProducts(); 
         // Đóng modal sau khi xóa thành công
        let modalElement = document.getElementById('deleteModal');
        if (modalElement) {
          let modalInstance = bootstrap.Modal.getInstance(modalElement);
          if (modalInstance) {
            modalInstance.hide();
          }
        }
        },
        error: (error) => {
          console.error('Lỗi khi xóa sản phẩm:', error);
          //alert('Xóa thất bại! Vui lòng thử lại.');
         this.notificationToastService.showError('Có lỗi xảy ra! Vui lòng thử lại.');
        }
      });
    }
  }

  
}
