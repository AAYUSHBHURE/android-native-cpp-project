# FLAM Web Viewer

TypeScript-based web viewer for displaying processed camera frames from the FLAM Android app.

## Setup

1. Install dependencies:
```bash
npm install
```

2. Build TypeScript:
```bash
npm run build
```

3. Serve the viewer:
```bash
npm run serve
```

4. Open browser:
   - Navigate to `http://localhost:8080`

## Usage

1. Run the FLAM Android app
2. Capture a frame (the app will save it to device storage)
3. Copy the frame image to this `/web` directory as `frame.jpg`
4. Refresh the web viewer to see the processed frame with stats

## Features

- Display processed camera frames (edge detection or raw)
- Real-time statistics overlay:
  - FPS (frames per second)
  - Resolution
  - Processing mode
  - OpenCV version
  - Timestamp
- Responsive design with glassmorphism UI

## Development

- Watch mode: `npm run watch` (auto-recompile on changes)
- The viewer expects a `frame.jpg` in the root directory
- Modify `src/main.ts` to customize stats or add features
