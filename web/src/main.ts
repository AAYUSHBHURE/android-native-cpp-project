// TypeScript web viewer for FLAM processed camera frames

interface FrameStats {
    fps: number;
    resolution: string;
    mode: string;
    opencvVersion: string;
    timestamp: string;
}

class FrameViewer {
    private frameElement: HTMLImageElement;
    private statsElements: {
        fps: HTMLElement;
        resolution: HTMLElement;
        mode: HTMLElement;
        opencvVersion: HTMLElement;
        timestamp: HTMLElement;
    };

    constructor() {
        this.frameElement = document.getElementById('frame') as HTMLImageElement;
        this.statsElements = {
            fps: document.getElementById('fps')!,
            resolution: document.getElementById('resolution')!,
            mode: document.getElementById('mode')!,
            opencvVersion: document.getElementById('opencv-version')!,
            timestamp: document.getElementById('timestamp')!
        };

        this.init();
    }

    private init(): void {
        // Display sample stats for the static frame
        const sampleStats: FrameStats = {
            fps: 30.0,
            resolution: '1920x1080',
            mode: 'Edge Detection (Canny)',
            opencvVersion: '4.8.0',
            timestamp: new Date().toLocaleString()
        };

        this.updateStats(sampleStats);

        // Check if frame.jpg exists and update image
        this.loadFrame();

        // Add image load event listener
        this.frameElement.addEventListener('load', () => {
            const img = this.frameElement;
            if (img.naturalWidth && img.naturalHeight) {
                this.statsElements.resolution.textContent = `${img.naturalWidth}x${img.naturalHeight}`;
            }
        });
    }

    private updateStats(stats: FrameStats): void {
        this.statsElements.fps.textContent = stats.fps.toFixed(1);
        this.statsElements.resolution.textContent = stats.resolution;
        this.statsElements.mode.textContent = stats.mode;
        this.statsElements.opencvVersion.textContent = stats.opencvVersion;
        this.statsElements.timestamp.textContent = stats.timestamp;
    }

    private loadFrame(): void {
        // Force reload the image to get latest version
        const timestamp = new Date().getTime();
        this.frameElement.src = `frame.jpg?t=${timestamp}`;
    }

    // Public method to refresh frame (can be called periodically if frames are updated)
    public refresh(): void {
        this.loadFrame();
        this.statsElements.timestamp.textContent = new Date().toLocaleString();
    }
}

// Initialize viewer on page load
window.addEventListener('DOMContentLoaded', () => {
    const viewer = new FrameViewer();
    
    // Optional: Auto-refresh every 5 seconds if frames are being updated
    // setInterval(() => viewer.refresh(), 5000);
    
    console.log('FLAM Web Viewer initialized');
});
