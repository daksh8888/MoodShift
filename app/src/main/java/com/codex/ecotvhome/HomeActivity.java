package com.codex.ecotvhome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.*;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** Vivid, remote-first TV home inspired by the supplied Apple TV home-screen reference. */
public class HomeActivity extends Activity {
    private static final int COLUMNS=5;
    private static final String[] FAVORITE_PACKAGES={
            "com.google.android.youtube.tv","com.spotify.tv.android",
            "com.amazon.amazonvideo.livingroom","com.netflix.ninja","tv.willow"
    };
    private final ArrayList<AppItem> favorites=new ArrayList<>(),apps=new ArrayList<>();private final ArrayList<ImageView> favoriteImages=new ArrayList<>();private boolean dockMorningMode;
    private RecyclerView grid;private LinearLayout favoriteRow;private AppAdapter adapter;private FrameLayout contentArea,shelf;private boolean gridExpanded=false;
    private View dockGlassView,gridFocusGlassView;private ColorWash wallpaper;private boolean dockGlassPositioned=false,gridFocusGlassPositioned=false;
    private GradientDrawable tileIdle;
    private TextView clockTime,clockDate,clockGreeting,clockWeekend,clockFestival;private LinearLayout clockHeader,clockBlock;private View clockSpacer;private boolean clockOnRight=false;private int focusedPosition=0,favoritePosition=0;private boolean focusInFavorites=true;
    private final Handler clockHandler=new Handler(Looper.getMainLooper());
    private final PathInterpolator transitionEase=new PathInterpolator(.22f,.61f,.36f,1f);
    private final Runnable clockUpdater=new Runnable(){@Override public void run(){updateClock();clockHandler.postDelayed(this,30_000);}};

    // India-wide reference dates through 2031. Lunar observances can differ by region and local panchang.
    private static final Map<String,String> FESTIVAL_GREETINGS=createFestivalGreetings();
    private static Map<String,String> createFestivalGreetings(){
        HashMap<String,String> dates=new HashMap<>();
        String[][] entries={
                {"20260114","Happy Makar Sankranti & Pongal!"},{"20260123","Happy Vasant Panchami!"},{"20260215","Happy Maha Shivaratri!"},{"20260304","Happy Holi!"},{"20260319","Happy Ugadi & Gudi Padwa!"},{"20260326","Happy Rama Navami!"},{"20260401","Happy Hanuman Jayanti!"},{"20260419","Happy Akshaya Tritiya!"},{"20260716","Happy Rath Yatra!"},{"20260729","Happy Guru Purnima!"},{"20260827","Happy Raksha Bandhan!"},{"20260904","Happy Krishna Janmashtami!"},{"20260914","Happy Ganesh Chaturthi!"},{"20260826","Happy Onam!"},{"20261011","Happy Navratri!"},{"20261020","Happy Dussehra!"},{"20261106","Happy Dhanteras!"},{"20261108","Happy Diwali!!"},{"20261110","Happy Govardhan Puja!"},{"20261111","Happy Bhai Dooj!"},{"20261115","Happy Chhath Puja!"},
                {"20270115","Happy Makar Sankranti & Pongal!"},{"20270211","Happy Vasant Panchami!"},{"20270306","Happy Maha Shivaratri!"},{"20270322","Happy Holi!"},{"20270407","Happy Ugadi & Gudi Padwa!"},{"20270415","Happy Rama Navami!"},{"20270420","Happy Hanuman Jayanti!"},{"20270509","Happy Akshaya Tritiya!"},{"20270705","Happy Rath Yatra!"},{"20270718","Happy Guru Purnima!"},{"20270817","Happy Raksha Bandhan!"},{"20270825","Happy Krishna Janmashtami!"},{"20270904","Happy Ganesh Chaturthi!"},{"20270912","Happy Onam!"},{"20270930","Happy Navratri!"},{"20271009","Happy Dussehra!"},{"20271027","Happy Dhanteras!"},{"20271029","Happy Diwali!!"},{"20271030","Happy Govardhan Puja!"},{"20271031","Happy Bhai Dooj!"},
                {"20280114","Happy Makar Sankranti & Pongal!"},{"20280131","Happy Vasant Panchami!"},{"20280223","Happy Maha Shivaratri!"},{"20280311","Happy Holi!"},{"20280327","Happy Ugadi & Gudi Padwa!"},{"20280403","Happy Rama Navami!"},{"20280409","Happy Hanuman Jayanti!"},{"20280427","Happy Akshaya Tritiya!"},{"20280624","Happy Rath Yatra!"},{"20280706","Happy Guru Purnima!"},{"20280805","Happy Raksha Bandhan!"},{"20280813","Happy Krishna Janmashtami!"},{"20280823","Happy Ganesh Chaturthi!"},{"20280901","Happy Onam!"},{"20280919","Happy Navratri!"},{"20280927","Happy Dussehra!"},{"20281015","Happy Dhanteras!"},{"20281017","Happy Diwali!!"},{"20281018","Happy Govardhan Puja!"},{"20281019","Happy Bhai Dooj!"},{"20281023","Happy Chhath Puja!"},
                {"20290114","Happy Makar Sankranti & Pongal!"},{"20290119","Happy Vasant Panchami!"},{"20290211","Happy Maha Shivaratri!"},{"20290301","Happy Holi!"},{"20290414","Happy Ugadi & Gudi Padwa!"},{"20290423","Happy Rama Navami!"},{"20290428","Happy Hanuman Jayanti!"},{"20290516","Happy Akshaya Tritiya!"},{"20290713","Happy Rath Yatra!"},{"20290725","Happy Guru Purnima!"},{"20290823","Happy Raksha Bandhan!"},{"20290901","Happy Krishna Janmashtami!"},{"20290911","Happy Ganesh Chaturthi!"},{"20290908","Happy Onam!"},{"20291008","Happy Navratri!"},{"20291016","Happy Dussehra!"},{"20291104","Happy Dhanteras!"},{"20291105","Happy Diwali!!"},{"20291106","Happy Govardhan Puja!"},{"20291107","Happy Bhai Dooj!"},{"20291111","Happy Chhath Puja!"},
                {"20300114","Happy Makar Sankranti & Pongal!"},{"20300207","Happy Vasant Panchami!"},{"20300302","Happy Maha Shivaratri!"},{"20300320","Happy Holi!"},{"20300403","Happy Ugadi & Gudi Padwa!"},{"20300412","Happy Rama Navami!"},{"20300417","Happy Hanuman Jayanti!"},{"20300505","Happy Akshaya Tritiya!"},{"20300702","Happy Rath Yatra!"},{"20300715","Happy Guru Purnima!"},{"20300813","Happy Raksha Bandhan!"},{"20300821","Happy Krishna Janmashtami!"},{"20300901","Happy Ganesh Chaturthi!"},{"20300909","Happy Onam!"},{"20300928","Happy Navratri!"},{"20301006","Happy Dussehra!"},{"20301024","Happy Dhanteras!"},{"20301026","Happy Diwali!!"},{"20301027","Happy Govardhan Puja!"},{"20301028","Happy Bhai Dooj!"},
                {"20310114","Happy Makar Sankranti & Pongal!"},{"20310127","Happy Vasant Panchami!"},{"20310220","Happy Maha Shivaratri!"},{"20310309","Happy Holi!"},{"20310323","Happy Ugadi & Gudi Padwa!"},{"20310401","Happy Rama Navami!"},{"20310407","Happy Hanuman Jayanti!"},{"20310424","Happy Akshaya Tritiya!"},{"20310802","Happy Raksha Bandhan!"},{"20310810","Happy Krishna Janmashtami!"},{"20310920","Happy Ganesh Chaturthi!"},{"20310830","Happy Onam!"},{"20311017","Happy Navratri!"},{"20311025","Happy Dussehra!"},{"20311102","Happy Karwa Chauth!"},{"20311112","Happy Dhanteras!"},{"20311114","Happy Diwali!!"},{"20311115","Happy Govardhan Puja!"},{"20311116","Happy Bhai Dooj!"},{"20311120","Happy Chhath Puja!"}
        };
        for(String[] entry:entries)dates.put(entry[0],entry[1]);
        return Collections.unmodifiableMap(dates);
    }

