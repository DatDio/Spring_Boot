import { AbstractControl, ValidatorFn, FormArray } from '@angular/forms';

// Validator kiểm tra trùng kích cỡ
export function uniqueSizeValidator(getSizesArray: () => FormArray): ValidatorFn {
    return (control: AbstractControl) => {
      if (!control.value) return null; // Nếu rỗng thì không kiểm tra
  
      const value = control.value.trim().toLowerCase();
  
      // Lấy danh sách kích cỡ trong cùng một màu (loại bỏ chính nó)
      const existingSizes = getSizesArray().controls
        .filter(ctrl => ctrl !== control.parent) // Bỏ qua chính nó
        .map(ctrl => ctrl.get('productSize')?.value?.trim().toLowerCase())
        .filter(size => size); // Lọc giá trị hợp lệ
  
      const isDuplicate = existingSizes.includes(value);
  
      return isDuplicate ? { duplicateSize: true } : null;
    };
  }
  

