import { createReducer, on } from '@ngrx/store';
import { initialTrackState, TrackState } from './track.state';
import * as TrackActions from './track.actions';

export const trackReducer = createReducer(
    initialTrackState,

    // Load Tracks
    on(TrackActions.loadTracks, (state): TrackState => ({
        ...state,
        loading: true,
        error: null,
    })),
    on(TrackActions.loadTracksSuccess, (state, { tracks }): TrackState => ({
        ...state,
        tracks,
        loading: false,
        error: null,
    })),
    on(TrackActions.loadTracksFailure, (state, { error }): TrackState => ({
        ...state,
        loading: false,
        error,
    })),

    // Load Single Track
    on(TrackActions.loadTrack, (state): TrackState => ({
        ...state,
        loading: true,
        error: null,
    })),
    on(TrackActions.loadTrackSuccess, (state, { track }): TrackState => ({
        ...state,
        selectedTrack: track,
        loading: false,
        error: null,
    })),
    on(TrackActions.loadTrackFailure, (state, { error }): TrackState => ({
        ...state,
        loading: false,
        selectedTrack: null,
        error,
    })),

    // Create Track
    on(TrackActions.createTrack, (state): TrackState => ({
        ...state,
        loading: true,
        error: null,
    })),
    on(TrackActions.createTrackSuccess, (state, { track }): TrackState => ({
        ...state,
        tracks: [track, ...state.tracks],
        loading: false,
        error: null,
    })),
    on(TrackActions.createTrackFailure, (state, { error }): TrackState => ({
        ...state,
        loading: false,
        error,
    })),

    // Update Track
    on(TrackActions.updateTrack, (state): TrackState => ({
        ...state,
        loading: true,
        error: null,
    })),
    on(TrackActions.updateTrackSuccess, (state, { track }): TrackState => ({
        ...state,
        tracks: state.tracks.map((t) => (t.id === track.id ? track : t)),
        selectedTrack: state.selectedTrack?.id === track.id ? track : state.selectedTrack,
        loading: false,
        error: null,
    })),
    on(TrackActions.updateTrackFailure, (state, { error }): TrackState => ({
        ...state,
        loading: false,
        error,
    })),

    // Delete Track
    on(TrackActions.deleteTrack, (state): TrackState => ({
        ...state,
        loading: true,
        error: null,
    })),
    on(TrackActions.deleteTrackSuccess, (state, { id }): TrackState => ({
        ...state,
        tracks: state.tracks.filter((t) => t.id !== id),
        selectedTrack: state.selectedTrack?.id === id ? null : state.selectedTrack,
        loading: false,
        error: null,
    })),
    on(TrackActions.deleteTrackFailure, (state, { error }): TrackState => ({
        ...state,
        loading: false,
        error,
    })),

    // Search Tracks
    on(TrackActions.searchTracks, (state): TrackState => ({
        ...state,
        loading: true,
        error: null,
    })),
    on(TrackActions.searchTracksSuccess, (state, { tracks }): TrackState => ({
        ...state,
        tracks,
        loading: false,
        error: null,
    })),
    on(TrackActions.searchTracksFailure, (state, { error }): TrackState => ({
        ...state,
        loading: false,
        error,
    })),

    // Select Track
    on(TrackActions.selectTrack, (state, { track }): TrackState => ({
        ...state,
        selectedTrack: track,
    })),

    // Clear Error
    on(TrackActions.clearError, (state): TrackState => ({
        ...state,
        error: null,
    }))
);
