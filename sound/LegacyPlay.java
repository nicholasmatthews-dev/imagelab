package sound;

import imagelab.ImgProvider;

/**
 * The legacy implementation of ImgProvider.play() as an AudioFilter
 * @author Dr. Jody Paul
 * @author Nicholas Matthews
 * @version 1.1
 */
public class LegacyPlay extends AudioFilter {
	
	@Override
	public void playImage(ImgProvider inputImage) {
		short[][] playRed = inputImage.getRed();     // Red plane
        short[][] playGreen = inputImage.getGreen(); // Green plane
        short[][] playBlue = inputImage.getBlue();   // Blue plane
        short[][] bw = inputImage.getBWImage();      // Black & white image
        short[][] playAlpha = inputImage.getAlpha(); // Alpha channel
        short[][] hue;
        short[][] saturation;
        short[][] brightness;

        final int numChannels = 3;         // Number of music channels
        final int cyclesPerSec = 256;      // Frequency cycles per second
        final int minorThird = 3;          // The number of semitones in a minor third interval.
        final int perfectFourth = 5;       // The number of semitones in a perfect fourth interval.
        final int perfectFifth = 7;        // The number of semitones in a perfect fifth interval.
        final int minorSeventh = 10;       // The number of semitones in a minor seventh interval.
        final int octaveFactor = 12;       // Octive modifier
        final int octaveStartOffset = -3;  // Max offset to low frequency keys.
        final int octaveEndOffset = 4;     // Max offset to high frequency keys.

        int height = bw.length;
        int width = bw[0].length;

        //System.out.println("Playing image number " + getid());

        Tune tune = new Tune();
        /* A 7-octave pentatonic scale. */
        Scale scale = new Scale();
        for (int i = octaveStartOffset; i < octaveEndOffset; i++) {
            scale.addPitch(Note.C + (octaveFactor * i));
            scale.addPitch((Note.C + minorThird) + (octaveFactor * i));
            scale.addPitch((Note.C + perfectFourth) + (octaveFactor * i));
            scale.addPitch((Note.C + perfectFifth) + (octaveFactor * i));
            scale.addPitch((Note.C + minorSeventh) + (octaveFactor * i));
        }
        int pitchRange = scale.numPitches();
        Chord chord;
        int[] velocity = {0, 0, 0};
        int velocityRange = Note.VRANGE;
        int tempo = Note.DE / 2;
        int rowSum = 0;
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        float[] hsb = {0, 0, 0};
        float hueSum = 0;
        float satSum = 0;
        float brtSum = 0;

        for (int row = 0; row < height; row++) {
           for (int column = 0; column < width; column++) {
               rowSum += (bw[row][column]);
               redSum += (playRed[row][column]);
               greenSum += (playGreen[row][column]);
               blueSum += (playBlue[row][column]);
               java.awt.Color.RGBtoHSB(playRed[row][column],
               playGreen[row][column], playBlue[row][column], hsb);
               hueSum += hsb[0];
               satSum += hsb[1];
               brtSum += hsb[2];
           }
           velocity[0] = (int) (Note.VPP + (velocityRange * (hueSum / width)));
           velocity[1] = (int) (Note.VPP + (velocityRange * (satSum / width)));
           velocity[2] = (int) (Note.VPP + (velocityRange * (brtSum / width)));
           chord = new Chord();
           chord.addNote(new Note(0, (scale.getPitch(pitchRange
                   * redSum / width / cyclesPerSec)), tempo, velocity[0]));
           chord.addNote(new Note(1, (scale.getPitch(pitchRange
                   * greenSum / width / cyclesPerSec)), tempo, velocity[1]));
           chord.addNote(new Note(2, (scale.getPitch(pitchRange
                   * blueSum / width / cyclesPerSec)), tempo, velocity[2]));
           tune.addChord(chord);
           rowSum = 0;
           redSum = 0;
           greenSum = 0;
           blueSum = 0;
           hueSum = 0;
           satSum = 0;
           brtSum = 0;
       }
           int[] instruments = {Note.VIBES, Note.PIZZICATO, Note.MELODIC_TOM};
           Music m = new Music(numChannels, instruments);
           m.playTune(tune);
	}
}
