import { Track } from '@core/models';

export interface TrackState {
    tracks: Track[];
    selectedTrack: Track | null;
    loading: boolean;
    error: string | null;
}

export const initialTrackState: TrackState = {
    tracks: [],
    selectedTrack: null,
    loading: false,
    error: null,
};
