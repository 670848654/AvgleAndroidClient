package pl.avgle.videos.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.bean.VideoBean;

/**
 * 数据库工具
 */
public class DatabaseUtil {
    public static SQLiteDatabase db;
    public static String DB_PATH = Environment.getExternalStorageDirectory() + "/AvgleVideos/Database/AvgleDatabase.db";

    /**
     * 创建用户收藏tables
     */
    public static void CREATE_TABLES() {
        db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        db.execSQL("create table if not exists f_channel(id integer primary key autoincrement,f_chid text, f_name text, f_slug text, f_total_videos integer, f_shortname text, f_category_url text, f_cover_url text)");
        db.execSQL("create table if not exists f_tags(id integer primary key autoincrement,f_id text, f_title text, f_keyword text, f_cover_url text, f_total_views integer, f_video_count integer, f_collection_url text)");
        db.execSQL("create table if not exists f_videos(id integer primary key autoincrement,f_vid text, f_uid text, f_title text, f_keyword text, f_channel text, f_duration real, f_framerate real, f_hd integer, f_addtime integer, f_viewnumber integer, f_likes integer, f_dislikes integer, f_video_url text, f_embedded_url text, f_preview_url text, f_preview_video_url text)");
    }

    /**
     * 关闭数据库连接
     */
    public static void closeDB() {
        db.close();
    }

    /**
     * 添加频道
     *
     * @param bean
     */
    public static void addChannel(ChannelBean.ResponseBean.CategoriesBean bean) {
        db.execSQL("insert into f_channel values(?,?,?,?,?,?,?,?)",
                new Object[]{null,
                        bean.getCHID(),
                        bean.getName(),
                        bean.getSlug(),
                        bean.getTotal_videos(),
                        bean.getShortname(),
                        bean.getCategory_url(),
                        bean.getCover_url()});
    }

    /**
     * 删除频道
     *
     * @param chid
     */
    public static void deleteChannel(String chid) {
        db.execSQL("delete from f_channel where f_chid=?", new String[]{chid});
    }

