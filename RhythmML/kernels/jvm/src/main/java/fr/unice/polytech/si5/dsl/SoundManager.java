/*
  --------------------------------------------------------------------------------------
  "THE BEER-WARE LICENSE" (Revision 42):
  Julien Deantoni wrote this file.  As long as you retain this notice you
  can do whatever you want with this stuff. If we meet some day, and you think
  this stuff is worth it, you can buy me/us a beer in return.
  --------------------------------------------------------------------------------------
 */

package fr.unice.polytech.si5.dsl;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class SoundManager {

    private Vector<AudioFormat> afs;
    private Vector<Integer> sizes;
    public Vector<Info> infos;
    private Vector<byte[]> audios;
    private int num = 0;

    public SoundManager() {
        afs = new Vector<>();
        sizes = new Vector<>();
        infos = new Vector<>();
        audios = new Vector<>();
    }

    /**
     * add a Clip (i.e., a sample) in a collection and return its id.
     *
     * @param filePath, the path to the wav file
     * @return the id of the new sample
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public int addClip(String filePath) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        AudioFormat af = audioInputStream.getFormat();
        int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
        byte[] audio = new byte[size];
        DataLine.Info info = new DataLine.Info(Clip.class, af, size);
        audioInputStream.read(audio, 0, size);

        afs.add(af);
        sizes.add(new Integer(size));
        infos.add(info);
        audios.add(audio);

        return num++;
    }

    /**
     * same as {@link #addClip(String)} but import only a sub sample from the file, from start to end in terms of byte representation of the signal. start and end must be even.
     *
     * @param filePath
     * @param start
     * @param end
     * @return the id of the new sample
     */
    public int addClip(String filePath, int start, int end) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        audioInputStream.skip(start);
        AudioFormat af = audioInputStream.getFormat();
        int fullSize = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
        System.out.println("full size " + fullSize);
        int size = end - start;//(int) (af.getFrameSize() * audioInputStream.getFrameLength());
        byte[] audio = new byte[fullSize];
        DataLine.Info info = new DataLine.Info(Clip.class, af, size);
        audioInputStream.read(audio, 0, fullSize);

        System.out.println("audio size =" + audio.length);


        byte[] audioSample;
        audioSample = Arrays.copyOfRange(audio, start, end);
        afs.add(af);
        sizes.add(audioSample.length);
        infos.add(info);
        System.out.println("sample size =" + audioSample.length);
        audios.add(audioSample);

        return num++;
    }

    /**
     * same as {@link #addClip(String)} but either shrink or enlarge according to the sampleSize value...
     *
     * @param filePath
     * @param sampleSize
     * @return the id of the new sample
     */
    public int addClip(String filePath, int sampleSize) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        AudioFormat af = audioInputStream.getFormat();
        int fullSize = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
        System.out.println("full size " + fullSize);
        int size = sampleSize;//(int) (af.getFrameSize() * audioInputStream.getFrameLength());
        byte[] audio = new byte[size];
        DataLine.Info info = new DataLine.Info(Clip.class, af, size);
        audioInputStream.read(audio, 0, Math.min(size, fullSize));

        System.out.println("audio size =" + audio.length);

        afs.add(af);
        sizes.add(audio.length);
        infos.add(info);
        System.out.println("sample size =" + audio.length);
        audios.add(audio);

        return num++;
    }

