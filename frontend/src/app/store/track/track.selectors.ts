import { createFeatureSelector, createSelector } from '@ngrx/store';
import { TrackState } from './track.state';

export const selectTrackState = createFeatureSelector<TrackState>('track');

export const selectAllTracks = createSelector(selectTrackState, (state) => state.tracks);

export const selectSelectedTrack = createSelector(selectTrackState, (state) => state.selectedTrack);

export const selectTrackLoading = createSelector(selectTrackState, (state) => state.loading);

export const selectTrackError = createSelector(selectTrackState, (state) => state.error);

export const selectTrackCount = createSelector(selectAllTracks, (tracks) => tracks.length);

export const selectTrackById = (id: string) =>
    createSelector(selectAllTracks, (tracks) => tracks.find((t) => t.id === id) ?? null);