    /**
     * 判断频道是否存在
     *
     * @param chid
     * @return
     */
    public static boolean checkChannel(String chid) {
        String Query = "select * from f_channel where f_chid =?";
        Cursor cursor = db.rawQuery(Query, new String[]{chid});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * 查询所有收藏的频道
     *
     * @return
     */
    public static List<ChannelBean.ResponseBean.CategoriesBean> queryAllChannel() {
        List<ChannelBean.ResponseBean.CategoriesBean> list = new ArrayList<>();
        Cursor c = db.rawQuery("select * from f_channel order by id desc", null);
        while (c.moveToNext()) {
            ChannelBean.ResponseBean.CategoriesBean bean = new ChannelBean.ResponseBean.CategoriesBean();
            bean.setCHID(c.getString(1));
            bean.setName(c.getString(2));
            bean.setSlug(c.getString(3));
            bean.setTotal_videos(c.getInt(4));
            bean.setShortname(c.getString(5));
            bean.setCategory_url(c.getString(6));
            bean.setCover_url(c.getString(7));
            list.add(bean);
        }
        c.close();
        return list;
    }

    /**
     * 查询所有收藏的频道CHID
     *
     * @return
     */
    public static List<String> queryAllChannelCHID() {
        List<String> list = new ArrayList<>();
        Cursor c = db.rawQuery("select * from f_channel order by id desc", null);
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        return list;
    }

    /**
     * 添加标签
     *
     * @param bean
     */
    public static void addTags(TagsBean.ResponseBean.CollectionsBean bean) {
        db.execSQL("insert into f_tags values(?,?,?,?,?,?,?,?)",
                new Object[]{null,
                        bean.getId(),
                        bean.getTitle(),
                        bean.getKeyword(),
                        bean.getCover_url(),
                        bean.getTotal_views(),
                        bean.getVideo_count(),
                        bean.getCollection_url()});
    }

    /**
     * 删除标签
     *
     * @param title
     */
    public static void deleteTag(String title) {
        db.execSQL("delete from f_tags where f_title=?", new String[]{title});
    }

    /**
     * 判断标签是否存在
     *
     * @param title
     * @return
     */
    public static boolean checkTag(String title) {
        String Query = "select * from f_tags where f_title =?";
        Cursor cursor = db.rawQuery(Query, new String[]{title});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * 查询所有收藏的标签
     *
     * @return
     */
    public static List<TagsBean.ResponseBean.CollectionsBean> queryAllTags() {
        List<TagsBean.ResponseBean.CollectionsBean> list = new ArrayList<>();
        Cursor c = db.rawQuery("select * from f_tags order by id desc", null);
        while (c.moveToNext()) {
            TagsBean.ResponseBean.CollectionsBean bean = new TagsBean.ResponseBean.CollectionsBean();
            bean.setId(c.getString(1));
            bean.setTitle(c.getString(2));
            bean.setKeyword(c.getString(3));
            bean.setCover_url(c.getString(4));
            bean.setTotal_views(c.getInt(5));
            bean.setVideo_count(c.getInt(6));
            bean.setCollection_url(c.getString(7));
            list.add(bean);
        }
        c.close();
        return list;
    }

    /**
     * 查询所有收藏的标签ID
     *
     * @return
     */
    public static List<String> queryAllTagIds() {
        List<String> list = new ArrayList<>();
        Cursor c = db.rawQuery("select * from f_tags order by id desc", null);
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        return list;
    }

    /**
     * 查询所有收藏的标签TITLE
     *
     * @return
     */
    public static List<String> queryAllTagTitles() {
        List<String> list = new ArrayList<>();
        Cursor c = db.rawQuery("select * from f_tags order by id desc", null);
        while (c.moveToNext()) {
            list.add(c.getString(2));
        }
        c.close();
        return list;
    }

    /**
     * 添加视频
     *
     * @param bean
     */
    public static void addVideo(VideoBean.ResponseBean.VideosBean bean) {

        db.execSQL("insert into f_videos values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{null,
                        bean.getVid(),
                        bean.getUid(),
                        bean.getTitle(),
                        bean.getKeyword(),
                        bean.getChannel(),
                        bean.getDuration(),
                        bean.getFramerate(),
                        bean.isHd() == true ? 1 : 0,
                        bean.getAddtime(),
                        bean.getViewnumber(),
                        bean.getLikes(),
                        bean.getDislikes(),
                        bean.getVideo_url(),
                        bean.getEmbedded_url(),
                        bean.getPreview_url(),
                        bean.getPreview_video_url()});
    }

    /**
     * 删除视频
     *
     * @param vid
     */
    public static void deleteVideo(String vid) {
        db.execSQL("delete from f_videos where f_vid=?", new String[]{vid});
    }

    /**
     * 判断视频是否存在
     *
     * @param vid
     * @return
     */
    public static boolean checkVideo(String vid) {
        String Query = "select * from f_videos where f_vid =?";
        Cursor cursor = db.rawQuery(Query, new String[]{vid});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * 查询所有收藏的视频
     *
     * @return
     */
    public static List<VideoBean.ResponseBean.VideosBean> queryAllVideos() {
        List<VideoBean.ResponseBean.VideosBean> list = new ArrayList<>();
        Cursor c = db.rawQuery("select * from f_videos order by id desc", null);
        while (c.moveToNext()) {
            VideoBean.ResponseBean.VideosBean bean = new VideoBean.ResponseBean.VideosBean();
            bean.setVid(c.getString(1));
            bean.setUid(c.getString(2));
            bean.setTitle(c.getString(3));
            bean.setKeyword(c.getString(4));
            bean.setChannel(c.getString(5));
            bean.setDuration(c.getDouble(6));
            bean.setFramerate(c.getDouble(7));
            bean.setHd(c.getInt(8) == 1 ? true : false);
            bean.setAddtime(c.getInt(9));
            bean.setViewnumber(c.getInt(10));
            bean.setLikes(c.getInt(11));
            bean.setDislikes(c.getInt(12));
            bean.setVideo_url(c.getString(13));
            bean.setEmbedded_url(c.getString(14));
            bean.setPreview_url(c.getString(15));
            bean.setPreview_video_url(c.getString(16));
            list.add(bean);
        }
        c.close();
        return list;
    }

    /**
     * 查询所有收藏的视频VID
     *
     * @return
     */
    public static List<String> queryAllVideoVIDs() {
        List<String> list = new ArrayList<>();
        Cursor c = db.rawQuery("select * from f_videos order by id desc", null);
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        return list;
    }
}
