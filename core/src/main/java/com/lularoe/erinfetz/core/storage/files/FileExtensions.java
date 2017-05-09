package com.lularoe.erinfetz.core.storage.files;

import android.content.Context;

import java.util.Map;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableMap;
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
        return extensionToMediaType.get(ext);
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
        return extensionToMediaType.get(ext.toLowerCase());
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
        return mediaTypeToExtension.get(mediatype);
    }
}
