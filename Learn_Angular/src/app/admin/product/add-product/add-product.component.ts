// add-product.component.ts
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ProductService } from 'src/app/core/services/product/product.service';
import { Product } from 'src/app/share/models/product.model';
import { Category } from '@models/category.model';
import { Brand } from '@models/brand.model';
import { finalize } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '@services/category/category.service';
import { BrandService } from '@services/brand/brand.service';
import { FileUpLoadService } from '@services/fileUpload/fileUpload.service';
import { ChangeDetectorRef } from '@angular/core';
import { environment } from '@environments/environment';
import { NotificationToastService } from '@services/notificationToast/notificationtoast.service';
import {uniqueColorValidator } from 'src/app/share/validators/unique-color.validator';
import {uniqueSizeValidator} from 'src/app/share/validators/unique-size.validator';
declare var bootstrap: any;
@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class AddProductComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  imageBaseUrl = environment.imageBaseUrl;
  categoryList: Category[] = [];
  brandList: Brand[] = [];
  productForm!: FormGroup;
  coverImagePreview: string | null = null;
  isLoading = false;
  fileError: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private cdr: ChangeDetectorRef,
    private notificationToastService:NotificationToastService,
    private productService: ProductService,
    private fileUploadService: FileUpLoadService,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.loadCategory();
    this.loadBrand();
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


  get productColors(): FormArray {
    return this.productForm.get('productColors') as FormArray;
  }


  getColorGroup(index: number): FormGroup {
    return this.productColors.at(index) as FormGroup;
  }

  getColorImagePreview(index: number): string | null {
    const colorGroup = this.getColorGroup(index);
    const imageUrl = colorGroup.get('colorImage')?.value;
  
    if (imageUrl) {
      return imageUrl.startsWith('http') ? imageUrl : environment.imageBaseUrl + imageUrl;
    }
    return null;
  }
  getVariants(colorIndex: number): FormArray {
    return this.getColorGroup(colorIndex).get('variants') as FormArray;
  }

  // Thêm một màu mới
  addProductColor(): void {
    console.log('Adding new color');

    const colorGroup = this.formBuilder.group({
    color: ['', { validators: [Validators.required, uniqueColorValidator(() => this.productColors)], updateOn: 'blur' }], 
      price: [0, [Validators.required, Validators.min(0)]],
      colorImage: [''],
      variants: this.formBuilder.array([])
    });

    this.productColors.push(colorGroup);


    console.log('Color added, total colors:', this.productColors.length);
    console.log('ProductColors array:', this.productColors.value);


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
    const variants = this.getVariants(colorIndex);
    variants.removeAt(variantIndex);
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
        if (type === 'cover' && data != undefined) {
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

  // Submit form
  onSubmit(): void {
    if (this.productForm.invalid) {
      // Đánh dấu tất cả các controls là đã touched để hiển thị lỗi
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
      name: formData.name,
      brandId: formData.brandId,
      categoryId: formData.categoryId,
      description: formData.description,
      imageUrl: formData.image,
      productColors: formData.productColors.map((color: any) => ({
        color: color.color,
        price: color.price,
        imageUrl: color.colorImage,
        variants: color.variants
      }))
    };

    this.productService.createProduct(productData)
      .pipe(finalize(() => this.isLoading = false))
      .subscribe({
        next: (response) => {
          this.openSuccessModal();
        },
        error: (err) => {

          console.error('Lỗi khi thêm sản phẩm:', err);
          this.notificationToastService.showError('Có lỗi xảy ra, vui lòng thử lại.');
        }
      });
  }

  goBack() {
    this.hideSuccessModal();
    this.router.navigate(['/admin', 'products']);
  }

  AddMoreProduct(){
    this.hideSuccessModal();
  }
  // Reset form
  resetForm(): void {
    this.productForm.reset();
    while (this.productColors.length) {
      this.productColors.removeAt(0);
    }
    this.coverImagePreview = null;
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
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

  // Hiển thị modal xác nhận hủy
  openSuccessModal(): void {
    let modal = new bootstrap.Modal(document.getElementById('AddProductSucces'));
    modal.show();
  }

  // Xác nhận hủy và chuyển hướng
  hideSuccessModal(): void {
    let modalElement = document.getElementById('AddProductSucces');
    if (modalElement) {
      let modalInstance = bootstrap.Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    }
    this.resetForm();
  }
}