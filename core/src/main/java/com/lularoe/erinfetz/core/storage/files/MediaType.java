package com.lularoe.erinfetz.core.storage.files;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public final class MediaType {
    private static final String TAG = MediaType.class.getSimpleName();

    private static final String WILDCARD_STRING = "*";

    private static final String TEXT = "text";
    public static final MediaType TEXT_WILDCARD = new MediaType(TEXT, WILDCARD_STRING);
    public static final MediaType TEXT_PLAIN = new MediaType(TEXT, "plain");
    public static final MediaType TEXT_CSS = new MediaType(TEXT, "css");
    public static final MediaType TEXT_CSV = new MediaType(TEXT, "csv");
    public static final MediaType TEXT_HTML = new MediaType(TEXT, "html");

    private static final String APPLICATION = "application";
    public static final MediaType APPLICATION_WILDCARD = new MediaType(APPLICATION, WILDCARD_STRING);
    public static final MediaType APPLICATION_JSON = new MediaType(APPLICATION, "json");
    public static final MediaType APPLICATION_LDJSON = new MediaType(APPLICATION, "ldjson");
    public static final MediaType APPLICATION_PROTOBUF = new MediaType(APPLICATION, "protobuf");
    public static final MediaType APPLICATION_XML = new MediaType(APPLICATION, "xml");
    public static final MediaType APPLICATION_PDF = new MediaType(APPLICATION, "pdf");
    public static final MediaType APPLICATION_RTF = new MediaType(APPLICATION, "rtf");
    public static final MediaType APPLICATION_RDF = new MediaType(APPLICATION, "rdf+xml");
    public static final MediaType APPLICATION_RSS = new MediaType(APPLICATION, "rss+xml");
    public static final MediaType APPLICATION_ZIP = new MediaType(APPLICATION, "zip");
    public static final MediaType APPLICATION_OCTET_STREAM = new MediaType(APPLICATION, "octet-stream");

    private static final String AUDIO = "audio";
    public static final MediaType AUDIO_WILDCARD = new MediaType(AUDIO, WILDCARD_STRING);
    public static final MediaType AUDIO_MP4 = new MediaType(AUDIO, "mp4");
    public static final MediaType AUDIO_MPEG = new MediaType(AUDIO, "mpeg");
    public static final MediaType AUDIO_OGG = new MediaType(AUDIO, "ogg");
    public static final MediaType AUDIO_VORBIS = new MediaType(AUDIO, "vorbis");

    private static final String IMAGE = "image";
    public static final MediaType IMAGE_WILDCARD = new MediaType(IMAGE, WILDCARD_STRING);
    public static final MediaType IMAGE_GIF = new MediaType(IMAGE, "gif");
    public static final MediaType IMAGE_JPEG = new MediaType(IMAGE, "jpeg");
    public static final MediaType IMAGE_TIFF = new MediaType(IMAGE, "tiff");
    public static final MediaType IMAGE_PNG = new MediaType(IMAGE, "png");
    public static final MediaType IMAGE_SVG = new MediaType(IMAGE, "svg+xml");
    public static final MediaType IMAGE_BMP = new MediaType(IMAGE, "bmp");

    private static final String VIDEO = "video";
    public static final MediaType VIDEO_WILDCARD = new MediaType(VIDEO, WILDCARD_STRING);
    public static final MediaType VIDEO_MPEG = new MediaType(VIDEO, "mpeg");
    public static final MediaType VIDEO_MP4 = new MediaType(VIDEO, "mp4");
    public static final MediaType VIDEO_OGG = new MediaType(VIDEO, "ogg");
    public static final MediaType VIDEO_WMV = new MediaType(VIDEO, "x-ms-wmv");
    public static final MediaType VIDEO_FLV = new MediaType(VIDEO, "x-flv");
    public static final MediaType VIDEO_MKV = new MediaType(VIDEO, "x-matroska");
    public static final MediaType VIDEO_AVI = new MediaType(VIDEO, "vnd.avi");
    public static final MediaType VIDEO_QUICKTIME = new MediaType(VIDEO, "quicktime");

    public static final MediaType VENDOR_XLSX = new MediaType(APPLICATION,
            "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static final MediaType VENDOR_PPTX = new MediaType(APPLICATION,
            "vnd.openxmlformats-officedocument.presentationml.presentation");
    public static final MediaType VENDOR_DOCX = new MediaType(APPLICATION,
            "vnd.openxmlformats-officedocument.wordprocessingml.document");

    public static final MediaType VENDOR_XLS = new MediaType(APPLICATION, "vnd.ms-excel");
    public static final MediaType VENDOR_PPT = new MediaType(APPLICATION, "vnd.ms-powerpoint");
    public static final MediaType VENDOR_DOC = new MediaType(APPLICATION, "msword");
    public static final MediaType VENDOR_ONE = new MediaType(APPLICATION, "onenote");

    public static final MediaType WILDCARD = new MediaType(WILDCARD_STRING, WILDCARD_STRING);

    private String type;
    private String subtype;
    private Map<String, String> parameters;

    private MediaType(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        this.parameters = new HashMap<>();
    }

    private MediaType(String type, String subtype, Map<String, String> parameters) {
        this.type = type;
        this.subtype = subtype;
        this.parameters = Maps.newHashMap(parameters);
    }

    /**
     * Create a <code>MediaType</code> given a <code>String</code>
     * representation.
     */
    public static MediaType parse(String mediaType) {

        String params = null;
        String type = mediaType;
        int idx = type.indexOf(';');
        if (idx > -1) {
            params = type.substring(idx + 1).trim();
            type = type.substring(0, idx);
        }

        String major = null;
        String subtype = null;
        String[] paths = type.split("/");
        if (paths.length < 2 && "*".equals(type)) {
            major = "*";
            subtype = "*";
        } else if (paths.length == 2) {
            major = paths[0];
            subtype = paths[1];
        } else {
            throw new IllegalArgumentException(
                    "Failure parsing MediaType string: " + type);
        }

        if (params != null && !"".equals(params)) {
            return new MediaType(major, subtype, parseParams(params));
        } else {
            return new MediaType(major, subtype);
        }
    }

    /**
     * Creates a new MediaType for the supplied type and subtype.  The existing
     * constants should be preferred when applicable.
     */
    public static MediaType create(String type, String subtype) {
        return new MediaType(type, subtype);
    }

    public static MediaType copyOf(MediaType mediaType) {
        return new MediaType(mediaType.getType(), mediaType.getSubtype(), mediaType.getParameters());
    }

    /**
     * Check if this media type is compatible with another media type. E.g.
     * image/* is compatible with image/jpeg, image/png, etc. Media type
     * parameters are ignored. The function is commutative.
     */
    public boolean isCompatible(MediaType mediaType) {
        return isCompatible(this, mediaType);
    }

    /**
     * Check if this media type is compatible with another media type. E.g.
     * image/* is compatible with image/jpeg, image/png, etc. Media type
     * parameters are ignored. The function is commutative.
     */
    public boolean isCompatible(String mediaType) {
        if(mediaType == null) {
            return false;
        }
        try {
            return isCompatible(MediaType.parse(mediaType));
        } catch (IllegalArgumentException e) {
            Log.e(TAG,"Invalid media type :" + mediaType);
            return false;
        }
    }

    /**
     * Check if the media types are compatible with each other. E.g. image/* is
     * compatible with image/jpeg, image/png, etc. Media type parameters are
     * ignored. The function is commutative.
     */
    public static boolean isCompatible(String mediaType1, String mediaType2) {

        if (mediaType1 == null && mediaType2 == null) {
            return true;
        }
        if (mediaType1 == null || mediaType2 == null) {
            return false;
        }

        MediaType a = parse(mediaType1);
        MediaType b = parse(mediaType2);
        return isCompatible(a, b);
    }

    /**
     * Check if the media types are compatible with each other. E.g. image/* is
     * compatible with image/jpeg, image/png, etc. Media type parameters are
     * ignored. The function is commutative.
     */
    public static boolean isCompatible(MediaType a, MediaType b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (WILDCARD_STRING.equals(a.type) || WILDCARD_STRING.equals(b.type)) {
            return true;
        }
        boolean equalTypes = a.type.equalsIgnoreCase(b.type);
        if (equalTypes && (WILDCARD_STRING.equals(a.subtype) || WILDCARD_STRING.equals(b.subtype))) {
            return true;
        }
        return equalTypes && a.subtype.equalsIgnoreCase(b.subtype);
    }

    /**
     * Creates a predicate that can evaluate whether a media type is compatible
     * with the provided media type.
     *
     * @param type The media type to check compatibility against.
     * @return A predicate that can evaluate the compatibility of media types.
     */
    public static Predicate<MediaType> isCompatiblePredicate(final MediaType type) {
        return new Predicate<MediaType>(){

            @Override
            public boolean apply(MediaType input) {
                return MediaType.isCompatible(type, input);
            }
        };
    }

    public static Predicate<MediaType> isCompatibleWithAnyOfPredicate(final MediaType... types) {
        List<Predicate<MediaType>> predicates = new ArrayList<>(types.length);
        for (int i = 0; i < types.length; ++i) {
            predicates.add(isCompatiblePredicate(types[i]));
        }
        return Predicates.or(predicates);
    }

    public static Predicate<MediaType> isCompatibleWithAnyOfPredicate(final Iterable<MediaType> types) {
        List<Predicate<MediaType>> predicates = new ArrayList<>(Iterables.size(types));
        for (MediaType type : types) {
            predicates.add(isCompatiblePredicate(type));
        }
        return Predicates.or(predicates);
    }

    private static Map<String, String> parseParams(String params) {
        Map<String, String> typeParams = Maps.newHashMap();
        int start = 0;
        while (start < params.length()) {
            start = setParam(typeParams, params, start);
        }
        return typeParams;
    }

    private static int getEndName(String params, int start) {
        int equals = params.indexOf('=', start);
        int semicolon = params.indexOf(';', start);
        if (equals == -1 && semicolon == -1) {
            return params.length();
        }
        if (equals == -1) {
            return semicolon;
        }
        if (semicolon == -1) {
            return equals;
        }
        return (equals < semicolon) ? equals : semicolon;
    }

    private static int setParam(Map<String, String> typeParams,
                                String params, int start) {
        boolean quote = false;
        boolean backslash = false;

        int end = getEndName(params, start);
        String name = params.substring(start, end).trim();
        if (end < params.length() && params.charAt(end) == '=') {
            end++;
        }

        StringBuilder buffer = new StringBuilder(params.length() - end);
        int i = end;
        for (; i < params.length(); i++) {
            char c = params.charAt(i);

            switch (c) {
                case '"':
                    if (backslash) {
                        backslash = false;
                        buffer.append(c);
                    } else {
                        quote = !quote;
                    }
                    break;
                case '\\':
                    if (backslash) {
                        backslash = false;
                        buffer.append(c);
                    }
                    break;
                case ';':
                    if (!quote) {
                        String value = buffer.toString().trim();
                        typeParams.put(name, value);
                        return i + 1;
                    } else {
                        buffer.append(c);
                    }
                    break;
                default:
                    buffer.append(c);
                    break;
            }
        }
        String value = buffer.toString().trim();
        typeParams.put(name, value);
        return i;
    }

    public int hashCode() {
        return (this.type.toLowerCase() + this.subtype.toLowerCase())
                .hashCode() + this.parameters.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MediaType)) {
            return false;
        }
        MediaType other = (MediaType) obj;
        return this.type.equalsIgnoreCase(other.type)
                && this.subtype.equalsIgnoreCase(other.subtype)
                && this.parameters.equals(other.parameters);
    }

    /**
     * Return the <code>String</code> representation of the given
     * <code>MediaType</code> without any parameters.
     */
    public String withoutParameters() {
        return getType().toLowerCase() + "/" + getSubtype().toLowerCase();
    }

    public String toString() {
        String rtn = getType().toLowerCase() + "/" + getSubtype().toLowerCase();
        if (getParameters() == null || getParameters().size() == 0) {
            return rtn;
        }
        for (String name : getParameters().keySet()) {
            String val = getParameters().get(name);
            rtn += ";" + name + "=" + val;
            // TODO definition says a TOKEN or QUOTAED String
            // rtn += ";" + name + "=\"" + val + "\"";
        }
        return rtn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

}