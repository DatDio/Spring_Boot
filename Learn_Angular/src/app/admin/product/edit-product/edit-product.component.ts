import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators,FormsModule } from '@angular/forms';
import { ProductService } from 'src/app/core/services/product/product.service';
import { Product, ProductColor } from 'src/app/share/models/product.model';
import { finalize } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '@environments/environment';
import { CategoryService } from '@services/category/category.service';
import { FileUpLoadService } from '@services/fileUpload/fileUpload.service';
import { ChangeDetectorRef } from '@angular/core';
import { Category } from '@models/category.model';
import { Brand } from '@models/brand.model';
import { BrandService } from '@services/brand/brand.service';
import { NotificationToastService } from '@services/notificationToast/notificationtoast.service';
import { uniqueColorValidator } from 'src/app/share/validators/unique-color.validator';
import {uniqueSizeValidator} from 'src/app/share/validators/unique-size.validator';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormControl } from '@angular/forms';
import { Observable, startWith, map } from 'rxjs';
declare var bootstrap: any;
@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,FormsModule,MatAutocompleteModule]
})

export class EditProductComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  imageBaseUrl = environment.imageBaseUrl;
  productForm!: FormGroup;
  coverImagePreview: string | null = null;
  isLoading = false;
  fileError: string | null = null;
  productId!: number;
  categoryList: Category[] = [];
  brandList: Brand[] = [];

  

  constructor(
    private formBuilder: FormBuilder,
    private cdr: ChangeDetectorRef,
    private notificationToastService: NotificationToastService,
    private productService: ProductService,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private fileUploadService: FileUpLoadService,
    private route: ActivatedRoute,
    private router: Router,
   
  ) { }


  ngOnInit(): void {
    this.initializeForm();
    this.loadCategory();
    this.loadBrand();
    // Lấy ID sản phẩm từ route
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.productId = +id;
        this.loadProduct(id);
      }
    });
    
  }
 
  private initializeForm(): void {
    this.productForm = this.formBuilder.group({
      name: ['', Validators.required],
      brandId: ['', Validators.required],
      categoryId: ['', Validators.required],
      description: ['', Validators.required],
      image: ['', Validators.required],
      productColors: this.formBuilder.array([])
    });
  }

  // Getter để truy cập FormArray "productColors"
  get productColors(): FormArray {
    return this.productForm.get('productColors') as FormArray;
  }

  // Getter để truy cập một FormGroup màu sắc cụ thể theo index
  getColorGroup(index: number): FormGroup {
    return this.productColors.at(index) as FormGroup;
  }

  // Getter để truy cập FormArray "variants" của một màu cụ thể
  getVariants(colorIndex: number): FormArray {
    return this.getColorGroup(colorIndex).get('variants') as FormArray;
  }
  getColorImagePreview(index: number): string | null {
    const colorGroup = this.getColorGroup(index);
    const imageUrl = colorGroup.get('colorImage')?.value;

    if (imageUrl) {
      return imageUrl.startsWith('http') ? imageUrl : environment.imageBaseUrl + imageUrl;
    }
    return null;
  }

 
  // Thêm một màu mới
  // Thêm một màu mới
