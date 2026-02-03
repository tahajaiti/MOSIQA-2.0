import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class FileApiService {
    private readonly baseUrl = `${environment.apiBaseUrl}/files`;

    constructor(private http: HttpClient) { }

    getAudioUrl(audioFileId: string): string {
        return `${this.baseUrl}/audio/${audioFileId}`;
    }

    getCoverImageUrl(coverImageId: string): string {
        return `${this.baseUrl}/cover/${coverImageId}`;
    }

    getAudioBlob(audioFileId: string): Observable<Blob> {
        return this.http.get(`${this.baseUrl}/audio/${audioFileId}`, {
            responseType: 'blob',
        });
    }

    getCoverImageBlob(coverImageId: string): Observable<Blob> {
        return this.http.get(`${this.baseUrl}/cover/${coverImageId}`, {
            responseType: 'blob',
        });
    }

    getAudioObjectUrl(audioFileId: string): Observable<string> {
        return this.getAudioBlob(audioFileId).pipe(map((blob) => URL.createObjectURL(blob)));
    }

    getCoverImageObjectUrl(coverImageId: string): Observable<string> {
        return this.getCoverImageBlob(coverImageId).pipe(map((blob) => URL.createObjectURL(blob)));
    }
}
