package co.sandbx;

import android.media.MediaMetadataRetriever;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import java.util.HashMap;

public class MetadataForMediaRetrieverModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public MetadataForMediaRetrieverModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "MetadataForMediaRetriever";
  }

  @ReactMethod
  public void getMetadata(String uri, Promise promise) {
    WritableMap map = Arguments.createMap();
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    try{
      if (uri.startsWith("http")) {
        mmr.setDataSource(uri, new HashMap<String, String>());
      } else {
        mmr.setDataSource(uri);
      }
      String songGenre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
      String album =  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
      String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
      String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
      String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

      map.putString("genre"  ,songGenre);
      map.putString("album",album);
      map.putString("duration",duration);
      map.putString("artist", artist);
      map.putString("title", title);
      promise.resolve(map);
    } catch(RuntimeException exp){
      promise.reject("Error getting meatdata", exp);
    } finally {
      mmr.release();
    }
  }
}
