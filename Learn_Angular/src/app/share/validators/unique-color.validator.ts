import { AbstractControl, ValidatorFn, FormArray } from '@angular/forms';

export function uniqueColorValidator(productColors: () => FormArray): ValidatorFn {
  return (control: AbstractControl) => {
    if (!control.value) return null; // Nếu rỗng thì không kiểm tra

    const value = control.value.trim().toLowerCase();

    // Lấy danh sách tất cả màu (bao gồm chính nó)
    const existingColors = productColors().controls
      .map(ctrl => ctrl.get('color')?.value?.trim().toLowerCase())
      .filter(color => color); // Loại bỏ giá trị rỗng

    // Đếm số lần xuất hiện của màu hiện tại
    const colorCount = existingColors.filter(color => color === value).length;

    return colorCount > 1 ? { duplicateColor: true } : null; // Nếu xuất hiện >1 lần thì báo lỗi
  };
  
}
