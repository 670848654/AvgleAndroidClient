package pl.avgle.videos.bean;

import java.util.List;

public class TagsBean {

    /**
     * success : true
     * response : {"has_more":true,"total_collections":120,"current_offset":0,"limit":50,"collections":[{"id":"1","title":"三上悠亜","keyword":"三上悠亜","cover_url":"https://static-clst.avgle.com/videos/tmb/19944/1.jpg","total_views":49986100,"video_count":144,"collection_url":"https://avgle.com/c/三上悠亜"},{"id":"2","title":"高橋しょう子","keyword":"高橋しょう子","cover_url":"https://static-clst.avgle.com/videos/tmb/236/1.jpg","total_views":26944034,"video_count":70,"collection_url":"https://avgle.com/c/高橋しょう子"},{"id":"3","title":"波多野結衣","keyword":"波多野結衣","cover_url":"https://static-clst.avgle.com/videos/tmb/2343/1.jpg","total_views":58887420,"video_count":830,"collection_url":"https://avgle.com/c/波多野結衣"},{"id":"4","title":"橋本ありな","keyword":"橋本ありな","cover_url":"https://static-clst.avgle.com/videos/tmb/918/1.jpg","total_views":24897124,"video_count":113,"collection_url":"https://avgle.com/c/橋本ありな"},{"id":"5","title":"椎名そら","keyword":"椎名そら","cover_url":"https://static-clst.avgle.com/videos/tmb/3612/1.jpg","total_views":29589613,"video_count":293,"collection_url":"https://avgle.com/c/椎名そら"},{"id":"6","title":"上原亜衣","keyword":"上原亜衣","cover_url":"https://static-clst.avgle.com/videos/tmb/1289/1.jpg","total_views":32629581,"video_count":274,"collection_url":"https://avgle.com/c/上原亜衣"},{"id":"7","title":"丘咲エミリ","keyword":"丘咲エミリ","cover_url":"https://static-clst.avgle.com/videos/tmb/11099/1.jpg","total_views":6682715,"video_count":87,"collection_url":"https://avgle.com/c/丘咲エミリ"},{"id":"8","title":"大槻ひびき","keyword":"大槻ひびき","cover_url":"https://static-clst.avgle.com/videos/tmb/12338/1.jpg","total_views":24942954,"video_count":337,"collection_url":"https://avgle.com/c/大槻ひびき"},{"id":"9","title":"凰かなめ","keyword":"凰かなめ","cover_url":"https://static-clst.avgle.com/videos/tmb/243/1.jpg","total_views":10335022,"video_count":45,"collection_url":"https://avgle.com/c/凰かなめ"},{"id":"10","title":"愛音まりあ","keyword":"愛音まりあ","cover_url":"https://static-clst.avgle.com/videos/tmb/3016/1.jpg","total_views":11853448,"video_count":65,"collection_url":"https://avgle.com/c/愛音まりあ"},{"id":"11","title":"栄川乃亜","keyword":"栄川乃亜","cover_url":"https://static-clst.avgle.com/videos/tmb/5700/1.jpg","total_views":15960404,"video_count":237,"collection_url":"https://avgle.com/c/栄川乃亜"},{"id":"12","title":"佐々木あき","keyword":"佐々木あき","cover_url":"https://static-clst.avgle.com/videos/tmb/3516/1.jpg","total_views":31719194,"video_count":474,"collection_url":"https://avgle.com/c/佐々木あき"},{"id":"13","title":"篠田あゆみ","keyword":"篠田あゆみ","cover_url":"https://static-clst.avgle.com/videos/tmb/997/1.jpg","total_views":19880088,"video_count":318,"collection_url":"https://avgle.com/c/篠田あゆみ"},{"id":"14","title":"有賀ゆあ","keyword":"有賀ゆあ","cover_url":"https://static-clst.avgle.com/videos/tmb/989/1.jpg","total_views":5045270,"video_count":43,"collection_url":"https://avgle.com/c/有賀ゆあ"},{"id":"15","title":"水野朝陽","keyword":"水野朝陽","cover_url":"https://static-clst.avgle.com/videos/tmb/3570/1.jpg","total_views":31430571,"video_count":424,"collection_url":"https://avgle.com/c/水野朝陽"},{"id":"16","title":"吉沢明歩","keyword":"吉沢明歩","cover_url":"https://static-clst.avgle.com/videos/tmb/2617/1.jpg","total_views":21116690,"video_count":272,"collection_url":"https://avgle.com/c/吉沢明歩"},{"id":"17","title":"紗倉まな","keyword":"紗倉まな","cover_url":"https://static-clst.avgle.com/videos/tmb/3823/1.jpg","total_views":16045764,"video_count":89,"collection_url":"https://avgle.com/c/紗倉まな"},{"id":"18","title":"姫川ゆうな","keyword":"姫川ゆうな","cover_url":"https://static-clst.avgle.com/videos/tmb/1914/1.jpg","total_views":16926775,"video_count":145,"collection_url":"https://avgle.com/c/姫川ゆうな"},{"id":"19","title":"西川ゆい","keyword":"西川ゆい","cover_url":"https://static-clst.avgle.com/videos/tmb/1926/1.jpg","total_views":6025160,"video_count":85,"collection_url":"https://avgle.com/c/西川ゆい"},{"id":"20","title":"AIKA","keyword":"AIKA","cover_url":"https://static-clst.avgle.com/videos/tmb/2820/1.jpg","total_views":25607475,"video_count":367,"collection_url":"https://avgle.com/c/AIKA"},{"id":"21","title":"小西悠","keyword":"小西悠","cover_url":"https://static-clst.avgle.com/videos/tmb/4225/1.jpg","total_views":3971509,"video_count":84,"collection_url":"https://avgle.com/c/小西悠"},{"id":"22","title":"香椎りあ","keyword":"香椎りあ","cover_url":"https://static-clst.avgle.com/videos/tmb/3389/1.jpg","total_views":8770295,"video_count":119,"collection_url":"https://avgle.com/c/香椎りあ"},{"id":"23","title":"マンコ図鑑","keyword":"マンコ図鑑","cover_url":"https://static-clst.avgle.com/videos/tmb/1167/1.jpg","total_views":11147087,"video_count":160,"collection_url":"https://avgle.com/c/マンコ図鑑"},{"id":"24","title":"ミラー号","keyword":"ミラー号","cover_url":"https://static-clst.avgle.com/videos/tmb/19016/1.jpg","total_views":38027869,"video_count":345,"collection_url":"https://avgle.com/c/ミラー号"},{"id":"25","title":"麻生希","keyword":"麻生希","cover_url":"https://static-clst.avgle.com/videos/tmb/1069/1.jpg","total_views":7868924,"video_count":94,"collection_url":"https://avgle.com/c/麻生希"},{"id":"26","title":"葉山瞳","keyword":"葉山瞳","cover_url":"https://static-clst.avgle.com/videos/tmb/994/1.jpg","total_views":953424,"video_count":15,"collection_url":"https://avgle.com/c/葉山瞳"},{"id":"27","title":"飛鳥りん","keyword":"飛鳥りん","cover_url":"https://static-clst.avgle.com/videos/tmb/3086/1.jpg","total_views":3475503,"video_count":45,"collection_url":"https://avgle.com/c/飛鳥りん"},{"id":"28","title":"RION","keyword":"RION","cover_url":"https://static-clst.avgle.com/videos/tmb/5974/1.jpg","total_views":39703777,"video_count":183,"collection_url":"https://avgle.com/c/RION"},{"id":"29","title":"椎名ゆな","keyword":"椎名ゆな","cover_url":"https://static-clst.avgle.com/videos/tmb/22307/1.jpg","total_views":6970499,"video_count":146,"collection_url":"https://avgle.com/c/椎名ゆな"},{"id":"30","title":"JULIA","keyword":"JULIA","cover_url":"https://static-clst.avgle.com/videos/tmb/3676/1.jpg","total_views":25828871,"video_count":450,"collection_url":"https://avgle.com/c/JULIA"},{"id":"31","title":"天海つばさ","keyword":"天海つばさ","cover_url":"https://static-clst.avgle.com/videos/tmb/4323/1.jpg","total_views":8677456,"video_count":155,"collection_url":"https://avgle.com/c/天海つばさ"},{"id":"32","title":"あやみ旬果","keyword":"あやみ旬果","cover_url":"https://static-clst.avgle.com/videos/tmb/3858/1.jpg","total_views":19692243,"video_count":156,"collection_url":"https://avgle.com/c/あやみ旬果"},{"id":"33","title":"若菜奈央","keyword":"若菜奈央","cover_url":"https://static-clst.avgle.com/videos/tmb/15113/1.jpg","total_views":14916319,"video_count":176,"collection_url":"https://avgle.com/c/若菜奈央"},{"id":"34","title":"みほの","keyword":"みほの","cover_url":"https://static-clst.avgle.com/videos/tmb/1052/1.jpg","total_views":5775004,"video_count":71,"collection_url":"https://avgle.com/c/みほの"},{"id":"35","title":"碧しの","keyword":"碧しの","cover_url":"https://static-clst.avgle.com/videos/tmb/2223/1.jpg","total_views":10794505,"video_count":146,"collection_url":"https://avgle.com/c/碧しの"},{"id":"36","title":"麻里梨夏","keyword":"麻里梨夏","cover_url":"https://static-clst.avgle.com/videos/tmb/19973/1.jpg","total_views":23002349,"video_count":373,"collection_url":"https://avgle.com/c/麻里梨夏"},{"id":"37","title":"北山かんな","keyword":"北山かんな","cover_url":"https://static-clst.avgle.com/videos/tmb/1000/1.jpg","total_views":4769812,"video_count":62,"collection_url":"https://avgle.com/c/北山かんな"},{"id":"38","title":"篠崎みお","keyword":"篠崎みお","cover_url":"https://static-clst.avgle.com/videos/tmb/3364/1.jpg","total_views":3752629,"video_count":60,"collection_url":"https://avgle.com/c/篠崎みお"},{"id":"39","title":"希崎ジェシカ","keyword":"希崎ジェシカ","cover_url":"https://static-clst.avgle.com/videos/tmb/4367/1.jpg","total_views":12394309,"video_count":160,"collection_url":"https://avgle.com/c/希崎ジェシカ"},{"id":"40","title":"宮崎あや","keyword":"宮崎あや","cover_url":"https://static-clst.avgle.com/videos/tmb/5852/1.jpg","total_views":15458321,"video_count":216,"collection_url":"https://avgle.com/c/宮崎あや"},{"id":"41","title":"S-Cute","keyword":"S-Cute","cover_url":"https://static-clst.avgle.com/videos/tmb/5111/1.jpg","total_views":29285022,"video_count":1089,"collection_url":"https://avgle.com/c/S-Cute"},{"id":"42","title":"259LUXU","keyword":"259LUXU","cover_url":"https://static-clst.avgle.com/videos/tmb/22321/1.jpg","total_views":51891484,"video_count":782,"collection_url":"https://avgle.com/c/259LUXU"},{"id":"43","title":"折原ほのか","keyword":"折原ほのか","cover_url":"https://static-clst.avgle.com/videos/tmb/8312/1.jpg","total_views":6113549,"video_count":81,"collection_url":"https://avgle.com/c/折原ほのか"},{"id":"44","title":"羽田真里","keyword":"羽田真里","cover_url":"https://static-clst.avgle.com/videos/tmb/1687/1.jpg","total_views":2308005,"video_count":23,"collection_url":"https://avgle.com/c/羽田真里"},{"id":"45","title":"小向美奈子","keyword":"小向美奈子","cover_url":"https://static-clst.avgle.com/videos/tmb/1913/1.jpg","total_views":5439616,"video_count":58,"collection_url":"https://avgle.com/c/小向美奈子"},{"id":"46","title":"二階堂ゆり","keyword":"二階堂ゆり","cover_url":"https://static-clst.avgle.com/videos/tmb/3161/1.jpg","total_views":8041021,"video_count":178,"collection_url":"https://avgle.com/c/二階堂ゆり"},{"id":"47","title":"三島奈津子","keyword":"三島奈津子","cover_url":"https://static-clst.avgle.com/videos/tmb/6012/1.jpg","total_views":11954023,"video_count":167,"collection_url":"https://avgle.com/c/三島奈津子"},{"id":"48","title":"江波りゅう","keyword":"江波りゅう","cover_url":"https://static-clst.avgle.com/videos/tmb/2069/1.jpg","total_views":5287637,"video_count":60,"collection_url":"https://avgle.com/c/江波りゅう"},{"id":"49","title":"青山未来","keyword":"青山未来","cover_url":"https://static-clst.avgle.com/videos/tmb/1065/1.jpg","total_views":976990,"video_count":22,"collection_url":"https://avgle.com/c/青山未来"},{"id":"50","title":"朝桐光","keyword":"朝桐光","cover_url":"https://static-clst.avgle.com/videos/tmb/1048/1.jpg","total_views":10892701,"video_count":130,"collection_url":"https://avgle.com/c/朝桐光"}]}
     */

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
        /**
         * has_more : true
         * total_collections : 120
         * current_offset : 0
         * limit : 50
         * collections : [{"id":"1","title":"三上悠亜","keyword":"三上悠亜","cover_url":"https://static-clst.avgle.com/videos/tmb/19944/1.jpg","total_views":49986100,"video_count":144,"collection_url":"https://avgle.com/c/三上悠亜"},{"id":"2","title":"高橋しょう子","keyword":"高橋しょう子","cover_url":"https://static-clst.avgle.com/videos/tmb/236/1.jpg","total_views":26944034,"video_count":70,"collection_url":"https://avgle.com/c/高橋しょう子"},{"id":"3","title":"波多野結衣","keyword":"波多野結衣","cover_url":"https://static-clst.avgle.com/videos/tmb/2343/1.jpg","total_views":58887420,"video_count":830,"collection_url":"https://avgle.com/c/波多野結衣"},{"id":"4","title":"橋本ありな","keyword":"橋本ありな","cover_url":"https://static-clst.avgle.com/videos/tmb/918/1.jpg","total_views":24897124,"video_count":113,"collection_url":"https://avgle.com/c/橋本ありな"},{"id":"5","title":"椎名そら","keyword":"椎名そら","cover_url":"https://static-clst.avgle.com/videos/tmb/3612/1.jpg","total_views":29589613,"video_count":293,"collection_url":"https://avgle.com/c/椎名そら"},{"id":"6","title":"上原亜衣","keyword":"上原亜衣","cover_url":"https://static-clst.avgle.com/videos/tmb/1289/1.jpg","total_views":32629581,"video_count":274,"collection_url":"https://avgle.com/c/上原亜衣"},{"id":"7","title":"丘咲エミリ","keyword":"丘咲エミリ","cover_url":"https://static-clst.avgle.com/videos/tmb/11099/1.jpg","total_views":6682715,"video_count":87,"collection_url":"https://avgle.com/c/丘咲エミリ"},{"id":"8","title":"大槻ひびき","keyword":"大槻ひびき","cover_url":"https://static-clst.avgle.com/videos/tmb/12338/1.jpg","total_views":24942954,"video_count":337,"collection_url":"https://avgle.com/c/大槻ひびき"},{"id":"9","title":"凰かなめ","keyword":"凰かなめ","cover_url":"https://static-clst.avgle.com/videos/tmb/243/1.jpg","total_views":10335022,"video_count":45,"collection_url":"https://avgle.com/c/凰かなめ"},{"id":"10","title":"愛音まりあ","keyword":"愛音まりあ","cover_url":"https://static-clst.avgle.com/videos/tmb/3016/1.jpg","total_views":11853448,"video_count":65,"collection_url":"https://avgle.com/c/愛音まりあ"},{"id":"11","title":"栄川乃亜","keyword":"栄川乃亜","cover_url":"https://static-clst.avgle.com/videos/tmb/5700/1.jpg","total_views":15960404,"video_count":237,"collection_url":"https://avgle.com/c/栄川乃亜"},{"id":"12","title":"佐々木あき","keyword":"佐々木あき","cover_url":"https://static-clst.avgle.com/videos/tmb/3516/1.jpg","total_views":31719194,"video_count":474,"collection_url":"https://avgle.com/c/佐々木あき"},{"id":"13","title":"篠田あゆみ","keyword":"篠田あゆみ","cover_url":"https://static-clst.avgle.com/videos/tmb/997/1.jpg","total_views":19880088,"video_count":318,"collection_url":"https://avgle.com/c/篠田あゆみ"},{"id":"14","title":"有賀ゆあ","keyword":"有賀ゆあ","cover_url":"https://static-clst.avgle.com/videos/tmb/989/1.jpg","total_views":5045270,"video_count":43,"collection_url":"https://avgle.com/c/有賀ゆあ"},{"id":"15","title":"水野朝陽","keyword":"水野朝陽","cover_url":"https://static-clst.avgle.com/videos/tmb/3570/1.jpg","total_views":31430571,"video_count":424,"collection_url":"https://avgle.com/c/水野朝陽"},{"id":"16","title":"吉沢明歩","keyword":"吉沢明歩","cover_url":"https://static-clst.avgle.com/videos/tmb/2617/1.jpg","total_views":21116690,"video_count":272,"collection_url":"https://avgle.com/c/吉沢明歩"},{"id":"17","title":"紗倉まな","keyword":"紗倉まな","cover_url":"https://static-clst.avgle.com/videos/tmb/3823/1.jpg","total_views":16045764,"video_count":89,"collection_url":"https://avgle.com/c/紗倉まな"},{"id":"18","title":"姫川ゆうな","keyword":"姫川ゆうな","cover_url":"https://static-clst.avgle.com/videos/tmb/1914/1.jpg","total_views":16926775,"video_count":145,"collection_url":"https://avgle.com/c/姫川ゆうな"},{"id":"19","title":"西川ゆい","keyword":"西川ゆい","cover_url":"https://static-clst.avgle.com/videos/tmb/1926/1.jpg","total_views":6025160,"video_count":85,"collection_url":"https://avgle.com/c/西川ゆい"},{"id":"20","title":"AIKA","keyword":"AIKA","cover_url":"https://static-clst.avgle.com/videos/tmb/2820/1.jpg","total_views":25607475,"video_count":367,"collection_url":"https://avgle.com/c/AIKA"},{"id":"21","title":"小西悠","keyword":"小西悠","cover_url":"https://static-clst.avgle.com/videos/tmb/4225/1.jpg","total_views":3971509,"video_count":84,"collection_url":"https://avgle.com/c/小西悠"},{"id":"22","title":"香椎りあ","keyword":"香椎りあ","cover_url":"https://static-clst.avgle.com/videos/tmb/3389/1.jpg","total_views":8770295,"video_count":119,"collection_url":"https://avgle.com/c/香椎りあ"},{"id":"23","title":"マンコ図鑑","keyword":"マンコ図鑑","cover_url":"https://static-clst.avgle.com/videos/tmb/1167/1.jpg","total_views":11147087,"video_count":160,"collection_url":"https://avgle.com/c/マンコ図鑑"},{"id":"24","title":"ミラー号","keyword":"ミラー号","cover_url":"https://static-clst.avgle.com/videos/tmb/19016/1.jpg","total_views":38027869,"video_count":345,"collection_url":"https://avgle.com/c/ミラー号"},{"id":"25","title":"麻生希","keyword":"麻生希","cover_url":"https://static-clst.avgle.com/videos/tmb/1069/1.jpg","total_views":7868924,"video_count":94,"collection_url":"https://avgle.com/c/麻生希"},{"id":"26","title":"葉山瞳","keyword":"葉山瞳","cover_url":"https://static-clst.avgle.com/videos/tmb/994/1.jpg","total_views":953424,"video_count":15,"collection_url":"https://avgle.com/c/葉山瞳"},{"id":"27","title":"飛鳥りん","keyword":"飛鳥りん","cover_url":"https://static-clst.avgle.com/videos/tmb/3086/1.jpg","total_views":3475503,"video_count":45,"collection_url":"https://avgle.com/c/飛鳥りん"},{"id":"28","title":"RION","keyword":"RION","cover_url":"https://static-clst.avgle.com/videos/tmb/5974/1.jpg","total_views":39703777,"video_count":183,"collection_url":"https://avgle.com/c/RION"},{"id":"29","title":"椎名ゆな","keyword":"椎名ゆな","cover_url":"https://static-clst.avgle.com/videos/tmb/22307/1.jpg","total_views":6970499,"video_count":146,"collection_url":"https://avgle.com/c/椎名ゆな"},{"id":"30","title":"JULIA","keyword":"JULIA","cover_url":"https://static-clst.avgle.com/videos/tmb/3676/1.jpg","total_views":25828871,"video_count":450,"collection_url":"https://avgle.com/c/JULIA"},{"id":"31","title":"天海つばさ","keyword":"天海つばさ","cover_url":"https://static-clst.avgle.com/videos/tmb/4323/1.jpg","total_views":8677456,"video_count":155,"collection_url":"https://avgle.com/c/天海つばさ"},{"id":"32","title":"あやみ旬果","keyword":"あやみ旬果","cover_url":"https://static-clst.avgle.com/videos/tmb/3858/1.jpg","total_views":19692243,"video_count":156,"collection_url":"https://avgle.com/c/あやみ旬果"},{"id":"33","title":"若菜奈央","keyword":"若菜奈央","cover_url":"https://static-clst.avgle.com/videos/tmb/15113/1.jpg","total_views":14916319,"video_count":176,"collection_url":"https://avgle.com/c/若菜奈央"},{"id":"34","title":"みほの","keyword":"みほの","cover_url":"https://static-clst.avgle.com/videos/tmb/1052/1.jpg","total_views":5775004,"video_count":71,"collection_url":"https://avgle.com/c/みほの"},{"id":"35","title":"碧しの","keyword":"碧しの","cover_url":"https://static-clst.avgle.com/videos/tmb/2223/1.jpg","total_views":10794505,"video_count":146,"collection_url":"https://avgle.com/c/碧しの"},{"id":"36","title":"麻里梨夏","keyword":"麻里梨夏","cover_url":"https://static-clst.avgle.com/videos/tmb/19973/1.jpg","total_views":23002349,"video_count":373,"collection_url":"https://avgle.com/c/麻里梨夏"},{"id":"37","title":"北山かんな","keyword":"北山かんな","cover_url":"https://static-clst.avgle.com/videos/tmb/1000/1.jpg","total_views":4769812,"video_count":62,"collection_url":"https://avgle.com/c/北山かんな"},{"id":"38","title":"篠崎みお","keyword":"篠崎みお","cover_url":"https://static-clst.avgle.com/videos/tmb/3364/1.jpg","total_views":3752629,"video_count":60,"collection_url":"https://avgle.com/c/篠崎みお"},{"id":"39","title":"希崎ジェシカ","keyword":"希崎ジェシカ","cover_url":"https://static-clst.avgle.com/videos/tmb/4367/1.jpg","total_views":12394309,"video_count":160,"collection_url":"https://avgle.com/c/希崎ジェシカ"},{"id":"40","title":"宮崎あや","keyword":"宮崎あや","cover_url":"https://static-clst.avgle.com/videos/tmb/5852/1.jpg","total_views":15458321,"video_count":216,"collection_url":"https://avgle.com/c/宮崎あや"},{"id":"41","title":"S-Cute","keyword":"S-Cute","cover_url":"https://static-clst.avgle.com/videos/tmb/5111/1.jpg","total_views":29285022,"video_count":1089,"collection_url":"https://avgle.com/c/S-Cute"},{"id":"42","title":"259LUXU","keyword":"259LUXU","cover_url":"https://static-clst.avgle.com/videos/tmb/22321/1.jpg","total_views":51891484,"video_count":782,"collection_url":"https://avgle.com/c/259LUXU"},{"id":"43","title":"折原ほのか","keyword":"折原ほのか","cover_url":"https://static-clst.avgle.com/videos/tmb/8312/1.jpg","total_views":6113549,"video_count":81,"collection_url":"https://avgle.com/c/折原ほのか"},{"id":"44","title":"羽田真里","keyword":"羽田真里","cover_url":"https://static-clst.avgle.com/videos/tmb/1687/1.jpg","total_views":2308005,"video_count":23,"collection_url":"https://avgle.com/c/羽田真里"},{"id":"45","title":"小向美奈子","keyword":"小向美奈子","cover_url":"https://static-clst.avgle.com/videos/tmb/1913/1.jpg","total_views":5439616,"video_count":58,"collection_url":"https://avgle.com/c/小向美奈子"},{"id":"46","title":"二階堂ゆり","keyword":"二階堂ゆり","cover_url":"https://static-clst.avgle.com/videos/tmb/3161/1.jpg","total_views":8041021,"video_count":178,"collection_url":"https://avgle.com/c/二階堂ゆり"},{"id":"47","title":"三島奈津子","keyword":"三島奈津子","cover_url":"https://static-clst.avgle.com/videos/tmb/6012/1.jpg","total_views":11954023,"video_count":167,"collection_url":"https://avgle.com/c/三島奈津子"},{"id":"48","title":"江波りゅう","keyword":"江波りゅう","cover_url":"https://static-clst.avgle.com/videos/tmb/2069/1.jpg","total_views":5287637,"video_count":60,"collection_url":"https://avgle.com/c/江波りゅう"},{"id":"49","title":"青山未来","keyword":"青山未来","cover_url":"https://static-clst.avgle.com/videos/tmb/1065/1.jpg","total_views":976990,"video_count":22,"collection_url":"https://avgle.com/c/青山未来"},{"id":"50","title":"朝桐光","keyword":"朝桐光","cover_url":"https://static-clst.avgle.com/videos/tmb/1048/1.jpg","total_views":10892701,"video_count":130,"collection_url":"https://avgle.com/c/朝桐光"}]
         */

