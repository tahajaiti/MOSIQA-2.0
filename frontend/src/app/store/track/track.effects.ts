import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, catchError, switchMap, tap } from 'rxjs/operators';
import { TrackApiService, TrackResponse } from '@core/api';
import { ToastService } from '@core/services';
import { Track, MusicCategory } from '@core/models';
import * as TrackActions from './track.actions';

@Injectable()
export class TrackEffects {
    constructor(
        private actions$: Actions,
        private trackApiService: TrackApiService,
        private toastService: ToastService
    ) { }

    private mapResponseToTrack(response: TrackResponse): Track {
        return {
            id: response.id,
            title: response.title,
            artist: response.artist,
            description: response.description,
            category: response.category,
            duration: response.duration,
            audioFileId: response.audioFileId,
            coverImageId: response.coverImageId,
            createdAt: new Date(response.createdAt),
            updatedAt: new Date(response.updatedAt),
        };
    }

    loadTracks$ = createEffect(() =>
        this.actions$.pipe(
            ofType(TrackActions.loadTracks),
            switchMap(() =>
                this.trackApiService.getAllTracks().pipe(
                    map((responses) => responses.map((r) => this.mapResponseToTrack(r))),
                    map((tracks) => TrackActions.loadTracksSuccess({ tracks })),
                    catchError((error) =>
                        of(TrackActions.loadTracksFailure({ error: error.message || 'Failed to load tracks' }))
                    )
                )
            )
        )
    );

    loadTrack$ = createEffect(() =>
        this.actions$.pipe(
            ofType(TrackActions.loadTrack),
            switchMap(({ id }) =>
                this.trackApiService.getTrackById(id).pipe(
                    map((response) => this.mapResponseToTrack(response)),
                    map((track) => TrackActions.loadTrackSuccess({ track })),
                    catchError((error) =>
                        of(TrackActions.loadTrackFailure({ error: error.message || 'Failed to load track' }))
                    )
                )
            )
        )
    );

    createTrack$ = createEffect(() =>
        this.actions$.pipe(
            ofType(TrackActions.createTrack),
            switchMap(({ formData }) =>
                this.trackApiService
                    .createTrack(
                        {
                            title: formData.title,
                            artist: formData.artist,
                            description: formData.description,
                            category: formData.category,
                            duration: 0, // Will be calculated by backend or frontend
                        },
                        formData.audioFile,
                        formData.coverImage
                    )
                    .pipe(
                        map((response) => this.mapResponseToTrack(response)),
                        map((track) => TrackActions.createTrackSuccess({ track })),
                        catchError((error) =>
                            of(TrackActions.createTrackFailure({ error: error.message || 'Failed to create track' }))
                        )
                    )
            )
        )
    );

    createTrackSuccess$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(TrackActions.createTrackSuccess),
                tap(() => this.toastService.success('Track created successfully'))
            ),
        { dispatch: false }
    );

    updateTrack$ = createEffect(() =>
        this.actions$.pipe(
            ofType(TrackActions.updateTrack),
            switchMap(({ id, updates }) =>
                this.trackApiService
                    .updateTrack(
                        id,
                        {
                            title: updates.title,
                            artist: updates.artist,
                            description: updates.description,
                            category: updates.category,
                        },
                        updates.audioFile,
                        updates.coverImage
                    )
                    .pipe(
                        map((response) => this.mapResponseToTrack(response)),
                        map((track) => TrackActions.updateTrackSuccess({ track })),
                        catchError((error) =>
                            of(TrackActions.updateTrackFailure({ error: error.message || 'Failed to update track' }))
                        )
                    )
            )
        )
    );

    updateTrackSuccess$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(TrackActions.updateTrackSuccess),
                tap(() => this.toastService.success('Track updated successfully'))
            ),
        { dispatch: false }
    );

    deleteTrack$ = createEffect(() =>
        this.actions$.pipe(
            ofType(TrackActions.deleteTrack),
            switchMap(({ id }) =>
                this.trackApiService.deleteTrack(id).pipe(
                    map(() => TrackActions.deleteTrackSuccess({ id })),
                    catchError((error) =>
                        of(TrackActions.deleteTrackFailure({ error: error.message || 'Failed to delete track' }))
                    )
                )
            )
        )
    );

    deleteTrackSuccess$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(TrackActions.deleteTrackSuccess),
                tap(() => this.toastService.success('Track deleted successfully'))
            ),
        { dispatch: false }
    );

    searchTracks$ = createEffect(() =>
        this.actions$.pipe(
            ofType(TrackActions.searchTracks),
            switchMap(({ query }) =>
                this.trackApiService.searchTracks(query).pipe(
                    map((responses) => responses.map((r) => this.mapResponseToTrack(r))),
                    map((tracks) => TrackActions.searchTracksSuccess({ tracks })),
                    catchError((error) =>
                        of(TrackActions.searchTracksFailure({ error: error.message || 'Failed to search tracks' }))
                    )
                )
            )
        )
    );

    // Show error toasts for failures
    showErrorToast$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(
                    TrackActions.loadTracksFailure,
                    TrackActions.loadTrackFailure,
                    TrackActions.createTrackFailure,
                    TrackActions.updateTrackFailure,
                    TrackActions.deleteTrackFailure,
                    TrackActions.searchTracksFailure
                ),
                tap(({ error }) => this.toastService.error(error))
            ),
        { dispatch: false }
    );
}
