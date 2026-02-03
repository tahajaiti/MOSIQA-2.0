import { computed, Injectable, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, of, map } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import { Track, TrackFormData, MusicCategory } from '@core/models/track.model';
import { FileApiService } from '@core/api';
import * as TrackActions from '../../../store/track/track.actions';
import * as TrackSelectors from '../../../store/track/track.selectors';

export type TrackOperationState = 'idle' | 'loading' | 'success' | 'error';

@Injectable({
  providedIn: 'root',
})
export class TrackService {
  private readonly store = inject(Store);
  private readonly fileApiService = inject(FileApiService);

  // NgRx selectors as signals
  private readonly tracksSignal = toSignal(this.store.select(TrackSelectors.selectAllTracks), {
    initialValue: [],
  });
  private readonly loadingSignal = toSignal(this.store.select(TrackSelectors.selectTrackLoading), {
    initialValue: false,
  });
  private readonly errorSignal = toSignal(this.store.select(TrackSelectors.selectTrackError), {
    initialValue: null,
  });
  private readonly selectedTrackSignal = toSignal(
    this.store.select(TrackSelectors.selectSelectedTrack),
    { initialValue: null }
  );

  // Public signals (readonly)
  readonly tracks = computed(() => this.tracksSignal());
  readonly tracks$ = this.store.select(TrackSelectors.selectAllTracks);
  readonly state = computed<TrackOperationState>(() => {
    if (this.loadingSignal()) return 'loading';
    if (this.errorSignal()) return 'error';
    return 'idle';
  });
  readonly error = computed(() => this.errorSignal());
  readonly selectedTrack = computed(() => this.selectedTrackSignal());
  readonly trackCount = computed(() => this.tracks().length);
  readonly isLoading = computed(() => this.loadingSignal());
  readonly hasError = computed(() => !!this.errorSignal());

  constructor() {
    // Load tracks on initialization
    this.loadTracks();
  }

  loadTracks(): void {
    this.store.dispatch(TrackActions.loadTracks());
  }

  createTrack(formData: TrackFormData): Observable<Track | null> {
    this.store.dispatch(TrackActions.createTrack({ formData }));
    // Return observable that resolves when track is created
    return this.store.select(TrackSelectors.selectAllTracks).pipe(
      map((tracks) => tracks[0] ?? null)
    );
  }

  updateTrack(
    id: string,
    updates: Partial<Omit<TrackFormData, 'audioFile'>> & { audioFile?: File }
  ): Observable<Track | null> {
    this.store.dispatch(TrackActions.updateTrack({ id, updates }));
    return this.store.select(TrackSelectors.selectTrackById(id));
  }

  deleteTrack(id: string): Observable<boolean> {
    this.store.dispatch(TrackActions.deleteTrack({ id }));
    return of(true);
  }

  selectTrack(track: Track | null): void {
    this.store.dispatch(TrackActions.selectTrack({ track }));
  }

  getTrackById(id: string): Track | undefined {
    return this.tracks().find((t: Track) => t.id === id);
  }

  getTrackById$(id: string): Observable<Track | null> {
    return this.store.select(TrackSelectors.selectTrackById(id));
  }

  getAudioUrl(audioFileId: string): Observable<string | null> {
    // Return direct URL to backend API
    return of(this.fileApiService.getAudioUrl(audioFileId));
  }

  getCoverImageUrl(coverImageId: string): Observable<string | null> {
    // Return direct URL to backend API
    return of(this.fileApiService.getCoverImageUrl(coverImageId));
  }

  clearError(): void {
    this.store.dispatch(TrackActions.clearError());
  }

  searchTracks(query: string): void {
    if (query.trim()) {
      this.store.dispatch(TrackActions.searchTracks({ query }));
    } else {
      this.loadTracks();
    }
  }
}
