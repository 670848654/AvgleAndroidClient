package pl.avgle.videos.bean;

public class EventState {

    private int state; // 0:channel 1:tag 2:videos

    public EventState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
