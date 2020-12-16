package pl.avgle.videos.bean;

public class ChangeState {

    private boolean isPortrait;

    public ChangeState(boolean isPortrait) {
        this.isPortrait = isPortrait;
    }

    public boolean isPortrait() {
        return isPortrait;
    }

    public void setPortrait(boolean portrait) {
        isPortrait = portrait;
    }
}
