package com.lularoe.erinfetz.core.storage.files;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.util.Map;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.lularoe.erinfetz.core.R;
import com.lularoe.erinfetz.core.res.ResourceUtils;

public class FileExtensions {

    private final Map<String, MediaType> extensionToMediaType;

    private final Map<MediaType, String> mediaTypeToExtension;

    public FileExtensions(Context context){
        extensionToMediaType = Maps.newHashMap();
        mediaTypeToExtension=Maps.newHashMap();
        Map<String,String> res = ResourceUtils.getHashMapResource(context, R.xml.file_extensions);
        for(Map.Entry<String,String> r : res.entrySet()){
            extensionToMediaType.put(r.getKey(), MediaType.parse(r.getValue()));
        }

        res = ResourceUtils.getHashMapResource(context, R.xml.media_types);
        for(Map.Entry<String,String> r : res.entrySet()){
            mediaTypeToExtension.put(MediaType.parse(r.getKey()), r.getValue());
        }
    }

    /**
     * Map the provided file extension to a <code>MediaType</code>. If not
     * mapping is defined for the given extension, <code>null</code> is
     * returned.
     *
     * @param ext
     * @return
     */
    public MediaType toMediaType(String ext) {
        ext = CharMatcher.is('.').removeFrom(ext).toLowerCase();
        MediaType m = extensionToMediaType.get(ext);
        return m==null?mimeTypeForExtension(ext):m;
    }

    private static MediaType mimeTypeForExtension(String ext){
        ext = CharMatcher.is('.').removeFrom(ext).toLowerCase();

        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

        if(Strings.isNullOrEmpty(type)){
            return null;
        }
        return MediaType.parse(type);
    }
    private static MediaType getMimeType(String url) {
        String type = "";

        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (!Strings.isNullOrEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        } else {
            String reCheckExtension = MimeTypeMap.getFileExtensionFromUrl(url.replaceAll("\\s+", ""));
            if (!Strings.isNullOrEmpty(reCheckExtension)) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(reCheckExtension);
            }
        }
        if(Strings.isNullOrEmpty(type)){
            return null;
        }
        return MediaType.parse(type);
    }
    private static String getFileExtension(MediaType type){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(type.withoutParameters());
    }

    /**
     * Map the file extension associated with the given file name to a
     * <code>MediaType</code>. If not mapping is defined for the given
     * extension, <code>null</code> is returned.
     *
     * @param name
     * @return
     */
    public MediaType toMediaTypeFromFileName(String name) {
        String ext = Files.getFileExtension(name);
        return toMediaType(ext.toLowerCase());
    }

    /**
     * Looks up the file extension associated with a particular media type.
     * For media types that have multiple appropriate extensions, an
     * arbitrary extension is picked.
     *
     * @param mediatype
     * @return
     */
    public String toFileExtension(MediaType mediatype) {
        String f= mediaTypeToExtension.get(mediatype);
        return Strings.isNullOrEmpty(f)?getFileExtension(mediatype):f;
    }
    public String toFileExtension(MediaType mediatype, boolean includePeriod) {
        String ext = toFileExtension(mediatype);
        if(Strings.isNullOrEmpty(ext)){
            return null;
        }
        if(includePeriod){
            return "."+ext;
        }
        return ext;
    }
}
