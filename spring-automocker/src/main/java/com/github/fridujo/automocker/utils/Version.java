package com.github.fridujo.automocker.utils;

import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class Version {

    private final int major;
    private final int minor;

    private Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public static Version major(int major) {
        return new Version(major, 0);
    }

    public static Version spring() {
        String springVersion = ApplicationContext.class.getPackage().getImplementationVersion();
        List<String> tokenizedSpringVersion = Splitter.on('.').splitToList(springVersion);
        int major = Ints.tryParse(tokenizedSpringVersion.get(0));
        int minor = Ints.tryParse(tokenizedSpringVersion.get(1));

        return major(major).minor(minor);
    }

    public Version minor(int minor) {
        return new Version(this.major, minor);
    }

    public boolean isBefore(Version otherVersion) {
        final boolean before;
        if (major < otherVersion.major) {
            before = true;
        } else if (major == otherVersion.major) {
            before = minor < otherVersion.minor;
        } else {
            before = false;
        }
        return before;
    }

    @Override
    public String toString() {
        return "v" + major + "." + minor;
    }
}
