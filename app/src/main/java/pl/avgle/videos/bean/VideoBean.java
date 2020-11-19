package pl.avgle.videos.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoBean {

    private boolean success;
    private ResponseBean response;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {

        private boolean has_more;
        private int total_videos;
        private int current_offset;
        private int limit;
        private List<VideosBean> videos;

        public boolean isHas_more() {
            return has_more;
        }

        public void setHas_more(boolean has_more) {
            this.has_more = has_more;
        }

        public int getTotal_videos() {
            return total_videos;
        }

        public void setTotal_videos(int total_videos) {
            this.total_videos = total_videos;
        }

        public int getCurrent_offset() {
            return current_offset;
        }

        public void setCurrent_offset(int current_offset) {
            this.current_offset = current_offset;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public List<VideosBean> getVideos() {
            return videos;
        }

        public void setVideos(List<VideosBean> videos) {
            this.videos = videos;
        }

        public static class VideosBean {
            /**
             * title : 健身誘惑系列5部合集中文字幕 - 2
             * keyword : 中文
             * channel : 1
             * duration : 7791.05
             * framerate : 29.97
             * hd : true
             * addtime : 1541094865
             * viewnumber : 10043
             * likes : 5
             * dislikes : 3
             * video_url : https://avgle.com/video/227455/健身誘惑系列5部合集中文字幕-2
             * embedded_url : https://avgle.com/embed/fd44e128c0c9218584c4
             * preview_url : https://static-clst.avgle.com/videos/tmb7/227455/1.jpg
             * preview_video_url : https://static-clst.avgle.com/videos/tmb7/227455/preview.mp4
             * private : false
             * vid : 227455
             * uid : 479208
             */

            private String title;
            private String keyword;
            private String channel;
            private double duration;
            private double framerate;
            private boolean hd;
            private int addtime;
            private int viewnumber;
            private int likes;
            private int dislikes;
            private String video_url;
            private String embedded_url;
            private String preview_url;
            private String preview_video_url;
            @SerializedName("private")
            private boolean privateX;
            private String vid;
            private String uid;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public double getDuration() {
                return duration;
            }

            public void setDuration(double duration) {
                this.duration = duration;
            }

            public double getFramerate() {
                return framerate;
            }

            public void setFramerate(double framerate) {
                this.framerate = framerate;
            }

            public boolean isHd() {
                return hd;
            }

            public void setHd(boolean hd) {
                this.hd = hd;
            }

            public int getAddtime() {
                return addtime;
            }

            public void setAddtime(int addtime) {
                this.addtime = addtime;
            }

            public int getViewnumber() {
                return viewnumber;
            }

            public void setViewnumber(int viewnumber) {
                this.viewnumber = viewnumber;
            }

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            public int getDislikes() {
                return dislikes;
            }

            public void setDislikes(int dislikes) {
                this.dislikes = dislikes;
            }

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public String getEmbedded_url() {
                return embedded_url;
            }

            public void setEmbedded_url(String embedded_url) {
                this.embedded_url = embedded_url;
            }

            public String getPreview_url() {
                return preview_url;
            }

            public void setPreview_url(String preview_url) {
                this.preview_url = preview_url;
            }

            public String getPreview_video_url() {
                return preview_video_url;
            }

            public void setPreview_video_url(String preview_video_url) {
                this.preview_video_url = preview_video_url;
            }

            public boolean isPrivateX() {
                return privateX;
            }

            public void setPrivateX(boolean privateX) {
                this.privateX = privateX;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}
