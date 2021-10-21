package sound;

import imagelab.ImgProvider;

/**
 * The controller for audio related functions for ImageLab
 * @author Nicholas Matthews (GitHub: nicholasmatthews-dev)
 * @version 0.1
 */
public class AudioController {
	/** Thread which plays the current audio. */
	private Thread playThread = null;
	
	/** The current AudioFilter to apply to the image */
	private AudioFilter currentFilter;
	
	/**
	 * Construct an AudioController, using default AudioFilter = LegacyPlay
	 */
	public AudioController() {
		currentFilter = new LegacyPlay();
	}
	
	/**
	 * Initializes a thread which will run the AudioFilter.playImage() defined in currentFilter
	 * @param inputImage The ImgProvider upon which the audio filter will operate
	 */
	public void playCurrentFilter(ImgProvider inputImage) {
		playThread = new Thread(() -> {currentFilter.playImage(inputImage);});
		playThread.start();
	}
	
}
