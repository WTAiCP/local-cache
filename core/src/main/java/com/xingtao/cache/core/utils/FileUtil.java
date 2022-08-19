package com.xingtao.cache.core.utils;

import com.github.houbb.heaven.response.exception.CommonRuntimeException;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.*;
import java.util.Collections;

/**
 * @Description 文件工具类
 *  1、用于获取文件中的内容
 * @Version
 * @BelongsPackage com.xingtao.cache.core.utils
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/11
 */
public class FileUtil {

    private FileUtil() {
    }


    /**
     * 写入文件信息
     * （1）默认 utf-8 编码
     * （2）默认新建一个文件
     * （3）默认为一行
     *
     * @param filePath    文件路径
     * @param line        行信息
     * @param openOptions 操作属性
     * @since 0.1.78
     */
    public static void write(final String filePath, final CharSequence line, OpenOption... openOptions) {
        write(filePath, Collections.singletonList(line), openOptions);
    }

    /**
     * 写入文件信息
     * （1）默认 utf-8 编码
     * （2）默认新建一个文件
     *
     * @param filePath    文件路径
     * @param lines       行信息
     * @param openOptions 文件选项
     * @since 0.1.22
     */
    public static void write(final String filePath, final Iterable<? extends CharSequence> lines, OpenOption... openOptions) {
        write(filePath, lines, "UTF-8", openOptions);
    }

    /**
     * 写入文件信息
     *
     * @param filePath    文件路径
     * @param lines       行信息
     * @param charset     文件编码
     * @param openOptions 文件操作选项
     * @since 0.1.22
     */
    public static void write(final String filePath, final Iterable<? extends CharSequence> lines,
                             final String charset, OpenOption... openOptions) {
        try {
            // ensure lines is not null before opening file
            if (null == lines) {
                throw new IllegalArgumentException("charSequences can not be null!");
            }
            //ArgUtil.notNull(lines, "charSequences");
            CharsetEncoder encoder = Charset.forName(charset).newEncoder();
            final Path path = Paths.get(filePath);

            // 创建父类文件夹
            Path pathParent = path.getParent();
            // 路径判断空
            if(pathParent != null) {
                File parent = pathParent.toFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
            }

            OutputStream out = path.getFileSystem().provider().newOutputStream(path, openOptions);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, encoder))) {
                for (CharSequence line : lines) {
                    writer.append(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 创建文件
     * （1）文件路径为空，则直接返回 false
     * （2）如果文件已经存在，则返回 true
     * （3）如果文件不存在，则创建文件夹，然后创建文件。
     * 3.1 如果父类文件夹创建失败，则直接返回 false.
     *
     * @param filePath 文件路径
     * @return 是否成功
     * @throws
     * @since 0.1.24
     */
    public static boolean createFile(final String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }

        if (FileUtil.exists(filePath)) {
            return true;
        }

        File file = new File(filePath);

        // 父类文件夹的处理
        File dir = file.getParentFile();
        if (dir != null && FileUtil.notExists(dir)) {
            boolean mkdirResult = dir.mkdirs();
            if (!mkdirResult) {
                return false;
            }
        }
        // 创建文件

        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 文件是否存在
     *
     * @param filePath 文件路径
     * @param options  连接选项
     * @return 是否存在
     * @since 0.1.24
     */
    public static boolean exists(final String filePath, LinkOption... options) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }

        Path path = Paths.get(filePath);
        return Files.exists(path, options);
    }

    /**
     * 文件是否不存在
     *
     * @param filePath 文件路径
     * @param options  连接选项
     * @return 是否存在
     * @since 0.1.24
     */
    public static boolean notExists(final String filePath, LinkOption... options) {
        return !exists(filePath, options);
    }


    /**
     * 文件是否不存在
     *
     * @param file 文件
     * @return 是否存在
     * @since 0.1.24
     */
    public static boolean notExists(final File file) {
        if (null == file) {
            throw new IllegalArgumentException("file can not be null!");
        }
        return !file.exists();
    }

    /**
     * 判断文件是否为空
     * （1）文件不存在，返回 true
     * （2）文件存在，且 {@link File#length()} 为0，则认为空。
     * （3）文件存在，且length大于0，则认为不空
     *
     * @param filePath 文件路径
     * @return 内容是否为空
     * @since 0.1.24
     */
    public static boolean isEmpty(final String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return true;
        }
        File file = new File(filePath);
        return file.length() <= 0;
    }

    /**
     * 清空文件内容
     * @param filePath 文件路径
     * @since 0.1.116
     */
    public static void truncate(final String filePath) {
        FileUtil.write(filePath, "", StandardOpenOption.TRUNCATE_EXISTING);
    }
}
