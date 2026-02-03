import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Track, MusicCategory } from '@core/models';

export interface TrackResponse {
    id: string;
    title: string;
    artist: string;
    description?: string;
    category: MusicCategory;
    duration: number;
    audioFileId: string;
    coverImageId?: string;
    createdAt: string;
    updatedAt: string;
}

export interface TrackCreateRequest {
    title: string;
    artist: string;
    description?: string;
    category: MusicCategory;
    duration: number;
}

export interface TrackUpdateRequest {
    title?: string;
    artist?: string;
    description?: string;
    category?: MusicCategory;
    duration?: number;
}

@Injectable({
    providedIn: 'root',
})
export class TrackApiService {
    private readonly baseUrl = `${environment.apiBaseUrl}/tracks`;

    constructor(private http: HttpClient) { }

    getAllTracks(): Observable<TrackResponse[]> {
        return this.http.get<TrackResponse[]>(this.baseUrl);
    }

    getTrackById(id: string): Observable<TrackResponse> {
        return this.http.get<TrackResponse>(`${this.baseUrl}/${id}`);
    }

    createTrack(
        metadata: TrackCreateRequest,
        audioFile: File,
        coverImage?: File
    ): Observable<TrackResponse> {
        const formData = new FormData();
        formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));
        formData.append('audioFile', audioFile);
        if (coverImage) {
            formData.append('coverImage', coverImage);
        }
        return this.http.post<TrackResponse>(this.baseUrl, formData);
    }

    updateTrack(
        id: string,
        metadata: TrackUpdateRequest,
        audioFile?: File,
        coverImage?: File
    ): Observable<TrackResponse> {
        const formData = new FormData();
        formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));
        if (audioFile) {
            formData.append('audioFile', audioFile);
        }
        if (coverImage) {
            formData.append('coverImage', coverImage);
        }
        return this.http.put<TrackResponse>(`${this.baseUrl}/${id}`, formData);
    }

    deleteTrack(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    searchTracks(query: string): Observable<TrackResponse[]> {
        const params = new HttpParams().set('q', query);
        return this.http.get<TrackResponse[]>(`${this.baseUrl}/search`, { params });
    }

    getTracksByCategory(category: MusicCategory): Observable<TrackResponse[]> {
        return this.http.get<TrackResponse[]>(`${this.baseUrl}/category/${category}`);
    }
}
