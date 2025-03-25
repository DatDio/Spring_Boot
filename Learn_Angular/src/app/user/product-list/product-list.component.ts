import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { Product } from '@models/product.model';
import { ProductService } from 'src/app/core/services/product/product.service';
import { ActivatedRoute , RouterModule} from '@angular/router';
import { CategoryService } from '@services/category/category.service';
import { BrandService } from '@services/brand/brand.service';
import { Category } from '@models/category.model';
import { Brand } from '@models/brand.model';
import { FormsModule } from '@angular/forms';
import { environment } from '@environments/environment';
@Component({
  selector: 'app-product-list',
  imports: [CommonModule, NgxPaginationModule, FormsModule,RouterModule],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  imageBaseUrl = environment.imageBaseUrl;
  selectedCategoryId: number | null = null;
  searchQuery: string = '';
  categoryList: Category[] = [];
  brandList: Brand[] = [];
  products: Product[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';
  totalItems: number = 0;
  currentPage: number = 1;
  pageSize: number = 6;
  totalPages: number = 1;
  pagesToShow: number[] = [];

  searchParams = {
    name: '',
    brandId: null as number | null,
    categoryId: null as number | null,
    priceFrom: null as number | null,
    priceTo: null as number | null,
    createdFrom: null as string | null,
    createdTo: null as string | null,
    sortBy: 'createAt',
    direction: 'desc'
  };

  constructor(private productService: ProductService,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    // Lắng nghe thay đổi trên queryParams (nếu người dùng search)
    this.route.queryParams.subscribe(params => {
      this.searchQuery = params['search'] || '';

      this.searchParams.name = this.searchQuery;
      this.loadProducts();
      this.loadBrand();
      this.loadCategory();
    });
  }

  /** Gọi API lấy danh sách sản phẩm */
  loadProducts(): void {
    this.isLoading = true;

    const params = {
      ...this.searchParams,
      page: this.currentPage,
      size: this.pageSize
    };

    this.productService.searchProducts(params).subscribe({
      next: (data) => {
        this.products = data.data;
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        this.isLoading = false;
        this.updatePagination();
      },
      error: () => {
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

  /** Thay đổi trang */
  onPageChange(page: number): void {
    if (page !== this.currentPage && page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadProducts();
    }
  }

  /** Thay đổi sắp xếp */
  onSortChange(event: any): void {
    const sortOptions: Record<string, { sortBy: string; direction: string }> = {
      'price_desc': { sortBy: 'price', direction: 'desc' },
      'price_asc': { sortBy: 'price', direction: 'asc' },
      'newest': { sortBy: 'createAt', direction: 'desc' }
    };

    const selectedValue = event.target.value;
    if (sortOptions[selectedValue]) {
      this.searchParams.sortBy = sortOptions[selectedValue].sortBy;
      this.searchParams.direction = sortOptions[selectedValue].direction;
      this.onFilterChange();
    }
  }
  onPageSizeChange(event: any): void {
    const pageSizeOptions: Record<string, number> = {
      '6_per_page': 6,
      '12_per_page': 12,
      '24_per_page': 24
    };

    const selectedValue = event.target.value;
    if (pageSizeOptions[selectedValue]) {
      this.pageSize = pageSizeOptions[selectedValue]; // Cập nhật pageSize
      this.currentPage = 1; // Reset về trang đầu tiên
      this.loadProducts(); // Gọi API để tải lại dữ liệu
    }
  }

  /** Áp dụng bộ lọc */
  onFilterChange(): void {
    this.currentPage = 1;
    this.loadProducts();
  }
  /** Lọc sản phẩm theo Danh Mục */

  onCategoryChange(categoryId: any): void {
    const id = Number(categoryId); // Ép kiểu ở đây
    this.selectedCategoryId = categoryId; // Lưu danh mục được chọn
    this.searchParams.categoryId = id;
    this.onFilterChange();
  }


  /** Lọc sản phẩm theo Thương Hiệu */
  onBrandChange(brandId: number): void {
    this.searchParams.brandId = brandId;
    this.onFilterChange(); // Gọi API lọc sản phẩm theo brand mới
  }


  /** Áp dụng tìm kiếm theo khoảng giá */
  applyPriceFilter(priceFrom: string, priceTo: string): void {
    const fromValue = parseFloat(priceFrom);
    const toValue = parseFloat(priceTo);

    if (!isNaN(fromValue)) this.searchParams.priceFrom = fromValue;
    if (!isNaN(toValue)) this.searchParams.priceTo = toValue;

    this.onFilterChange();
  }

  // Load danh mục
  loadCategory(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categoryList = data;
      },
      error: (err) => {
        console.error('Lỗi khi tải danh mục:', err);
      }
    });
  }
  loadBrand(): void {
    this.brandService.getAllBrands().subscribe({
      next: (data) => {
        this.brandList = data;
      },
      error: (err) => {
        console.error('Lỗi khi tải danh mục:', err);
      }
    });
  }
  getPriceRange(product: Product): string {
    const { minPrice, maxPrice } = this.productService.getMinMaxPrice(product);
    return minPrice === maxPrice ? `${minPrice} VND` : `${minPrice} - ${maxPrice} VND`;
  }

}
