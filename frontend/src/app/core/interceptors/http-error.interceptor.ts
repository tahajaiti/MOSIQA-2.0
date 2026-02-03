import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ToastService } from '@core/services';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
    const toastService = inject(ToastService);

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            let errorMessage = 'An unexpected error occurred';

            if (error.error instanceof ErrorEvent) {
                // Client-side error
                errorMessage = error.error.message;
            } else {
                // Server-side error
                switch (error.status) {
                    case 0:
                        errorMessage = 'Unable to connect to server. Please check your connection.';
                        break;
                    case 400:
                        errorMessage = error.error?.message || 'Invalid request';
                        break;
                    case 404:
                        errorMessage = error.error?.message || 'Resource not found';
                        break;
                    case 413:
                        errorMessage = 'File size too large';
                        break;
                    case 500:
                        errorMessage = 'Server error. Please try again later.';
                        break;
                    default:
                        errorMessage = error.error?.message || `Error: ${error.status}`;
                }
            }

            // Don't show toast here - let components/effects handle it
            console.error('HTTP Error:', error);

            return throwError(() => new Error(errorMessage));
        })
    );
};