//    /**
//     * Warning: does not work ! (only with similar files which is not useful :-/
//     * @param s1
//     * @param s2
//     * @return
//     */
//    public int addClipFromClipSum(int s1, int s2) {
//
//    	int bsize1 = sizes.get(s1);
//    	int bsize2 = sizes.get(s2);
//    	int bsize = Math.max(bsize1, bsize2);
//        AudioFormat af = afs.get(s1); //arbitrarily they should be the same anyway.
//
//        int[] sampleS1 = AudioUtils.convertByteArray(audios.get(s1), af);
//        int[] sampleS2 = AudioUtils.convertByteArray(audios.get(s2), af);
//        int ssize1 = sampleS1.length;
//    	int ssize2 = sampleS2.length;
//    	int ssize = Math.max(ssize1, ssize2);
//        int[] summedSamples = new int[ssize];
//
//        for(int i = 0; i < ssize; i++) {
//        	if (i >= ssize1) {
//        		System.arraycopy(sampleS2, i, summedSamples, i, ssize-i);
//        		break;
//        	}
//        	if (i >= ssize2) {
//        		System.arraycopy(sampleS1, i, summedSamples, i, ssize-i);
//        		break;
//        	}
//        	summedSamples[i] = (sampleS1[i]+sampleS2[i])/2;
//        }
//        byte[] res = AudioUtils.convertIntArray(summedSamples, af);
//        DataLine.Info info = new DataLine.Info(Clip.class, afs.get(s1), bsize);
//
//
//        afs.add(af);
//        sizes.add(new Integer(res.length));
//        infos.add(info);
//        audios.add(res);
//
//        return num++;
//    }


    /**
     * Concatenated saple s1 to sample s2, separated by a silence of size inBetweenSilenceSize
     * Warning: works only if formats are the very same.
     *
     * @param s1
     * @param s2
     * @param inBetweenSilenceSize
     * @return the id of the new sample
     */
    public int addClipFromClipConcat(int s1, int s2, int inBetweenSilenceSize) {

        int size1 = sizes.get(s1);
        int size2 = sizes.get(s2);
        int size = size1 + size2 + inBetweenSilenceSize;
        byte[] res = new byte[size];

        System.arraycopy(audios.get(s1), 0, res, 0, size1);
        System.arraycopy(audios.get(s2), 0, res, size1 + inBetweenSilenceSize, size2);

        AudioFormat af = afs.get(s1); //arbitrarily they should be the same anyway.
        DataLine.Info info = new DataLine.Info(Clip.class, afs.get(s1), size);


        afs.add(af);
        sizes.add(res.length);
        infos.add(info);
        audios.add(res);

        return num++;
    }

    /**
     * add a silence of size offset to the begin of sample with id (index)
     *
     * @param index
     * @param offset (must be even !)
     */
    public void addOffset(int index, int offset) {
        int size = sizes.get(index) + offset;
        sizes.set(index, size);
        byte[] audio = audios.get(index);
        byte[] newAudio = new byte[size];
        System.arraycopy(audio, 0, newAudio, offset, audio.length);
        audios.set(index, newAudio);
    }

    /**
     * add a silence of size offset to the end of sample with id (index)
     *
     * @param index
     * @param silence (must be even !)
     */
    public void addEndSilence(int index, int silence) {
        int size = sizes.get(index) + silence;
        sizes.set(index, size);
        byte[] audio = audios.get(index);
        byte[] newAudio = new byte[size];
        System.arraycopy(audio, 0, newAudio, 0, audio.length);
        audios.set(index, newAudio);
    }


    /**
     * non blocking play of the sound with id 'x'
     *
     * @param x
     * @param loop
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public void playSound(int x, int loop) throws Exception {
        if (x > num) {
            System.out.println("playSound: sample nr[" + x + "] is not available");
        } else {
            Clip clip = (Clip) AudioSystem.getLine(infos.elementAt(x));
            clip.open(afs.elementAt(x), audios.elementAt(x), 0, sizes.elementAt(x));
            clip.loop(loop);
        }
    }


    public static void main(String[] args) throws Exception {
        SoundManager sou = new SoundManager();
        int pluck = sou.addClip("/usr/lib64/libreoffice/share/gallery/sounds/pluck.wav");
        int spaceEnd = sou.addClip("/usr/lib64/libreoffice/share/gallery/sounds/space.wav", 44_000, 64_000);
        System.out.println("------------spaceEnd :" + sou.infos.get(spaceEnd));
        sou.addEndSilence(spaceEnd, 80_000);
        int space = sou.addClip("/usr/lib64/libreoffice/share/gallery/sounds/space.wav");
        int kick = sou.addClip("wavs/Toms 3 040.wav", 0, 10_000);
        System.out.println("------------kick :" + sou.infos.get(kick));
        int kick2 = sou.addClip("wavs/Toms 3 040.wav", 0, 90_000);
        sou.addOffset(kick2, 10_000);
        System.out.println("------------kick :" + sou.infos.get(kick));
        int clave = sou.addClip("wavs/Toms 3 072.wav", 0, 10_000);
        System.out.println("------------clave :" + sou.infos.get(clave));
        int bongo = sou.addClip("wavs/Bongos 060.wav", 10_000);
        System.out.println("------------clap :" + sou.infos.get(bongo));
        int clave2 = sou.addClip("wavs/Toms 3 072.wav", 20_000);
        sou.addOffset(clave2, 30_000);

        int bongo2 = sou.addClip("wavs/Bongos 061.wav", 60_000);
        System.out.println("------------clap :" + sou.infos.get(bongo2));
        sou.addOffset(bongo2, 6_666);

        int bongoLong = sou.addClip("wavs/Bongos 060.wav", 50_000);
        System.out.println("------------clap :" + sou.infos.get(bongoLong));


        sou.playSound(bongoLong, 40);
        sou.playSound(kick2, 20);
        sou.playSound(bongo2, 30);
        sou.playSound(spaceEnd, 10); //other frequency !

//    	Thread.sleep(8000);
//
//    	int concatenated = sou.addClipFromClipConcat(kick, spaceEnd, 40_000); //warning, concat two deferent frequencies...
//    	int concatenated2 = sou.addClipFromClipConcat(clave, bongo, 30_000);   //warning, concat two deferent frequencies...
//    	sou.playSound(concatenated, 5);
//    	sou.playSound(concatenated2, 6);


    }

}