    private static final class AppItem {
        final String pkg,name;final Drawable icon,banner;
        AppItem(String p,String n,Drawable i,Drawable b){pkg=p;name=n;icon=i;banner=b;}
    }
    @Override public void onCreate(Bundle state){super.onCreate(state);getWindow().getDecorView().setSystemUiVisibility(5894|1024|512);build();}
    private int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    private GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private TextView text(String s,float size,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setGravity(Gravity.CENTER_VERTICAL);t.setIncludeFontPadding(false);return t;}

    private void build(){
        FrameLayout root=new FrameLayout(this);root.setBackgroundColor(0xff000000);int currentHour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);dockMorningMode=isMorningHour(currentHour);wallpaper=new ColorWash();wallpaper.setWallpaperForHour(currentHour);FrameLayout screen=new FrameLayout(this);screen.setBackground(wallpaper);screen.setClipToOutline(true);screen.setOutlineProvider(new android.view.ViewOutlineProvider(){@Override public void getOutline(View view,android.graphics.Outline outline){outline.setRoundRect(0,0,view.getWidth(),view.getHeight(),dp(18));}});root.addView(screen,new FrameLayout.LayoutParams(-1,-1));setContentView(root);
        tileIdle=shape(0x00000000,17);
        LinearLayout page=new LinearLayout(this);page.setOrientation(LinearLayout.VERTICAL);page.setPadding(dp(34),dp(23),dp(34),dp(20));screen.addView(page,new FrameLayout.LayoutParams(-1,-1));

