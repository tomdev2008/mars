package com.birdstudio.mars.remoting.support;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.URL;

/**
 * @author <a href="mailto:gang.lvg@birdstudio-inc.com">kimi</a>
 */
public class ProtocolUtils {

    private ProtocolUtils() {
    }

    public static String serviceKey(URL url) {
        return serviceKey(url.getPort(), url.getPath(), url.getParameter(Constants.VERSION_KEY),
                          url.getParameter(Constants.GROUP_KEY));
    }

    public static String serviceKey(int port, String serviceName, String serviceVersion, String serviceGroup) {
        StringBuilder buf = new StringBuilder();
        if (serviceGroup != null && serviceGroup.length() > 0) {
            buf.append(serviceGroup);
            buf.append("/");
        }
        buf.append(serviceName);
        if (serviceVersion != null && serviceVersion.length() > 0 && !"0.0.0".equals(serviceVersion)) {
            buf.append(":");
            buf.append(serviceVersion);
        }
        buf.append(":");
        buf.append(port);
        return buf.toString();
    }

    public static boolean isGeneric(String generic) {
        return generic != null
            && !"".equals(generic)
            && (Constants.GENERIC_SERIALIZATION_DEFAULT.equalsIgnoreCase(generic)  /* 正常的泛化调用 */
            || Constants.GENERIC_SERIALIZATION_NATIVE_JAVA.equalsIgnoreCase(generic)); /* 支持java序列化的流式泛化调用 */
    }

    public static boolean isDefaultGenericSerialization(String generic) {
        return isGeneric(generic)
            && Constants.GENERIC_SERIALIZATION_DEFAULT.equalsIgnoreCase(generic);
    }

    public static boolean isJavaGenericSerialization(String generic) {
        return isGeneric(generic)
            && Constants.GENERIC_SERIALIZATION_NATIVE_JAVA.equalsIgnoreCase(generic);
    }
}
