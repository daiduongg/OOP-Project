package uet.cs.dictionaryfx.dictionary.model;


import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.w3c.dom.Text;

public class TextToSpeech {
    private String voiceName = "kevin16";
    private VoiceManager freeVM;
    private Voice voice;

    public TextToSpeech() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice(voiceName);
        if (voice != null) {
            voice.allocate();// Allocating Voice
            try {
                // voice.setRate(110);
                //voice.setPitch(110);
                //voice.setVolume(3);
                voice.setRate(150);
                voice.setPitch(100);
                voice.setVolume(0.8f);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }

    public void SpeakText(String words) {
        voice.speak(words);
    }

    public static void main(String[] args) {
        TextToSpeech ttp = new TextToSpeech();
        ttp.SpeakText("Hello im Peaky Blinders");
        ttp.SpeakText("Shut the fuck up");
    }
}



