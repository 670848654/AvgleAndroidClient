package pl.avgle.videos.bean;

public class EventState {

    private int state; // 0:channel 1:tag 2:videos

    private boolean isPortrait; // true:portrait false:landscape

    public EventState(int state) {
        this.state = state;
    }

    public EventState(boolean isPortrait) {
        this.isPortrait = isPortrait;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isPortrait() {
        return isPortrait;
    }

    public void setPortrait(boolean portrait) {
        isPortrait = portrait;
    }
}
