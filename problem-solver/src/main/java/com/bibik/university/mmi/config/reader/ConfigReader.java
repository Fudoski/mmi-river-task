package com.bibik.university.mmi.config.reader;

import com.bibik.university.mmi.config.TaskConfig;

import java.io.IOException;

public interface ConfigReader {

    TaskConfig read() throws IOException;
}
