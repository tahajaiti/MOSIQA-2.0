import { createAction, props } from '@ngrx/store';
import { Track, TrackFormData, MusicCategory } from '@core/models';

// Load Tracks
export const loadTracks = createAction('[Track] Load Tracks');
export const loadTracksSuccess = createAction(
    '[Track] Load Tracks Success',
    props<{ tracks: Track[] }>()
);
export const loadTracksFailure = createAction('[Track] Load Tracks Failure', props<{ error: string }>());

// Load Single Track
export const loadTrack = createAction('[Track] Load Track', props<{ id: string }>());
export const loadTrackSuccess = createAction('[Track] Load Track Success', props<{ track: Track }>());
export const loadTrackFailure = createAction('[Track] Load Track Failure', props<{ error: string }>());

// Create Track
export const createTrack = createAction(
    '[Track] Create Track',
    props<{ formData: TrackFormData }>()
);
export const createTrackSuccess = createAction(
    '[Track] Create Track Success',
    props<{ track: Track }>()
);
export const createTrackFailure = createAction(
    '[Track] Create Track Failure',
    props<{ error: string }>()
);

// Update Track
export const updateTrack = createAction(
    '[Track] Update Track',
    props<{ id: string; updates: Partial<TrackFormData> }>()
);
export const updateTrackSuccess = createAction(
    '[Track] Update Track Success',
    props<{ track: Track }>()
);
export const updateTrackFailure = createAction(
    '[Track] Update Track Failure',
    props<{ error: string }>()
);

// Delete Track
export const deleteTrack = createAction('[Track] Delete Track', props<{ id: string }>());
export const deleteTrackSuccess = createAction('[Track] Delete Track Success', props<{ id: string }>());
export const deleteTrackFailure = createAction('[Track] Delete Track Failure', props<{ error: string }>());

// Search Tracks
export const searchTracks = createAction('[Track] Search Tracks', props<{ query: string }>());
export const searchTracksSuccess = createAction(
    '[Track] Search Tracks Success',
    props<{ tracks: Track[] }>()
);
export const searchTracksFailure = createAction(
    '[Track] Search Tracks Failure',
    props<{ error: string }>()
);

// Select Track
export const selectTrack = createAction('[Track] Select Track', props<{ track: Track | null }>());

// Clear Error
export const clearError = createAction('[Track] Clear Error');
