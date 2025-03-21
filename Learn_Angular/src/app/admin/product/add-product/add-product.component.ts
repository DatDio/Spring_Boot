import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from 'src/app/core/services/product/product.service';
import { Product } from 'src/app/share/models/product.model';
import { finalize } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '@environments/environment';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  imports: [CommonModule, ReactiveFormsModule],
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  productForm!: FormGroup;
  selectedFile: File | null = null;
  imagePreview: string | null = null;
  isLoading = false;
  fileError: string | null = null;
  productId: string | null = null;
  product: Product | null = null;
  isEditing: boolean = false;
  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    
    this.route.paramMap.subscribe(params => {
      this.productId = params.get('id');
      if (this.productId) {
        this.isEditing = true; 
        this.loadProduct(this.productId);
      } else {
        this.isEditing = false; 
      }
      
      this.initializeForm();
    });
  }

  /**
   * Khởi tạo form
   */
  private initializeForm(): void {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [null, [Validators.required, Validators.min(1)]],
      image: [null,this.isEditing?[]: Validators.required]
    });
  }

  /**
   * Getter để truy cập form controls dễ dàng hơn
   */
  get f() {
    return this.productForm.controls;
  }

  /**
   * Tải dữ liệu sản phẩm từ ID
   */
  private loadProduct(id: string): void {
    this.productService.searchProducts({ id }).subscribe({
      next: (data) => {
        if (data.data.length > 0) {
          this.product = data.data[0];
          this.productForm.patchValue({
            name: this.product.name,
            price: this.product.price,
            description: this.product.description
          });
          this.imagePreview = environment.imageBaseUrl + this.product.imageUrl;
        }
      },
      error: (err) => {
        console.error('Lỗi khi tải sản phẩm:', err.message);
        alert('Không thể tải thông tin sản phẩm.');
      }
    });
  }

  /**
   * Xử lý khi chọn file ảnh
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.validateFile(file);
    }
  }

  /**
   * Kiểm tra file ảnh có hợp lệ không
   */
  private validateFile(file: File): void {
    const maxSize = 20 * 1024 * 1024; // 20MB

    if (file.size > maxSize) {
      this.fileError = 'File quá lớn! Vui lòng chọn file dưới 20MB.';
      return;
    }

    this.fileError = null;
    this.selectedFile = file;
    this.previewImage(file);
    this.productForm.patchValue({ image: file.name });
    this.productForm.get('image')?.updateValueAndValidity();
  }

  /**
   * Hiển thị ảnh xem trước
   */
  private previewImage(file: File): void {
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  /**
   * Xử lý khi submit form
   */
  onSubmit(): void {
    if (this.productForm.invalid) return;
    this.isLoading = true;

    const formValues = this.productForm.getRawValue();
    const productData: Product = {
      name: formValues.name,
      price: formValues.price,
      description: formValues.description,
      imageFile: this.selectedFile || undefined
    };
    
    if (this.isEditing) {
      this.updateProduct(Number(this.productId), productData);
    } else {
      this.createProduct(productData);
    }
  }

  /**
   * Tạo mới sản phẩm
   */
  private createProduct(productData: Product): void {
    this.productService.createProduct(productData)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          alert('Thêm sản phẩm thành công!');
          this.resetForm();
        },
        error: (err) => {
          console.error('Lỗi khi thêm sản phẩm:', err.message);
          alert('Có lỗi xảy ra, vui lòng thử lại.');
        }
      });
  }

  /**
   * Cập nhật sản phẩm
   */
  private updateProduct(productId: number, productData: Product): void {
    this.productService.updateProduct(productId, productData)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          alert('Cập nhật sản phẩm thành công!');
          this.router.navigate(['/admin','products']);
        },
        error: (err) => {
          console.error('Lỗi khi cập nhật sản phẩm:', err.message);
          alert('Có lỗi xảy ra, vui lòng thử lại.');
        }
      });
  }

  /**
   * Reset form và input file
   */
  resetForm(): void {
    this.productForm.reset();
    this.selectedFile = null;
    this.imagePreview = null;
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
    this.productForm.markAsPristine();
    this.productForm.markAsUntouched();
  }
}
