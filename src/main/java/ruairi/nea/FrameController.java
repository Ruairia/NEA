package ruairi.nea;



public class FrameController {
    private int currentIndex;
    private Image[] frames;

    public Image getCurrentFrame(double currentTime){
        return frames[currentIndex];
    }

}

//TO REWRITE