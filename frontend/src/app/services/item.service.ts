import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, Item, ItemUpsert } from '../models';

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  private apiUrl = 'http://localhost:8080/api/items';

  constructor(private http: HttpClient) {}

  getAllItems(): Observable<ApiResponse<Item[]>> {
    return this.http.get<ApiResponse<Item[]>>(this.apiUrl);
  }

  getShowsFiltered(filters: any): Observable<ApiResponse<any>> {
    let params = new HttpParams();
    Object.keys(filters).forEach((key) => {
      const value = filters[key];
      if (value !== null && value !== '') {
        params = params.append(key, value);
      }
    });

    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/shows/filtered`, {
      params,
    });
  }

  getHeadliners(page: number, size: number): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/headliners`, {
      params,
    });
  }

  getEpisodes(
    seriesId: number,
    page: number,
    size: number
  ): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ApiResponse<any>>(
      `${this.apiUrl}/series/${seriesId}/episodes`,
      { params }
    );
  }

  createItem(item: ItemUpsert): Observable<ApiResponse<Item>> {
    return this.http.post<ApiResponse<Item>>(this.apiUrl, item);
  }

  updateItem(id: number, item: ItemUpsert): Observable<ApiResponse<Item>> {
    return this.http.put<ApiResponse<Item>>(`${this.apiUrl}/${id}`, item);
  }

  deleteItem(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}