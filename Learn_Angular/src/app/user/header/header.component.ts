import { Component, OnInit } from '@angular/core';
import { Router , RouterModule} from '@angular/router';
import { FormControl } from '@angular/forms';
import { CommonModule } from '@angular/common'; 
import { ProductService } from 'src/app/core/services/product/product.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { ReactiveFormsModule } from '@angular/forms'; 
@Component({
  selector: 'app-header',
  imports:[ReactiveFormsModule,RouterModule ,CommonModule ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  searchControl = new FormControl('');
  searchResults: any[] = [];
  showDropdown = false; // Hiển thị dropdown gợi ý

  constructor(private router: Router, private productService: ProductService) { }

  ngOnInit(): void {
    this.searchControl.valueChanges.pipe(
      debounceTime(1000),
      distinctUntilChanged(),
      switchMap(term => {
        if (!term?.trim()) { // Kiểm tra null hoặc rỗng
          this.searchResults = [];
          return [];
        }
        return this.productService.searchProducts({ name: term });
      })
    ).subscribe((data: any) => {
      this.searchResults = data?.data ?? []; // Nếu data null, gán mảng rỗng
      this.showDropdown = this.searchResults.length > 0;
    });

  }

  /** Khi ấn Enter -> Chuyển hướng */
  onEnter(): void {
    const searchTerm = this.searchControl.value?.trim() ?? '';
    if (searchTerm) {
      this.router.navigate(['/products'], { queryParams: { search: searchTerm } });
      this.showDropdown = false; // Ẩn dropdown
    }
  }

  /** Chọn 1 sản phẩm từ gợi ý */
  selectProduct(product: any): void {
    this.router.navigate(['/products'], { queryParams: { search: product.name } });
    this.showDropdown = false;
  }

   /** Ẩn dropdown khi rời khỏi input */
   hideDropdown(): void {
    setTimeout(() => {
      this.showDropdown = false;
    }, 2000000); // Đợi 200ms để tránh bị ẩn khi click vào gợi ý
  }
}