        private boolean has_more;
        private int total_collections;
        private int current_offset;
        private int limit;
        private List<CollectionsBean> collections;

        public boolean isHas_more() {
            return has_more;
        }

        public void setHas_more(boolean has_more) {
            this.has_more = has_more;
        }

        public int getTotal_collections() {
            return total_collections;
        }

        public void setTotal_collections(int total_collections) {
            this.total_collections = total_collections;
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

        public List<CollectionsBean> getCollections() {
            return collections;
        }

        public void setCollections(List<CollectionsBean> collections) {
            this.collections = collections;
        }

        public static class CollectionsBean {
            /**
             * id : 1
             * title : 三上悠亜
             * keyword : 三上悠亜
             * cover_url : https://static-clst.avgle.com/videos/tmb/19944/1.jpg
             * total_views : 49986100
             * video_count : 144
             * collection_url : https://avgle.com/c/三上悠亜
             */

            private String id;
            private String title;
            private String keyword;
            private String cover_url;
            private int total_views;
            private int video_count;
            private String collection_url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

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

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public int getTotal_views() {
                return total_views;
            }

            public void setTotal_views(int total_views) {
                this.total_views = total_views;
            }

            public int getVideo_count() {
                return video_count;
            }

            public void setVideo_count(int video_count) {
                this.video_count = video_count;
            }

            public String getCollection_url() {
                return collection_url;
            }

            public void setCollection_url(String collection_url) {
                this.collection_url = collection_url;
            }
        }
    }
}