        clockHeader=new LinearLayout(this);clockHeader.setGravity(Gravity.CENTER_VERTICAL);page.addView(clockHeader,new LinearLayout.LayoutParams(-1,dp(108)));
        clockBlock=new LinearLayout(this);clockBlock.setOrientation(LinearLayout.VERTICAL);clockBlock.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);clockHeader.addView(clockBlock,new LinearLayout.LayoutParams(dp(420),-1));
        clockTime=text("",20,0xf5ffffff);clockTime.setTypeface(android.graphics.Typeface.create("sans-serif-medium",0));clockBlock.addView(clockTime,new LinearLayout.LayoutParams(-1,dp(25)));
        clockDate=text("",11,0xd0ffffff);clockBlock.addView(clockDate,new LinearLayout.LayoutParams(-1,dp(16)));
        clockGreeting=text("",15,0xf0ffffff);clockBlock.addView(clockGreeting,new LinearLayout.LayoutParams(-1,dp(20)));
        clockWeekend=text("",13,0xe0ffffff);clockBlock.addView(clockWeekend,new LinearLayout.LayoutParams(-1,dp(18)));
        clockFestival=text("",13,0xfff7d9ff);clockFestival.setTypeface(android.graphics.Typeface.create("sans-serif-medium",0));clockBlock.addView(clockFestival,new LinearLayout.LayoutParams(-1,dp(18)));
        clockSpacer=new View(this);clockHeader.addView(clockSpacer,new LinearLayout.LayoutParams(0,1,1));

        contentArea=new FrameLayout(this);page.addView(contentArea,new LinearLayout.LayoutParams(-1,0,1));gridFocusGlassView=new View(this);GradientDrawable gridLens=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0x72ffffff,0x43ffffff,0x26ffffff});gridLens.setCornerRadius(dp(16));gridLens.setStroke(dp(1),0x96ffffff);gridFocusGlassView.setBackground(gridLens);gridFocusGlassView.setElevation(0);gridFocusGlassView.setTranslationZ(0);gridFocusGlassView.setStateListAnimator(null);gridFocusGlassView.setFocusable(false);gridFocusGlassView.setClickable(false);gridFocusGlassView.setVisibility(View.INVISIBLE);contentArea.addView(gridFocusGlassView,new FrameLayout.LayoutParams(dp(102),dp(64),Gravity.TOP|Gravity.LEFT));
        grid=new RecyclerView(this);grid.setVisibility(View.GONE);grid.setHasFixedSize(true);grid.setItemViewCacheSize(18);grid.setItemAnimator(null);grid.setClipChildren(false);grid.setClipToPadding(false);grid.setPadding(dp(2),dp(13),dp(2),dp(8));grid.setOverScrollMode(View.OVER_SCROLL_NEVER);grid.setLayoutManager(new GridLayoutManager(this,COLUMNS));FrameLayout.LayoutParams gridLp=new FrameLayout.LayoutParams(dp(579),-1,Gravity.TOP|Gravity.CENTER_HORIZONTAL);gridLp.topMargin=dp(102);gridLp.bottomMargin=dp(8);contentArea.addView(grid,gridLp);
        adapter=new AppAdapter();grid.setAdapter(adapter);grid.setOnKeyListener((v,k,e)->e.getAction()==KeyEvent.ACTION_DOWN&&handleGridKey(k));
        shelf=new FrameLayout(this);shelf.setBackground(new LiquidDockDrawable(dp(25),dp(1)));shelf.setElevation(0);shelf.setStateListAnimator(null);shelf.setClipChildren(false);shelf.setClipToPadding(false);shelf.setPadding(dp(8),0,dp(8),0);FrameLayout.LayoutParams shelfParams=new FrameLayout.LayoutParams(-2,dp(86),Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);contentArea.addView(shelf,shelfParams);
        dockGlassView=new View(this);GradientDrawable lens=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0x72ffffff,0x43ffffff,0x26ffffff});lens.setCornerRadius(dp(19));lens.setStroke(dp(1),0x96ffffff);dockGlassView.setBackground(lens);dockGlassView.setElevation(0);dockGlassView.setTranslationZ(0);dockGlassView.setStateListAnimator(null);dockGlassView.setClickable(false);dockGlassView.setFocusable(false);FrameLayout.LayoutParams lensParams=new FrameLayout.LayoutParams(dp(116),dp(76),Gravity.LEFT|Gravity.CENTER_VERTICAL);shelf.addView(dockGlassView,lensParams);
        favoriteRow=new LinearLayout(this);favoriteRow.setGravity(Gravity.CENTER);favoriteRow.setClipChildren(false);favoriteRow.setClipToPadding(false);FrameLayout.LayoutParams favParams=new FrameLayout.LayoutParams(-2,-1,Gravity.CENTER);shelf.addView(favoriteRow,favParams);
        loadApps();for(int i=0;i<favorites.size();i++)favoriteRow.addView(createFavoriteTile(favorites.get(i),i));
        adapter.notifyDataSetChanged();
        if(!favorites.isEmpty())favoriteRow.post(()->{View first=favoriteRow.getChildAt(0);if(first!=null)first.requestFocus();});
    }


    private boolean isMorningHour(int hour){return hour>=6&&hour<17;}
    private void updateClock(){if(clockTime==null||clockDate==null||clockGreeting==null||clockWeekend==null||clockFestival==null)return;Date now=new Date();clockTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT,Locale.getDefault()).format(now));clockDate.setText(new SimpleDateFormat("EEEE, MMMM d",Locale.getDefault()).format(now));Calendar c=Calendar.getInstance();c.setTime(now);int hour=c.get(Calendar.HOUR_OF_DAY);String greeting=hour<5||hour>=22?"Why Still Up?":hour<12?"Good Morning":hour<17?"Good Afternoon":"Good Evening";clockGreeting.setText(greeting);boolean morning=isMorningHour(hour);if(dockMorningMode!=morning){dockMorningMode=morning;for(int i=0;i<favoriteImages.size()&&i<favorites.size();i++)bindFavoriteArtwork(favoriteImages.get(i),favorites.get(i),morning);}int clockColor=morning?0xff111318:0xf5ffffff;clockTime.setTextColor(clockColor);clockDate.setTextColor(morning?0xff111318:0xd0ffffff);clockGreeting.setTextColor(morning?0xff111318:0xf0ffffff);clockWeekend.setTextColor(morning?0xff111318:0xe0ffffff);clockFestival.setTextColor(morning?0xff111318:0xfff7d9ff);alignClockBlock(hour<17||hour>=20);boolean friday=c.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY;clockWeekend.setText("Any plans for weekend?");clockWeekend.setVisibility(friday?View.VISIBLE:View.GONE);String key=new SimpleDateFormat("yyyyMMdd",Locale.US).format(now);String festival=FESTIVAL_GREETINGS.get(key);clockFestival.setText(festival==null?"":festival);clockFestival.setVisibility(festival==null?View.GONE:View.VISIBLE);if(wallpaper!=null)wallpaper.setWallpaperForHour(hour);}
    private void alignClockBlock(boolean right){if(clockHeader==null||clockBlock==null||clockSpacer==null||clockOnRight==right)return;clockHeader.removeAllViews();int gravity=Gravity.CENTER_VERTICAL|(right?Gravity.RIGHT:Gravity.LEFT);clockBlock.setGravity(gravity);for(TextView line:new TextView[]{clockTime,clockDate,clockGreeting,clockWeekend,clockFestival})line.setGravity(gravity);LinearLayout.LayoutParams blockParams=new LinearLayout.LayoutParams(dp(420),-1);LinearLayout.LayoutParams spacerParams=new LinearLayout.LayoutParams(0,1,1);if(right){clockHeader.addView(clockSpacer,spacerParams);clockHeader.addView(clockBlock,blockParams);}else{clockHeader.addView(clockBlock,blockParams);clockHeader.addView(clockSpacer,spacerParams);}clockOnRight=right;}    @Override protected void onResume(){super.onResume();clockHandler.removeCallbacks(clockUpdater);clockUpdater.run();}
    @Override protected void onPause(){clockHandler.removeCallbacks(clockUpdater);super.onPause();}

    private void loadApps(){
        HashMap<String,AppItem> found=new HashMap<>();Intent query=new Intent(Intent.ACTION_MAIN);query.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        for(android.content.pm.ResolveInfo ri:getPackageManager().queryIntentActivities(query,0)){
            try{String pkg=ri.activityInfo.packageName;CharSequence label=ri.loadLabel(getPackageManager());String name=label==null?pkg:label.toString();Drawable icon=ri.loadIcon(getPackageManager());Drawable banner=null;try{banner=ri.activityInfo.loadBanner(getPackageManager());}catch(Exception ignored){}if(banner==null)try{android.content.pm.ApplicationInfo ai=getPackageManager().getApplicationInfo(pkg,0);banner=ai.loadBanner(getPackageManager());}catch(Exception ignored){}found.put(pkg,new AppItem(pkg,name,icon,banner));}catch(Exception ignored){}
        }
        for(String pkg:FAVORITE_PACKAGES){AppItem item=found.remove(pkg);if(item!=null)favorites.add(item);}
        found.remove("com.android.tv.settings");
        ArrayList<AppItem> candidates=new ArrayList<>(found.values());candidates.sort(Comparator.comparing(a->a.name.toLowerCase(Locale.ROOT)));
        // Keep the grid intentionally curated and ordered; unlisted system shortcuts stay installed but hidden here.
        String[] order={"jiosphere","stube","flick","cast","novaplayer","playstore"};
        for(String key:order){AppItem item=findCuratedGridApp(candidates,key);if(item!=null){apps.add(item);candidates.remove(item);}}
        Drawable settingsIcon=getDrawable(android.R.drawable.ic_menu_preferences);String settingsLabel="Settings";
        try{android.content.pm.ResolveInfo ri=getPackageManager().resolveActivity(new Intent(Settings.ACTION_SETTINGS),0);if(ri!=null){settingsIcon=ri.loadIcon(getPackageManager());CharSequence label=ri.loadLabel(getPackageManager());if(label!=null)settingsLabel=label.toString();}}catch(Exception ignored){}
        apps.add(new AppItem("@system-settings",settingsLabel,settingsIcon,null));
    }

    private AppItem findCuratedGridApp(List<AppItem> candidates,String key){
        for(AppItem item:candidates){String name=item.name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]","");String pkg=item.pkg.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]","");boolean match=false;
            switch(key){
                case "jiosphere":match=name.contains("jiosphere")||pkg.contains("jiosphere");break;
                case "stube":match=name.equals("stube")||name.contains("smarttube")||pkg.contains("smarttube")||pkg.contains("stube");break;
                case "flick":match=name.contains("flick")||pkg.contains("flick");break;
                case "cast":match=name.equals("cast")||name.equals("googlecast")||pkg.equals("comgoogleandroidappsmediashell");break;
                case "novaplayer":match=name.contains("novaplayer")||name.equals("nova")||pkg.contains("nova");break;
                case "playstore":match=name.contains("googleplaystore")||name.equals("playstore")||pkg.equals("comandroidvending");break;
            }
            if(match)return item;
        }
        return null;
    }
    private int dockIconOverride(String pkg){
        if("com.google.android.youtube.tv".equals(pkg))return R.drawable.dock_youtube_vector;
        if("com.amazon.amazonvideo.livingroom".equals(pkg))return R.drawable.dock_prime_video_vector;
        if("com.netflix.ninja".equals(pkg))return R.drawable.dock_netflix_vector;
        if("tv.willow".equals(pkg))return R.drawable.dock_willow_vector;
        return 0;
    }
    private int morningDockIconOverride(String pkg){
        if("com.google.android.youtube.tv".equals(pkg))return R.drawable.dock_youtube_morning_vector;
        if("com.amazon.amazonvideo.livingroom".equals(pkg))return R.drawable.dock_prime_video_morning_vector;
        if("com.netflix.ninja".equals(pkg))return R.drawable.dock_netflix_morning_vector;
        if("tv.willow".equals(pkg))return R.drawable.dock_willow_morning_vector;
        return 0;
    }
    private void bindFavoriteArtwork(ImageView image,AppItem item,boolean morning){
        int iconRes=morning?morningDockIconOverride(item.pkg):dockIconOverride(item.pkg);
        boolean spotifyMorning=morning&&"com.spotify.tv.android".equals(item.pkg);
        boolean custom=iconRes!=0||spotifyMorning;
        image.setScaleType((custom||item.banner!=null)?ImageView.ScaleType.FIT_XY:ImageView.ScaleType.FIT_CENTER);
        image.setBackground(shape(morning&&custom?0xffffffff:iconRes!=0?0xff000000:0xff17131f,12));
        image.setClipToOutline(true);
        Drawable artwork=spotifyMorning?new SpotifyMorningDrawable():iconRes!=0?getDrawable(iconRes):(item.banner!=null?item.banner:item.icon);
        image.setImageDrawable(artwork);
    }
    private View createFavoriteTile(AppItem item,int index){
        FrameLayout tile=new FrameLayout(this);tile.setFocusable(true);tile.setStateListAnimator(null);tile.setElevation(0);tile.setTranslationZ(0);tile.setContentDescription(item.name);tile.setBackground(tileIdle);LinearLayout column=new LinearLayout(this);column.setOrientation(LinearLayout.VERTICAL);column.setGravity(Gravity.CENTER);tile.addView(column,new FrameLayout.LayoutParams(dp(115),-1));
        ImageView image=new ImageView(this);image.setStateListAnimator(null);image.setElevation(0);image.setTranslationZ(0);bindFavoriteArtwork(image,item,dockMorningMode);image.setContentDescription(item.name);favoriteImages.add(image);column.addView(image,new LinearLayout.LayoutParams(dp(93),dp(56)));
        tile.setLayoutParams(new LinearLayout.LayoutParams(dp(115),-1));
        tile.setOnFocusChangeListener((v,focus)->{animateTile(tile,image,focus);if(focus){focusInFavorites=true;favoritePosition=index;moveDockGlass(tile);}});tile.setOnClickListener(v->launch(item.pkg));tile.setOnKeyListener((v,k,e)->e.getAction()==KeyEvent.ACTION_DOWN&&handleFavoriteKey(k,index));return tile;
    }

    private static final class SpotifyMorningDrawable extends Drawable {
        private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        @Override public void draw(Canvas canvas){
            Rect bounds=getBounds();float sx=bounds.width()/512f,sy=bounds.height()/308f;
            int save=canvas.save();canvas.translate(bounds.left,bounds.top);
            paint.setStyle(Paint.Style.FILL);paint.setColor(0xff1db954);canvas.drawCircle(138*sx,154*sy,61*sx,paint);
            paint.setStyle(Paint.Style.STROKE);paint.setStrokeCap(Paint.Cap.ROUND);paint.setColor(0xffffffff);
            paint.setStrokeWidth(9*sx);Path p=new Path();p.moveTo(101*sx,132*sy);p.cubicTo(125*sx,124*sy,155*sx,127*sy,177*sx,140*sy);canvas.drawPath(p,paint);
            paint.setStrokeWidth(8*sx);p.reset();p.moveTo(106*sx,153*sy);p.cubicTo(127*sx,147*sy,151*sx,150*sy,170*sx,161*sy);canvas.drawPath(p,paint);
            paint.setStrokeWidth(7*sx);p.reset();p.moveTo(111*sx,173*sy);p.cubicTo(128*sx,169*sy,146*sx,172*sy,160*sx,180*sy);canvas.drawPath(p,paint);
            paint.setStyle(Paint.Style.FILL);paint.setColor(0xff111820);paint.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));paint.setTextSize(58*sy);canvas.drawText("Spotify",220*sx,174*sy,paint);
            canvas.restoreToCount(save);
        }
        @Override public void setAlpha(int alpha){paint.setAlpha(alpha);invalidateSelf();}
        @Override public void setColorFilter(android.graphics.ColorFilter filter){paint.setColorFilter(filter);invalidateSelf();}
        @Override public int getOpacity(){return android.graphics.PixelFormat.TRANSLUCENT;}
    }

    private void animateTile(View tile,View image,boolean focused){
        tile.animate().cancel();
        tile.setBackground(tileIdle);
        tile.animate().scaleX(focused?1.09f:1f).scaleY(focused?1.09f:1f)
                .translationY(0).setDuration(focused?135:105)
                .setInterpolator(new DecelerateInterpolator(1.2f)).start();
    }
    private void moveDockGlass(View child){
        if(dockGlassView==null||favoriteRow==null||child.getWidth()==0||dockGlassView.getWidth()==0){if(child!=null)child.post(()->moveDockGlass(child));return;}
        float target=favoriteRow.getX()+child.getX()+(child.getWidth()-dockGlassView.getWidth())/2f-dockGlassView.getLeft();
        if(!dockGlassPositioned){dockGlassView.setTranslationX(target);dockGlassPositioned=true;return;}
        dockGlassView.animate().cancel();
        dockGlassView.animate().translationX(target).setDuration(145)
                .setInterpolator(new DecelerateInterpolator(1.2f)).start();
    }
    private void moveGridGlass(View tile){if(gridFocusGlassView==null||grid==null||tile==null||tile.getWidth()==0||gridFocusGlassView.getWidth()==0){if(tile!=null)tile.post(()->moveGridGlass(tile));return;}float x=grid.getX()+tile.getX()+(tile.getWidth()-gridFocusGlassView.getWidth())/2f;float y=grid.getY()+tile.getY()+(tile.getHeight()-gridFocusGlassView.getHeight())/2f;gridFocusGlassView.animate().cancel();if(!gridFocusGlassPositioned){gridFocusGlassPositioned=true;gridFocusGlassView.setTranslationX(x);gridFocusGlassView.setTranslationY(y);gridFocusGlassView.setAlpha(0f);gridFocusGlassView.setScaleX(.94f);gridFocusGlassView.setScaleY(.90f);gridFocusGlassView.setVisibility(View.VISIBLE);gridFocusGlassView.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(205).setInterpolator(new android.view.animation.OvershootInterpolator(.24f)).withLayer().start();return;}gridFocusGlassView.setVisibility(View.VISIBLE);gridFocusGlassView.animate().translationX(x).translationY(y).alpha(1f).setDuration(155).setInterpolator(transitionEase).withLayer().start();}
    private boolean isSelectKey(int key){return key==KeyEvent.KEYCODE_DPAD_CENTER||key==KeyEvent.KEYCODE_ENTER||key==KeyEvent.KEYCODE_NUMPAD_ENTER;}
    private boolean handleFavoriteKey(int key,int index){if(isSelectKey(key)){if(index>=0&&index<favorites.size())launch(favorites.get(index).pkg);return true;}if(key==KeyEvent.KEYCODE_DPAD_LEFT){if(index>0)favoriteRow.getChildAt(index-1).requestFocus();return true;}if(key==KeyEvent.KEYCODE_DPAD_RIGHT){if(index+1<favoriteRow.getChildCount())favoriteRow.getChildAt(index+1).requestFocus();return true;}if(key==KeyEvent.KEYCODE_DPAD_DOWN){showGrid();return true;}if(key==KeyEvent.KEYCODE_DPAD_UP)return true;return false;}
    private boolean handleGridKey(int key){View f=getCurrentFocus();if(f==null)return false;int p=grid.getChildAdapterPosition(f);if(p==RecyclerView.NO_POSITION)return false;focusInFavorites=false;focusedPosition=p;if(isSelectKey(key)){if(p>=0&&p<apps.size())launch(apps.get(p).pkg);return true;}if(key==KeyEvent.KEYCODE_DPAD_LEFT||key==KeyEvent.KEYCODE_DPAD_RIGHT){int n=p+(key==KeyEvent.KEYCODE_DPAD_RIGHT?1:-1);if(n>=0&&n<apps.size())focusGrid(n);return true;}if(key==KeyEvent.KEYCODE_DPAD_UP){if(p>=COLUMNS)focusGrid(p-COLUMNS);else hideGridToDock();return true;}if(key==KeyEvent.KEYCODE_DPAD_DOWN){if(p+COLUMNS<apps.size())focusGrid(p+COLUMNS);return true;}return false;}
    private void showGrid(){
        if(apps.isEmpty()||gridExpanded)return;
        if(contentArea.getHeight()==0){contentArea.post(this::showGrid);return;}
        shelf.animate().cancel();grid.animate().cancel();
        gridExpanded=true;int travel=Math.max(0,contentArea.getHeight()-dp(8)-shelf.getHeight());
        FrameLayout.LayoutParams dockLp=(FrameLayout.LayoutParams)shelf.getLayoutParams();dockLp.gravity=Gravity.TOP|Gravity.CENTER_HORIZONTAL;dockLp.topMargin=dp(8);shelf.setLayoutParams(dockLp);shelf.setTranslationY(travel);
        grid.setVisibility(View.VISIBLE);grid.setAlpha(0f);grid.setTranslationY(dp(24));grid.setPivotY(0f);grid.setScaleY(.965f);
        shelf.animate().translationY(0).setDuration(490).setInterpolator(new android.view.animation.OvershootInterpolator(.22f)).withLayer().start();
        grid.animate().alpha(1f).translationY(0).scaleY(1f).setStartDelay(12).setDuration(440).setInterpolator(transitionEase).withLayer().start();
        grid.postDelayed(()->{if(gridExpanded)focusGrid(Math.min(focusedPosition,apps.size()-1));},90);
    }
    private void hideGridToDock(){
        if(!gridExpanded){focusFavorite(favoritePosition);return;}
        gridExpanded=false;shelf.animate().cancel();grid.animate().cancel();
        if(gridFocusGlassView!=null){gridFocusGlassView.animate().cancel();gridFocusGlassView.animate().alpha(0f).scaleX(.96f).scaleY(.92f).setDuration(115).setInterpolator(transitionEase).withEndAction(()->{if(!gridExpanded){gridFocusGlassView.setVisibility(View.INVISIBLE);gridFocusGlassPositioned=false;gridFocusGlassView.setScaleX(1f);gridFocusGlassView.setScaleY(1f);}}).start();}
        int travel=Math.max(0,contentArea.getHeight()-dp(8)-shelf.getHeight());
        FrameLayout.LayoutParams dockLp=(FrameLayout.LayoutParams)shelf.getLayoutParams();dockLp.gravity=Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;dockLp.topMargin=0;shelf.setLayoutParams(dockLp);shelf.setTranslationY(-travel);
        shelf.animate().translationY(0).setDuration(490).setInterpolator(new android.view.animation.OvershootInterpolator(.22f)).withLayer().withEndAction(()->{if(!gridExpanded)focusFavorite(favoritePosition);}).start();
        grid.animate().alpha(0f).translationY(dp(16)).scaleY(.985f).setDuration(310).setInterpolator(transitionEase).withLayer().withEndAction(()->{if(!gridExpanded){grid.setVisibility(View.GONE);grid.setAlpha(1f);grid.setTranslationY(0);grid.setScaleY(1f);}}).start();
    }    private void focusGrid(int p){if(apps.isEmpty())return;focusedPosition=Math.max(0,Math.min(p,apps.size()-1));grid.scrollToPosition(focusedPosition);grid.post(()->{RecyclerView.ViewHolder h=grid.findViewHolderForAdapterPosition(focusedPosition);if(h!=null)h.itemView.requestFocus();});}
    private void focusFavorite(int p){if(favoriteRow.getChildCount()==0)return;favoritePosition=Math.max(0,Math.min(p,favoriteRow.getChildCount()-1));View v=favoriteRow.getChildAt(favoritePosition);if(v!=null)v.requestFocus();}

    private final class AppAdapter extends RecyclerView.Adapter<AppAdapter.Holder>{
        final class Holder extends RecyclerView.ViewHolder{final FrameLayout tile;final ImageView image;Holder(FrameLayout v,ImageView i){super(v);tile=v;image=i;}}
        @Override public Holder onCreateViewHolder(ViewGroup parent,int type){FrameLayout tile=new FrameLayout(HomeActivity.this);tile.setFocusable(true);tile.setStateListAnimator(null);tile.setElevation(0);tile.setTranslationZ(0);tile.setBackground(tileIdle);LinearLayout col=new LinearLayout(HomeActivity.this);col.setOrientation(LinearLayout.VERTICAL);col.setGravity(Gravity.CENTER);tile.addView(col,new FrameLayout.LayoutParams(-1,-1));ImageView image=new ImageView(HomeActivity.this);image.setScaleType(ImageView.ScaleType.FIT_XY);image.setStateListAnimator(null);image.setElevation(0);image.setTranslationZ(0);image.setBackground(shape(0xff17131f,11));image.setClipToOutline(true);LinearLayout.LayoutParams imageParams=new LinearLayout.LayoutParams(dp(82),dp(49));col.addView(image,imageParams);tile.setContentDescription("App");tile.setLayoutParams(new RecyclerView.LayoutParams(-1,dp(74)));return new Holder(tile,image);}
        @Override public void onBindViewHolder(Holder h,int position){AppItem item=apps.get(position);h.image.setImageDrawable(item.banner!=null?item.banner:item.icon);h.image.setScaleType(item.banner!=null?ImageView.ScaleType.FIT_XY:ImageView.ScaleType.FIT_CENTER);h.image.setContentDescription(item.name);h.tile.setContentDescription(item.name);h.tile.setOnClickListener(v->launch(item.pkg));h.tile.setOnFocusChangeListener((v,focus)->{animateTile(h.tile,h.image,focus);if(focus){focusInFavorites=false;focusedPosition=h.getBindingAdapterPosition();moveGridGlass(h.tile);}});h.tile.setOnKeyListener((v,k,e)->e.getAction()==KeyEvent.ACTION_DOWN&&handleGridKey(k));}
        @Override public int getItemCount(){return apps.size();}
    }

    private void launch(String pkg){try{Intent i;if("@system-settings".equals(pkg)){i=new Intent(Settings.ACTION_SETTINGS);}else{i=getPackageManager().getLaunchIntentForPackage(pkg);if(i==null){i=new Intent(Intent.ACTION_MAIN);i.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);i.setPackage(pkg);android.content.pm.ResolveInfo resolved=getPackageManager().resolveActivity(i,0);if(resolved!=null&&resolved.activityInfo!=null)i.setClassName(pkg,resolved.activityInfo.name);}}i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);startActivity(i);}catch(Exception ignored){}}
    @Override public boolean dispatchKeyEvent(KeyEvent event){
        // The RC-36 YouTube Music shortcut arrives as Linux KEY_DATABASE (219).
        // Consume both edges so the firmware's original shortcut cannot race our app launch.
        int scan=event.getScanCode();
        android.util.Log.d("EcoTVKey","action="+event.getAction()+" key="+event.getKeyCode()+" scan="+scan+" device="+event.getDeviceId());
        if(scan==219||scan==0x0c0189){
            if(event.getAction()==KeyEvent.ACTION_DOWN){if(event.getRepeatCount()==0)launch("com.spotify.tv.android");return true;}
            if(event.getAction()==KeyEvent.ACTION_UP)return true;
        }
        return super.dispatchKeyEvent(event);
    }
    @Override public boolean onKeyDown(int keyCode,KeyEvent event){if(keyCode==KeyEvent.KEYCODE_SETTINGS){try{startActivity(new Intent(Settings.ACTION_SETTINGS));return true;}catch(Exception ignored){}}return super.onKeyDown(keyCode,event);}
    @Override public void onBackPressed(){if(grid!=null&&grid.getVisibility()==View.VISIBLE){hideGridToDock();return;}super.onBackPressed();}

    /** Smoked graphite liquid glass dock with a restrained neutral sheen and fine inset edges. */
    private final class LiquidDockDrawable extends Drawable{
        private final Paint fill=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint sheen=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint refraction=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint edge=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float radius,strokeWidth;
        private int drawableAlpha=255;
        private android.graphics.ColorFilter colorFilter;
        LiquidDockDrawable(float radius,float strokeWidth){this.radius=radius;this.strokeWidth=strokeWidth;edge.setStyle(Paint.Style.STROKE);edge.setStrokeWidth(strokeWidth);}
        @Override public void draw(Canvas canvas){
            Rect bounds=getBounds();if(bounds.isEmpty())return;
            RectF r=new RectF(bounds);
            fill.setShader(new LinearGradient(r.left,r.top,r.left,r.bottom,new int[]{0xd817191e,0xde0b0d12,0xe111141a},null,Shader.TileMode.CLAMP));
            fill.setAlpha(drawableAlpha);fill.setColorFilter(colorFilter);canvas.drawRoundRect(r,radius,radius,fill);
            sheen.setShader(new LinearGradient(r.left,r.top,r.left,r.bottom,new int[]{0x24ffffff,0x0cffffff,0x00ffffff},new float[]{0f,.38f,1f},Shader.TileMode.CLAMP));
            sheen.setAlpha(drawableAlpha);sheen.setColorFilter(colorFilter);canvas.drawRoundRect(r,radius,radius,sheen);
            refraction.setShader(new LinearGradient(r.left,r.top,r.right,r.bottom,new int[]{0x14ffffff,0x04171c28,0x1b7c8798},new float[]{0f,.52f,1f},Shader.TileMode.CLAMP));
            refraction.setAlpha(drawableAlpha);refraction.setColorFilter(colorFilter);canvas.drawRoundRect(r,radius,radius,refraction);
            edge.setShader(null);edge.setColor(0x55d2d9e3);edge.setAlpha(drawableAlpha);edge.setColorFilter(colorFilter);canvas.drawRoundRect(r,radius,radius,edge);
            RectF inner=new RectF(r);inner.inset(strokeWidth*1.6f,strokeWidth*1.6f);
            edge.setColor(0x20ffffff);canvas.drawRoundRect(inner,Math.max(0,radius-strokeWidth*1.6f),Math.max(0,radius-strokeWidth*1.6f),edge);
        }
        @Override public void setAlpha(int alpha){drawableAlpha=alpha;invalidateSelf();}
        @Override public void setColorFilter(android.graphics.ColorFilter filter){colorFilter=filter;invalidateSelf();}
        @Override public int getOpacity(){return android.graphics.PixelFormat.TRANSLUCENT;}
    }
    /** Saturated, soft-focus color field from the supplied visual reference. */
    private final class ColorWash extends Drawable{
        private final Paint bitmapPaint=new Paint(Paint.FILTER_BITMAP_FLAG);
        private Bitmap cached;private int cachedWidth,cachedHeight,currentResource;
        void setWallpaperForHour(int hour){int resource=hour>=20||hour<6?R.drawable.wallpaper_night:hour>=17?R.drawable.wallpaper_evening:R.drawable.wallpaper_morning;if(currentResource==resource)return;currentResource=resource;if(cached!=null){cached.recycle();cached=null;}invalidateSelf();}
        @Override public void draw(Canvas c){Rect b=getBounds();int w=b.width(),h=b.height();if(w<=0||h<=0)return;
            if(cached==null||cachedWidth!=w||cachedHeight!=h){
                if(cached!=null)cached.recycle();
                BitmapFactory.Options options=new BitmapFactory.Options();options.inScaled=false;
                options.inPreferredConfig=Bitmap.Config.ARGB_8888;
                int resource=currentResource;
                android.util.DisplayMetrics metrics=getResources().getDisplayMetrics();
                int targetWidth=Math.max(w,metrics.widthPixels),targetHeight=Math.max(h,metrics.heightPixels);
                android.graphics.BitmapFactory.Options bounds=new android.graphics.BitmapFactory.Options();bounds.inJustDecodeBounds=true;
                BitmapFactory.decodeResource(getResources(),resource,bounds);
                int sample=1;while(bounds.outWidth/(sample*2)>=targetWidth&&bounds.outHeight/(sample*2)>=targetHeight)sample*=2;
                options.inSampleSize=sample;
                cached=BitmapFactory.decodeResource(getResources(),resource,options);
                cachedWidth=w;cachedHeight=h;
            }
            c.drawBitmap(cached,null,b,bitmapPaint);
        }
        @Override public void setAlpha(int a){bitmapPaint.setAlpha(a);invalidateSelf();}@Override public void setColorFilter(android.graphics.ColorFilter f){bitmapPaint.setColorFilter(f);invalidateSelf();}@Override public int getOpacity(){return android.graphics.PixelFormat.OPAQUE;}
    }
}