addProductColor(): void {
  const colorGroup = this.formBuilder.group({
    color: ['', { validators: [Validators.required, uniqueColorValidator(() => this.productColors)], updateOn: 'blur' }], 
    price: [0, [Validators.required, Validators.min(1)]],
    colorImage: [''],
    variants: this.formBuilder.array([])
  });

  this.productColors.push(colorGroup);

  this.cdr.detectChanges();
}




  // Xóa một màu
  removeProductColor(index: number): void {
    this.productColors.removeAt(index);
    this.cdr.detectChanges();
  }

  // Thêm một variant (kích cỡ) cho một màu cụ thể
  addVariant(colorIndex: number): void {
    const variants = this.getVariants(colorIndex); 
    const variantGroup = this.formBuilder.group({
      productSize: ['', [Validators.required, uniqueSizeValidator(() => variants)]],
      stockQuantity: [1, [Validators.required, Validators.min(1)]]
    });

    this.getVariants(colorIndex).push(variantGroup);
    this.cdr.detectChanges();
  }

  // Xóa một variant
  removeVariant(colorIndex: number, variantIndex: number): void {
    this.getVariants(colorIndex).removeAt(variantIndex);
    this.cdr.detectChanges();
  }

  // Kiểm tra lỗi của một control
  hasError(control: any, errorName: string): boolean {
    return control && control.touched && control.hasError(errorName);
  }

  // Xử lý khi chọn file ảnh bìa
  onCoverFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.uploadImage(file, 'cover');
    }
  }

  // Xử lý khi chọn file ảnh màu
  onColorImageSelected(event: Event, colorIndex: number): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.uploadImage(file, 'color', colorIndex);
    }
  }

  // Upload ảnh
  private uploadImage(file: File, type: 'cover' | 'color', colorIndex?: number): void {
    const maxSize = 20 * 1024 * 1024; // 20MB
    if (file.size > maxSize) {
      this.fileError = 'File quá lớn! Vui lòng chọn file dưới 20MB.';
      return;
    }

    this.fileError = null;
    this.isLoading = true;

    this.fileUploadService.uploadFile(file).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (data) => {
        if (type === 'cover') {
          this.productForm.patchValue({ image: data });
          this.coverImagePreview = data;
        } else if (type === 'color' && colorIndex !== undefined) {
          const colorGroup = this.getColorGroup(colorIndex);
          colorGroup.patchValue({ colorImage: data });
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Lỗi khi upload ảnh:', err);
        this.fileError = 'Có lỗi xảy ra khi upload ảnh, vui lòng thử lại.';
      }
    });
  }

  // Tải dữ liệu sản phẩm
  private loadProduct(id: string): void {
    this.productService.searchProducts({ id }).subscribe({
      next: (response) => {
        if (response.data.length > 0) {
          const product = response.data[0];

          // Cập nhật form chính
          this.productForm.patchValue({
            name: product.name,
            categoryId: product.categoryId,
            brandId: product.brandId,
            description: product.description,
            image: product.imageUrl
          });

          // Cập nhật ảnh preview
          this.coverImagePreview = product.imageUrl ?? null;

          // Thêm các màu và variants
          product.productColors?.forEach(color => {
            const colorGroup = this.formBuilder.group({

              id: [color.id],
              color: [color.color, Validators.required],
              price: [color.price, [Validators.required, Validators.min(1)]],
              colorImage: [color.imageUrl],
              variants: this.formBuilder.array([])
            });

            // Thêm các variants cho màu này
            color.variants?.forEach(variant => {
              const variantGroup = this.formBuilder.group({
                id: [variant.id],
                productSize: [variant.productSize, Validators.required],
                stockQuantity: [variant.stockQuantity, [Validators.required, Validators.min(1)]]
              });

              (colorGroup.get('variants') as FormArray).push(variantGroup);
            });

            this.productColors.push(colorGroup);
          });
          console.log(product);
          this.cdr.detectChanges();
        }
      },
      error: (err) => {
        console.error('Lỗi khi tải sản phẩm:', err);
        alert('Không thể tải thông tin sản phẩm.');
        this.router.navigate(['/admin', 'products']);
      }
    });
  }


  // Submit form
  onSubmit(): void {
    if (this.productForm.invalid) {
      // Đánh dấu tất cả các controls là touched để hiển thị lỗi
      Object.keys(this.productForm.controls).forEach(key => {
        const control = this.productForm.get(key);
        control?.markAsTouched();
      });

      // Duyệt qua các màu và variants để đánh dấu chúng là touched
      this.productColors.controls.forEach((colorGroup: any) => {
        Object.keys(colorGroup.controls).forEach(key => {
          const control = colorGroup.get(key);
          control?.markAsTouched();

          if (key === 'variants') {
            const variants = colorGroup.get('variants') as FormArray;
            variants.controls.forEach((variantGroup: any) => {
              Object.keys(variantGroup.controls).forEach(varKey => {
                const varControl = variantGroup.get(varKey);
                varControl?.markAsTouched();
              });
            });
          }
        });
      });

      return;
    }

    this.isLoading = true;
    const formData = this.productForm.getRawValue();

    const productData: Product = {
      id: this.productId, // ✅ Lấy id từ form luôn
      name: formData.name,
      description: formData.description,
      imageUrl: formData.image,
      categoryId: formData.categoryId,
      brandId: formData.brandId,
      productColors: formData.productColors.map((color: any) => ({
        id: color.id, // ✅ Nếu có id của productColor, cũng lấy luôn
        color: color.color,
        price: color.price,
        imageUrl: color.colorImage,
        variants: color.variants.map((variant: any) => ({
          id: variant.id, // ✅ Nếu có id của variant, lấy luôn
          productSize: variant.productSize,
          stockQuantity: variant.stockQuantity
        }))
      }))
    };

    this.updateProduct(productData);
  }



  // Cập nhật sản phẩm
  private updateProduct(productData: Product): void {
    this.productService.updateProduct(productData)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: () => {
          this.notificationToastService.showSuccess('Cập nhật sản phẩm thành công!');
          this.router.navigate(['/admin', 'products']);
        },
        error: (err) => {
          console.error('Lỗi khi cập nhật sản phẩm:', err.errorMessage);
          this.notificationToastService.showError('Có lỗi xảy ra, vui lòng thử lại.');
        }
      });
  }

  // Hiển thị modal xác nhận hủy
  openCancelModal(): void {
    let modal = new bootstrap.Modal(document.getElementById('cancelModal'));
    modal.show();
  }

  // Load danh mục
  loadCategory(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categoryList = data;
        this.cdr.detectChanges();
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
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Lỗi khi tải danh mục:', err);
      }
    });
  }
  // Xác nhận hủy và chuyển hướng
  confirmCancel(): void {
    let modalElement = document.getElementById('cancelModal');
    if (modalElement) {
      let modalInstance = bootstrap.Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    }
    this.router.navigate(['/admin', 'products']);
  }
}