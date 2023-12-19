package gh2;

import deque.ArrayDeque;
import deque.Deque;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final double CONCERT_A = 440.0;
    public static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public Deque<GuitarString> strings;

    public GuitarHero() {
        int size = keyboard.length();
        strings = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            strings.addLast(new GuitarString(CONCERT_A * Math.pow(2, (i - 24.0) / 12.0)));
        }
    }

    public static void main(String[] args) {
        GuitarHero gh = new GuitarHero();

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index != -1 ) {
                    gh.pluckString(index);
                }
            }

            /* compute the superposition of samples */
            double sample = gh.computeSample();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            gh.tic();
        }
    }

    private void pluckString(int index) {
        strings.get(index).pluck();
    }

    private double computeSample() {
        double result = 0;
        for (int i = 0; i < strings.size(); i++) {
            result += strings.get(i).sample();
        }
        return result;
    }

    private void tic() {
        for (int i = 0; i < strings.size(); i++) {
           strings.get(i).tic();
        }
    }
}
