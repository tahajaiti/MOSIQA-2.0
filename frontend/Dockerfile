# Development Dockerfile - runs ng serve directly
FROM node:22-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Copy source code
COPY . .

# Expose Angular dev server port
EXPOSE 4200

# Start Angular dev server with host binding for Docker
CMD ["ng", "serve", "--host", "0.0.0.0"]
