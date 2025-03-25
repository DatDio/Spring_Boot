import { Injectable } from '@angular/core';
import { ApiService } from 'src/app/core/services/api/api.service';
import { Category } from '@models/category.model';
import { Observable } from 'rxjs';
@Injectable({
    providedIn: 'root'
})
export class FileUpLoadService {
    private url = '/file'
    constructor(private apiService: ApiService) {

    }
    uploadFile(file:File):Observable<any>{
        const formData = new FormData();
        if(file!=null){
            formData.append('file', file);
        }
        return this.apiService.postTypeRequest<any>(this.url+'/upload',formData);
    }

}